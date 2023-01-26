import './EggApp.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';
import ChooseEgg from './ChooseEgg';
import DisplayMoney from '../components/DisplayMoneyComponent';

const IncubatorAdress = "http://localhost:8082";
const ShopAddress = "http://localhost:8084";

class EggApp extends React.Component {
  
	pricesIncubators = [0, 100, 1000, 10000, 500000, 1000000];

  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      incubators: [],
      isChooseEggDisplayed: false,
      incubatorId: 0,
      money: 0,
      last_update: new Date()
    }
  }

  setIsDisplayed(isDisplayed) {
    this.setState({isDisplayed: isDisplayed, last_update: new Date()});
  }

  setChooseEggDisplayed(isDisplayed, valid, incubatorId) {
    if(valid) this.setState({isChooseEggDisplayed: isDisplayed, incubatorId: incubatorId, last_update: new Date()});
  }

  getPercentFromIncubator(incubator) {
    let percent = 0;
    if(incubator.isUsed) {
      let now = new Date();
      if(!incubator.startDate) return percent;
      if(!incubator.endDate) return percent;
      let startDate = new Date(incubator.startDate);
      let endDate = new Date(incubator.endDate);
      if(now.getTime() > endDate.getTime()) return 100;
      let duration = endDate.getTime() - startDate.getTime();
      let elapsed = now.getTime() - startDate.getTime();
      percent = elapsed / duration * 100;
      if(percent > 100) percent = 100;
      if(percent < 0) percent = 0;
    }
    return percent;
  }
  
  async updateMoney() {
    let response = await fetch("http://localhost:8087" + "/GetMoney?jwt_token=" + sessionStorage.getItem("jwt_token"));
    let money = await response.json();
    if(money != this.state.money) {
      this.setState({money: money, last_update: new Date()});
    }
  }

  async buyIncubator() {
    let username = sessionStorage.getItem("username");
    let jwt_token = sessionStorage.getItem("jwt_token");
    let response = await fetch(ShopAddress + "/buyIncubator", { method: "POST", body: JSON.stringify({username: username, jwt_token: jwt_token})});
    let data = await response.text();
    if(data !== undefined && data !== null && data !== "") {
        if(data !== "Incubator bought") alert(data);
        else alert("You bought an incubator !");
    } else alert("An error occured");
    this.updateIncubators();
  }

  async updateIncubators() {
    let response = await fetch(IncubatorAdress + "/getIncubatorList?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
    let incubators = await response.json();
    if(JSON.stringify(incubators) !== JSON.stringify(this.state.incubators)) this.setState({incubators: incubators, last_update: new Date()});
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
              let percent = this.getPercentFromIncubator(incubator);
              let className = "Incubator";
              if(incubator.isUsed) className += " used";
              if(incubator.price === null) className += " displayIncubator";
              let img_filename = "Egg_2k.webp";
              if(incubator.eggType === "rare") img_filename = "Egg_5k.webp";
              if(incubator.eggType === "epic") img_filename = "Egg_10k.webp";
              let title = "";
              if(incubator.endDate) title += "End on " + new Date(incubator.endDate).toLocaleDateString() + " at " + new Date(incubator.endDate).toLocaleTimeString();
              if(incubator.price || incubator.price == 0) title += "Price: " + incubator.price;
              return (
                <div className={className} key={index} title={title} onClick={() => { if(incubator.eggType === null) this.setChooseEggDisplayed(true, incubator.price === null, incubator.id)}}>
                  <div className="IncubatorBar" data-end={new Date(incubator.endDate).getTime()} data-duration={new Date(incubator.endDate).getTime() - new Date(incubator.startDate).getTime()} data-width={incubator.eggType !== null ? percent : -1} style={{width: percent + "%"}}></div>
                  <img className="IncubatorEgg" src={"/"+img_filename}></img>
                  <img className="IncubatorImg" src={"incubator.png"}></img>
                  <h4>{incubator.eggType !== null ? incubator.eggType.charAt(0).toUpperCase() + incubator.eggType.slice(1) : ""} Egg</h4>
                  <h4>Unlock</h4>
                  {incubator.price !== null ? <DisplayMoney money={incubator.price} onClick={() => this.buyIncubator()}></DisplayMoney> : <></>}
                </div>
              );
            })}    
          </div>
          <DisplayMoney money={this.state.money}></DisplayMoney>
          <ChooseEgg isDisplayed={this.state.isChooseEggDisplayed} setIsDisplayed={(isDisplayed) => this.setChooseEggDisplayed(isDisplayed, true)} incubatorId={this.state.incubatorId}></ChooseEgg>
        </div>
      </div>
    );
  }
}

export default EggApp;