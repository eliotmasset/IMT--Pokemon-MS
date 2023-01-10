import './EggApp.css';
import React from 'react';

class EggApp extends React.Component {
  
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
      <div id="EggApp" className={className} onClick={() => this.setIsDisplayed(true)}>
        <div id="backdrop">
          <h2> PENSION </h2>
        </div>


        <div id="EggWindow">
        </div>

      </div>
    );
  }
}

export default EggApp;