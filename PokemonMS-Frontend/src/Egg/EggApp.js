import './EggApp.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';
import ChooseEgg from './ChooseEgg';
import DisplayMoney from '../components/DisplayMoneyComponent';
import ChooseTeam from './ChooseTeam';

const IncubatorAdress = "http://localhost:8082/incubation";
const userAdress = "http://localhost:8087/user";

class EggApp extends React.Component {
  
	pricesIncubators = [0, 100, 1000, 10000, 500000, 1000000];

  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      incubators: [],
      isChooseEggDisplayed: false,
      isChooseTeamDisplayed: false,
      incubatorId: 0,
      money: 0,
      last_update: new Date()
    }
  }

  getEggPrice(egg_type) {
    if(egg_type === "common") return 180;
    if(egg_type === "rare") return 330;
    if(egg_type === "epic") return 480;
    return 0;
  }

  setIsDisplayed(isDisplayed) {
    this.setState({isDisplayed: isDisplayed, last_update: new Date()});
  }

  setChooseEggDisplayed(isDisplayed, incubatorId) {
    this.setState({isChooseEggDisplayed: isDisplayed, incubatorId: incubatorId, last_update: new Date()});
  }

  setChooseTeamDisplayed(isDisplayed, incubatorId) {
    this.setState({isChooseTeamDisplayed: isDisplayed, incubatorId: incubatorId, last_update: new Date()});
  }

  getPercentFromIncubator(incubator, endDate) {
    let percent = 0;
    let now = new Date();
    if(!incubator.start_date_time) return percent;
    if(!endDate) return percent;
    let start_date_time = new Date(incubator.start_date_time);
    if(now.getTime() > endDate.getTime()) return 100;
    let duration = endDate.getTime() - start_date_time.getTime();
    let elapsed = now.getTime() - start_date_time.getTime();
    percent = elapsed / duration * 100;
    if(percent > 100) percent = 100;
    if(percent < 0) percent = 0;
    return percent;
  }
  
  async updateMoney() {
    let response = await fetch(userAdress + "/getMoney?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
    let data = await response.json();
    if(data !== undefined && data !== null && data.response != this.state.money) {
      this.setState({money: data.response, last_update: new Date()});
    }
  }

  async buyIncubator() {
    let username = sessionStorage.getItem("username");
    let jwt_token = sessionStorage.getItem("jwt_token");
    let response = await fetch(IncubatorAdress + "/buyIncubator", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token})});
    let data = await response.json();
    if(data !== undefined && data !== null && data.status !== "") {
        if(data.status !== "success") alert(data.message);
        else alert("You bought an incubator !");
    } else alert("An error occured");
    this.updateIncubators();
  }

  async updateIncubators() {
    let response = await fetch(IncubatorAdress + "/getIncubatorList?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
    let data = await response.json();
    console.log(JSON.stringify(data.response));
    console.log(JSON.stringify(this.state.incubators));
    console.log(JSON.stringify(this.state.incubators) !== JSON.stringify(data.response));
    if(JSON.stringify(data.response) !== JSON.stringify(this.state.incubators)) this.setState({incubators: data.response, last_update: new Date()});
  }

  async sellEgg(egg) {

    let username = sessionStorage.getItem("username");
    let jwt_token = sessionStorage.getItem("jwt_token");
    let response = await fetch(IncubatorAdress + "/sellEgg", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token, id_egg: egg.id})});
    let data = await response.json();
    if(data !== undefined && data !== null && data.status !== "") {
        if(data.status !== "success") alert(data.message);
        else alert("You sell an egg !");
    } else alert("An error occured");
    this.updateIncubators();
  }

  componentDidMount() {
    setInterval(() => this.updateIncubators(), 100000);
  }

  render() {
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";

    this.updateMoney();
    this.updateIncubators();

    return (
      <div id="EggApp" className={className}>
        <div className="backdrop">
          <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
          <h2> PENSION </h2>
        </div>


        <div id="EggWindow">
          <h3> PENSION </h3>
          <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
          <div id="Incubators">
            {this.state.incubators.map((incubator, index) => {
              let className = "Incubator";
              let endDate = null;
              if(incubator.start_date_time && incubator.egg) endDate = new Date(new Date(incubator.start_date_time).getTime() + (incubator.egg.time_to_incubate*1000));
              let percent = this.getPercentFromIncubator(incubator, endDate);
              if(incubator.egg) className += " used";
              if(incubator.price === null) className += " displayIncubator";
              let img_filename = "Egg_2k.webp";
              if(incubator.egg && incubator.egg.type === "rare") img_filename = "Egg_5k.webp";
              if(incubator.egg && incubator.egg.type === "epic") img_filename = "Egg_10k.webp";
              let title = "";
              if(endDate) title += "End on " + new Date(endDate).toLocaleDateString() + " at " + new Date(endDate).toLocaleTimeString();
              return (
                <div className={className} key={index} title={title} data-width={incubator.egg !== null ? percent : -1} onClick={() => { if(incubator.egg === null) this.setChooseEggDisplayed(true, incubator.id); else if(percent === 100) this.setChooseTeamDisplayed(true, incubator.id);}}>
                  <div className="IncubatorBar" data-end={new Date(endDate).getTime()} data-duration={new Date(endDate).getTime() - new Date(incubator.start_date_time).getTime()} data-width={incubator.egg !== null ? percent : -1} style={{width: percent + "%"}}></div>
                  <img className="IncubatorEgg" src={"/"+img_filename}></img>
                  <img className="IncubatorImg" src={"incubator.png"}></img>
                  <h4>{incubator.egg !== null ? incubator.egg.type.charAt(0).toUpperCase() + incubator.egg.type.slice(1) : ""} Egg</h4>
                  { percent === 100 ? <a className='sellEgg' onClick={ (e) => {e.preventDefault();e.stopPropagation();this.sellEgg(incubator.egg);} } title={"vendre l'oeuf pour "+this.getEggPrice(incubator.egg.type)+" pokedollars"}><img src="/pokedollars.png"></img></a> : <></>}
                </div>
              );
            })}    
            <div className="Incubator buy" key={this.state.incubators.length} title={"Price: " + (this.pricesIncubators[this.state.incubators.length])}>
              <h4>Unlock</h4>
              <DisplayMoney money={this.pricesIncubators[this.state.incubators.length]} onClick={() => this.buyIncubator()}></DisplayMoney>
            </div>
          </div>
          <DisplayMoney money={this.state.money}></DisplayMoney>
          <ChooseEgg isDisplayed={this.state.isChooseEggDisplayed} setIsDisplayed={(isDisplayed) => this.setChooseEggDisplayed(isDisplayed)} incubatorId={this.state.incubatorId}></ChooseEgg>
          <ChooseTeam isDisplayed={this.state.isChooseTeamDisplayed} setIsDisplayed={(isDisplayed) => this.setChooseTeamDisplayed(isDisplayed)} incubatorId={this.state.incubatorId}></ChooseTeam>
        </div>
      </div>
    );
  }
}

export default EggApp;