package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.Team;
import IMT.PokemonMS.Model.Pokemon;
import IMT.PokemonMS.Repository.TeamRepository;
import IMT.PokemonMS.Repository.PokemonRepository;
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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;





@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PokemonRepository pokemonRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient String identifier = "5a4435c65134598c985385664e54ff742046dbb877c3ab108cf67b0c9242a084";
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

    private float getRandomMultiplier() {
        return (float) (Math.random() * (1.5 - 0.5) + 0.5);
    }

    private float getRandomLevel() {
        return (float) (Math.random() * (50 - 1) + 1);
    }

    @GetMapping("/getTeam")
    public JSONObject getTeam(@RequestParam String username, @RequestParam String jwt_token) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        if (!isValidToken(jwt_token, username)){
            response.put("message", "Invalid token");
            return response;
        }
        Team team = teamRepository.findByUsername(username);
        if(team == null){
            var newTeam = new Team();
            newTeam.setUsername(username);
            teamRepository.save(newTeam);
            team = teamRepository.findByUsername(username);
        }
        response.put("status", "success");
        response.put("message", "Team found");
        response.put("response", team);
        return response;
    }
    
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(String message) {
        try {
            JSONParser parser = new JSONParser();  
            JSONObject json = (JSONObject) parser.parse(message); 
            if(((String)json.get("request")).equals("addPokemon")) {
                this.addPokemon((JSONObject)json.get("body"));
            } else if(((String)json.get("request")).equals("replacePokemon")) {
                this.replacePokemon((JSONObject)json.get("body"));
            } else {
                System.out.println("Unknown request :" + json.get("request"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public void addPokemon(JSONObject message) {
        try {
            JSONObject json = message;
            String username = (String) json.get("username");
            String identifier = (String) json.get("identifier");
            JSONObject pokemon = (JSONObject) json.get("pokemon");
            if (!identifier.equals(this.identifier)){
                return;
            }
            Team team = teamRepository.findByUsername(username);
            if(team == null){
                return;
            }

            Pokemon pokemon_new = new Pokemon();
            pokemon_new.setId_pokedex(Math.toIntExact((long) pokemon.get("id")));
            pokemon_new.setName((String) pokemon.get("name"));
            pokemon_new.setEnglish_name((String) pokemon.get("english_name"));
            pokemon_new.setHp(Math.round(Math.toIntExact((long) pokemon.get("hp"))*this.getRandomMultiplier()));
            pokemon_new.setAttack(Math.round(Math.toIntExact((long) pokemon.get("attack"))*this.getRandomMultiplier()));
            pokemon_new.setDefense(Math.round(Math.toIntExact((long) pokemon.get("defense"))*this.getRandomMultiplier()));
            pokemon_new.setAttack_special(Math.round(Math.toIntExact((long) pokemon.get("attack_special"))*this.getRandomMultiplier()));
            pokemon_new.setDefense_special(Math.round(Math.toIntExact((long) pokemon.get("defense_special"))*this.getRandomMultiplier()));
            pokemon_new.setSpeed(Math.round(Math.toIntExact((long) pokemon.get("speed"))*this.getRandomMultiplier()));
            pokemon_new.setLevel(Math.round(this.getRandomLevel()));
            pokemon_new.setGender((Boolean) pokemon.get("gender"));
            pokemon_new.setIs_legendary((Boolean) pokemon.get("is_legendary"));
            pokemonRepository.save(pokemon_new);
            
            if(team.getPokemon1() == null)
                team.setPokemon1(pokemon_new);
            else if(team.getPokemon2() == null)
                team.setPokemon2(pokemon_new);
            else if(team.getPokemon3() == null)
                team.setPokemon3(pokemon_new);
            else if(team.getPokemon4() == null)
                team.setPokemon4(pokemon_new);
            else if(team.getPokemon5() == null)
                team.setPokemon5(pokemon_new);
            else if(team.getPokemon6() == null)
                team.setPokemon6(pokemon_new);
            else{
                pokemonRepository.delete(pokemon_new);
                return;
            }
            teamRepository.save(team);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void replacePokemon(JSONObject message) {
        try {
            JSONObject body = message;
            String username = (String) body.get("username");
            String identifier = (String) body.get("identifier");
            JSONObject pokemon = (JSONObject) body.get("pokemon");
            int position = Math.toIntExact((long) body.get("position"));
            if (!identifier.equals(this.identifier)) {
                return;
            }
            Team team = teamRepository.findByUsername(username);
            if(team == null){
                return;
            }

            Pokemon pokemon_new = new Pokemon();
            pokemon_new.setId_pokedex(Math.toIntExact((long) pokemon.get("id")));
            pokemon_new.setName((String) pokemon.get("name"));
            pokemon_new.setEnglish_name((String) pokemon.get("english_name"));
            pokemon_new.setHp(Math.round(Math.toIntExact((long) pokemon.get("hp"))*this.getRandomMultiplier()));
            pokemon_new.setAttack(Math.round(Math.toIntExact((long) pokemon.get("attack"))*this.getRandomMultiplier()));
            pokemon_new.setDefense(Math.round(Math.toIntExact((long) pokemon.get("defense"))*this.getRandomMultiplier()));
            pokemon_new.setAttack_special(Math.round(Math.toIntExact((long) pokemon.get("attack_special"))*this.getRandomMultiplier()));
            pokemon_new.setDefense_special(Math.round(Math.toIntExact((long) pokemon.get("defense_special"))*this.getRandomMultiplier()));
            pokemon_new.setSpeed(Math.round(Math.toIntExact((long) pokemon.get("speed"))*this.getRandomMultiplier()));
            pokemon_new.setLevel(Math.round(this.getRandomLevel()));
            pokemon_new.setGender((Boolean) pokemon.get("gender"));
            pokemon_new.setIs_legendary((Boolean) pokemon.get("is_legendary"));
            pokemonRepository.save(pokemon_new);

            if(position == 1) {
                Pokemon pokemon_old = team.getPokemon1();
                team.setPokemon1(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else if(position == 2) {
                Pokemon pokemon_old = team.getPokemon2();
                team.setPokemon2(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else if(position == 3) {
                Pokemon pokemon_old = team.getPokemon3();
                team.setPokemon3(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else if(position == 4) {
                Pokemon pokemon_old = team.getPokemon4();
                team.setPokemon4(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else if(position == 5) {
                Pokemon pokemon_old = team.getPokemon5();
                team.setPokemon5(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else if(position == 6) {
                Pokemon pokemon_old = team.getPokemon6();
                team.setPokemon6(pokemon_new);
                teamRepository.save(team);
                if(pokemon_old != null) pokemonRepository.delete(pokemon_old);
            } else{
                pokemonRepository.delete(pokemon_new);
                return;
            }

            teamRepository.save(team);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}