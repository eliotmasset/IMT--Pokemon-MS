package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.Egg;
import IMT.PokemonMS.Repository.EggRepository;
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
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;




@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private EggRepository eggRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient String identifier = "ed9bd7861561e5eaadd3f15bd7c0bca004ee7a9298b4b5019820d86622a0cccf";
    private transient String incubation_identifier = "a3e50f775c77079fef6da38c8286cc4b85509fd1ca2bdddd665a50cbbc401697";
    private transient String apiIncubationUrl = "http://incubation-container:8082/incubation/";
	private transient int iterations = 10000;
	private transient int keyLength = 100;

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

    private Boolean callSetEggInIncubator(String username, int id_incubator, String eggType, int pokemonId) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(this.apiIncubationUrl + "setEggInIncubator");
            JSONObject json = new JSONObject();
            json.put("identifier", this.incubation_identifier);
            json.put("username", username);
            json.put("id_incubator", id_incubator);
            json.put("egg_type", eggType);
            json.put("id_pokemon", pokemonId);
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

    @GetMapping("/eggs")
    public JSONObject getEggs(@RequestParam("jwt_token") String jwt_token, @RequestParam("username") String username) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            if (!isValidToken(jwt_token, username)) {
                response.put("message", "Invalid token");
                return response;
            }

            List<Egg> eggs = eggRepository.findByUsername(username);

            response.put("response", eggs);
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
        response.put("status", "success");
        response.put("message", "Success");
        return response;
    }

    @PostMapping("/addEgg")
    public JSONObject addEgg(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            String username = "";
            String identifier = "";
            Boolean isPokemon = false;
            int type = 0;
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                username = (String) json.get("username");
                identifier = (String) json.get("identifier");
                isPokemon = (Boolean) json.get("is_pokemon");
                type = Math.toIntExact((Long) json.get("type"));
            } catch (Exception e) {
                e.printStackTrace();
                response.put("message", "Invalid body");
                return response;
            }
            if (!this.identifier.equals(identifier)) {
                response.put("message", "Invalid identifier");
                return response;
            }

            Egg egg = new Egg();
            egg.setUsername(username);
            if(!isPokemon) egg.setType(type == 0 ? "common" : type == 1 ? "rare" : "epic");
            else egg.setType("common");
            if(isPokemon) egg.setPokemon(type);
            
            eggRepository.save(egg);
            response.put("status", "success");
            response.put("message", "Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    
	@PostMapping("/setEggInIncubator")
	public JSONObject setEggInIncubator(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            String username = "";
            String jwt_token = "";
            int id_incubator = 0;
            int id_egg = 0;
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                username = (String) json.get("username");
                jwt_token = (String) json.get("jwt_token");
                id_incubator = ((Long) json.get("id_incubator")).intValue();
                id_egg = ((Long) json.get("id_egg")).intValue();
            } catch (Exception e) {
                e.printStackTrace();
                response.put("message", "Invalid body");
                return response;
            }
            if (!isValidToken(jwt_token, username)) {
                response.put("message", "Invalid token");
                return response;
            }
            Egg egg = eggRepository.findById(id_egg).orElse(null);
            if(egg == null) {
                response.put("message", "Egg not found");
                return response;
            }
            String eggType = egg.getType();
            int pokemonId = egg.getPokemon();
            if(!callSetEggInIncubator(username, id_incubator, eggType, pokemonId)) {
                return response;
            }
            this.eggRepository.delete(egg);
            response.put("status", "success");
            response.put("message", "Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }
}