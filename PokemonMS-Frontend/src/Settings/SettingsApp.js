import './SettingsApp.css';
import React from 'react';

class SettingsApp extends React.Component {
  render() {
    return (
      <div id="SettingsApp">
        <div id="backdrop">
            <img class="littleIco" src="/HomeIco/SettingsIco.png"/>
        </div>


        <div id="SettingsWindow">
        </div>

      </div>
    );
  }
}

export default SettingsApp;