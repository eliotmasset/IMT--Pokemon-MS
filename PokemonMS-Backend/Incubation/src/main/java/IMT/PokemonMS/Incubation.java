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
import java.io.InputStream;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;




@SpringBootApplication
@RestController
public class Incubation {

	private static byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
	private static int[] prices = {0, 100, 1000, 10000, 500000, 1000000};

	public static void main(String[] args) {
		SpringApplication.run(Incubation.class, args);
	}

	@GetMapping("/test")
	public Boolean test() {
		return true;
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

	@GetMapping("/getIncubatorList")
	public ArrayList<JSONObject> getIncubatorList(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
		var ret = new ArrayList<JSONObject>();
		Connection conn = null;
		if(!isConnected(jwt_token, username)) return ret;
		try {
			conn = Database.getConnection();
			var stmt = conn.prepareStatement("SELECT i.id, e.time_to_incubate, et.type, i.start_date_time, e.id as egg FROM User u INNER JOIN Incubator i ON u.id = i.id_user LEFT JOIN Egg e ON i.id_egg = e.id LEFT JOIN Egg_type et ON e.id_egg_type = et.id INNER JOIN Incubator_type it ON i.id_incubator_type = it.id WHERE username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				var item = new JSONObject();
				item.put("id", rs.getInt("id"));
				item.put("isUsed", rs.getInt("egg") != 0);
				item.put("startDate", rs.getTimestamp("start_date_time") != null ? rs.getTimestamp("start_date_time").getTime() : null);
				item.put("endDate", rs.getTimestamp("start_date_time") != null ? (new Timestamp(rs.getTimestamp("start_date_time").getTime() + rs.getInt("time_to_incubate"))).getTime() : null);
				item.put("eggType", rs.getString("type"));
				item.put("price", null);
				ret.add(item);
				i++;
			}
			var item = new JSONObject();
			item.put("isUsed", false);
			item.put("startDate", null);
			item.put("endDate", null);
			item.put("eggType", null);
			item.put("price", prices[i]);
			ret.add(item);
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
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
	
	@GetMapping("/getAvailableEggs")
	public ArrayList<JSONObject> getAvailableEggs(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
		var ret = new ArrayList<JSONObject>();
		Connection conn = null;
		if(!isConnected(jwt_token, username)) return ret;
		try {
			conn = Database.getConnection();
			var stmt = conn.prepareStatement("SELECT e.id, et.type FROM Egg e INNER JOIN Egg_type et ON e.id_egg_type = et.id INNER JOIN User u ON u.id=e.id_user WHERE username = ? AND e.id NOT IN (SELECT id_egg FROM Incubator WHERE id_egg IS NOT NULL)");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				var item = new JSONObject();
				item.put("id", rs.getInt("id"));
				item.put("eggType", rs.getString("type"));
				ret.add(item);
			}
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
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

	@PostMapping("/setEggInIncubator")
	public String setEggInIncubator(@RequestBody String body) {
		String jwt_token = "";
		String username = "";
		int id_egg = 0;
		int id_incubator = 0;
		try {
			JSONObject json = (JSONObject) new org.json.simple.parser.JSONParser().parse(body);
			jwt_token = (String) json.get("jwt_token");
			username = (String) json.get("username");
			id_egg = ((Long) json.get("id_egg")).intValue();
			id_incubator = ((Long) json.get("id_incubator")).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occured";
		}
		if(!isConnected(jwt_token, username)) return "You are not connected";
		Connection conn = null;
		try {
			conn = Database.getConnection();
			if(conn == null) return "Database connection failed";
			var stmt = conn.prepareStatement("SELECT id FROM Incubator WHERE id_user = (SELECT id FROM User WHERE username = ?) AND id = ? AND id_egg IS NULL");
			stmt.setString(1, username);
			stmt.setInt(2, id_incubator);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) return "You don't have this incubator";
			stmt = conn.prepareStatement("SELECT id FROM Egg WHERE id_user = (SELECT id FROM User WHERE username = ?) AND id = ? AND id_egg_type IS NOT NULL");
			stmt.setString(1, username);
			stmt.setInt(2, id_egg);
			rs = stmt.executeQuery();
			if(!rs.next()) return "You don't have this egg";
			stmt = conn.prepareStatement("UPDATE Incubator SET id_egg = ?, start_date_time = datetime('now', 'localtime') WHERE id = ?");
			stmt.setInt(1, id_egg);
			stmt.setInt(2, id_incubator);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return e.getMessage();
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Egg set";
	}

}
