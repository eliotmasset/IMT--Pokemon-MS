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
public class Database implements CommandLineRunner {

	static String url = "jdbc:sqlite:../pokemonMS.db";
	//static String url = "jdbc:sqlite:/mnt/c/pokemonMS.db"; // for wsl development

	@Override
	public void run(String... strings) throws Exception {
		System.out.println("Testing database connection...");
		var conn = getConnection();
		if (conn != null) {
			System.out.println("Connection to SQLite has been established.");
			conn.close();
		} else {
			System.out.println("Connection to SQLite has failed.");
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
}