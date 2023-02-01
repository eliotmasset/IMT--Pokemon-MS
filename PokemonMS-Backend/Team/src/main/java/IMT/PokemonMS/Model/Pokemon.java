package IMT.PokemonMS.Model;

import jakarta.persistence.*;

@Entity
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int id_pokedex;

    private String name;

    private String english_name;

    private String description;

    private int defense;

    private int attack;

    private int defense_special;

    private int attack_special;

    private int speed;

    private int hp;

    private int level;

    private Boolean is_legendary;

    private Boolean gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_pokedex() {
        return id_pokedex;
    }

    public void setId_pokedex(int id_pokedex) {
        this.id_pokedex = id_pokedex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense_special() {
        return defense_special;
    }

    public void setDefense_special(int defense_special) {
        this.defense_special = defense_special;
    }

    public int getAttack_special() {
        return attack_special;
    }

    public void setAttack_special(int attack_special) {
        this.attack_special = attack_special;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Boolean getIs_legendary() {
        return is_legendary;
    }

    public void setIs_legendary(Boolean is_legendary) {
        this.is_legendary = is_legendary;
    }
    
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
}
