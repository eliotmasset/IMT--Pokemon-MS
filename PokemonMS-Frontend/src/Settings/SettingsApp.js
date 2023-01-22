import './SettingsApp.css';
import React from 'react';

class SettingsApp extends React.Component {

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
      <div id="SettingsApp" className={className}>
        <div className="backdrop">
            <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
            <img className="littleIco" src="/HomeIco/SettingsIco.png"/>
        </div>


        <div id="SettingsWindow">
        </div>

      </div>
    );
  }
}

export default SettingsApp;