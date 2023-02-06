package IMT.PokemonMS.Model;

import jakarta.persistence.*;
import org.json.simple.JSONObject;

@Entity
@Table(name = "\"PokemonType\"")
public class PokemonType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    

    private String name;

    private String english_name;

    private String description;

    private int defense;

    private int attack;

    private int defense_special;

    private int attack_special;

    private int speed;

    private int hp;

    private Boolean is_legendary;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("english_name", this.english_name);
        json.put("description", this.description);
        json.put("defense", this.defense);
        json.put("attack", this.attack);
        json.put("defense_special", this.defense_special);
        json.put("attack_special", this.attack_special);
        json.put("speed", this.speed);
        json.put("hp", this.hp);
        json.put("is_legendary", this.is_legendary);
        return json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean getIs_legendary() {
        return is_legendary;
    }

    public void setIs_legendary(Boolean is_legendary) {
        this.is_legendary = is_legendary;
    }

}
