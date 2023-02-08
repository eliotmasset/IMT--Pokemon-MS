package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.Egg;
import IMT.PokemonMS.Model.Incubator;
import IMT.PokemonMS.Model.PokemonType;
import IMT.PokemonMS.Repository.EggRepository;
import IMT.PokemonMS.Repository.IncubatorRepository;
import IMT.PokemonMS.Repository.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@RestController
@RequestMapping("/incubation")
public class IncubationController {

    @Autowired
    private IncubatorRepository incubatorRepository;
    @Autowired
    private EggRepository eggRepository;
    @Autowired
    private PokemonTypeRepository pokemonTypeRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient String apiUserUrl = "http://localhost:8087/user/";
    private transient String apiTeamUrl = "http://localhost:8081/team/";
    private transient String user_identifier = "5000520b9842577d402722b88797865cd0f881e20c66948f851fda676b7d4e5e";
    private transient String team_identifier = "5a4435c65134598c985385664e54ff742046dbb877c3ab108cf67b0c9242a084";
    private transient String identifier = "a3e50f775c77079fef6da38c8286cc4b85509fd1ca2bdddd665a50cbbc401697";
	private transient int iterations = 10000;
	private transient int keyLength = 100;
    private transient int[] incubatorPrice = {0, 100, 1000, 10000, 500000, 1000000};

    private Boolean isValidToken(String jwt_token, String username) {
        try {
            var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();

            if (!claims.get("username").equals(username)) return false;
            if ((long)claims.get("end_date") < System.currentTimeMillis()) return false;

            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Boolean removeMoney(String username, int price) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(this.apiUserUrl + "removeMoney");
            JSONObject json = new JSONObject();
            json.put("identifier", this.user_identifier);
            json.put("username", username);
            json.put("money", price);
            StringEntity entity = new StringEntity(json.toString());
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) return false;
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONParser parser = new JSONParser();
            JSONObject responseJson = new JSONObject();
            responseJson = (JSONObject) parser.parse(responseString);
            if(!responseJson.get("status").equals("success")) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean callTeamSetPokemon(String username, int position, PokemonType pokemonType) {
        try {
            JSONObject pokemon = pokemonType.toJson();
            pokemon.put("level", 1);
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = null;
            if(position == 0) httppost = new HttpPost(this.apiTeamUrl + "addPokemon");
            else httppost = new HttpPost(this.apiTeamUrl + "replacePokemon");
            JSONObject json = new JSONObject();
            json.put("identifier", this.team_identifier);
            json.put("username", username);
            json.put("pokemon", pokemon);
            if(position != 0) json.put("position", position);
            StringEntity entity = new StringEntity(json.toString());
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) return false;
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONParser parser = new JSONParser();
            JSONObject responseJson = new JSONObject();
            responseJson = (JSONObject) parser.parse(responseString);
            if(!responseJson.get("status").equals("success")) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private PokemonType getPokemon(Egg egg) {
        if(egg.getPokemon() != 0 && egg.getPokemon() != 1) return pokemonTypeRepository.findById(egg.getPokemon());
        switch (egg.getType()) {
            case "common":
                return pokemonTypeRepository.findById((int)(Math.random() * 40) + 1);
            case "rare":
                return pokemonTypeRepository.findById((int)(Math.random() * 40) + 40 + 1);
            default:
                return pokemonTypeRepository.findById((int)(Math.random() * 26) + 80 + 1);
        }
    }

    @GetMapping("/getIncubatorList")
	public JSONObject getIncubatorList(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "Invalid token");
        response.put("response",  "");
        if (isValidToken(jwt_token, username)) {
            response.put("status", "success");
            response.put("message", "Success");
            response.put("response", incubatorRepository.findByUsername(username));
        }
        return response;
    }

    
	@PostMapping("/setEggInIncubator")
	public JSONObject setEggInIncubator(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
		String username = "";
        try {
            int id_incubator = 0;
            String eggType = "";
            int pokemonId = 0;
            String identifier = "";
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                username = (String) json.get("username");
                id_incubator = ((Long) json.get("id_incubator")).intValue();
                eggType = (String) json.get("egg_type");
                pokemonId = ((Long) json.get("id_pokemon")).intValue();
                identifier = (String) json.get("identifier");
            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
            if(!identifier.equals(this.identifier)) {
                response.put("message", "Invalid identifier");
                return response;
            }
            Incubator incubator = null;
            try {incubator = incubatorRepository.findById(id_incubator);}
            catch (Exception e) {
                response.put("message", "Incubator not found");
                return response;
            }
            if(incubator == null) {
                response.put("message", "Incubator not found");
                return response;
            }
            if (incubator.getEgg() != null) {
                response.put("message", "Incubator already used");
                return response;
            }
            Egg egg = new Egg();
            egg.setType(eggType);
            egg.setPokemon(pokemonId);
            if(eggType.equalsIgnoreCase("common")) egg.setTime_to_incubate(3600);
            else if(eggType.equalsIgnoreCase("rare")) egg.setTime_to_incubate(3600 *2);
            else egg.setTime_to_incubate(3600 * 24);
            incubator.setStart_date_time(LocalDateTime.now());
            eggRepository.save(egg);
            incubator.setEgg(egg);
            incubatorRepository.save(incubator);
            response.put("response", "success");
        } catch (Exception e) {
            return response;
        }
        response.put("status", "success");
        response.put("message", "Egg set in incubator");
        return response;
    }

    @PostMapping("/buyIncubator")
    public JSONObject buyIncubator(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            String jwt_token = "";
            String username = "";
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                jwt_token = (String) json.get("jwt_token");
                username = (String) json.get("username");
            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
            if (!isValidToken(jwt_token, username)) {
                response.put("message", "Invalid token");
                return response;
            }
            int incubatorCount = incubatorRepository.countByUsername(username);
            if (incubatorCount >= incubatorPrice.length) incubatorCount = incubatorPrice.length - 1;
            if (!this.removeMoney(username, this.incubatorPrice[incubatorCount])) {
                response.put("message", "Not enough money");
                return response;
            }
            Incubator incubator = new Incubator();
            incubator.setUsername(username);
            incubatorRepository.save(incubator);
            response.put("response", "success");
        } catch (Exception e) {
            return response;
        }
        response.put("status", "success");
        response.put("message", "Incubator bought");
        return response;
    }

    

