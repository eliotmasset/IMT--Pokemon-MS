import './App.css';
import FightApp from './Fight/FightApp';
import ShopApp from './Shop/ShopApp';
import EggApp from './Egg/EggApp';
import FarmApp from './Farm/FarmApp';
import React from 'react';

class App extends React.Component {
  render() {
    return (
      <div id="App">
        <div class="background"><div></div><div></div><div></div><div></div></div>
        <div className="flex-column">
          <div className="flex-row">
            <FightApp />
            <ShopApp />
          </div>
          <div className="flex-row">
            <EggApp />
            <div className="flex-row flex-max-2">
              <div className="temp-cube"></div>
              <div className="temp-cube"></div>
              <div className="temp-cube"></div>
              <div className="temp-cube"></div>
            </div>
            <FarmApp />
          </div>
        </div>
      </div>
    );
  }
}

export default App;