import React from 'react';
import './ShopItem.css';
export default class ShopItem extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        const boxGifSrc = "/box_sprite/" + this.props.engName.toLowerCase() + ".png";
        return (
            <div className="shopItem" onClick={() => this.props.onClick()}>
                <ul className="itemInfos">
                    <li><img src={boxGifSrc}/></li>
                    <li className="itemName">{this.props.name}</li>
                    <li><div className="itemPrice">{this.props.price} <div className="validPayement"> <img src="/pokedollars.png"/></div></div></li>
                </ul>
            </div>
        );
    }
}