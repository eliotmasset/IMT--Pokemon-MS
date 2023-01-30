package IMT.PokemonMS;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import IMT.PokemonMS.Repository.ShopRepository;
import IMT.PokemonMS.Repository.PokemonTypeRepository;
import IMT.PokemonMS.Model.Shop;
import IMT.PokemonMS.Model.PokemonType;
import java.util.List;

@Configuration
@EnableScheduling
class ShopCron  {

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private PokemonTypeRepository pokemonTypeRepository;
    
	@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")	
	private void reloadShops() {
		List<Shop> shops = shopRepository.findAll();
		for(Shop shop : shops) {
            List<PokemonType> pokemonTypes = pokemonTypeRepository.findAll();
            int randomIndex1 = (int) (Math.random() * pokemonTypes.size());
            int randomIndex2 = (int) (Math.random() * pokemonTypes.size());
            int randomIndex3 = (int) (Math.random() * pokemonTypes.size());
            shop.setPokemonType1(pokemonTypes.get(randomIndex1));
            shop.setPokemonType2(pokemonTypes.get(randomIndex2));
            shop.setPokemonType3(pokemonTypes.get(randomIndex3));
            shop.setPrice1((int) (100 + (Math.random() * 200)));
            shop.setPrice2((int) (100 + (Math.random() * 200)));
            shop.setPrice3((int) (100 + (Math.random() * 200)));
			shopRepository.save(shop);
		}
	}
}
