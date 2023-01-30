package IMT.PokemonMS.Model;

import jakarta.persistence.*;
import IMT.PokemonMS.Model.PokemonType;
import IMT.PokemonMS.Repository.PokemonTypeRepository;
import java.util.List;


@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    @ManyToOne
    private PokemonType pokemonType1;

    @ManyToOne
    private PokemonType pokemonType2;

    @ManyToOne
    private PokemonType pokemonType3;

    private int price1;

    private int price2;

    private int price3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PokemonType getPokemonType1() {
        return pokemonType1;
    }

    public void setPokemonType1(PokemonType pokemonType1) {
        this.pokemonType1 = pokemonType1;
    }

    public PokemonType getPokemonType2() {
        return pokemonType2;
    }

    public void setPokemonType2(PokemonType pokemonType2) {
        this.pokemonType2 = pokemonType2;
    }

    public PokemonType getPokemonType3() {
        return pokemonType3;
    }

    public void setPokemonType3(PokemonType pokemonType3) {
        this.pokemonType3 = pokemonType3;
    }

    public int getPrice1() {
        return price1;
    }

    public void setPrice1(int price1) {
        this.price1 = price1;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public int getPrice3() {
        return price3;
    }

    public void setPrice3(int price3) {
        this.price3 = price3;
    }

}