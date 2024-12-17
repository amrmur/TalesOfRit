package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;

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

        attackArea.width = 36;
        attackArea.height  = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }
    public void getPlayerImage() {
        up1 = setup("player/walking/rit_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("player/walking/rit_up_2",gp.tileSize,gp.tileSize);
        down1 = setup("player/walking/rit_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("player/walking/rit_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("player/walking/rit_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("player/walking/rit_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("player/walking/rit_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("player/walking/rit_right_2",gp.tileSize,gp.tileSize);
    }
    public void getPlayerAttackImage(){
        attackUp1 = setup("player/attacking/rit_attack_up_1",gp.tileSize,gp.tileSize*2);
        attackUp2 = setup("player/attacking/rit_attack_up_2",gp.tileSize,gp.tileSize*2);
        attackDown1 = setup("player/attacking/rit_attack_down_1",gp.tileSize,gp.tileSize*2);
        attackDown2 = setup("player/attacking/rit_attack_down_2",gp.tileSize,gp.tileSize*2);
        attackLeft1 = setup("player/attacking/rit_attack_left_1",gp.tileSize*2,gp.tileSize);
        attackLeft2 = setup("player/attacking/rit_attack_left_2",gp.tileSize*2,gp.tileSize);
        attackRight1 = setup("player/attacking/rit_attack_right_1",gp.tileSize*2,gp.tileSize);
        attackRight2 = setup("player/attacking/rit_attack_right_2",gp.tileSize*2,gp.tileSize);
    }

    public void setDefaultValues() {
        worldX = (int) (gp.tileSize * 50.5);
        worldY = (int) (gp.tileSize * 48.5);
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1; // > Strength, > Attack
        dexterity = 1; // > Dexterity, > Defense
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack();
        defense = getDefense();
    }
    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }
    public void update() { 
        if(attacking == true){
            attacking();
        } else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed 
        || keyH.enterPressed ) {
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
            
            // if no collision and enter, player can move
            if(collisionOn == false && !keyH.enterPressed) {
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

            if(keyH.enterPressed && !attackCanceled){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;

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
    public void attacking(){
        spriteCounter++;
        if(spriteCounter <= 5 || spriteCounter > 19 && spriteCounter<=25) {
            spriteNum = 1;
        } else if(spriteCounter > 5 && spriteCounter <= 19){
            spriteNum = 2;

            // all of this checks if the attack box collides with the monster

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attackArea
            switch(direction){
                case "up":worldY-=attackArea.height; break;
                case "down":worldY+=attackArea.height; break;
                case "left":worldX-=attackArea.width; break;
                case "right":worldX+=attackArea.width; break;
            }
            // attackArea becomes solidArea
            solidArea.width = attackArea.height;
            solidArea.height = attackArea.width;

            // check monster collision with updated worldX,worldY, and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // restoring original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        } else if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void pickUpObject(int i) {
        if(i != 999) {
        }
    }
    public void interactNPC(int i){
        if(gp.keyH.enterPressed == true){
            if(i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            } 
        }
        
    }
    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                gp.playSE(6);
                int damage = gp.monster[i].attack - defense;
                if(damage < 0){damage = 0;}
                life -= damage;
                invincible = true;
            }
        }
    }
    public void damageMonster(int i){
        if(i != 999){
            if(gp.monster[i].invincible == false){
                gp.playSE(5);
                int damage = attack - gp.monster[i].defense;
                if(damage < 0){damage = 0;}

                // critical hit
                Random random = new Random();
                int j = random.nextInt(100) + 1;
                if(j>94){
                    damage*=2;
                    gp.monster[i].life -= damage;
                    gp.ui.addMessage("Critical hit! " + damage + " dmg!");
                } else {
                    gp.monster[i].life -= damage;
                    gp.ui.addMessage(damage + " damage!");
                }

                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();
                if(gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Slayed the " + gp.monster[i].name + "!");
                    gp.ui.addMessage("Gained " + gp.monster[i].exp + " exp");
                    exp += gp.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            // exp increases for each level
            nextLevelExp = (int)(5 + 9*(level-1) + 9*((level-1)*(level-2))/2);
            maxLife += 2;
            life+=2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "Level Up!  " + (level - 1) + " > " + level;
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) {
            case "up":
                if(!attacking){
                    if(spriteNum == 1) { image = up1;}
                    if(spriteNum == 2) {image = up2;}
                } else if(attacking){
                    tempScreenY-=gp.tileSize;
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                break;
            case "down":
                if(!attacking){
                    if(spriteNum == 1) { image = down1;}
                    if(spriteNum == 2) {image = down2;}
                } else if(attacking){
                    if(spriteNum == 1) {image = attackDown1;}
                    if(spriteNum == 2) {image = attackDown2;}
                }
                break;
            case "left":
                if(!attacking){
                    if(spriteNum == 1) { image = left1;}
                    if(spriteNum == 2) {image = left2;}
                } else if(attacking){
                    tempScreenX-=gp.tileSize;
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                break;
            case "right":
                if(!attacking){
                    if(spriteNum == 1) { image = right1;}
                    if(spriteNum == 2) {image = right2;}
                } else if(attacking){
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2;}
                }
                break;
        }

        if(invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);
        
        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));        

        // draw collision for troubleshooting
        // g2.drawRect(tempScreenX + solidArea.x, tempScreenY + solidArea.y, solidArea.width, solidArea.height);
    
        // DEBUG
        // g2.setFont(new Font("Arial", Font.PLAIN, 26));
        // g2.setColor(Color.WHITE);
        // g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }
 }
