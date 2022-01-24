// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";
import "./Struct.sol";
//SafeMath is generally not needed starting with Solidity 0.8, since the compiler now has built in overflow checking.
contract Game is AccessControl {
    bytes32 public constant GAME_MASTER = keccak256("GAME_MASTER");
    string public constant GAME_NAME = "Test Game";

    ItemArmory[] private items;
    mapping(address => Stat) private infoPlayer;

    constructor(){
        _setupRole(GAME_MASTER, msg.sender);
        createItem("Sward", 100, 50);
        createItem("Hammer", 500, 10);
        createItem("Bow", 200, 5);
        createItem("Spear", 1000000000000000000, 1);
    }

    fallback() external payable {
        infoPlayer[msg.sender].gold += msg.value;
    }//In teoria questo metodo non serve perché msg.data dovrebbe essere sempre empty.
    receive() external payable {
        infoPlayer[msg.sender].gold += msg.value;
    }

    //Funzioni Generali
    function getGameName() external pure returns (string memory){
        return GAME_NAME;
    }

    //Funzioni per la gestione dell'armeria (items)
    function createItem(string memory name, uint256 price, uint256 qtyMAX) public {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        ItemArmory storage tmp = items.push(); //.push() appends a zero-initialized element and returns a reference to it.
        tmp.name = name;
        tmp.price = price;
        tmp.qtyMAX = qtyMAX;

    }
    function getArmoryWeight() external view returns (uint256){
        return items.length;
    }
    function getItemFromArmory(uint256 id) external view returns (string memory, uint256, uint256){
        assert(id >= 0 && id < items.length); //out of bound
        return (items[id].name, items[id].price, items[id].qtyMAX - items[id].delta);
    }

    //Funzioni per la gestione delle infoPlayer (Gold/Inventory)
    function getGold() external view returns (uint256){
        return infoPlayer[msg.sender].gold;
    }
    function drawback() external {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        payable(msg.sender).transfer(address(this).balance);
    }

    //Funzioni per la gestione dell'inventario (inventory)
    function getOwner(uint256 id, uint256 num) external view returns (address){
        assert(id >= 0 && id < items.length);
        assert(num >= 0 && num < items[id].qtyMAX);
        return items[id].owners[num];
    }
    function getInventoryWeight() external view returns (uint256){
        return infoPlayer[msg.sender].inventory.length;
    }
    function getItemFromInvenory(uint256 idInventory) external view returns (string memory, uint256, uint256){
        assert(idInventory >= 0 && idInventory < infoPlayer[msg.sender].inventory.length);
        ItemInventory memory  tmp = infoPlayer[msg.sender].inventory[idInventory];
        return (items[tmp.id].name,tmp.id,tmp.num);
    }

    //Funzioni per la compra vendita di item
    function buyItem(uint256 id) external {
        assert(id >= 0 && id < items.length);//out of bound
        //Se la condizione è falsa fai vedere il messaggio
        require(items[id].delta < items[id].qtyMAX, "Sono stati venduti tutti gli esemplari di quest'Item");
        require(infoPlayer[msg.sender].gold >= items[id].price, "Non hai abbastanza Gold");

        infoPlayer[msg.sender].gold -= items[id].price;

        if (items[id].selledNum.length == 0)
            items[id].selledNum.push(items[id].delta);

        uint256 selledNum = items[id].selledNum[items[id].selledNum.length - 1];
        items[id].selledNum.pop();

        ItemInventory storage purchasedItem = infoPlayer[msg.sender].inventory.push();
        purchasedItem.id = id;
        purchasedItem.num = selledNum;

        items[id].delta++;
        items[id].owners[selledNum] = msg.sender;
    }
    function sellItem(uint256 idInventory) external {
        assert(idInventory >= 0 && idInventory < infoPlayer[msg.sender].inventory.length);

        ItemInventory memory tmp = infoPlayer[msg.sender].inventory[idInventory];

        infoPlayer[msg.sender].inventory[idInventory] = infoPlayer[msg.sender].inventory[infoPlayer[msg.sender].inventory.length - 1];
        infoPlayer[msg.sender].inventory.pop();

        items[tmp.id].delta--;
        delete items[tmp.id].owners[tmp.num];
        items[tmp.id].selledNum.push(tmp.num);

        infoPlayer[msg.sender].gold += (items[tmp.id].price * 80) / 100;
    }

    //    DebugOnly
    //    function updatePlayerInfo(address who, uint256 goldAdd) external {
    //        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
    //        infoPlayer[who].gold += goldAdd;
    //    }
}
