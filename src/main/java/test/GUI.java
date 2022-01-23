package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JPanel GUIPanel;
    private JPanel LoginPanel;
    private JPanel InvetoryPanel;
    private JButton loginButton;
    private JTextField gameAddr;
    private JTextField pKey;
    private JCheckBox checkBox1;
    private JPanel LoggedPanel;
    private JButton compraGoldButton;
    private JTextField a001TextField;
    private JPanel ProfilePanel;
    private JPanel ArmeryPanel;
    private JButton inventarioButton;
    private JButton armeriaButton;
    private JPanel ActionPanel;
    private JPanel FooterPanel;

    public GUI() {
        inventarioButton.addActionListener(el -> {
            ((CardLayout) LoggedPanel.getLayout()).show(ActionPanel, "InventoryCard");
        });
        armeriaButton.addActionListener(el -> {
            ((CardLayout) LoggedPanel.getLayout()).show(ActionPanel, "ArmeryCard");
        });
        loginButton.addActionListener(el -> {
            ((CardLayout) GUIPanel.getLayout()).show(GUIPanel, "LoggedCard");
        });
    }
}
