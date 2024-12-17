package main;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp=gp;

        eventRect = new EventRect[gp.maxWorldRow][gp.maxWorldCol];
        for(int row = 0; row < gp.maxWorldRow; row++){
            for(int col = 0; col < gp.maxWorldCol; col++){
                eventRect[row][col] = new EventRect();
                eventRect[row][col].x = 23;
                eventRect[row][col].y = 23;
                eventRect[row][col].width = 2;
                eventRect[row][col].height = 2;
                eventRect[row][col].eventRectDefaultX = eventRect[row][col].x;
                eventRect[row][col].eventRectDefaultY = eventRect[row][col].y;
            }
        }
        // healing pool event
        eventRect[57][80].x = -1 * gp.tileSize/4;
        eventRect[57][80].y = -1 * gp.tileSize/4;
        eventRect[57][80].width = (int) (gp.tileSize * 1.5);
        eventRect[57][80].height = (int) (gp.tileSize * 1.5);
        eventRect[57][80].eventRectDefaultX = eventRect[57][80].x;
        eventRect[57][80].eventRectDefaultY = eventRect[57][80].y;
    }

    public void checkEvent(){
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent){
            if(hit(38,83,"any")){
                damagePit(gp.dialogueState,38,83);
            }
            if(hit(65,49,"any")){
                damagePit(gp.dialogueState,65,49);
            }
            if(hit(44,45,"any")){
                damagePit(gp.dialogueState,44,45);
            }
            if(hit(57,80,"any")){
                healingPool(gp.dialogueState);
            }
            if(hit(44,36,"any")){
                teleport(gp.dialogueState, (int) (69.5*gp.tileSize),54*gp.tileSize);
            }
        }
    }

    public boolean hit(int row, int col, String reqDirection){
        boolean hit = false;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[row][col].x = col*gp.tileSize+eventRect[row][col].x;
        eventRect[row][col].y = row*gp.tileSize+eventRect[row][col].y;

        if(gp.player.solidArea.intersects(eventRect[row][col]) && eventRect[row][col].eventDone == false){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[row][col].x = eventRect[row][col].eventRectDefaultX;
        eventRect[row][col].y = eventRect[row][col].eventRectDefaultY;
        return hit;
    }
    public void teleport(int gameState, int x, int y){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You stepped in a colorful \nportal and were teleported \nto the other side of the \nisland.";
        gp.player.worldX=x;
        gp.player.worldY=y;
    }
    public void damagePit(int gameState, int row, int col){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a sinkhole!";
        gp.player.life-=1;
        canTouchEvent = false;
    }
    public void healingPool(int gameState){
        if(gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            if(gp.player.life != gp.player.maxLife) {
                gp.ui.currentDialogue = "You drank the healing water \nand gained your health back.\nYou hear the echoes of \nmonsters.";
                gp.player.life=gp.player.maxLife;
                gp.aSetter.setMonster();
            } else {
                gp.ui.currentDialogue = "Your life is full. No need for \nthe healing water.";
            }
        }
    }
}
