package IMT.PokemonMS;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
@EnableScheduling
class StoreCron  {
    
	@Scheduled(cron = "0 * * * * *", zone = "Europe/Paris")	
	private void reloadStores() {
		Connection conn = null;
		try {
			conn = Database.getConnection();
			if(conn == null) return;
			var stmt = conn.prepareStatement("SELECT * FROM Store");
			var rs = stmt.executeQuery();
			var stmt2 = conn.prepareStatement("SELECT * FROM Pokemon_type ORDER BY RANDOM() LIMIT 3");
			var rs2 = stmt2.executeQuery();
			while(rs.next()) {
				var stmt3 = conn.prepareStatement("UPDATE Store SET id_pokemon_type_1 = ?, id_pokemon_type_2 = ?, id_pokemon_type_3 = ?, price_1 = ?, price_2 = ?, price_3 = ?");
				rs2.next();
				stmt3.setInt(1, rs2.getInt("id"));
				rs2.next();
				stmt3.setInt(2, rs2.getInt("id"));
				rs2.next();
				stmt3.setInt(3, rs2.getInt("id"));
				stmt3.setInt(4, (int)( 100 + (int)(Math.random() * (200 + 1))));
				stmt3.setInt(5, (int)( 100 + (int)(Math.random() * (200 + 1))));
				stmt3.setInt(6, (int)( 100 + (int)(Math.random() * (200 + 1))));
				stmt3.executeUpdate();
			}
			conn.close();
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
	}
}
