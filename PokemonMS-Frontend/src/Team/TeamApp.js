import './TeamApp.css';
import React from 'react';
import PokemonItem from "./PokemonItem";
import BackButtonComponent from "../components/BackButtonComponent";

class TeamApp extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        isDisplayed: null,
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
        let list = this.state.pokemon_list.map((pokemon, key)=> {
            let className = "";
            let checked = key === this.state.pokemon_select;
            if(key === this.state.pokemon_select) className = "selected";
            return (
                <li className={className} key={key}>
                    <input type="radio" id={key} name="pkm" value={key} checked={checked} onChange={() => {return;}} readOnly/>
                    <PokemonItem name={pokemon.name} gender={pokemon.gender} level={pokemon.level} onClick={()=> this.selectPokemon(key)}/>
                </li>
            );
        });
        className = "cardMenu";
        if(this.state.isDisplayed) className = "cardMenu displayed";
        return (
            <div id="TeamApp" className={className}>
                <div className="backdrop">
                    <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
                    <img className="littleIco" src="/HomeIco/TeamIco.png"/>
                </div>


                <div id="TeamWindow">
                    <h3>POKEMON TEAM</h3>
                    <div className="pokemonList">
                        <ul>
                            {list}
                        </ul>
                    </div>
                    <div className="pokemonSpriteDiv">
                        <img className="pokemonSprite" src={"/sprite/" + this.state.pokemon_list[this.state.pokemon_select].engName  + "_sprite.gif"} />
                    </div>
                    <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
                </div>

            </div>
        );
    }
}

export default TeamApp;