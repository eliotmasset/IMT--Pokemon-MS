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
    let className = "cardParralax";
    if(this.state.isDisplayed) className = "cardParralax displayed";
    return (
      <div id="SettingsApp" className={className} onClick={() => this.setIsDisplayed(true)}>
        <div className="backdrop">
            <img className="littleIco" src="/HomeIco/SettingsIco.png"/>
        </div>


        <div id="SettingsWindow">
        </div>

      </div>
    );
  }
}

export default SettingsApp;