package it.gennaro.unisa;

import it.gennaro.unisa.utils.Game;
import it.gennaro.unisa.utils.GameUtil;
import it.gennaro.unisa.utils.Item;

import java.util.List;


public class MainTest {
    public static void main(String[] args) throws Exception {
        String gameAddr = "0xbA52D847DFE9eB509af85Aa161A7210Bd50A9128";

        String adminAddr = "0x36DE4Cd32B0179eE51Bd824C6210bf652f0154ED";
        String adminPrivateKey = "087666530921b7806c23d70c39b54b056937ff9d9c6f0ac7b3f42f750445e7f6";

        String userAddr = "0x82Ebe74809B611f5618988faFB3248d8bd1De7EE";
        String userPrivateKey = "f2909bd0da75d79136b0d3b9df5ad04a7f2995f6148e8b0feb26821c1e3b2c16";

        GameUtil gu1 = new GameUtil(gameAddr, adminPrivateKey);
        GameUtil gu2 = new GameUtil(gameAddr, userPrivateKey);


        gu2.buy(1);
        System.out.println(gu2.getInventory());

//        List<Item> l = gu1.getInventory();
//        System.out.println(l);

        gu1.close();
        gu2.close();
    }
}
