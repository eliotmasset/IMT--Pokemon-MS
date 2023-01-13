import './EggApp.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';

class EggApp extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      incubators: [
        { isUsed: true, startDate: new Date("2023-01-12T16:00:00"), endDate: new Date("2023-01-14T00:00:00"), eggType: "common", price: null },
        { isUsed: true, startDate: new Date("2023-01-12T16:00:00"), endDate: new Date("2023-01-14T00:00:00"), eggType: "common", price: null },
        { isUsed: true, startDate: new Date("2023-01-12T16:00:00"), endDate: new Date("2023-01-14T00:00:00"), eggType: "rare", price: null },
        { isUsed: true, startDate: new Date("2023-01-12T16:00:00"), endDate: new Date("2023-01-14T00:00:00"), eggType: "legendary", price: null },
        { isUsed: true, startDate: new Date("2023-01-12T16:00:00"), endDate: new Date("2023-01-14T00:00:00"), eggType: "rare", price: null},
        { isUsed: false, startDate: null, endDate: null, eggType: "", price: 10000 },
      ]
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
    let className = "";
    if(this.state.isDisplayed) className = "displayed";
    ( async () => {
      
    })();
    return (
      <div id="EggApp" className={className}>
        <div id="backdrop" onClick={() => this.setIsDisplayed(true)}>
          <h2> PENSION </h2>
        </div>


        <div id="EggWindow">
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
                  <h4>{incubator.eggType.charAt(0).toUpperCase() + incubator.eggType.slice(1)} Egg</h4>
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