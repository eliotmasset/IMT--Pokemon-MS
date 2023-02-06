import './ChooseTeam.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';
import PokemonItem from './PokemonItem';

const TeamAdress = "http://localhost:8081/team";
const IncubationAdress = "http://localhost:8082/incubation";

class ChooseTeam extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            pokemon_list : []
        }
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

    async selectPokemon(key) {
        let positionAtTheMaxTop = true;
        let position = 1;

        for(let i = 0; i < key; i++) {
            if(this.state.pokemon_list[i] === undefined || this.state.pokemon_list[i] === null) {
                positionAtTheMaxTop = false;
                break;
            };
            position++;
        }
        if(positionAtTheMaxTop) position = key + 1;

        let response = await fetch(IncubationAdress + "/hatchingPokemon",
        { method: "POST", body: JSON.stringify({username: sessionStorage.getItem("username"), jwt_token: sessionStorage.getItem("jwt_token"), incubator_id: this.props.incubatorId, position: position})});
        let data = await response.json();
        if(data !== undefined && data !== null && data.status !== "") {
            if(data.status !== "success") alert(data.message);
            else alert("Pokemon hatched!");
        } else alert("An error occured");
        this.props.setIsDisplayed(false);

    }

    render() {

        this.updateTeam();

        let className = "";
        if(this.props.isDisplayed) className = "displayed";

        return (
            <div id="ChooseTeamWrapper" className={className}>
                <div id="ChooseTeam">
                    <h3> Choose Team </h3>
                    <BackButtonComponent onClick={() => this.props.setIsDisplayed(false)}></BackButtonComponent>
                    <div className="pokemonList">
                        <ul>
                            {
                                this.state.pokemon_list.map((pokemon, key)=> {
                                    let className = "";
                                    return (
                                        <li className={className} key={key}>
                                            <input type="radio" id={key} name="pkm" value={key} onChange={() => {return;}} readOnly/>
                                            {
                                                pokemon === undefined || pokemon === null ? <PokemonItem onClick={()=> this.selectPokemon(key)}/> : <PokemonItem name={pokemon.name} gender={pokemon.gender} level={pokemon.level} onClick={()=> this.selectPokemon(key)}/>
                                            }
                                        </li>
                                    );
                                })
                            }
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default ChooseTeam;