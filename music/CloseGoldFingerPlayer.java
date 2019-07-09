package pers.euphoria.aircraftbattle.music;

public class CloseGoldFingerPlayer extends MusicPlayer {
    private CloseGoldFingerPlayer() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/close_god.mp3");
    }

    public static void play() {
        new CloseGoldFingerPlayer().start();
    }
}
