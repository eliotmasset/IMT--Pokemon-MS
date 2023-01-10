import './ShopApp.css';
import React from 'react';

class ShopApp extends React.Component {

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
      <div id="ShopApp" className={className} onClick={() => this.setIsDisplayed(true)}>
        <div id="backdrop">
          <h2> SHOP </h2>
        </div>


        <div id="ShopWindow">
        </div>

      </div>
    );
  }
}

export default ShopApp;