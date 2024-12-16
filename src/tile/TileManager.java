package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[40];

        getTileImage();
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        loadMap("/res/maps/world01.txt");
    }

    public void getTileImage() {
        setup(0, "000", true);
        setup(1, "001", false);
        setup(2, "002", false);
        setup(3, "003", true);
        setup(4, "004", false);
        setup(5, "005", true);
        setup(6, "006", true);
        setup(7, "007", true);
        setup(8, "008", true);
        setup(9, "009", true);
        setup(10, "010", true);
        setup(11, "011", true);
        setup(12, "012", true);
        setup(13, "013", true);
        setup(14, "014", true);
        setup(15, "015", true);
        setup(16, "016", true);
        setup(17, "017", true);
        setup(18, "018", true);
        setup(19, "019", false);
        setup(20, "020", false);
        setup(21, "021", false);
        setup(22, "022", false);
        setup(23, "023", false);
        setup(24, "024", false);
        setup(25, "025", false);
        setup(26, "026", false);
        setup(27, "027", false);
        setup(28, "028", false);
        setup(29, "029", false);
        setup(30, "030", false);
        setup(31, "031", true);
        setup(32, "032", false);
        setup(33, "033", true);
        setup(34, "034", false);

    }
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
            tile[index].collision = collision;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String file) {
        try {
            InputStream is = getClass().getResourceAsStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for(int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                for(int col = 0; col < gp.maxWorldCol; col++) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col] = num;
                }
            }  
        } catch(Exception e) {
        }
    }

    public void draw(Graphics2D g2) {
        // you shouldnt print out the whole screen every frame
        int startRow = (gp.player.worldY - gp.player.screenY)/gp.tileSize - 1;
        int endRow = (gp.player.worldY + gp.player.screenY)/gp.tileSize + 2;
        for(int row = startRow; row < endRow; row++) {
            // worldY gives the absolute position of the tile
            // player.worldY is the absolute position of the character
            // player.screenY makes it from relative to the player to relative to the screen
            int worldY = row * gp.tileSize;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            int startCol = (gp.player.worldX - gp.player.screenX)/gp.tileSize - 1;
            int endCol = (gp.player.worldX + gp.player.screenX)/gp.tileSize + 2;
            for(int col = startCol; col < endCol; col++) {
                int worldX = col * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;

                // generates fog on edges
                int tileNum = row < 0 || row >= gp.maxWorldRow || col < 0 || col >=gp.maxWorldCol? 6: mapTileNum[row][col];
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }  
    }
    /* draw but drawing fog on edges
    public void draw(Graphics2D g2) {
        // you shouldnt print out the whole screen every frame
        int startRow = Math.max((gp.player.worldY - gp.player.screenY)/gp.tileSize - 1,0);
        int endRow = Math.min((gp.player.worldY + gp.player.screenY)/gp.tileSize + 2,gp.maxWorldRow);
        for(int row = startRow; row < endRow; row++) {
            // worldY gives the absolute position of the tile
            // player.worldY is the absolute position of the character
            // player.screenY makes it from relative to the player to relative to the screen
            int worldY = row * gp.tileSize;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            int startCol = Math.max((gp.player.worldX - gp.player.screenX)/gp.tileSize - 1,0);
            int endCol = Math.min((gp.player.worldX + gp.player.screenX)/gp.tileSize + 2,gp.maxWorldCol);
            for(int col = startCol; col < endCol; col++) {
                int worldX = col * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;

                int tileNum = mapTileNum[row][col];
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }  
    }
     */
}
