package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;



class MyButton extends JButton implements MouseListener {
    private boolean isMouseOver =  false;

    private Color curColor = new Color(0,0,0);
    public Color colorOver = new Color(179, 250, 160);
    public Color colorClick = new Color(152, 184, 144);
    public Color colorBackground = new Color(30, 136, 56);
    public Color colorBorders = new Color(30, 136, 56);
    public int borderSize = 2;
    // public int locX = 0;
    // public int locY = 0;

    private Rectangle rectangleOfButton = new Rectangle();

    public void SetLocation(int x, int y){
        rectangleOfButton.x = x;
        rectangleOfButton.y = y;
    }

    public void SetSize(int x, int y){
        rectangleOfButton.width = x;
        rectangleOfButton.height = y;
    }

    public void SetUp(){
        //this.setBounds(rectangleOfButton);
        //this.setLocation(rectangleOfButton.x,rectangleOfButton.y);
        //this.setSize(rectangleOfButton.width,rectangleOfButton.height);
        this.setBounds(rectangleOfButton);
        addMouseListener(this);
    }

    
    public MyButton(){
        //this.setLayout(new FlowLayout());
        this.addMouseListener(null);
        this.setVisible(false);
        this.setEnabled(true);
        GameFrame.addButton(this);
        //GameFrame.add(this);
    }

    public void toDraw(Graphics2D g2d){
        this.setLayout(new FlowLayout());
        g2d.setColor(colorBorders);
        g2d.fillRect(rectangleOfButton.x, rectangleOfButton.y, rectangleOfButton.width, rectangleOfButton.height);
        g2d.setColor(curColor);
        g2d.fillRect(rectangleOfButton.x + borderSize, rectangleOfButton.y + borderSize, rectangleOfButton.width - borderSize * 2 , rectangleOfButton.height - borderSize *2);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() == this){
            System.out.println(1);
            curColor = colorOver;
            isMouseOver = true;
        }
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource() == this){
            System.out.println(2);
            curColor = colorOver;
            isMouseOver = false;
        }
    
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() == this){
            System.out.println(3);
            curColor = colorClick;
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource() == this){
            System.out.println(4);
            if (isMouseOver) {
                curColor = colorOver;
            } else {
                curColor = colorBackground;
            }
        }   
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == this){
            System.out.println(5);
        }
    }
}