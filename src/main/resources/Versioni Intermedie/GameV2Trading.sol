// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";
import "./Struct.sol";
    enum TradingStatus {Accepted, Declined, Created}

    struct TradingInfo{
        mapping(address => TradeObj) trades;
        address[] outcoming;
        address[] incoming;
    }
    struct TradeObj{
        Item item;
        uint gold;
        TradingStatus status;
    }

//SafeMath is generally not needed starting with Solidity 0.8, since the compiler now has built in overflow checking.
contract Game is AccessControl {
    bytes32 public constant GAME_MASTER = keccak256("GAME_MASTER");
    string public constant GAME_NAME = "Test Game";

    Item[] private items;
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
        infoPlayer[msg.sender].inventory.push(Item(items[pos].id,
            items[pos].num++,
            items[pos].name,
            items[pos].price,
            items[pos].qtyMAX));
    }
    function startTrade(address whom, uint itemPosInventory, uint gold) external {
        require(itemPosInventory >= -1 && itemPosInventory < infoPlayer[msg.sender].inventory.length, "Assicurati di possedere l'item");
        infoPlayer[whom].trading.incoming.push(msg.sender);//Io sto avvisando all'altro player che sto cercando di fare trading con lui
        infoPlayer[msg.sender].trading.outcoming.push(whom);//Ma devo anche ricordare che sto facendo trading con lui
        infoPlayer[whom].trades[msg.sender] = TradeObj(itemPosInventory == -1 ? 0 : infoPlayer[msg.sender].inventory[itemPosInventory], gold, TradingStatus.Created);
    }
    function checkMyPropose(address whom) external returns (uint, string memory, uint, TradingStatus){//id name gold
        require(infoPlayer[whom].trading.trades[msg.sender] != 0, "Trading inesistente");
        return(
                infoPlayer[whom].trading.trades[msg.sender].item.id,
                infoPlayer[whom].trading.trades[msg.sender].item.name,
                infoPlayer[whom].trading.trades[msg.sender].gold,
                infoPlayer[whom].trading.trades[msg.sender].status,
        );
    }
    function checkPropose(address whom) external returns (uint, string memory, uint, TradingStatus){//id name gold
        require(infoPlayer[msg.sender].trading.trades[whom] != 0, "Trading inesistente");
        return(
                infoPlayer[msg.sender].trading.trades[whom].item.id,
                infoPlayer[msg.sender].trading.trades[whom].item.name,
                infoPlayer[msg.sender].trading.trades[whom].gold,
                infoPlayer[msg.sender].trading.trades[whom].status,
        );
    }
    function finishTrade() external{

    }

    function deleteTrade(address whom) external{

    }
    function changeTradeStatus(address whom, TradingStatus status) external {
        require(infoPlayer[msg.sender].trades[whom] != 0, "Trading inesistente");
        infoPlayer[msg.sender].trades[whom].status = status;
    }


    function getLengthOutComingTrades() external returns (uint){
        return infoPlayer[msg.sender].trading.outcoming.length;
    }
    function getAddressFromOutComingTrades(uint pos) external returns (address){
        assert(pos >= 0 && pos < infoPlayer[msg.sender].trading.outcoming.length);
        return infoPlayer[msg.sender].trading.outcoming[pos];
    }

    function getLengthInComingTrades() external returns (uint){
        return infoPlayer[msg.sender].trading.incoming.length;
    }
    function getAddressFromInComingTrades(uint pos) external returns (address){
        assert(pos >= 0 && pos < infoPlayer[msg.sender].trading.outcoming.length);
        return infoPlayer[msg.sender].trading.incoming[pos];
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
