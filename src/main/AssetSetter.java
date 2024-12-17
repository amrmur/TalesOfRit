package main;

import entity.NPC_guitarist;
import monster.MON_Ghost;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
    }
    public void setNPC() {
        gp.npc[0] = new NPC_guitarist(gp);
        gp.npc[0].setPos(53 * gp.tileSize, 50 * gp.tileSize);
        gp.npc[0].moveHeight = 5 * gp.tileSize;
        gp.npc[0].moveWidth = 5 * gp.tileSize;
    }
    public void setMonster(){
        int i = 0;
        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 67;
        gp.monster[i].worldY = gp.tileSize * 63;  
        i++;
        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 69;
        gp.monster[i].worldY = gp.tileSize * 65;  
        i++;
        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 77;
        gp.monster[i].worldY = gp.tileSize * 36;  
        i++;
        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 79;
        gp.monster[i].worldY = gp.tileSize * 38;   
        i++; 
    }

}
