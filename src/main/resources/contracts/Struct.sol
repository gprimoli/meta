pragma solidity ^0.8.0;

    struct ItemArmory {
        string name;
        uint256 price;
        uint256 qtyMAX;
        uint256 delta;
        uint256[] selledNum;
        mapping(uint256 => address) owners;
    }

    struct ItemInventory {
        uint256 id;
        uint256 num;
//        uint256 price; //Prezzo di acquisto
    }

    struct Stat {
        uint256 gold;
        ItemInventory[] inventory;
    }


