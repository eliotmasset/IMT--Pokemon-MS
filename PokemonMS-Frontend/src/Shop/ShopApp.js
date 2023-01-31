import './ShopApp.css';
import React from 'react';
import DisplayMoney from "../components/DisplayMoneyComponent";
import ShopItem from "./ShopItemComponent";
import BackButtonComponent from "../components/BackButtonComponent";

const ShopAdress = "http://localhost:8084/shop";
const userAdress = "http://localhost:8087/user";

class ShopApp extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      isDisplayed: false,
      money: 0,
      item_selected: 0,
      item_list : [
        {name:"oeuf commun", engName:"commonEgg", price:"200", description:"Ceci est un oeuf commun. Il permettra d’obtenir des pokémons de rareté commune tel que des Etourmis."},
        {name:"oeuf Rare", engName: "rareEgg",price:"350", description:"Ceci est un oeuf rare. Il permettra d’obtenir des pokémons de rareté rare tel que Carchacrok."},
        {name:"oeuf epique", engName: "epicEgg",price:"500", description:"Ceci est un oeuf épique. Il permettra d’obtenir des pokémons fabuleux ou légendaire tel que Arceus."},
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
  }

  async updateMoney() {
    let response = await fetch(userAdress + "/getMoney?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
      let data = await response.json();
      if(data.response != this.state.money) {
        this.setState({money: data.response});
      }
  }

  async updateStore() {
    let response = await fetch(ShopAdress + "/getShop?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
    let data = await response.json();
    let store = data.response;
    let new_state = [...this.state.item_list];
    new_state[3] = store.pokemon1;
    new_state[4] = store.pokemon2;
    new_state[5] = store.pokemon3;
    if(JSON.stringify(new_state) !== JSON.stringify(this.state.item_list)) this.setState({item_list: new_state});
  }

  async buyReload() {
    let response = await fetch(ShopAdress + "/buyReload", {
      method: 'POST',
      body: JSON.stringify({
        jwt_token: sessionStorage.getItem("jwt_token"),
        username: sessionStorage.getItem("username")
      })
    });
    response = await response.json();
    alert(response.message);
    this.updateStore();
  }

  render() {
    let list = this.state.item_list.map((item, key)=> {
      let className = "";
      let checked = key === this.state.item_selected;
      if(key === this.state.item_selected) className = "selected";
      return (
          <li className={className} key={key}>
            <input type="radio" id={key} name="itm" value={key} checked={checked} readOnly/>
            <ShopItem engName={item.engName} name={item.name} id={key + 1} price={item.price} onClick={()=> this.selectItem(key)} updateMoney={() => this.updateMoney()} />
          </li>
      );
    });
    let className = "cardMenu";
    if(this.state.isDisplayed) className = "cardMenu displayed";
    this.updateMoney();
    this.updateStore();
    let money = this.state.money;

    return (
      <div id="ShopApp" className={className}>
        <div className="backdrop">
          <div className='bright-effect' onClick={() => this.setIsDisplayed(true)}></div>
          <h2> SHOP </h2>
        </div>


        <div id="ShopWindow">
          <h3>SHOP</h3>
          <DisplayMoney money={this.state.money}></DisplayMoney>
          <div className="itemList">
            <ul>
              {list}
              <ul className="shopButton">
                <li><button className="rechargerButton" onClick={() => this.buyReload()}>
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