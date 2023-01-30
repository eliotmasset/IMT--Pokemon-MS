package IMT.PokemonMS.Controller;

import IMT.PokemonMS.Model.User;
import IMT.PokemonMS.Repository.UserRepository;
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
import org.apache.commons.codec.binary.Hex;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
    private transient byte[] identifier = "5000520b9842577d402722b88797865cd0f881e20c66948f851fda676b7d4e5e".getBytes();
	private transient int iterations = 10000;
	private transient int keyLength = 100;

    /**
     * Generate a random string.
     */
    private static String nextString() {
        return RandomStringUtils.randomAlphanumeric(20).toUpperCase();
    }

	private static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded();
            return res;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    private Boolean isValidToken(String jwt_token, String username) {
        try {
            var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();

            if (!claims.get("username").equals(username)) return false;
            if ((long)claims.get("end_date") < System.currentTimeMillis()) return false;

            User user = userRepository.findByUsername(username);

            if (user == null) return false;
            if ((Integer)claims.get("id") != user.getId()) return false;

            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @GetMapping("/connected")
    public JSONObject connected(@RequestParam("jwt_token") String jwt_token, @RequestParam("username") String username) {
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "User not connected");
        response.put("response", false);
        if(isValidToken(jwt_token, username)) {
            response.put("message", "User connected");
            response.put("response", true);
        }
        return response;
    }

    @PostMapping("/subscribe")
    public JSONObject subscribe(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response", "");
		try {
            String username = "";
            String password = "";
            Boolean gender = false;
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                username = (String) json.get("username");
                password = (String) json.get("password");
                gender = ((Boolean) json.get("gender") ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
            User userExists = userRepository.findByUsername(username);
            if (userExists != null) {
                response.put("message", "User already exists");
                return response;
            }
            //Hash Password
            String salt = nextString();
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = salt.getBytes();
            byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
            String hashedString = Hex.encodeHexString(hashedBytes);
            User user = new User();
            user.setUsername(username);
            user.setGender(gender);
            user.setMoney(10000);
            user.setPassword(hashedString);
            user.setSalt(salt.toString());
            userRepository.save(user);

            String jwt_token = Jwts.builder()
                .setSubject(username)
                .claim("id", user.getId())
                .claim("username", username)
                .claim("end_date", System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))
                .signWith(Keys.hmacShaKeyFor(keyHMAC), SignatureAlgorithm.HS256)
                .compact();
            response.put("response", jwt_token);
        } catch (Exception e) {
            return response;
        }
        response.put("status", "success");
        response.put("message", "User created");
        return response;
    }

    @PostMapping("/connect")
    public JSONObject connect(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response", "");
		try {
            String username = "";
            String password = "";
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                username = (String) json.get("username");
                password = (String) json.get("password");
            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
            User user = userRepository.findByUsername(username);
            if (user == null) {
                response.put("message", "User not found");
                return response;
            }
            //Hash Password
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = user.getSalt().getBytes();
            byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
            String hashedString = Hex.encodeHexString(hashedBytes);

            if(!hashedString.equals(user.getPassword())) {
                response.put("message", "Wrong password");
                return response;
            }

            String jwt_token = Jwts.builder()
                .setSubject(username)
                .claim("id", user.getId())
                .claim("username", username)
                .claim("end_date", System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))
                .signWith(Keys.hmacShaKeyFor(keyHMAC), SignatureAlgorithm.HS256)
                .compact();
            response.put("response", jwt_token);
        } catch (Exception e) {
            return response;
        }
        response.put("status", "success");
        response.put("message", "User connected");
        return response;
    }

    @GetMapping("/getMoney")
    public JSONObject getMoney(@RequestParam("jwt_token") String jwt_token, @RequestParam("username") String username) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response", -1);
		try {
            if(isValidToken(jwt_token, username)) {
                User user = userRepository.findByUsername(username);
                response.put("response", user.getMoney());
            } else {
                response.put("message", "Invalid token");
                return response;
            }
        } catch (Exception e) {
            return response;
        }
        response.put("status", "success");
        response.put("message", "Success");
        return response;
    }

    @PostMapping("/addMoney")
    public JSONObject addMoney(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response", "");
        try {
            String identifier = "";
            String username = "";
            Integer money = 0;
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                identifier = (String) json.get("identifier");
                username = (String) json.get("username");
                money = Math.toIntExact((Long) json.get("money"));
            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
    
            User userExists = userRepository.findByUsername(username);
            if (userExists == null) {
                response.put("message", "User not found");
                return response;
            }
            if (identifier.equals(new String(this.identifier))) {
                User user = userRepository.findByUsername(username);
                user.setMoney(user.getMoney() + money);
                userRepository.save(user);
                response.put("status", "success");
            }
        } catch (Exception e) {
            return response;
        }
        response.put("message", "Success");
        return response;
    }

    @PostMapping("/removeMoney")
    public JSONObject removeMoney(@RequestBody String body) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", "An error occured");
        response.put("response", "");
        try {
            String identifier = "";
            String username = "";
            Integer money = 0;
            try {
                JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
                identifier = (String) json.get("identifier");
                username = (String) json.get("username");
                money = Math.toIntExact((Long) json.get("money"));
            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
    
            User userExists = userRepository.findByUsername(username);
            if (userExists == null) {
                response.put("message", "User not found");
                return response;
            }
            if (identifier.equals(new String(this.identifier))) {
                User user = userRepository.findByUsername(username);
                if(user.getMoney() < money) {
                    response.put("message", "Not enough money");
                    return response;
                }
                user.setMoney(user.getMoney() - money);
                userRepository.save(user);
                response.put("status", "success");
            }
        } catch (Exception e) {
            return response;
        }
        response.put("message", "Success");
        return response;
    }
}