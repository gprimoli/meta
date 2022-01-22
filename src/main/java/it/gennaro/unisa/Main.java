package it.gennaro.unisa;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame ("Meta Game");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        //GUI è stato scritto con l'unico scopo di fare TEST!
        frame.getContentPane().add (new GUI());
        frame.pack();
        frame.setVisible (true);
    }
}
