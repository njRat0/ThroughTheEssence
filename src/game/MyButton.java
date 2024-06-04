package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;





class MyButton{
    private boolean isMouseOver =  false;

    private Color curColor = new Color(0,0,0);
    public Color colorOver = new Color(179, 250, 160);
    public Color colorClick = new Color(152, 184, 144);
    public Color colorBackground = new Color(30, 136, 56);
    public Color colorBorders = new Color(30, 136, 56);
    public int borderSize = 2;
    public int id;
    // public int locX = 0;
    // public int locY = 0;
    private Player player;

    private Rectangle rectangleOfButton = new Rectangle();

    public void SetLocation(int x, int y){
        rectangleOfButton.x = x;
        rectangleOfButton.y = y;
    }

    public int GetLocationX(){
        return rectangleOfButton.x;
    }

    public int GetLocationY(){
        return rectangleOfButton.y;
    }

    public void SetSize(int x, int y){
        rectangleOfButton.width = x;
        rectangleOfButton.height = y;
    }
    //rectangleOfButton.x <= player.mouseX && player.mouseX <= rectangleOfButton.x + rectangleOfButton.width && rectangleOfButton.y <= GameLoop.mouseY && GameLoop.mouseY <= rectangleOfButton.y + rectangleOfButton.height
    public void update(){
        if(rectangleOfButton.contains(player.mouseX, player.mouseY)) {
            if(isMouseOver == false){
                isMouseOver = true;
                curColor = colorOver;
            }
        }
        else{
            if(isMouseOver == true){
                isMouseOver = false;
                curColor = colorBackground;
            }
        }

        if(player.mousePress == true && isMouseOver == true ){
            curColor = colorClick;
            GameLoop.EndChoosingSkills(id);
        }
        else{
            if(isMouseOver == true){
                curColor = colorOver;
            }
            else{
                curColor = colorBackground;
            }
        }
    }

    
    public MyButton(Player player){
        this.player = player;
        //this.setLayout(new FlowLayout());
        curColor = colorBackground;
        //GameFrame.add(this);
    }

    public void toDraw(Graphics2D g2d){
        g2d.setColor(colorBorders);
        g2d.fillRect(rectangleOfButton.x, rectangleOfButton.y, rectangleOfButton.width, rectangleOfButton.height);
        g2d.setColor(curColor);
        g2d.fillRect(rectangleOfButton.x + borderSize, rectangleOfButton.y + borderSize, rectangleOfButton.width - borderSize * 2 , rectangleOfButton.height - borderSize *2);
    }
}