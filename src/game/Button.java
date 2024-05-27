package game;

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.event.InputMethodEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.*;


class Button extends JButton {
    private boolean isMouseOver =  false;

    private Color curColor = new Color(0,0,0);
    public Color colorOver = new Color(179, 250, 160);
    public Color colorClick = new Color(152, 184, 144);
    public Color colorBackground = new Color(30, 136, 56);
    public int sizeX = 10;
    public int sizeY = 10;
    public int locX = 0;
    public int locY = 0;

    
    public Button(){

    }

    public void toDraw(Graphics2D g2d){

    }
}