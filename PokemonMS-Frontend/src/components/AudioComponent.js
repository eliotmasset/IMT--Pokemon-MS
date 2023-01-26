import React from "react";
import './AudioComponent.css';

export default class AudioComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isPlaying: false
        };
    }

    playAudio(e, play) {
        e.preventDefault();
        if(play) {
            document.getElementById("audio").play();
            this.setState({isPlaying: true});
        } else {
            document.getElementById("audio").pause();
            this.setState({isPlaying: false});
        }
    }

    render(){
        return (
            <div id="audio_button">
                <audio id="audio" src={this.props.src} loop></audio>
                { this.state.isPlaying ? <button onClick={(e) => this.playAudio(e, false)}><img src="/audio_on.svg"></img></button> : <button onClick={(e) => this.playAudio(e, true)}><img src="/audio_off.svg"></img></button> }
            </div>
        );
    }
}

