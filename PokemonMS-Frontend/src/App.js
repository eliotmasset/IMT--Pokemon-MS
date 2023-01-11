import './App.css';
import FightApp from './Fight/FightApp';
import ShopApp from './Shop/ShopApp';
import EggApp from './Egg/EggApp';
import FarmApp from './Farm/FarmApp';

import ProfileApp from './Profile/ProfileApp';
import SettingsApp from './Settings/SettingsApp';
import TeamApp from './Team/TeamApp';

import React from 'react';

const UserAdress = "http://localhost:8087";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      connected: false,
      inscriptionPage: false,
      jwt_token_is_verified: false
    }
  }

  async connect(event) {
    event.preventDefault();
    event.stopPropagation();
    let form = document.querySelector("form");
    let button = form.querySelector("button");
    button.classList.add("loadingButton");
    var lastHTML = button.innerHTML;
    button.innerHTML = "<div class='loadingButtonSpinner'><div></div><div></div></div>";
    let formData = new FormData(form);
    let username = formData.get("username");
    let password = formData.get("password");
    let response = await fetch(UserAdress + "/Connect?username=" + username + "&password=" + password);
    if(response === undefined || response === null) {
      alert("An error occured");
      button.classList.remove("loadingButton");
      button.innerHTML = lastHTML;
      return false;
    }
    let data = await response.text();
    if(data !== undefined && data !== null && data !== "" && data.slice(0, 5) !== "Error") {
      sessionStorage.setItem("jwt_token", data);
      response = await fetch(UserAdress + "/Connected?jwt_token=" + sessionStorage.getItem("jwt_token"));
      data = await response.text();
      if(data == "true" && this.state.connected == false) this.setState({connected: true, jwt_token_is_verified: true});
      else if(data == "false" && this.state.connected == true) this.setState({connected: false, jwt_token_is_verified: true});
      else if(this.state.jwt_token_is_verified == false) this.setState({jwt_token_is_verified: true});
    } else {
      document.querySelectorAll("input[type='password']").forEach((input) => input.value = "");
      if(data !== undefined && data !== null && data !== "") alert(data.slice(6));
      else alert("An error occured");
    }
    button.innerHTML = lastHTML;
    button.classList.remove("loadingButton");
    return false;
  }

  async disconnect(event) {
    event.preventDefault();
    event.stopPropagation();
    sessionStorage.removeItem("jwt_token");
    this.setState({connected: false, jwt_token_is_verified: true});
    return false;
  }

  async subscrib(event) {
    event.preventDefault();
    event.stopPropagation();
    let form = document.querySelector("form");
    let button = form.querySelector("button");
    button.classList.add("loadingButton");
    var lastHTML = button.innerHTML;
    button.innerHTML = "<div class='loadingButtonSpinner'><div></div><div></div></div>";
    let formData = new FormData(form);
    let username = formData.get("username");
    let password = formData.get("password");
    let password2 = formData.get("password-retyped");
    if(password !== password2) {
      alert("Passwords are not the same");
      button.classList.remove("loadingButton");
      button.innerHTML = lastHTML;
      return false;
    }

    let response = await fetch(UserAdress + "/Subscribe?username=" + username + "&password=" + password + "&gender=0");
    if(response === undefined || response === null) {
      alert("An error occured");
      button.classList.remove("loadingButton");
      button.innerHTML = lastHTML;
      return false;
    }
    let data = await response.text();
    if(data !== undefined && data !== null && data !== "" && data.slice(0, 5) !== "Error") {
      sessionStorage.setItem("jwt_token", data);
      response = await fetch(UserAdress + "/Connected?jwt_token=" + sessionStorage.getItem("jwt_token"));
      data = await response.text();
      if(data == "true" && this.state.connected == false) this.setState({connected: true, jwt_token_is_verified: true});
      else if(data == "false" && this.state.connected == true) this.setState({connected: false, jwt_token_is_verified: true});
      else if(this.state.jwt_token_is_verified == false) this.setState({jwt_token_is_verified: true});
    } else {
      document.querySelectorAll("input[type='password']").forEach((input) => input.value = "");
      if(data !== undefined && data !== null && data !== "") alert(data.slice(6));
      else alert("An error occured");
    }
    button.classList.remove("loadingButton");
    button.innerHTML = lastHTML;
    return false;
  }

  render() {
    ( async () => {
        if(sessionStorage.getItem("jwt_token") !== undefined && sessionStorage.getItem("jwt_token") !== null && sessionStorage.getItem("jwt_token") !== "") {
          let response = await fetch(UserAdress + "/Connected?jwt_token=" + sessionStorage.getItem("jwt_token"));
          let data = await response.text();
          if(data == "true" && this.state.connected == false) this.setState({connected: true, jwt_token_is_verified: true});
          else if(data == "false" && this.state.connected == true) this.setState({connected: false, jwt_token_is_verified: true});
          else if(this.state.jwt_token_is_verified == false) this.setState({jwt_token_is_verified: true});
        } else if(this.state.jwt_token_is_verified == false) this.setState({jwt_token_is_verified: true});
      }
    )();
    if(!this.state.jwt_token_is_verified || (this.state.connected && this.state.jwt_token_is_verified)) return (
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
    else if(!this.state.inscriptionPage) return (
      <div id="App">
        <div className="background"><div></div><div></div><div></div><div></div></div>
        <div className="login">
          <form>
            <h2>Welcome</h2>
            <div className="underline-connection"></div>
            <input type="text" placeholder="Username" name="username" />
            <input type="password" placeholder="Password" name="password" />
            <button type="submit" onClick={async (e) => this.connect(e)}>Connexion</button>
            <div className="underline-form-connection"></div>
            <p>Not already subscribe ?</p>
            <a onClick={() => this.setState({inscriptionPage: true})}>Subscribe</a>
          </form>
        </div>
      </div>
    );
    else if(this.state.inscriptionPage) return (
      <div id="App">
        <div className="background"><div></div><div></div><div></div><div></div></div>
        <div className="login">
          <form>
            <h2>Welcome</h2>
            <div className="underline-connection"></div>
            <input type="text" placeholder="Username" name="username" />
            <input type="password" placeholder="Password" name="password" />
            <input type="password" placeholder="Password (re-type)" name="password-retyped" />
            <button type="submit" onClick={async (e) => this.subscrib(e)}>Subscribe</button>
            <div className="underline-form-connection"></div>
            <p>Want to connect ?</p>
            <a onClick={() => this.setState({inscriptionPage: false})}>Connexion</a>
          </form>
        </div>
      </div>
    );

  }
}

export default App;