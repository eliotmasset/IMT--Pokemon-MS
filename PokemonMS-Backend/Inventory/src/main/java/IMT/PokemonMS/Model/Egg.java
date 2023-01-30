package IMT.PokemonMS.Model;

import jakarta.persistence.*;

@Entity
public class Egg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String type;

    private int pokemon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}