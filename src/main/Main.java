package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Tales of Rit");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // Window sized based on subcomponent

        window.setLocationRelativeTo(null); // center
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
