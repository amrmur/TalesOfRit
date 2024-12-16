package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import object.OBJ_Heart;
import entity.Entity;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font yoster, yoster40, yoster60;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;

    public UI(GamePanel gp){
        this.gp = gp;

        InputStream is = getClass().getResourceAsStream("/res/fonts/yoster.ttf");
        try {
            yoster = Font.createFont(Font.TRUETYPE_FONT, is);
            yoster60 = yoster.deriveFont(60f);

        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // HEADS-UP DISPLAY
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(yoster);
        g2.setColor(Color.white);

        if(gp.gameState == gp.playState){
            drawPlayerLife();
        } else if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        } else if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        } else if(gp.gameState == gp.titleState){
            drawTitleScreen(); 
        }
    }
    public void drawPlayerLife(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        int drawLife = gp.player.life;
        while(i < gp.player.maxLife/2) {
            if(drawLife>1){
                g2.drawImage(heart_full, x, y, null);
                drawLife-=2;
            }else if(drawLife==1){
                g2.drawImage(heart_half, x, y, null);
                drawLife-=1;
            }else{
                g2.drawImage(heart_blank, x, y, null);
            }
            i++;
            x+=gp.tileSize;
        }
    }
    public void drawTitleScreen(){
        // BACKGROUND
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "Tales of Rit";

        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text, x+5, y+5);

        // MAIN TEXT
        g2.setColor(Color.magenta);
        g2.drawString(text, x, y);

        // RIT IMAGE
        x = (int) (gp.screenWidth/2 - gp.tileSize*1.5);
        y+= gp.tileSize;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*3,gp.tileSize*3,null);
        
        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize*4.5;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-gp.tileSize,y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-gp.tileSize,y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-gp.tileSize,y);
        }

        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,14F));
        text = "Made by Amrit Murali using RyiSnow's tutorial";
        x = getXforCenteredText(text);
        y = gp.screenHeight - 5;
        g2.drawString(text, x, y);
    }
    public void drawPauseScreen() {
        g2.setFont(yoster60);
        g2.setColor(Color.blue);
        String text = "Press P to Continue";
        g2.drawString(text,getXforCenteredText(text),gp.screenHeight/2);
    }
    public void drawDialogueScreen() {
        // WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - gp.tileSize*4;
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32));
        x += 25;
        y += 40;
        for(String line: currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y+=40;

        }
    }
    public void drawSubWindow(int x, int y, int width, int height){
        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x+5, y+4, width-10, height-10, 25, 25);
    }
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}
