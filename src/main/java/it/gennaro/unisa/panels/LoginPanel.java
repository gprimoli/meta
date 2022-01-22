package it.gennaro.unisa.panels;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private static final String gameAddrHC = "0xdC531385C4CD0d55E744595B7c8824070E724BB7";
    private static final String pKeyHC = "f2909bd0da75d79136b0d3b9df5ad04a7f2995f6148e8b0feb26821c1e3b2c16";
    private final JTextField pkey = new JTextField(40);
    private final JTextField gameAddr = new JTextField(40);
    public LoginPanel() {
        gameAddr.setText(gameAddrHC);
        pkey.setText(pKeyHC);

        setLayout(new GridLayout(2, 1, 0, 0));
        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Inserisci la tua chiave privata"));
        row1.add(pkey);

        JPanel row2 = new JPanel(new FlowLayout());
        row2.add(new JLabel("Address Gioco"));
        row2.add(gameAddr);

        add(row2);
        add(row1);
    }

    public String getPKey(){
        return pkey.getText();
    }
    public String getGameAddr(){
        return gameAddr.getText();
    }
}
