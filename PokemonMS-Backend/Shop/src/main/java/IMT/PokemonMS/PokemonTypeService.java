package IMT.PokemonMS;

import IMT.PokemonMS.Model.PokemonType;
import IMT.PokemonMS.Repository.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PokemonTypeService {

    @Autowired
    private PokemonTypeRepository pokemonTypeRepository;

    public PokemonType save(PokemonType pokemonType) {
        return pokemonTypeRepository.save(pokemonType);
    }
}