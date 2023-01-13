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


@SpringBootApplication
@RestController
public class Incubation {

	private static int[] prices = {0, 100, 1000, 10000, 500000, 1000000};

	public static void main(String[] args) {
		SpringApplication.run(Incubation.class, args);
	}

	@GetMapping("/test")
	public Boolean test() {
		return true;
	}

	private static Boolean isConnected(String jwt_token, String username) {
		var ret = false;
		try {
			URL url = new URL("http://localhost:8087/Connected?jwt_token=" + jwt_token+"&username="+username);
			HttpURLConnection req = (HttpURLConnection) url.openConnection();
			req.setRequestMethod("GET");
			req.setRequestProperty("Content-Type", "application/json; utf-8");
			req.setRequestProperty("Accept", "application/json");
			req.setDoOutput(true);
			int status = req.getResponseCode();
			BufferedReader in = new BufferedReader(
			new InputStreamReader(req.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			ret = content.toString().equals("true");
			in.close();
			req.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	@GetMapping("/getIncubatorList")
	public ArrayList<JSONObject> getIncubatorList(@RequestParam(value = "jwt_token") String jwt_token, @RequestParam(value = "username") String username) {
		var ret = new ArrayList<JSONObject>();
		Connection conn = null;
		if(!isConnected(jwt_token, username)) return ret;
		try {
			conn = Database.getConnection();
			var stmt = conn.prepareStatement("SELECT e.time_to_incubate, et.type, i.start_date_time FROM User u INNER JOIN Incubator i ON u.id = i.id_user LEFT JOIN Egg e ON i.id_egg = e.id LEFT JOIN Egg_type et ON e.id_egg_type = et.id INNER JOIN Incubator_type it ON i.id_incubator_type = it.id WHERE username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				var item = new JSONObject();
				item.put("isUsed", true);
				item.put("startDate", rs.getTimestamp("start_date_time").toLocalDateTime());
				item.put("endDate", (new Timestamp(rs.getTimestamp("start_date_time").getNanos() - rs.getInt("time_to_incubate"))).toLocalDateTime());
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
