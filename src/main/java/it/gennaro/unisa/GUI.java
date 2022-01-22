package it.gennaro.unisa;

import it.gennaro.unisa.panels.ArmoryPanel;
import it.gennaro.unisa.panels.InventoryPanel;
import it.gennaro.unisa.panels.LoginPanel;
import it.gennaro.unisa.panels.ProfilePanel;
import it.gennaro.unisa.utils.GameUtil;

import java.awt.*;
import javax.swing.*;

public class GUI extends JPanel {
    private LoginPanel login;
    private ArmoryPanel armory;
    private InventoryPanel inventory;
    private ProfilePanel head;
    private Panel body;
    private Panel footer;
    private GameUtil gm;


    public GUI() {
        setPreferredSize(new Dimension(900, 600));
        setLayout(new BorderLayout(0, 30));

        initBody();
        initFooter();

        add(body, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void initBody() {
        CardLayout card = new CardLayout();
        body = new Panel(card);
        login = new LoginPanel();

        body.add(login, "login");
        card.show(body, "login");
    }

    public void initFooter() {
        footer = new Panel(new FlowLayout());

        CardLayout cards = (CardLayout) body.getLayout();

        JButton loginButton = new JButton("Login");
        JButton inventarioButton = new JButton("Invetario");
        JButton armeriaButton = new JButton("Armeria");

        loginButton.addActionListener(el -> {
            if (el.getActionCommand().compareTo("Login") == 0) {
                String gameAddr = login.getGameAddr();
                String pkey = login.getPKey();
                if (gameAddr.length() == 0 || pkey.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Campi Obbligatori", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    gm = new GameUtil(gameAddr, pkey);
                    login();
                    loginButton.setText("Logout");
                    footer.add(inventarioButton);
                    footer.add(armeriaButton);
                    cards.show(body, "inventory");
                }
            } else {
                footer.removeAll();
                footer.add(loginButton);
                remove(head);
                loginButton.setText("Login");
                cards.show(body, "login");
                footer.repaint();
            }
        });
        inventarioButton.addActionListener(el -> {
            cards.show(body, "inventory");
            inventory.setup();
        });
        armeriaButton.addActionListener(el -> {
            cards.show(body, "armory");
            armory.setup();
        });

        footer.add(loginButton);
    }

    public void login() {
        try {
            head = new ProfilePanel(gm);
            add(head, BorderLayout.NORTH);

            armory = new ArmoryPanel(gm);
            body.add(armory, "armory");

            inventory = new InventoryPanel(gm);
            body.add(inventory, "inventory");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sulla blockchain: visualizza la console", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
