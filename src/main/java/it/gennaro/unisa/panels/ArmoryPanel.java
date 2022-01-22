package it.gennaro.unisa.panels;

import it.gennaro.unisa.panels.customization.Button;
import it.gennaro.unisa.utils.GameUtil;
import it.gennaro.unisa.utils.Item;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ArmoryPanel extends JPanel {
    private final GameUtil gm;

    public ArmoryPanel(GameUtil gm) {
        this.gm = gm;
        setup();
    }


    public void setup() {
        try {
            removeAll();
            List<Item> GameItems = gm.getArmory();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel row1 = new JPanel(new FlowLayout());
            row1.add(new JLabel("Armeria"));
            add(row1);
            JPanel row2 = new JPanel(new GridLayout((GameItems.size() / 5) + 1, 5, 40, 0));
            add(row2);
            for (int i = 0; i < GameItems.size(); i++) {
                JPanel p = new JPanel();
                BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
                p.setLayout(bl);

                p.add(new JLabel("Nome: " + GameItems.get(i).getName()));
                p.add(new JLabel("Prezzo: " + GameItems.get(i).getVal1()));
                p.add(new JLabel("Qauntità disponibile: " + GameItems.get(i).getVal2()));
                Button acq = new Button("Acquista", i);
                acq.addActionListener(el -> {
                    try {
                        gm.buy(((Button) el.getSource()).getPos());
                        JOptionPane.showMessageDialog(this, "Acquisto effettuato con successo", "OK", JOptionPane.INFORMATION_MESSAGE);
                        setup();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Errore sulla blockchain contralla la console", "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                });
                p.add(acq);
                row2.add(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sulla blockchain contralla la console", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
