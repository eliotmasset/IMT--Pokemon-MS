package IMT.PokemonMS;

import IMT.PokemonMS.Model.Incubator;
import IMT.PokemonMS.Repository.IncubatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IncubatorService {

    @Autowired
    private IncubatorRepository incubatorRepository;

    public Incubator save(Incubator incubation) {
        return incubatorRepository.save(incubation);
    }
}