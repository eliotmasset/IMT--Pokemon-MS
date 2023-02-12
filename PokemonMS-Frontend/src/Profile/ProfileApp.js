import './ProfileApp.css';
import React from 'react';

const userAdress = "http://localhost:8087/user";

class ProfileApp extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isDisplayed: false,
            money: 0
        }
    }

    setIsDisplayed(isDisplayed) {
        this.setState({isDisplayed: isDisplayed});

        document.getElementById("backWall").style.display = "block";
        this.refreshMoney();
    }

    async refreshMoney() {
        let response = await fetch(userAdress + "/getMoney?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
        let data = await response.json();
        if(data.response != this.state.money) {
            this.setState({money: data.response});
        }
    }

  render() {
      let className = "cardMenu";
      if(this.state.isDisplayed) className = "cardMenu displayed";
      let pseudoUser = sessionStorage.username;
    return (
      <div id="ProfileApp" className={className}>
        <div className="backdrop">
            <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
            <img className="littleIco" src="/HomeIco/ProfileIco.png"/>
        </div>

          <div id="backWall" onClick={() => this.setIsDisplayed(false)}></div>
        <div id="ProfileWindow">
            <img className="icoPoke" src="/ProfilePicture/pokeball.png"/>
            <img className="icoPoke" src="/ProfilePicture/pokeball.png"/>
            <div id="titleProfile">
                <h2> CARTE DE DRESSEUR </h2>
                <div id="backProfile"></div>
            </div>

            <div id="contentProfile">
                <div id="descDress">
                    <div className="oneContent">
                        <p> Nom : </p>
                        <p id="nameProfile"> {pseudoUser} </p>
                    </div>
                    <div className="oneContent">
                        <p> Argent : </p>
                        <p id="moneyProfile">{this.state.money}</p>
                    </div>
                    <div className="oneContent">
                        <p> Etage : </p>
                        <p id="etageProfile"></p>
                    </div>

                    <div id="buttonProfile">
                        <button> ↻ Niveau </button>
                        <button onClick={(event) => this.props.disconnect(event)}> Deconnexion </button>
                    </div>


                </div>

                <div id="icoDress">
                    <img src="/ProfilePicture/Girl.png"/>
                </div>
            </div>



        </div>

      </div>
    );
  }
}

export default ProfileApp;