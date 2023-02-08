package IMT.PokemonMS;

import IMT.PokemonMS.Model.Incubator;
import IMT.PokemonMS.Repository.IncubatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.json.simple.JSONObject;
import org.apache.http.entity.StringEntity;

@Service
public class IncubatorService {

    @Autowired
    private IncubatorRepository incubatorRepository;

    private RabbitTemplate rabbitTemplate;

    public Incubator save(Incubator incubation) {
        return incubatorRepository.save(incubation);
    }
    
    @Autowired
    public IncubatorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
    }

}