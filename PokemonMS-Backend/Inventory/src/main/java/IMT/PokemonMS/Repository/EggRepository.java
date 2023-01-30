package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.Egg;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EggRepository extends CrudRepository<Egg, Integer> {

    List<Egg> findAll();
    List<Egg> findByUsername(String username);
    
}