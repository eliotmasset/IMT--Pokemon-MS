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
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    return (
      <div id="FightApp" className={className}>

        <div className="backdrop">
            <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
            <h2> COMBATS </h2>
        </div>


        <div id="FightWindow">
        </div>

      </div>
    );
  }
}
export default FightApp;