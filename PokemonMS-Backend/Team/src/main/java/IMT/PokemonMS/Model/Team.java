package IMT.PokemonMS.Model;

import jakarta.persistence.*;
import IMT.PokemonMS.Model.Pokemon;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @ManyToOne
    private Pokemon pokemon1;
    @ManyToOne
    private Pokemon pokemon2;
    @ManyToOne
    private Pokemon pokemon3;
    @ManyToOne
    private Pokemon pokemon4;
    @ManyToOne
    private Pokemon pokemon5;
    @ManyToOne
    private Pokemon pokemon6;

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

    public Pokemon getPokemon1() {
        return pokemon1;
    }

    public void setPokemon1(Pokemon pokemon) {
        this.pokemon1 = pokemon;
    }

    public Pokemon getPokemon2() {
        return pokemon2;
    }

    public void setPokemon2(Pokemon pokemon) {
        this.pokemon2 = pokemon;
    }

    public Pokemon getPokemon3() {
        return pokemon3;
    }

    public void setPokemon3(Pokemon pokemon) {
        this.pokemon3 = pokemon;
    }

    public Pokemon getPokemon4() {
        return pokemon4;
    }

    public void setPokemon4(Pokemon pokemon) {
        this.pokemon4 = pokemon;
    }

    public Pokemon getPokemon5() {
        return pokemon5;
    }

    public void setPokemon5(Pokemon pokemon) {
        this.pokemon5 = pokemon;
    }

    public Pokemon getPokemon6() {
        return pokemon6;
    }

    public void setPokemon6(Pokemon pokemon) {
        this.pokemon6 = pokemon;
    }

}