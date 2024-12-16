package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_Ghost extends Entity{
    public MON_Ghost(GamePanel gp){
        super(gp);
        type = 2;
        name="Ghost";
        speed=1;
        maxLife=5;
        life=maxLife;

        solidArea.x = 3;
        solidArea.y = 12;
        solidArea.width = 39;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage(){
        up1 = setup("/monster/ghost_down_1");
        up2 = setup("/monster/ghost_down_2");
        down1 = setup("/monster/ghost_down_1");
        down2 = setup("/monster/ghost_down_2");
        left1 = setup("/monster/ghost_down_1");
        left2 = setup("/monster/ghost_down_2");
        right1 = setup("/monster/ghost_down_1");
        right2 = setup("/monster/ghost_down_2");
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
}
