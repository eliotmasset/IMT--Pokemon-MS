package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.PokemonType;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PokemonTypeRepository extends CrudRepository<PokemonType, Integer> {

    List<PokemonType> findAll();
    PokemonType findById(int id);
    
}