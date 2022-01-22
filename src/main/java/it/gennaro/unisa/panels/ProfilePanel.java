package it.gennaro.unisa.panels;

import it.gennaro.unisa.utils.GameUtil;

import javax.swing.*;
import java.math.BigInteger;

public class ProfilePanel extends JPanel {
    private JLabel info;
    private JTextField buyInfo;
    private final GameUtil gm;


    public ProfilePanel(GameUtil gm) {
        this.gm = gm;
        try {
            JButton acq = new JButton("Acquista Gold");
            acq.addActionListener(el -> {
                try {
                    double x = Double.parseDouble(buyInfo.getText());
                    gm.payForGold(x);
                    info.setText(gm.getGold().toString());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Devi inserire un numero!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errore sulla blockchain contralla la console", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            info = new JLabel(gm.getGold().toString());
            buyInfo = new JTextField(20);
            buyInfo.setText("0.01");
            add(new JLabel("Gold: "));
            add(info);
            add(buyInfo);
            add(acq);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sulla blockchain contralla la console", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
