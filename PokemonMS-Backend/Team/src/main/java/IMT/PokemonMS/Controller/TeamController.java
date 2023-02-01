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

    @PostMapping("/addPokemon")
    public JSONObject addPokemon(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
            String username = (String) json.get("username");
            String identifier = (String) json.get("identifier");
            JSONObject pokemon = (JSONObject) json.get("pokemon");
            if (!identifier.equals(this.identifier)){
                response.put("message", "Invalid identifier");
                return response;
            }
            Team team = teamRepository.findByUsername(username);
            if(team == null){
                response.put("message", "Team not found");
                return response;
            }

            Pokemon pokemon_new = new Pokemon();
            pokemon_new.setId_pokedex(Math.toIntExact((long) pokemon.get("id_pokedex")));
            pokemon_new.setName((String) pokemon.get("name"));
            pokemon_new.setEnglish_name((String) pokemon.get("english_name"));
            pokemon_new.setHp(Math.toIntExact((long) pokemon.get("hp")));
            pokemon_new.setAttack(Math.toIntExact((long) pokemon.get("attack")));
            pokemon_new.setDefense(Math.toIntExact((long) pokemon.get("defense")));
            pokemon_new.setAttack_special(Math.toIntExact((long) pokemon.get("attack_special")));
            pokemon_new.setDefense_special(Math.toIntExact((long) pokemon.get("defense_special")));
            pokemon_new.setSpeed(Math.toIntExact((long) pokemon.get("speed")));
            pokemon_new.setLevel(Math.toIntExact((long) pokemon.get("level")));
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
                response.put("message", "Team is full");
                return response;
            }
            teamRepository.save(team);
            response.put("status", "success");
            response.put("message", "Pokemon added to team");
            response.put("response", team);
            return response;
        } catch (Exception e) {
            return response;
        }
    }

    @PostMapping("/replacePokemon")
    public JSONObject replacePokemon(@RequestBody String bodyString) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response",  "");
        try {
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(bodyString);
            String username = (String) body.get("username");
            String identifier = (String) body.get("jwt_token");
            JSONObject pokemon = (JSONObject) body.get("pokemon");
            int position = (int) body.get("position");
            if (!identifier.equals(this.identifier)){
                response.put("message", "Invalid identifier");
                return response;
            }
            Team team = teamRepository.findByUsername(username);
            if(team == null){
                response.put("message", "Team not found");
                return response;
            }

            Pokemon pokemon_new = new Pokemon();
            pokemon_new.setId_pokedex(Math.toIntExact((long) pokemon.get("id_pokedex")));
            pokemon_new.setName((String) pokemon.get("name"));
            pokemon_new.setEnglish_name((String) pokemon.get("english_name"));
            pokemon_new.setHp(Math.toIntExact((long) pokemon.get("hp")));
            pokemon_new.setAttack(Math.toIntExact((long) pokemon.get("attack")));
            pokemon_new.setDefense(Math.toIntExact((long) pokemon.get("defense")));
            pokemon_new.setAttack_special(Math.toIntExact((long) pokemon.get("attack_special")));
            pokemon_new.setDefense_special(Math.toIntExact((long) pokemon.get("defense_special")));
            pokemon_new.setSpeed(Math.toIntExact((long) pokemon.get("speed")));
            pokemon_new.setLevel(Math.toIntExact((long) pokemon.get("level")));
            pokemon_new.setGender((Boolean) pokemon.get("gender"));
            pokemon_new.setIs_legendary((Boolean) pokemon.get("is_legendary"));
            pokemonRepository.save(pokemon_new);

            if(position == 1) {
                Pokemon pokemon_old = team.getPokemon1();
                team.setPokemon1(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else if(position == 2) {
                Pokemon pokemon_old = team.getPokemon2();
                team.setPokemon2(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else if(position == 3) {
                Pokemon pokemon_old = team.getPokemon3();
                team.setPokemon3(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else if(position == 4) {
                Pokemon pokemon_old = team.getPokemon4();
                team.setPokemon4(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else if(position == 5) {
                Pokemon pokemon_old = team.getPokemon5();
                team.setPokemon5(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else if(position == 6) {
                Pokemon pokemon_old = team.getPokemon6();
                team.setPokemon6(pokemon_new);
                pokemonRepository.delete(pokemon_old);
            } else{
                pokemonRepository.delete(pokemon_new);
                response.put("message", "Invalid position");
                return response;
            }

            teamRepository.save(team);
            response.put("status", "success");
            response.put("message", "Pokemon added to team");
            response.put("response", team);
            return response;
        } catch (Exception e) {
            return response;
        }
    }

}