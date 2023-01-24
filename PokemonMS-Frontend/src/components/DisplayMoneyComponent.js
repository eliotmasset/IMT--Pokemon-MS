import React from 'react';
import './DisplayMoneyComponent.css';
export default class DisplayMoney extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="moneyDiv">
                <ul>
                    <li>
                        <img className="imgPokedollar" src="/pokedollars.png"/>
                    </li>
                    <li className="amountMoney">
                        {this.props.money}
                    </li>
                </ul>
            </div>
        );
    }
}