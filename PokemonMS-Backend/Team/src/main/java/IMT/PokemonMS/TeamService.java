package IMT.PokemonMS;

import IMT.PokemonMS.Model.Team;
import IMT.PokemonMS.Repository.TeamRepository;
import netscape.javascript.JSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team save(Team team) {
        return teamRepository.save(team);
    }
    
    @Autowired
    public void ConsumerService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

}