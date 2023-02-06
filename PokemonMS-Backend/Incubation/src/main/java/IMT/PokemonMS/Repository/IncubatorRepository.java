package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.Incubator;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface IncubatorRepository extends CrudRepository<Incubator, Integer> {

    List<Incubator> findByUsername(String username);
    int countByUsername(String username);
    Incubator findById(int id);

}