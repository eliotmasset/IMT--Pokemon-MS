import React from "react";
import './BackButtonComponent.css';


export default class BackButtonComponent extends React.Component {
    constructor(props) {
        super(props);
    }
    render(){
        return (
            <div className="backButton" onClick={this.props.onClick}>
                <a>
                    <p>Retour</p>
                </a>
            </div>
        );
    }
}