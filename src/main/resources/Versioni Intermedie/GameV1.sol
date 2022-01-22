// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";
//SafeMath is generally not needed starting with Solidity 0.8, since the compiler now has built in overflow checking.
contract Game is AccessControl{
    bytes32 public constant GAME_MASTER = keccak256("GAME_MASTER");
    string public constant GAME_NAME = "Test Game";

    struct Item {
        string name;//Sward, Hammer, Bow, Spear
        uint256 price;
    }

    Item[] private items;
    mapping(address => Item[]) private inventory;
    mapping(address => uint256) private exp;
    mapping(address => uint256) private gold;

    constructor(){
        _setupRole(GAME_MASTER, msg.sender);
        items.push(Item("Sward", 100));
        items.push(Item("Hammer", 80));
        items.push(Item("Bow", 60));
        items.push(Item("Spear", 500));
        lastIdInserted = 0;
    }

    //Funzioni Generali
    function getGameName() external pure returns (string memory){
        return GAME_NAME;
    }

    //Funzioni per la gestione dell'armeria FT (items)
    function createItem(string memory name, uint256 price) external {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        items.push(Item(name, price));
    }
    function getArmoryWeight() external view returns (uint256){
        return items.length;
    }
    function getItemFromArmory(uint256 pos) external view returns (string memory, uint256){
        assert(pos >= 0 && pos < items.length); //out of bound
        return (items[pos].name, items[pos].price);
    }

    //Funzioni per la gestione dell'inventario (inventory)
    function getInventoryWeight() external view returns (uint256){
        return inventory[msg.sender].length;
    }
    function getItemFromInvenory(uint256 pos) external view returns (string memory, uint256){
        assert(pos >= 0 && pos < inventory[msg.sender].length); //out of bound
        return (inventory[msg.sender][pos].name, inventory[msg.sender][pos].price);
    }

    //Funzioni per la compra vendita di item
    function buyItem(uint256 pos) external{
        assert(pos >= 0 && pos < items.length); //out of bound
        require(gold[msg.sender] >= items[pos].price, "Non hai abbastanza Gold");//Se la condizione è falsa fai vedere il messaggio
        gold[msg.sender] -= items[pos].price;
        inventory[msg.sender].push(items[pos]);
    }
    function sellItem(uint256 pos) external{
        assert(pos >= 0 && pos < inventory[msg.sender].length); //out of bound
        gold[msg.sender] += (inventory[msg.sender][pos].price * 80) / 100;
        inventory[msg.sender][pos] = inventory[msg.sender][inventory[msg.sender].length - 1];
        inventory[msg.sender].pop();
    }

    //Funzioni per la gestione delle infoPlayer (Exp/Gold)
    function getExp() external view returns (uint256){
        return exp[msg.sender];
    }
    function getGold() external view returns (uint256){
        return gold[msg.sender];
    }
    function save(address who, uint256 goldAdd, uint256 expAdd) external {
        require(hasRole(GAME_MASTER, msg.sender), "Devi essere un Game Master per eseguire questa operazione");
        exp[who] += expAdd;
        gold[who] += goldAdd;
    }


}
