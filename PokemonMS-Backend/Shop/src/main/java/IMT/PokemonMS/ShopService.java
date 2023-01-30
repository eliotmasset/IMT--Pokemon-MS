package IMT.PokemonMS;

import IMT.PokemonMS.Model.Shop;
import IMT.PokemonMS.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }
}