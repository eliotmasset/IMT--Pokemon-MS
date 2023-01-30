package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.Egg;
import IMT.PokemonMS.Model.Incubator;
import IMT.PokemonMS.Repository.EggRepository;
import IMT.PokemonMS.Repository.IncubatorRepository;
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


@RestController
@RequestMapping("/incubation")
public class IncubationController {

    @Autowired
    private IncubatorRepository incubatorRepository;
    @Autowired
    private EggRepository eggRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient String apiUserUrl = "http://localhost:8087/user/";
    private transient String user_identifier = "5000520b9842577d402722b88797865cd0f881e20c66948f851fda676b7d4e5e";
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

    @GetMapping("/getIncubatorList")
	public List<Incubator> getIncubatorList(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
        if (!isValidToken(jwt_token, username)) return null;
        return incubatorRepository.findByUsername(username);
    }

    
	@PostMapping("/setEggInIncubator")
	public String setEggInIncubator(@RequestBody String body) {
		String username = "";
		int id_incubator = 0;
        String eggType = "";
        int pokemonId = 0;
        String identifier = "";
		try {
			JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
			username = (String) json.get("username");
			id_incubator = ((Long) json.get("id_incubator")).intValue();
            eggType = (String) json.get("eggType");
            pokemonId = ((Long) json.get("pokemonId")).intValue();
            identifier = (String) json.get("identifier");
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occured";
		}
        if(!identifier.equals(this.identifier)) return "Invalid identifier";
        Incubator incubator = null;
        try {incubator = incubatorRepository.findById(id_incubator).get();}
        catch (Exception e) { return "Incubator not found";}
        if(incubator == null) return "Incubator not found";
        if (incubator.getEgg() != null) return "Incubator already has an egg";
        Egg egg = new Egg();
        egg.setType(eggType);
        egg.setPokemon(pokemonId);
        egg.setTime_to_incubate(3600);
        incubator.setStart_date_time(LocalDateTime.now());
        eggRepository.save(egg);
        incubator.setEgg(egg);
        incubatorRepository.save(incubator);
        return "Success";
    }

    @PostMapping("/buyIncubator")
    public String buyIncubator(@RequestBody String body) {
        String jwt_token = "";
        String username = "";
        try {
            JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
            jwt_token = (String) json.get("jwt_token");
            username = (String) json.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occured";
        }
        if (!isValidToken(jwt_token, username)) return "Invalid token";
        int incubatorCount = incubatorRepository.countByUsername(username);
        if (incubatorCount >= incubatorPrice.length) incubatorCount = incubatorPrice.length - 1;
        if (!this.removeMoney(username, this.incubatorPrice[incubatorCount])) return "Not enough money";
        Incubator incubator = new Incubator();
        incubator.setUsername(username);
        incubatorRepository.save(incubator);
        return "Incubator bought";
    }


}