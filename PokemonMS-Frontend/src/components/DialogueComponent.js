import React from "react";
import './DialogueComponent.css';


export default class DialogueComponent extends React.Component {
    constructor(props) {
        super(props);
    }
    render(){
        let className = "";
        if(this.props.isDisplay) className="isDisplay";
        return (
            <div className="pokemon-dialog">
                <p>{this.props.text}</p>
            </div>
        );
    }
}