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
    let className = "cardParralax";
    if(this.state.isDisplayed) className = "cardParralax displayed";
    return (
      <div id="FightApp" className={className} onClick={() => this.setIsDisplayed(true)}>

        <div className="backdrop">
            <h2> COMBATS </h2>
        </div>


        <div id="FightWindow">
        </div>

      </div>
    );
  }
}
export default FightApp;