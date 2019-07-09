package pers.euphoria.aircraftbattle.music;

public class OpenGoldFingerPlayer extends MusicPlayer {
    private OpenGoldFingerPlayer() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/open_god.mp3");
    }

    public static void play() {
        new OpenGoldFingerPlayer().start();
    }
}
