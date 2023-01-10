import './ProfileApp.css';
import React from 'react';

class ProfileApp extends React.Component {
  render() {
    return (
      <div id="ProfileApp">
        <div id="backdrop">
            <img class="littleIco" src="/HomeIco/ProfileIco.png"/>
        </div>


        <div id="ProfileWindow">
        </div>

      </div>
    );
  }
}

export default ProfileApp;