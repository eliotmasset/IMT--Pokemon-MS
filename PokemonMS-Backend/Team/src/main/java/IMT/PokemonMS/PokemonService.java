package IMT.PokemonMS;

import IMT.PokemonMS.Model.Pokemon;
import IMT.PokemonMS.Repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;

    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }
}