import './ChooseEgg.css';
import React from 'react';
import BackButtonComponent from '../components/BackButtonComponent';

const IncubatorAddress = "http://localhost:8082/incubation";
const InventoryAddress = "http://localhost:8080/inventory";

class ChooseEgg extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            eggs: []
        }
    }

    async updateEggs() {
        let response = await fetch(InventoryAddress + "/eggs?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username"));
        let data = await response.json();
        if(JSON.stringify(data.response) !== JSON.stringify(this.state.eggs)) this.setState({eggs: data.response});
    }

    async setEggInIncubator(eggId, incubatorId) {
        let response = await fetch(InventoryAddress + "/setEggInIncubator?jwt_token=" + sessionStorage.getItem("jwt_token") + "&username=" + sessionStorage.getItem("username") + "&eggId=" + eggId + "&incubatorId=" + incubatorId,
        { method: "POST", body: JSON.stringify({username: sessionStorage.getItem("username"), jwt_token: sessionStorage.getItem("jwt_token"), id_egg: eggId, id_incubator: incubatorId})});
        let data = await response.json();
        if(data !== undefined && data !== null && data.status !== "") {
            if(data.status !== "success") alert(data.message);
            else alert("You set the egg in the incubator !");
        } else alert("An error occured");
        this.props.setIsDisplayed(false);
    }

    render() {

        let className = "";
        if(this.props.isDisplayed) className = "displayed";

        this.updateEggs();

        return (
            <div id="ChooseEggWrapper" className={className}>
                <div id="ChooseEgg">
                    <h3> Choose Egg </h3>
                    <BackButtonComponent onClick={() => this.props.setIsDisplayed(false)}></BackButtonComponent>
                    <div id="ChooseEggs">
                        {this.state.eggs.map((egg, index) => {
                            let className = "ChooseEgg";
                            let img_filename = "Egg_2k.webp";
                            if(egg.type === "rare") img_filename = "Egg_5k.webp";
                            if(egg.type === "epic") img_filename = "Egg_10k.webp";
                            return (
                                <div className={className} key={index} onClick={() => this.setEggInIncubator(egg.id, this.props.incubatorId)}>
                                    <img className="EggChooseImg" src={img_filename}></img>
                                    <h4>{egg.type.charAt(0).toUpperCase() + egg.type.slice(1)} egg</h4>
                                </div>
                            );
                            })
                        }
                    </div>
                </div>
            </div>
        );
    }
}

export default ChooseEgg;