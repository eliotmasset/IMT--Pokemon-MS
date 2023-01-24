package IMT.PokemonMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
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
public class Transaction {

	private static byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();

	public static void main(String[] args) {
		SpringApplication.run(Transaction.class, args);
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

	@PostMapping("/buyEggInShop")
	public String buyEggInShop(@RequestBody String body) {
		String jwt_token = "";
		String username = "";
		Integer egg_type = 0;
		try {
			JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
			jwt_token = (String) json.get("jwt_token");
			username = (String) json.get("username");
			egg_type = Math.toIntExact((Long) json.get("egg_type"));
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occured";
		}
		if(!isConnected(jwt_token, username)) return "You are not connected";
		Connection conn = null;
		try {
			conn = Database.getConnection();
			if(conn == null) throw new Exception("Database connection failed");
			var stmt = conn.prepareStatement("SELECT price FROM Egg_type WHERE id = ?");
			stmt.setInt(1, egg_type);
			var rs = stmt.executeQuery();
			if(!rs.next()) throw new Exception("Egg type not found");
			int price = rs.getInt("price");
			stmt = conn.prepareStatement("SELECT money FROM User WHERE username = ?");
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if(!rs.next()) throw new Exception("User not found");
			int money = rs.getInt("money");
			if(money < price) throw new Exception("Not enough money");
			stmt = conn.prepareStatement("UPDATE User SET money = ? WHERE username = ?");
			stmt.setInt(1, money - price);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt = conn.prepareStatement("INSERT INTO Egg (id_egg_type, id_pokemon_type, id_user, time_to_incubate) VALUES (?, NULL, (SELECT id FROM User WHERE username = ?), ?)");
			stmt.setInt(1, egg_type);
			stmt.setString(2, username);
			if(egg_type == 1) stmt.setInt(3, 1000 * 60 * 60 * 1);
			else if(egg_type == 2) stmt.setInt(3, 1000 * 60 * 60 * 3);
			else if(egg_type == 3) stmt.setInt(3, 1000 * 60 * 60 * 24);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			conn.close();
			return "An error occured : "+e.getMessage();
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Egg bought";
	}

	
	@PostMapping("/buyPokemonInShop")
	public String buyPokemonInShop(@RequestBody String body) {
		String jwt_token = "";
		String username = "";
		Integer pokemon_store = 0;
		try {
			JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
			jwt_token = (String) json.get("jwt_token");
			username = (String) json.get("username");
			pokemon_store = Math.toIntExact((Long) json.get("pokemon_store"));
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occured";
		}
		if(pokemon_store < 1 || pokemon_store > 3) return "Pokemon store not found";
		if(!isConnected(jwt_token, username)) return "You are not connected";
		Connection conn = null;
		try {
			conn = Database.getConnection();
			if(conn == null) throw new Exception("Database connection failed");
			var stmt = conn.prepareStatement("SELECT id_pokemon_type_1, price_1, id_pokemon_type_2, price_2, id_pokemon_type_3, price_3 FROM Store WHERE id_user = (SELECT id FROM User WHERE username = ?)");
			stmt.setString(1, username);
			var rs = stmt.executeQuery();
			if(!rs.next()) throw  new Exception("Store not found");
			int id_pokemon_type = 0;
			int price = 0;
			if(pokemon_store == 1) {
				id_pokemon_type = rs.getInt("id_pokemon_type_1");
				price = rs.getInt("price_1");
			} else if(pokemon_store == 2) {
				id_pokemon_type = rs.getInt("id_pokemon_type_2");
				price = rs.getInt("price_2");
			} else if(pokemon_store == 3) {
				id_pokemon_type = rs.getInt("id_pokemon_type_3");
				price = rs.getInt("price_3");
			}
			stmt = conn.prepareStatement("SELECT money FROM User WHERE username = ?");
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if(!rs.next()) throw  new Exception("User not found");
			int money = rs.getInt("money");
			if(money < price) throw  new Exception("Not enough money");
			stmt = conn.prepareStatement("UPDATE User SET money = ? WHERE username = ?");
			stmt.setInt(1, money - price);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt = conn.prepareStatement("INSERT INTO Egg (id_egg_type, id_pokemon_type, id_user, time_to_incubate) VALUES (?, ?, (SELECT id FROM User WHERE username = ?), ?)");
			stmt.setInt(1, 1);
			stmt.setInt(2, id_pokemon_type);
			stmt.setString(3, username);
			stmt.setInt(4, 1000 * 60 * 60 * 1);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			conn.close();
			return "An error occured : " + e.getMessage();
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Egg bought";
	}


	@PostMapping("/buyReload")
	public String buyReload(@RequestBody String body) {
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
		if(!isConnected(jwt_token, username)) return "You are not connected";
		Connection conn = null;
		try {
			conn = Database.getConnection();
			if(conn == null) return "Database connection failed";
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			conn.close();
			return "An error occured";
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Reload bought";
	}

}
