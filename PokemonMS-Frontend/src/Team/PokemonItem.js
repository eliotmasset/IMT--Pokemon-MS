import React from 'react';
import './PokemonItem.css';
export default class PokemonItem extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        let genderLi;
        if (this.props.gender === "male") {
            genderLi = <li><img className="gender male" src='/male.png'/></li>;
        } else if (this.props.gender === "female") {
            genderLi = <li><img className="gender female" src='/female.png'/></li>
        } else {
            genderLi = <li></li>
        }
        const boxGifSrc = "/box_sprite/" + this.props.engName.toLowerCase() + ".png";
        return (
            <div className="pokemonItem" onClick={() => this.props.onClick()}>
                <ul className="pokemonInfos">
                    {this.props.level ? <li><img className="pokemonBoxSprite" src={boxGifSrc}/></li> : <li></li>}
                    <li className="pokemonName">{this.props.name}</li>
                    {genderLi}
                    {this.props.level ? <li>Lv. {this.props.level}</li> : <li></li>}
                </ul>
            </div>
        );
    }
}