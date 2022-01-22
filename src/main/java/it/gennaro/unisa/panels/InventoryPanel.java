package it.gennaro.unisa.panels;

import it.gennaro.unisa.panels.customization.Button;
import it.gennaro.unisa.utils.GameUtil;
import it.gennaro.unisa.utils.Item;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private final GameUtil gm;

    public InventoryPanel(GameUtil gm) {
        this.gm = gm;
        setup();
    }

    public void setup() {
        try {
            removeAll();
            List<Item> GameItems = gm.getInventory();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel row1 = new JPanel(new FlowLayout());
            row1.add(new JLabel("Inventario"));
            add(row1);
            JPanel row2 = new JPanel(new GridLayout((GameItems.size() / 5) + 1, 5, 40, 0));
            add(row2);
            for (int i = 0; i < GameItems.size(); i++) {
                JPanel p = new JPanel();
                BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
                p.setLayout(bl);

                p.add(new JLabel("Nome: " + GameItems.get(i).getName()));
                p.add(new JLabel("Id: " + GameItems.get(i).getVal1()));
                p.add(new JLabel("Numero: " + GameItems.get(i).getVal2()));
                it.gennaro.unisa.panels.customization.Button acq = new it.gennaro.unisa.panels.customization.Button("Vendi", i);
                acq.addActionListener(el -> {
                    try {
                        gm.sell(((Button) el.getSource()).getPos());
                        JOptionPane.showMessageDialog(this, "Vendita effettuata con successo", "OK", JOptionPane.INFORMATION_MESSAGE);
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
