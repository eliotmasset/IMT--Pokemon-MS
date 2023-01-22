import './EggApp.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';

const IncubatorAdress = "http://localhost:8082";

class EggApp extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      incubators: []
    }
  }

  setIsDisplayed(isDisplayed) {
    this.setState({isDisplayed: isDisplayed});
  }

  getPercentFromIncubator(incubator) {
    let percent = 0;
    if(incubator.isUsed) {
      let now = new Date();
      let startDate = incubator.startDate;
      let endDate = incubator.endDate;
      let duration = endDate.getTime() - startDate.getTime();
      let elapsed = now.getTime() - startDate.getTime();
      percent = elapsed / duration * 100;
      if(percent > 100) percent = 100;
      if(percent < 0) percent = 0;
    }
    return percent;
  }

  render() {
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    ( async () => {
      let response = await fetch(IncubatorAdress + "/getIncubatorList?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
      let incubators = await response.json();
      if(JSON.stringify(incubators) !== JSON.stringify(this.state.incubators)) this.setState({incubators: incubators});
    })();
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
              let img_filename = "Egg_2k.webp";
              if(incubator.eggType === "rare") img_filename = "Egg_5k.webp";
              if(incubator.eggType === "legendary") img_filename = "Egg_10k.webp";
              let title = "";
              if(incubator.endDate) title += "End on " + incubator.endDate.toLocaleDateString() + " at " + incubator.endDate.toLocaleTimeString();
              if(incubator.price) title += "Price: " + incubator.price;
              return (
                <div className={className} key={index} title={title}>
                  <div className="IncubatorBar" style={{width: percent + "%"}}></div>
                  <img className="IncubatorEgg" src={"/"+img_filename}></img>
                  <img className="IncubatorImg" src={"incubator.png"}></img>
                  <h4>{incubator.eggType !== null ? incubator.eggType.charAt(0).toUpperCase() + incubator.eggType.slice(1) : ""} Egg</h4>
                  <h4>Unlock</h4>
                </div>
              );
            })}    
          </div>
        </div>
      </div>
    );
  }
}

export default EggApp;