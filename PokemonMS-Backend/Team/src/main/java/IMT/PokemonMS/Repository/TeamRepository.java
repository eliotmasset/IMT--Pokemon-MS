package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.Team;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    Team findByUsername(String username);
}