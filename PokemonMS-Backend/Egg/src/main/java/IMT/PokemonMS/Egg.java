package IMT.PokemonMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Egg {

	public static void main(String[] args) {
		SpringApplication.run(Egg.class, args);
	}

	@GetMapping("/test")
	public Boolean test() {
		return true;
	}

}
