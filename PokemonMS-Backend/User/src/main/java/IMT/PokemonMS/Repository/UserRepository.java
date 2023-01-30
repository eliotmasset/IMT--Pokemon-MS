package IMT.PokemonMS.Repository;

import IMT.PokemonMS.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}