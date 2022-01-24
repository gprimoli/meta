package it.gennaro.unisa;

import it.gennaro.unisa.utils.Game;
import it.gennaro.unisa.utils.GameUtil;
import it.gennaro.unisa.utils.Item;

import java.util.List;


public class MainTest {
    public static void main(String[] args) throws Exception {
        String gameAddr = "0x1bb44c35129034c97F3B0b2Ed33452958d845f1d";

        String adminAddr = "0x3F3B888Af7E10B5f99cfD3F38Eb7e124AC74414F";
        String adminPrivateKey = "43359905831ca59d079e979ee2540a0eb27f65b5d420a60192f27c82298a57b3";

        String userAddr = "0x67E7D479efd8a8c0779B5cf1d23E84df3afeaDa3";
        String userPrivateKey = "f7e9a32bb319fb3096679f2fae9f8a26957158e485b0efbfbb72d45159c267d4";

        GameUtil gu1 = new GameUtil(gameAddr, adminPrivateKey);
        GameUtil gu2 = new GameUtil(gameAddr, userPrivateKey);

        gu1.createItem("Test", 0.01, 10);

        gu1.close();
        gu2.close();
    }
}
