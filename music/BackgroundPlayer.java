package pers.euphoria.aircraftbattle.music;

public class BackgroundPlayer extends MusicPlayer {
    private static BackgroundPlayer backgroundPlayer;

    private BackgroundPlayer() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/game_music.mp3");
    }

    public static void play() {
        backgroundPlayer = new BackgroundPlayer();
        backgroundPlayer.start();
    }

    public static BackgroundPlayer getBackgroundPlayer() {
        return backgroundPlayer;
    }
}
