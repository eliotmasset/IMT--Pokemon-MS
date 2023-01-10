import './TeamApp.css';
import React from 'react';

class TeamApp extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        isDisplayed: false
      }
    }
  
    setIsDisplayed(isDisplayed) {
      this.setState({isDisplayed: isDisplayed});
    }
  
    render() {
      let className = "";
      if(this.state.isDisplayed) className = "displayed";
        return (
            <div id="TeamApp" className={className} onClick={() => this.setIsDisplayed(true)}>
                <div id="backdrop">
                    <img class="littleIco" src="/HomeIco/TeamIco.png"/>
                </div>


                <div id="TeamWindow">
                </div>

            </div>
        );
    }
}

export default TeamApp;