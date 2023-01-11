import React from "react";
import './BackButtonComponent.css';


export default class BackButtonComponent extends React.Component {
    constructor(props) {
        super(props);
    }
    render(){
        return (
            <button className="backButton" onClick={this.props.onClick}>&#60;</button>
        );
    }
}