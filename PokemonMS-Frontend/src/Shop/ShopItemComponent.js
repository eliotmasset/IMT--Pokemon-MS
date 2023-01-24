import React from 'react';
import './ShopItem.css';

const transactionAddress = "http://localhost:8086";

export default class ShopItem extends React.Component {

    constructor(props) {
        super(props);
    }

    async buy(event) {
        event.preventDefault();
        if(!event.target.parentElement.parentElement.parentElement.parentElement.classList.contains("selected") && !event.target.parentElement.parentElement.parentElement.classList.contains("selected")) return;
        let username = sessionStorage.getItem("username");
        let jwt_token = sessionStorage.getItem("jwt_token");
        if(this.props.id <= 3) {
            let response = await fetch(transactionAddress + "/buyEggInShop", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token, egg_type: this.props.id})});
            let data = await response.text();
            if(data !== undefined && data !== null && data !== "") {
                if(data !== "Egg bought") alert(data);
                else alert("You bought an egg !");
            } else alert("An error occured");
        } else {
            let response = await fetch(transactionAddress + "/buyPokemonInShop", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token, pokemon_store: this.props.id - 3})});
            let data = await response.text();
            if(data !== undefined && data !== null && data !== "") {
                if(data !== "Egg bought") alert(data);
                else alert("You bought an egg !");
            } else alert("An error occured");
        }
        return true;
    }

    render() {
        const boxGifSrc = "/box_sprite/" + this.props.engName.toLowerCase() + ".png";
        return (
            <div className="shopItem" onClick={() => this.props.onClick()}>
                <ul className="itemInfos">
                    <li><img src={boxGifSrc}/></li>
                    <li className="itemName">{this.props.name}</li>
                    <li onClick={async (e) => this.buy(e)}><div className="itemPrice">{this.props.price} <div className="validPayement"> <img src="/pokedollars.png"/></div></div></li>
                </ul>
            </div>
        );
    }
}