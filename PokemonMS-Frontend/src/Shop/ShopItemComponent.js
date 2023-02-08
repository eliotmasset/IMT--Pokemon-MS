import React from 'react';
import './ShopItem.css';

const shopAddress = "http://localhost:8084/shop";

export default class ShopItem extends React.Component {

    constructor(props) {
        super(props);
    }

    async buy(event) {
        event.preventDefault();
        if(!event.target.parentElement.parentElement.parentElement.parentElement.classList.contains("selected") 
        && !event.target.parentElement.parentElement.parentElement.parentElement.parentElement.classList.contains("selected") 
        && !event.target.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.classList.contains("selected")) return;
        let username = sessionStorage.getItem("username");
        let jwt_token = sessionStorage.getItem("jwt_token");
        let response = await fetch(shopAddress + "/buyEgg", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token, id_shop: this.props.id})});
        let data = await response.json();
        if(data !== undefined && data !== null && data.status !== "") {
            if(data.status !== "success") alert(data.message);
            else alert("You bought an egg !");
        } else alert("An error occured");
        this.props.updateMoney();
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