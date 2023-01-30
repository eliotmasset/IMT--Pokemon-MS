package IMT.PokemonMS.Model;

import jakarta.persistence.*;
import IMT.PokemonMS.Model.Egg;
import java.time.LocalDateTime;

@Entity
public class Incubator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String speed;

    @OneToOne
    private Egg egg;

    private String username;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime start_date_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Egg getEgg() {
        return egg;
    }

    public void setEgg(Egg egg) {
        this.egg = egg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStart_date_time() {
        return start_date_time;
    }

    public void setStart_date_time(LocalDateTime start_date_time) {
        this.start_date_time = start_date_time;
    }

}