package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import object.OBJ_Heart;
import entity.Entity;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font yoster, yoster40, yoster60;
    BufferedImage heart_full, heart_half, heart_blank;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean messageOn = false;
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
    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(yoster);
        g2.setColor(Color.white);

        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        } else if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        } else if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        } else if(gp.gameState == gp.titleState){
            drawTitleScreen(); 
        } else if(gp.gameState == gp.characterState){
            drawPlayerLife();
            drawCharacterScreen();
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
    public void drawMessage(){
        int messageX = 24;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(25F));
        
        if(message.isEmpty()) return;

        for(int i = 0; i < message.size(); i++){
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(message.get(i), messageX, messageY);
            g2.setColor(Color.WHITE);
            g2.drawString(message.get(i), messageX+2, messageY+2);

            int counter = messageCounter.get(i)+1;
            messageCounter.set(i, counter);
            messageY += 35;

            if(messageCounter.get(i) > 180){
                message.remove(i);
                messageCounter.remove(i);
            }            
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
    public void drawCharacterScreen(){
        //FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(25F));

        int textX = frameX + 15;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Lvl", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight+10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight+10;
        g2.drawString("Shield", textX, textY);

        // VALUES
        int tailX = (frameX + frameWidth) - 18;
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += 10;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY, null);

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
    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return tailX - length;
    }
}
