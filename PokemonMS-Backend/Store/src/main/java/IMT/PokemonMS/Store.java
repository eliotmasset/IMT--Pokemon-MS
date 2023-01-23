package IMT.PokemonMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@RestController
public class Store {

	private static byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();

	public static void main(String[] args) {
		SpringApplication.run(Store.class, args);
	}

	private static Boolean isConnected(String jwt_token, String username) {
		Connection conn = null;
		// Check if the token is valid
		try {
			Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token);
			// Check end date 
			var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();
			if(claims.get("end_date") == null || claims.get("id") == null) return false;
			if((long)claims.get("end_date") < System.currentTimeMillis()) return false;
			if(!((String)claims.get("username")).equals(username)) return false;
			if((int)claims.get("id") < 0) return false;
			conn = Database.getConnection();
			if(conn == null) return false;

			var stmt = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
			stmt.setInt(1, (int)claims.get("id"));
			var rs = stmt.executeQuery();
			Boolean attempt = rs.next();
			conn.close();
			if(!attempt) return false;
		} catch (SignatureException e) {
			System.out.println("Invalid JWT signature trace: "+e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token trace: "+e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("Expired JWT token trace: "+e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("Unsupported JWT token trace: "+e.getMessage());
		} catch (Exception e) {
			System.out.println("trace: "+e.getMessage());
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Check if the database is connected
		return true;
	}

	@GetMapping("/getStore")
	public ArrayList getStore(@RequestParam(value = "jwt_token") String jwt_token,
							@RequestParam(value = "username") String username) {
		var ret = new ArrayList<JSONObject>();
		Connection conn = null;
		try {
			conn = Database.getConnection();
			var stmt = conn.prepareStatement("SELECT id_pokemon_type_1, id_pokemon_type_2, id_pokemon_type_3, price_1, price_2, price_3 FROM Store s INNER JOIN User u ON u.id=s.id_user WHERE u.username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				var obj = new JSONObject();
				obj.put("id_pokemon_type_1", rs.getInt("id_pokemon_type_1"));
				obj.put("id_pokemon_type_2", rs.getInt("id_pokemon_type_2"));
				obj.put("id_pokemon_type_3", rs.getInt("id_pokemon_type_3"));
				obj.put("price_1", rs.getInt("price_1"));
				obj.put("price_2", rs.getInt("price_2"));
				obj.put("price_3", rs.getInt("price_3"));
				ret.add(obj);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return ret;
	}

}
