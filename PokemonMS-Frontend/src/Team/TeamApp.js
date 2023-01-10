import './TeamApp.css';
import React from 'react';
import PokemonItem from "./PokemonItem";
import BackButtonComponent from "./BackButtonComponent";

class TeamApp extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        isDisplayed: false,
          pokemon_select: 2,
          pokemon_list : [
              {name:"darkrai", engName:"darkrai", gender:"no", level:"100"},
              {name:"palkia", engName:"palkia", gender:"no", level:"100"},
              {name:"dialga", engName:"dialga", gender:"no", level:"100"},
              {name:"tortipouss", engName:"turtwig", gender:"male", level:"10"},
              {name:"tiplouf", engName:"piplup", gender:"female", level:"10"},
              {name:"ouisticram", engName:"chimchar", gender:"male", level:"10"}
          ]
      }
    }
  
    setIsDisplayed(isDisplayed) {
      this.setState({isDisplayed: isDisplayed});
    }
    selectPokemon(key) {
        this.setState({pokemon_select:key});
    }
  
    render() {
      let className = "";
        let sprite = require("./assets/sprite/" + this.state.pokemon_list[this.state.pokemon_select].engName  + "_sprite.gif");
        let list = this.state.pokemon_list.map((pokemon, key)=> {
            let className = "";
            let checked = key === this.state.pokemon_select;
            if(key === this.state.pokemon_select) className = "selected";
            return (
                <li className={className} key={key}>
                    <input type="radio" id={key} name="pkm" value={key} checked={checked}/>
                    <PokemonItem name={pokemon.name} gender={pokemon.gender} level={pokemon.level} onClick={()=> this.selectPokemon(key)}/>
                </li>
            );
        });
      if(this.state.isDisplayed) className = "displayed";
        return (
            <div id="TeamApp" className={className} onClick={() => this.setIsDisplayed(true)}>
                <div id="backdrop">
                    <img class="littleIco" src="/HomeIco/TeamIco.png"/>
                </div>


                <div id="TeamWindow">
                    <div className="pokemonList">
                        <ul>
                            {list}
                        </ul>
                    </div>
                    <div className="pokemonSpriteDiv">
                        <img className="pokemonSprite" src={sprite} />
                    </div>
                    <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
                </div>

            </div>
        );
    }
}

export default TeamApp;