import React from 'react';
import './ShopItem.css';
export default class ShopItem extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="shopItem" onClick={() => this.props.onClick()}>
                <ul className="itemInfos">
                    <li className="itemName">{this.props.name}</li>
                    <li><div className="itemPrice">{this.props.price}P</div></li>
                </ul>
            </div>
        );
    }
}