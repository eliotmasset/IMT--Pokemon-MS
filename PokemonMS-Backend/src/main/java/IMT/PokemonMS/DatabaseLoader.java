package IMT.PokemonMS;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
public class DatabaseLoader implements CommandLineRunner {

	@Override
	public void run(String... strings) throws Exception {
		// this.repository.save(new PokemonType("Pikachu"));
	}
}