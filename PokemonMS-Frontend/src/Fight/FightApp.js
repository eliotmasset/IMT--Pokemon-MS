import './FightApp.css';
import React from 'react';

class FightApp extends React.Component {

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
      <div id="FightApp" className={className} onClick={() => this.setIsDisplayed(true)}>

        <div id="backdrop">
            <h2> COMBATS </h2>
        </div>


        <div id="FightWindow">
        </div>

      </div>
    );
  }
}
export default FightApp;