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
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    return (
      <div id="FarmApp" className={className}>
        <div className="backdrop">
          <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
          <h2> FARM </h2>
        </div>


        <div id="FarmWindow">
        </div>

      </div>
    );
  }
}

export default FarmApp;