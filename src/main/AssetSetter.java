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
        gp.monster[0] = new MON_Ghost(gp);
        gp.monster[0].worldX = gp.tileSize * 67;
        gp.monster[0].worldY = gp.tileSize * 63;  
        
        gp.monster[1] = new MON_Ghost(gp);
        gp.monster[1].worldX = gp.tileSize * 69;
        gp.monster[1].worldY = gp.tileSize * 65;  
    }

}