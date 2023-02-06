import './TeamApp.css';
import React from 'react';
import PokemonItem from "./PokemonItem";
import BackButtonComponent from "../components/BackButtonComponent";

const TeamAdress = "http://localhost:8081/team";

class TeamApp extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        isDisplayed: null,
        pokemon_select: 2,
        pokemon_list : []
      }
    }
  
    setIsDisplayed(isDisplayed) {
      this.setState({isDisplayed: isDisplayed});
    }

    selectPokemon(key) {
        this.setState({pokemon_select:key});
    }
    
    async updateTeam() {
        let response = await fetch(TeamAdress + "/getTeam?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
        let data = await response.json();
        let team = [
            data.response.pokemon1,
            data.response.pokemon2,
            data.response.pokemon3,
            data.response.pokemon4,
            data.response.pokemon5,
            data.response.pokemon6
        ];
        if(JSON.stringify(team) !== JSON.stringify(this.state.pokemon_list)) this.setState({pokemon_list: team});
    }
  
    render() {

        this.updateTeam();

        let className = "";
        let list = this.state.pokemon_list.map((pokemon, key)=> {
            let className = "";
            let checked = key === this.state.pokemon_select;
            if(key === this.state.pokemon_select) className = "selected";
            return (
                <li className={className} key={key}>
                    <input type="radio" id={key} name="pkm" value={key} checked={checked} onChange={() => {return;}} readOnly/>
                    {
                        pokemon === undefined || pokemon === null ? <div></div> : <PokemonItem name={pokemon.name} gender={pokemon.gender} level={pokemon.level} onClick={()=> this.selectPokemon(key)}/>
                    }
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
                        {
                            this.state.pokemon_list[this.state.pokemon_select] === undefined || this.state.pokemon_list[this.state.pokemon_select] === null ? <img className="pokemonSprite" src="/sprite/unknown_sprite.gif"/> : <img className="pokemonSprite" src={"/sprite/"+this.state.pokemon_list[this.state.pokemon_select].english_name+"_sprite.gif"}/>
                        }
                    </div>
                    <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
                </div>

            </div>
        );
    }
}

export default TeamApp;