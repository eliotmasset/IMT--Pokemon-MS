package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.Shop;
import IMT.PokemonMS.Model.PokemonType;
import IMT.PokemonMS.Repository.ShopRepository;
import IMT.PokemonMS.Repository.PokemonTypeRepository;
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




@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private PokemonTypeRepository pokemonTypeRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient String user_identifier = "5000520b9842577d402722b88797865cd0f881e20c66948f851fda676b7d4e5e";
    private transient String inventory_identifier = "ed9bd7861561e5eaadd3f15bd7c0bca004ee7a9298b4b5019820d86622a0cccf";
	private transient int iterations = 10000;
	private transient int keyLength = 100;
    private transient String apiUserUrl = "http://localhost:8087/user/";
    private transient String apiInventoryUrl = "http://localhost:8080/inventory/";

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

    private Boolean removeMoney(int money, String username) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(this.apiUserUrl + "removeMoney");
            JSONObject json = new JSONObject();
            json.put("identifier", this.user_identifier);
            json.put("username", username);
            json.put("money", 500);
            StringEntity entity = new StringEntity(json.toString());
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) return false;
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            if(!responseString.equals("Success")) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean addEggToInventory(String username, int type, Boolean isPokemon) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(this.apiInventoryUrl + "addEgg");
            JSONObject json = new JSONObject();
            json.put("identifier", this.inventory_identifier);
            json.put("username", username);
            json.put("type", type);
            json.put("isPokemon", isPokemon);
            StringEntity entity = new StringEntity(json.toString());
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) return false;
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            if(!responseString.equals("true")) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void reloadShop(Shop shop) {
        List<PokemonType> pokemonTypes = pokemonTypeRepository.findAll();
        int randomIndex1 = (int) (Math.random() * pokemonTypes.size());
        int randomIndex2 = (int) (Math.random() * pokemonTypes.size());
        int randomIndex3 = (int) (Math.random() * pokemonTypes.size());
        shop.setPokemonType1(pokemonTypes.get(randomIndex1));
        shop.setPokemonType2(pokemonTypes.get(randomIndex2));
        shop.setPokemonType3(pokemonTypes.get(randomIndex3));
        shop.setPrice1((int) (100 + (Math.random() * 200)));
        shop.setPrice2((int) (100 + (Math.random() * 200)));
        shop.setPrice3((int) (100 + (Math.random() * 200)));
        shopRepository.save(shop);
    }

    @GetMapping("/getShop")
    public JSONObject getShop(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
        if (!isValidToken(jwt_token, username)) return null;
        if(shopRepository.findByUsername(username) == null) {
            Shop shop = new Shop();
            shop.setUsername(username);
            this.reloadShop(shop);
        }
        Shop shop = shopRepository.findByUsername(username);
        JSONObject json = new JSONObject();
        JSONObject jsonP1 = new JSONObject();
        JSONObject jsonP2 = new JSONObject();
        JSONObject jsonP3 = new JSONObject();
        jsonP1.put("name", shop.getPokemonType1().getName());
        jsonP1.put("description", shop.getPokemonType1().getDescription());
        jsonP1.put("price", shop.getPrice1());
        jsonP2.put("name", shop.getPokemonType2().getName());
        jsonP2.put("description", shop.getPokemonType2().getDescription());
        jsonP2.put("price", shop.getPrice2());
        jsonP3.put("name", shop.getPokemonType3().getName());
        jsonP3.put("description", shop.getPokemonType3().getDescription());
        jsonP3.put("price", shop.getPrice3());
        json.put("pokemon1", jsonP1);
        json.put("pokemon2", jsonP2);
        json.put("pokemon3", jsonP3);
        json.put("id", shop.getId());
        return json;
    }

    @PostMapping("/buyEgg")
	public String buyEgg(@RequestBody String body) {
        String username = "";
        String jwt_token = "";
        int shopId = 0;
		try {
			JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
			username = (String) json.get("username");
            jwt_token = (String) json.get("jwt_token");
			shopId = Math.toIntExact((Long) json.get("shopId"));
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occured";
		}
        if (!isValidToken(jwt_token, username)) return "Invalid token";
        Shop shop = shopRepository.findByUsername(username);
        if (shop == null) return "Shop not found";

        if (shopId == 1) {
            if(!removeMoney(200, username)) return "An error occured";
            if(addEggToInventory(username, 0, false)) return "Success";
        } else if (shopId == 2) {
            if(!removeMoney(350, username)) return "An error occured";
            if(addEggToInventory(username, 1, false)) return "Success";
        } else if (shopId == 3) {
            if(!removeMoney(500, username)) return "An error occured";
            if(addEggToInventory(username, 2, false)) return "Success";
        } else if (shopId == 4) {
            if (shop.getPokemonType1() == null) return "Pokemon not found";
            if(!removeMoney(shop.getPrice1(), username)) return "An error occured";
            if(addEggToInventory(username, shop.getPokemonType1().getId(), true)) return "Success";
        } else if (shopId == 5) {
            if (shop.getPokemonType2() == null) return "Pokemon not found";
            if(!removeMoney(shop.getPrice2(), username)) return "An error occured";
            if(addEggToInventory(username, shop.getPokemonType2().getId(), true)) return "Success";
        } else if (shopId == 6) {
            if (shop.getPokemonType3() == null) return "Pokemon not found";
            if(!removeMoney(shop.getPrice3(), username)) return "An error occured";
            if(addEggToInventory(username, shop.getPokemonType3().getId(), true)) return "Success";
        } else {
            return "Invalid shop id";
        }
        return "[CRITICAL ERROR]";
    }

    @PostMapping("/buyReload")
    public String buyReload(@RequestBody String body) {
        String username = "";
        String jwt_token = "";
        try {
            JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
            username = (String) json.get("username");
            jwt_token = (String) json.get("jwt_token");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occured";
        }
        if (!isValidToken(jwt_token, username)) return "Invalid token";
        Shop shop = shopRepository.findByUsername(username);
        if (shop == null) return "Shop not found";

        if(!removeMoney(500, username)) return "An error occured";
        this.reloadShop(shop);

        return "ok";
    }
}