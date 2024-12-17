package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/res/sound/world01.wav");
        soundURL[1] = getClass().getResource("/res/sound/coin.wav");
        soundURL[2] = getClass().getResource("/res/sound/open.wav");
        soundURL[3] = getClass().getResource("/res/sound/unlock.wav");
        soundURL[5] = getClass().getResource("/res/sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/res/sound/receiveddamage.wav");
        soundURL[7] = getClass().getResource("/res/sound/swingweapon.wav");
        soundURL[8] = getClass().getResource("/res/sound/levelup.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch(Exception e) {

        }
    }
    public void play() {
        clip.start();
    }
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();
    }
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }
    
}
