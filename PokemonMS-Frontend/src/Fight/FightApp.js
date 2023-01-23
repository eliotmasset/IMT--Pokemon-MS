import './FightApp.css';
import React from 'react';
import BackButtonComponent from "../components/BackButtonComponent";

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
            <div id="blackOpacity">
                <div className="towerLvl">23
                    <h1 className="etage">Etage</h1>
                </div>
                <button className="towerFight">
                    <img className="hide-on-hover" src={'/towerMenuImg/black_swords.png'}/>
                    <img className="show-on-hover" src={'/towerMenuImg/white_swords.png'}/>
                    <h1 className="startFight">Commencer le combat</h1>
                </button>
                <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
            </div>
        </div>

      </div>
    );
  }
}
export default FightApp;