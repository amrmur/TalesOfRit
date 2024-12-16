package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
    GamePanel gp;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    // where hitbox is in world
    public Rectangle solidArea = new Rectangle(0,0,48,48);

    // for resetting after collision detection
    public int solidAreaDefaultX, solidAreaDefaultY;

    public boolean collisionOn = false;

    public int actionLockCounter = 0;

    public boolean invincible = false;
    public int invincibleCounter = 0;

    String dialogues[] = new String[10];
    int dialogueIndex = 0;

    public int type; // 0 = player, 1 = npc, 2 = monster

    // MOVEMENT AREA
    public int moveWidth;
    public int moveHeight;
    public int startX;
    public int startY;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;

    public Entity(GamePanel gp){
        this.gp=gp;
    }
    public void setPos(int x, int y){
        worldX = x;
        startX = x;
        worldY = y;
        startY = y;
    }
    public void setAction() {

    }
    public void speak(){
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }
    public void update(){
        // do not update if not visible
        if(!(worldX > gp.player.worldX - gp.player.screenX - gp.tileSize  &&
        worldX < gp.player.worldX + gp.player.screenX + gp.tileSize  &&
        worldY > gp.player.worldY - gp.player.screenY - gp.tileSize  &&
        worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) ) 
            return;
        
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 2 && contactPlayer){
            if(!gp.player.invincible){
                gp.player.life-=1;
                gp.player.invincible = true;
            }
        }

        if(collisionOn == false) {
            switch(direction) {
                case "up":
                    worldY -= speed;
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
        if(spriteCounter > 12) {
            spriteNum = spriteNum == 1 ? 2: 1;
            spriteCounter = 0;
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        // is it on screen
        if(worldX > gp.player.worldX - gp.player.screenX - gp.tileSize  &&
        worldX < gp.player.worldX + gp.player.screenX + gp.tileSize  &&
        worldY > gp.player.worldY - gp.player.screenY - gp.tileSize  &&
        worldY < gp.player.worldY + gp.player.screenY + gp.tileSize ) { 
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
    
            g2.drawImage(image, screenX, screenY, null);    
            // draw collision for troubleshooting
            // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }  
    }
    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/"+imagePath+".png"));
            image=uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
