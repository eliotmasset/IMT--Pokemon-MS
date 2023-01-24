import './ShopApp.css';
import React from 'react';
import DisplayMoney from "../components/DisplayMoneyComponent";
import ShopItem from "./ShopItemComponent";
import BackButtonComponent from "../components/BackButtonComponent";

const ShopAdress = "http://localhost:8084";

class ShopApp extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      money: 0,
      item_selected: 0,
      item_list : [
        {name:"oeuf commun", engName:"commonEgg", price:"150", description:"Ceci est un oeuf commun. Il permettra d’obtenir des pokémons de rareté commune tel que des Etourmis."},
        {name:"oeuf Rare", engName: "rareEgg",price:"250", description:"Ceci est un oeuf rare. Il permettra d’obtenir des pokémons de rareté rare tel que Carchacrok."},
        {name:"oeuf epique", engName: "epicEgg",price:"350", description:"Ceci est un oeuf épique. Il permettra d’obtenir des pokémons fabuleux ou légendaire tel que Arceus."},
        {name:"", engName:"", price:"", description:""},
        {name:"", engName:"", price:"", description:""},
        {name:"", engName:"", price:"", description:""},
      ]
    }
  }

  setIsDisplayed(isDisplayed) {
    this.setState({isDisplayed: isDisplayed});
  }

  selectItem(key) {
    this.setState({item_selected:key});
    console.log(this.state.item_list[this.state.item_selected]);
  }

  render() {
    let list = this.state.item_list.map((item, key)=> {
      let className = "";
      let checked = key === this.state.item_selected;
      if(key === this.state.item_selected) className = "selected";
      return (
          <li className={className} key={key}>
            <input type="radio" id={key} name="itm" value={key} checked={checked} readOnly/>
            <ShopItem engName={item.engName} name={item.name} id={key + 1} price={item.price} onClick={()=> this.selectItem(key)}/>
          </li>
      );
    });
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    ( async () => {
      let response = await fetch(ShopAdress + "/getStore?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
      let store = await response.json();
      let new_state = this.state.item_list;
      new_state[3] = store[0];
      new_state[4] = store[1];
      new_state[5] = store[2];
      if(JSON.stringify(new_state) !== JSON.stringify(this.state.item_list)) this.setState({item_list: new_state});
    })();

    ( async () => {
      let response = await fetch("http://localhost:8087" + "/GetMoney?jwt_token=" + sessionStorage.getItem("jwt_token"));
      let money = await response.json();
      if(money != this.state.money) {
        this.setState({money: money});
      }

    })();
    let money = this.state.money;

    return (
      <div id="ShopApp" className={className}>
        <div className="backdrop">
          <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
          <h2> SHOP </h2>
        </div>


        <div id="ShopWindow">
          <DisplayMoney money={this.state.money}></DisplayMoney>
          <div className="itemList">
            <ul>
              {list}
              <ul className="shopButton">
                <li><button className="rechargerButton">
                  <ul>
                    <li>Recharger 500 <img src="/pokedollars.png"/></li>
                  </ul>
                </button></li>
              </ul>
              <div className="nameDescription"><h1>{this.state.item_list[this.state.item_selected].name}</h1></div>
              <li className="itemDescription" title={this.state.item_list[this.state.item_selected].description}>{this.state.item_list[this.state.item_selected].description}</li>
            </ul>
          </div>
          <div className="shopSpriteDiv">
            <img className="shopSprite" src={"/sprite/" + this.state.item_list[this.state.item_selected].engName.toLowerCase()  + "_sprite.gif"} />
          </div>
          <BackButtonComponent onClick={() => this.setIsDisplayed(false)}></BackButtonComponent>
        </div>

      </div>
    );
  }
}

export default ShopApp;