package it.gennaro.unisa.panels.customization;

import javax.swing.*;

public class Button extends JButton {
    private final int pos;
    public Button(String text, int pos) {
        super(text);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
