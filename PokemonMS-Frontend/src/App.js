import './App.css';
import FightApp from './Fight/FightApp';
import ShopApp from './Shop/ShopApp';
import EggApp from './Egg/EggApp';
import FarmApp from './Farm/FarmApp';

import ProfileApp from './Profile/ProfileApp';
import SettingsApp from './Settings/SettingsApp';
import TeamApp from './Team/TeamApp';

import React from 'react';

class App extends React.Component {
  render() {
    return (
      <div id="App">
        <div className="background"><div></div><div></div><div></div><div></div></div>
        <div className="flex-column">
          <div className="flex-row">
            <FightApp />
            <ShopApp />
          </div>
          <div className="flex-row">
            <EggApp />
            <div className="flex-row flex-max-2">
                <ProfileApp />

                <TeamApp />


                <SettingsApp />
              <div className="temp-cube"/>
            </div>
            <FarmApp />
          </div>
        </div>
      </div>
    );
  }
}

export default App;