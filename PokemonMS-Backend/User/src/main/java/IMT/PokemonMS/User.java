package IMT.PokemonMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import IMT.PokemonMS.Database;
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
import java.sql.ResultSet;
import java.sql.Connection;


@SpringBootApplication
@RestController
public class User {
	
	private transient byte[] keyHMAC = "f032932c1cebbd5d6e2b6b48e45ea772f80e998b32dcc21f74ac1b3adf197871".getBytes();
	private transient int iterations = 10000;
	private transient int keyLength = 100;

	public static void main(String[] args) {
		SpringApplication.run(User.class, args);
	}

	@GetMapping("/test")
	public Boolean test() {
		return true;
	}

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
            byte[] res = key.getEncoded( );
            return res;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

	@GetMapping("/Subscribe")
	public String subscribe(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("gender") Boolean gender) {
		// Check if the database is connected
		Connection conn = Database.getConnection();
		if(conn == null) return "Error Database not connected";
		var jwt_token = "";

		try {
			// Check if the username is already taken
			var stmt = conn.prepareStatement("SELECT * FROM User WHERE username = ?");
			stmt.setString(1, username);
			var rs = stmt.executeQuery();

			if(rs.next()) {
				conn.close();
				return "Error Username already taken";
			}

			//Hash Password
			String salt = this.nextString();
			char[] passwordChars = password.toCharArray();
			byte[] saltBytes = salt.getBytes();

			byte[] hashedBytes = this.hashPassword(passwordChars, saltBytes, this.iterations, this.keyLength);
			String hashedString = Hex.encodeHexString(hashedBytes);

			var query = "INSERT INTO Trainer (name, gender, money_to_give) VALUES (?, ?, ?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, "");
			stmt.setInt(2, gender ? 1 : 0);
			stmt.setInt(3, 0);
			stmt.executeUpdate();
			long trainerId = 0;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					trainerId = generatedKeys.getLong(1);
				}
				else {
					throw new Exception("Creating trainer failed, no ID obtained.");
				}
			}

			// Insert the user in the database
			query = "INSERT INTO User (username, password, salt, id_trainer, money) VALUES (?, ?, ?, ?, 0)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, hashedString);
			stmt.setString(3, salt);
			stmt.setLong(4, trainerId);
			stmt.executeUpdate();
			long insertId = 0;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					insertId = generatedKeys.getLong(1);
				}
				else {
					throw new Exception("Creating trainer failed, no ID obtained.");
				}
			}

			conn.close();

			jwt_token = Jwts.builder()
				.setSubject(username)
				.claim("id", insertId)
				.claim("end_date", System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))
				.signWith(Keys.hmacShaKeyFor(keyHMAC), SignatureAlgorithm.HS256)
				.compact();

		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return "Error "+e.getMessage();
		}

		return jwt_token;
	}

	@GetMapping("/Connect")
	public String connect(@RequestParam("username") String username, @RequestParam("password") String password) {
		// Check if the database is connected
		Connection conn = Database.getConnection();
		if(conn == null) return null;
		String jwt_token = null;

		try {
			var query = "SELECT * FROM User WHERE username = ?";
			var stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			var rs = stmt.executeQuery();
			
			if(!rs.next()) {
				conn.close();
				return "Error Username not found";
			}

			//Hash Password
			String salt = rs.getString("salt");
			char[] passwordChars = password.toCharArray();
			byte[] saltBytes = salt.getBytes();

			byte[] hashedBytes = this.hashPassword(passwordChars, saltBytes, this.iterations, this.keyLength);
			String hashedString = Hex.encodeHexString(hashedBytes);

			if(!hashedString.equals(rs.getString("password"))) {
				conn.close();
				return "Error Wrong password";
			}


			jwt_token = Jwts.builder()
				.setSubject(username)
				.claim("id", rs.getLong("id"))
				.claim("end_date", System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))
				.signWith(Keys.hmacShaKeyFor(keyHMAC), SignatureAlgorithm.HS256)
				.compact();
			
			conn.close();

		} catch (Exception e) {
			System.out.println("Error"+e.getMessage());
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return "Error"+e.getMessage();
		}

		return jwt_token;
	}

	@GetMapping("/GetUsername")
	public String getUsername(@RequestParam("jwt_token") String jwt_token) {
		Connection conn = null;
		try {
			// Check end date 
			var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();
			if(claims.get("end_date") == null || claims.get("id") == null) return null;
			if((long)claims.get("end_date") < System.currentTimeMillis()) return null;
			if((int)claims.get("id") < 0) return null;
			conn = Database.getConnection();
			if(conn == null) return null;
			var query = "SELECT username FROM User WHERE id = ?";
			var stmt = conn.prepareStatement(query);
			stmt.setLong(1, (int)claims.get("id"));
			var rs = stmt.executeQuery();
			if(!rs.next()) {
				conn.close();
				return null;
			}
			String username = rs.getString("username");
			conn.close();
			return username;
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return "Error: "+e.getMessage();
		}
	}

	@GetMapping("/GetMoney")
	public int getMoney(@RequestParam("jwt_token") String jwt_token) {
		Connection conn = null;
		// Check if the token is valid
		try {
			Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token);
			// Check end date 
			var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();
			if(claims.get("end_date") == null || claims.get("id") == null) return -1;
			if((long)claims.get("end_date") < System.currentTimeMillis()) return -1;
			if((int)claims.get("id") < 0) return -1;
			conn = Database.getConnection();
			if(conn == null) return -1;
			var query = "SELECT money FROM User WHERE id = ?";
			var stmt = conn.prepareStatement(query);
			stmt.setLong(1, (int)claims.get("id"));
			var rs = stmt.executeQuery();
			if(!rs.next()) {
				conn.close();
				return -1;
			}
			int money = rs.getInt("money");
			conn.close();
			return money;
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return -1;
		}
	}

	@GetMapping("/Connected")
	public Boolean Connected(@RequestParam("jwt_token") String jwt_token) {
		Connection conn = null;
		// Check if the token is valid
		try {
			Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token);
			// Check end date 
			var claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(keyHMAC)).parseClaimsJws(jwt_token).getBody();
			if(claims.get("end_date") == null || claims.get("id") == null) return false;
			if((long)claims.get("end_date") < System.currentTimeMillis()) return false;
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

}
