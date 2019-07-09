package pers.euphoria.aircraftbattle.music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义媒体
 */

public abstract class MusicPlayer extends Thread {
    private Player player;

    MusicPlayer(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            player = new Player(bufferedInputStream);
        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}
