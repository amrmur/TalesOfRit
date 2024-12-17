package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_Ghost extends Entity{
    GamePanel gp;
    public MON_Ghost(GamePanel gp){
        super(gp);
        this.gp = gp;
        
        type = 2;
        name="Ghost";
        speed=2;
        maxLife=12;
        life=maxLife;
        attack = 5;
        defense = 0;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 12;
        solidArea.width = 39;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage(){
        up1 = setup("/monster/ghost/ghost_down_1",gp.tileSize,gp.tileSize);
        up2 = setup("/monster/ghost/ghost_down_2",gp.tileSize,gp.tileSize);
        down1 = setup("/monster/ghost/ghost_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/monster/ghost/ghost_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/monster/ghost/ghost_down_1",gp.tileSize,gp.tileSize);
        left2 = setup("/monster/ghost/ghost_down_2",gp.tileSize,gp.tileSize);
        right1 = setup("/monster/ghost/ghost_down_1",gp.tileSize,gp.tileSize);
        right2 = setup("/monster/ghost/ghost_down_2",gp.tileSize,gp.tileSize);
    }
    public void setAction(){
        actionLockCounter++;
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if(i <= 25){
                direction = "up";
            } else if(i <= 50){
                direction = "down";
            } else if(i <= 75){
                direction = "left";
            } else {
                direction = "right";          
            }
            actionLockCounter = 0;
        }
    }
    public void damageReaction(){
        actionLockCounter = 0;
        Random random = new Random();
        int i = random.nextInt(100) + 1;
        // AI where the ghost goes perpendicular to player
        if(gp.player.direction == "up" || gp.player.direction == "down"){
            if(i > 50){
                direction = "left";
            } else direction = "right";
        } else if(gp.player.direction == "left" || gp.player.direction == "right"){
            if(i > 50){
                direction = "up";
            } else direction = "down";
        }    
    }
}
