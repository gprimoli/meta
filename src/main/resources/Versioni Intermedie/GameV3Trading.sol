// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";
import "./Struct.sol";

    struct Bazar {
        Trade[] info;
        mapping(uint => address[]) map;
    }

    struct Trade {
        TradeOBJ outcoming;
        mapping(address => TradeOBJ) incoming;
    }

    struct TradeOBJ {
        Item item;
        uint gold;
    }


//SafeMath is generally not needed starting with Solidity 0.8, since the compiler now has built in overflow checking.
contract Game is AccessControl {
    bytes32 public constant GAME_MASTER = keccak256("GAME_MASTER");
    string public constant GAME_NAME = "Test Game";

    Item[] private items;
    Bazar[] private bazar;
    mapping(address => Stat) private infoPlayer;
    mapping(uint => mapping(uint => address)) private fromItemtoPlayer;//pos => num => address


    constructor(){
        _setupRole(GAME_MASTER, msg.sender);
        createItem("Sward", 100, 50);
        createItem("Hammer", 500, 10);
        createItem("Bow", 200, 5);
        createItem("Spear", 1000, 1);
    }

    //Funzioni Generali
    function getGameName() external pure returns (string memory){
        return GAME_NAME;
    }

    //Funzioni per la gestione dell'armeria (items)
    function createItem(string memory name, uint price, uint qtyMAX) public {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        items.push(Item(items.length, 0, name, price, qtyMAX));
    }
    function getArmoryWeight() external view returns (uint){
        return items.length;
    }
    function getItemFromArmory(uint pos) external view returns (string memory, uint, uint){
        assert(pos >= 0 && pos < items.length);
        //out of bound
        return (items[pos].name, items[pos].price, items[pos].qtyMAX);
    }

    //Funzioni per la gestione dell'inventario (inventory)
    function getOwner(uint pos, uint num) external view returns (address){
        return fromItemtoPlayer[pos][num];
    }
    function getInventoryWeight() external view returns (uint){
        return infoPlayer[msg.sender].inventory.length;
    }
    function getItemFromInvenory(uint pos) external view returns (uint, uint, string memory){
        assert(pos >= 0 && pos < infoPlayer[msg.sender].inventory.length); //out of bound
        return (infoPlayer[msg.sender].inventory[pos].id,
                infoPlayer[msg.sender].inventory[pos].num,
                infoPlayer[msg.sender].inventory[pos].name);
    }

    //Funzioni per comprare e scambiare item
    function buyItem(uint pos) external {
        assert(pos >= 0 && pos < items.length); //out of bound
        //Se la condizione è falsa fai vedere il messaggio
        require(items[pos].num <= items[pos].qtyMAX, "Sono stati venduti tutti gli esemplari di quest'Item");
        require(infoPlayer[msg.sender].gold >= items[pos].price, "Non hai abbastanza Gold");

        infoPlayer[msg.sender].gold -= items[pos].price;
        infoPlayer[msg.sender].inventory.push(Item(
                items[pos].id,
                items[pos].num++,
                items[pos].name,
                items[pos].price,
                items[pos].qtyMAX)
        );
    }
    function getBazarSize() external returns(uint){
        return bazar.info.length;
    }
    function getBazarOffer(uint pos) external returns(uint, uint, string memory, uint){
        assert(pos >= 0 && pos < bazar.info.length);
        return (
            bazar.info[pos].item.id,
            bazar.info[pos].item.num,
            bazar.info[pos].item.name,
            bazar.info[pos].gold,
        );
    }
    function addBazarOffer(uint inventoryPos, uint gold) external{
        assert(inventoryPos >= 0 && inventoryPos < infoPlayer[msg.sender].inventory.length);
        bazar.info.push(Trade({ outcoming: TradeOBJ(infoPlayer[msg.sender].inventory[inventoryPos], gold) }));
    }

    //Funzioni per la gestione delle infoPlayer (Exp/Gold)
    function getExp() external view returns (uint){
        return infoPlayer[msg.sender].exp;
    }
    function getGold() external view returns (uint){
        return infoPlayer[msg.sender].exp;
    }
    function save(address who, uint goldAdd, uint expAdd) external {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        infoPlayer[who].exp += expAdd;
        infoPlayer[who].exp += goldAdd;
    }

}
