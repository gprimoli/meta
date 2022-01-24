package it.gennaro.unisa;

import it.gennaro.unisa.Exception.BlockChainException;
import it.gennaro.unisa.utils.Button;
import it.gennaro.unisa.utils.GameUtil;
import it.gennaro.unisa.utils.Item;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class GUI extends JPanel {
    private static final String gameAddrHC = "0x1bb44c35129034c97F3B0b2Ed33452958d845f1d";
    private static final String pKeyHC = "f7e9a32bb319fb3096679f2fae9f8a26957158e485b0efbfbb72d45159c267d4";
    private final JPanel loggedPanel = new JPanel();
    private final JPanel loginPanel = new JPanel();
    private final JLabel infoGold = new JLabel("Wei");
    private final JPanel armeriaContent = new JPanel();
    private final JPanel inventaryContent = new JPanel();
    private GameUtil gm;

    public GUI() {
        CardLayout c = new CardLayout();
        setLayout(c);
        add(loggedPanel, "afterLogin");
        add(loginPanel, "login");
        c.show(this, "login");
        setupLogin();
    }

    private void setupLogin() {
        JTextField pkey = new JTextField(40);
        pkey.setText(pKeyHC);

        JTextField gameAddr = new JTextField(40);
        gameAddr.setText(gameAddrHC);

        BoxLayout bd = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
        loginPanel.setLayout(bd);

        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Inserisci la tua chiave privata"));
        row1.add(pkey);
        loginPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout());
        row2.add(new JLabel("Address Gioco"));
        row2.add(gameAddr);
        loginPanel.add(row2);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(el -> {
            if (gameAddr.getText().length() == 0 || pkey.getText().length() == 0) {
                showError("Campi Obbligatori");
            } else {
                gm = new GameUtil(gameAddr.getText(), pkey.getText());
                ((CardLayout) getLayout()).show(this, "afterLogin");
                setupAfterLogin();
                reloadGold();
            }
        });
        JPanel row3 = new JPanel(new FlowLayout());
        row3.add(loginButton);
        loginPanel.add(row3);
    }

    private void setupAfterLogin() {
        loggedPanel.removeAll();
        loggedPanel.setLayout(new BorderLayout());
        loggedPanel.add(setupProfile(), BorderLayout.NORTH);

        JPanel body = new JPanel(new CardLayout());
        loggedPanel.add(body, BorderLayout.CENTER);

        loggedPanel.add(setupFooter(body), BorderLayout.SOUTH);
    }

    private JPanel setupProfile() {
        JPanel ret = new JPanel(new FlowLayout());

        ret.add(new JLabel("Gold: "));

        ret.add(infoGold);

        JTextField buyInfo = new JTextField(20);
        buyInfo.setText("0.01");
        ret.add(buyInfo);

        JButton acq = new JButton("Compra Gold");
        acq.addActionListener(el -> {
            try {
                double x = Double.parseDouble(buyInfo.getText());
                gm.payForGold(x);
            } catch (NumberFormatException e) {
                showError("Devi inserire un numero!");
            } catch (BlockChainException e) {
                showError();
            }
        });
        ret.add(acq);
        return ret;
    }

    private JPanel setupFooter(JPanel body) {
        body.add(setupArmery(), "armeria");
        body.add(setupInventary(), "inventario");

        JPanel ret = new JPanel(new FlowLayout());
        JButton draw = new JButton("Riscuoti");
        draw.addActionListener(el -> {
            try {
                gm.drawBack();
            } catch (BlockChainException e) {
                showError();
            }
        });
        ret.add(draw);
        JButton inventarioButton = new JButton("Inventario");
        JButton armeriaButton = new JButton("Armeria");
        JButton logout = new JButton("Logout");

        CardLayout c = (CardLayout) body.getLayout();
        inventarioButton.addActionListener(el -> {
            c.show(body, "inventario");
        });
        armeriaButton.addActionListener(el -> {
            c.show(body, "armeria");
        });
        logout.addActionListener(el -> {
            ((CardLayout) getLayout()).show(this, "login");
        });

        ret.add(inventarioButton);
        ret.add(armeriaButton);
        ret.add(logout);
        return ret;
    }

    public JPanel setupInventary() {
        JPanel ret = new JPanel();
        BoxLayout bt = new BoxLayout(ret, BoxLayout.Y_AXIS);
        ret.setLayout(bt);

        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Inventario"));
        ret.add(row1);

        ret.add(inventaryContent);
        reloadInventoryContent();
        return ret;
    }

    public JPanel setupArmery() {
        JPanel ret = new JPanel();
        BoxLayout bt = new BoxLayout(ret, BoxLayout.Y_AXIS);
        ret.setLayout(bt);

        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Armeria"));
        ret.add(row1);
        ret.add(armeriaContent);
        reloadArmeryContent();
        return ret;
    }

    //Reload Content
    private void reloadGold() {
        BigInteger gold = BigInteger.ZERO;
        try {
            gold = gm.getGold();
        } catch (BlockChainException e) {
            showError();
        }
        infoGold.setText(gold.toString() + " wei");
    }

    private void reloadArmeryContent() {
        armeriaContent.removeAll();
        List<Item> GameItems = new LinkedList<>();
        try {
            GameItems = gm.getArmory();
        } catch (BlockChainException e) {
            showError();
        }
        armeriaContent.setLayout(new GridLayout((GameItems.size() / 5) + 1, 5));
        for (int i = 0; i < GameItems.size(); i++) {
            JPanel p = new JPanel();
            BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
            p.setLayout(bl);

            p.add(new JLabel("Nome: " + GameItems.get(i).getName()));
            p.add(new JLabel("Prezzo: " + (GameItems.get(i).getVal1()) + " Wei"));
            p.add(new JLabel("Qauntità disponibile: " + GameItems.get(i).getVal2()));
            Button acq = new Button("Compra", i);
            acq.addActionListener(el -> {
                try {
                    gm.buy(((Button) el.getSource()).getPos());
                    showInfo("Acquisto effettuato con successo");
                    reloadGold();
                    reloadArmeryContent();
                } catch (BlockChainException e) {
                    showError();
                }
            });
            p.add(acq);
            armeriaContent.add(p);
        }
    }

    private void reloadInventoryContent() {
        inventaryContent.removeAll();
        List<Item> GameItems = new LinkedList<>();
        try {
            GameItems = gm.getInventory();
        } catch (BlockChainException e) {
            showError();
        }
        inventaryContent.setLayout(new GridLayout((GameItems.size() / 5) + 1, 5));
        for (int i = 0; i < GameItems.size(); i++) {
            JPanel p = new JPanel();
            BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
            p.setLayout(bl);

            p.add(new JLabel("Nome: " + GameItems.get(i).getName()));
            p.add(new JLabel("Id: " + GameItems.get(i).getVal1()));
            p.add(new JLabel("Numero: " + GameItems.get(i).getVal2()));
            Button acq = new Button("Vendi", i);
            acq.addActionListener(el -> {
                try {
                    gm.sell(((Button) el.getSource()).getPos());
                    showInfo("Vendita effettuata con successo");
                    reloadGold();
                    reloadInventoryContent();
                } catch (BlockChainException e) {
                    showError();
                }
            });
            p.add(acq);
            inventaryContent.add(p);
        }
    }

    //Metodi di servizio
    private void showError() {
        JOptionPane.showMessageDialog(this, "Errore sulla blockchain contralla la console", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void showError(String err) {
        JOptionPane.showMessageDialog(this, err, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String info) {
        JOptionPane.showMessageDialog(this, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
