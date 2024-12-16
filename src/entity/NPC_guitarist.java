package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_guitarist extends Entity{
    public NPC_guitarist(GamePanel gp) {
        super(gp);

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getNPCImage();
        setDefaultValues();
        setDialogue();
    }
    public void getNPCImage() {
        up1 = setup("npc/guitarist_up_1");
        up2 = setup("npc/guitarist_up_2");
        down1 = setup("npc/guitarist_down_1");
        down2 = setup("npc/guitarist_down_2");
        left1 = setup("npc/guitarist_left_1");
        left2 = setup("npc/guitarist_left_2");
        right1 = setup("npc/guitarist_right_1");
        right2 = setup("npc/guitarist_right_2");
    }
    public void setDefaultValues() {
        speed = 1;
        direction = "down";
    }
    public void setDialogue(){
        dialogues[0] = "So you also came to \nStrumstone island to find the \nmythical guitar pick.";
        dialogues[1] = "Legend has it that those who \ntouch the pick will shred \nbetter than Jimi Hendrix.";
        dialogues[4] = "Kinda random but is my \nbalding super noticeable? \nIt's gotten worse since I came \nto this island.";
        dialogues[3] = "I got this leather jacket on \none of my guitar tours. I \nstopped doing tours since I \ncame to Strumstone though.";
        dialogues[2] = "If you want to speak with the \nTrader, he lives in the woods \non the left side of the island.";
        dialogues[5] = "If you lose your health, \nthere's a healing pool on the \nearthy right side of the \nisland.";
    }
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter == 80) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // random number from 1 to 100
            if(i <= 25){
                // make npc stay in set box
                if(worldY < startY - moveHeight/2){
                    direction="down";
                } else {
                    direction = "up";
                }
            } else if(i <= 50){
                if(worldY > startY + moveHeight/2){
                    direction="up";
                } else {
                    direction = "down";
                }
            } else if(i <= 75){
                if(worldX < startX - moveWidth/2){
                    direction="right";
                } else {
                    direction = "left";
                }
            } else {
                if(worldX > startX + moveWidth/2){
                    direction="left";
                } else {
                    direction = "right";
                }            
            }

            actionLockCounter = 0;
        }
    }
    public void speak(){
        super.speak();
    }
}
