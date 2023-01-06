package IMT.PokemonMS;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileReader;
import java.io.BufferedReader;

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

			Statement stmt = conn.createStatement();
			FileReader reader = new FileReader("start.sql");
			BufferedReader buffer = new BufferedReader(reader);
			String str = "", sql = "";
            while ((str = buffer.readLine()) != null) {
				sql += str + "\n";
				if(str.indexOf(';') == -1) continue;
				stmt.execute(sql);
				sql = "";
			}
			reader.close();

			System.out.println("Database created and populated with success.");

		} catch (Exception e) {
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