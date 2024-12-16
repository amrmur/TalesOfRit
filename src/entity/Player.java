package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }
    public void getPlayerImage() {
        up1 = setup("player/rit_up_1");
        up2 = setup("player/rit_up_2");
        down1 = setup("player/rit_down_1");
        down2 = setup("player/rit_down_2");
        left1 = setup("player/rit_left_1");
        left2 = setup("player/rit_left_2");
        right1 = setup("player/rit_right_1");
        right2 = setup("player/rit_right_2");
    }

    public void setDefaultValues() {
        worldX = (int) (gp.tileSize * 50.5);
        worldY = gp.tileSize * 48;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }
    public void update() {
        // CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // CHECK OBJECT COLLISION
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        // CHECK NPC COLLISION
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        // CHECK MONSTER COLLISION
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        // CHECK EVENT
        gp.eHandler.checkEvent();
        
        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if(keyH.upPressed == true) {
                direction = "up";
            }
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
            }

            gp.eHandler.checkKeyDepEvent();
            gp.keyH.enterPressed = false;
            
            // if false, player can move
            if(collisionOn == false) {
                switch(direction) {
                    case "up":
                        worldY -= speed; // upper left is 0,0
                        break;
                    case "down":
                        worldY += speed; 
                        break;
                    case "left":
                        worldX -= speed; 
                        break;
                    case "right":
                        worldX += speed; 
                        break;
                }
            }

            spriteCounter++;
            // player image changes every 12 frames
            if(spriteCounter > 12) {
                spriteNum = spriteNum == 1 ? 2: 1;
                spriteCounter = 0;
            }
        }
        else {
            standCounter++;
            if(standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void pickUpObject(int i) {
        if(i != 999) {
        }
    }
    public void interactNPC(int i){
        if(i != 999) {
            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }
    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                life -= 1;
                invincible = true;
            }
        }
    }
    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);

        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction) {
            case "up":
            if(spriteNum == 1) {
                image = up1;
            }
            if(spriteNum == 2) {
                image = up2;
            }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        if(invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);
        
        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));        

        // draw collision for troubleshooting
        // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    
        // DEBUG
        // g2.setFont(new Font("Arial", Font.PLAIN, 26));
        // g2.setColor(Color.WHITE);
        // g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }
 }
