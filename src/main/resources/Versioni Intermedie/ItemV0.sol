//pragma solidity ^0.8.0;
////https://eips.ethereum.org/EIPS/eip-1155
//import "@openzeppelin/contracts/token/ERC1155/IERC1155.sol";
////semi-fungible tokens
//contract Item {
//    enum Rarity{Common, Rare, UltraRare, Legend, Unique}
//    struct Item {
//        uint256 id;
//        string name;//UserName, Exp, Gold, Sward, Hammer, Bow, Spear
//        bool nft;
//    }
//
//    mapping(uint256 => uint256) public itemToStat;
//
//    //l'alternativa è l'utilizzo di json ma è questo il nostro scopo? No, deve essere tutto On-chain
//    struct Stat {
//        uint256 id;
//        uint64 att;
//        uint64 dif;
//        uint128 qty;//Decrementando ho un numero univoco per quel determinato item
//        uint128 priceGold;
//        uint128 priceETH;
//    }
//
//    struct Armory {
//        uint128 id;
//        uint64 itemId;//ItemID
//        uint64 statQty;//StatsQty
//    }
//
//    //Wallet A => inventario (1, "A", Sward, 10, 10, 200, 1000, 1), (1, "A", Sward, 10, 10, 199, 1000, 1)
//    //Wallet B => inventario (2, "B", Sward, 10, 10, 200, 1000, 1), (1, "A", Sward, 10, 10, 198, 1000, 1)
//
//    Item[] public items; // Il GameMaster può aggiungere nuovi items.
//    mapping(address => uint256) public count;
//    mapping(address => mapping(uint256 => Item)) public items;
//    mapping(address => mapping(uint256 => Armory)) public uniqueItems;
//    mapping(uint128 => address) public ownersItems; //itemId => address
//    mapping(uint128 => address) public ownersUniqueItems; //armoryId => address
//
//    constructor(){
//
//    }
//
//
//    function getInventory() external view returns (Item memory){
//        return count[items];
//    }
//
//    function balanceOf() external view returns (uint256){
//        return count[msg.sender];
//    }
//
//}
