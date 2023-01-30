package IMT.PokemonMS;

import IMT.PokemonMS.Model.Egg;
import IMT.PokemonMS.Repository.EggRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EggService {

    @Autowired
    private EggRepository eggRepository;

    public Egg save(Egg egg) {
        return eggRepository.save(egg);
    }
}