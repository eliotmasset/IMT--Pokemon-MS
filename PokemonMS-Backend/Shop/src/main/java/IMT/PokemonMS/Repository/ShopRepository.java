package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.Shop;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ShopRepository extends CrudRepository<Shop, Integer> {

    List<Shop> findAll();
    Shop findByUsername(String username);
    
}