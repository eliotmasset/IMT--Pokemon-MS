import './FarmApp.css';
import React from 'react';

class FarmApp extends React.Component {
  
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
      <div id="FarmApp" className={className} onClick={() => this.setIsDisplayed(true)}>
        <div id="backdrop">
          <h2> FARM </h2>
        </div>


        <div id="FarmWindow">
        </div>

      </div>
    );
  }
}

export default FarmApp;