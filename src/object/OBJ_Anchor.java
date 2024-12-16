package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Anchor extends Entity{
    public OBJ_Anchor(GamePanel gp){
        super(gp);

        name = "Anchor";
        down1 = setup("/objects/Anchor");
    }
}
