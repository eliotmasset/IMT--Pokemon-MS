package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.Pokemon;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PokemonRepository extends CrudRepository<Pokemon, Integer> {

}