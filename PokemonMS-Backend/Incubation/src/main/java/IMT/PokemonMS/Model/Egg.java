package IMT.PokemonMS.Model;

import jakarta.persistence.*;

@Entity
public class Egg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    private int pokemon;

    private long time_to_incubate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPokemon() {
        return pokemon;
    }

    public void setPokemon(int pokemon) {
        this.pokemon = pokemon;
    }

    public long getTime_to_incubate() {
        return time_to_incubate;
    }

    public void setTime_to_incubate(long time_to_incubate) {
        this.time_to_incubate = time_to_incubate;
    }

}