    @PostMapping("/hatchingPokemon")
    public JSONObject hatchingPokemon(@RequestBody String bodyString) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(bodyString);
            String username = (String) body.get("username");
            String jwt_token = (String) body.get("jwt_token");
            int incubator_id = ((Long) body.get("id_incubator")).intValue();
            int position = ((Long) body.get("position")).intValue();
            if(!isValidToken(jwt_token, username)) {
                response.put("message", "Invalid token");
                return response;
            }
            Incubator incubator = incubatorRepository.findById(incubator_id);
            if(incubator == null) {
                response.put("message", "Incubator not found");
                return response;
            }
            if(incubator.getEgg() == null) {
                response.put("message", "No egg in incubator");
                return response;
            }
            /*if( ( (incubator.getStart_date_time().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000) + incubator.getEgg().getTime_to_incubate() ) >= ( LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000 ) ) {
                response.put("message", "Egg can't be hatched, time left : " + (( (incubator.getStart_date_time().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000) + incubator.getEgg().getTime_to_incubate() ) - ( LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000 )) + " seconds" );
                return response;
            }*/
            if(position < 0 || position > 6) {
                response.put("message", "Invalid position");
                return response;
            }

            if(!this.callTeamSetPokemon(username, position, this.getPokemon(incubator.getEgg()))) {
                response.put("message", "An error occured when attempting to call team service");
                return response;
            }

            Egg egg_save = incubator.getEgg();

            incubator.setEgg(null);
            eggRepository.delete(egg_save);
            response.put("status", "success");
            response.put("message", "Pokemon added to team");
            response.put("response", "");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
    }

}