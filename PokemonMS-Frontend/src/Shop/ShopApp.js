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
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    return (
      <div id="ShopApp" className={className}>
        <div className="backdrop">
          <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
          <h2> SHOP </h2>
        </div>


        <div id="ShopWindow">
        </div>

      </div>
    );
  }
}

export default ShopApp;