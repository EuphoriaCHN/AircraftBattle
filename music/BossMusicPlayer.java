package pers.euphoria.aircraftbattle.music;

public class BossMusicPlayer extends MusicPlayer {
    private static BossMusicPlayer bossMusic;

    private BossMusicPlayer() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/euphoria.mp3");
    }

    public static void play() {
        bossMusic = new BossMusicPlayer();
        bossMusic.start();
    }

    public static BossMusicPlayer getBossMusic() {
        return bossMusic;
    }
}
