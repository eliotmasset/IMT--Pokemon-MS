package IMT.PokemonMS;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private static Connection conn = null;

	@Override
	public void run(String... strings) throws Exception {
		connect();
	}

   public static void connect() {
		try {
			String url = "jdbc:sqlite:pokemonMS.db";
			//String url = "jdbc:sqlite:/mnt/c/pokemonMS.db"; // for wsl development
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static Connection getConnection() {
		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}