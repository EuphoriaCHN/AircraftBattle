package pers.euphoria.aircraftbattle.music;

public class EnemyLevelTwoDown extends MusicPlayer {
    private EnemyLevelTwoDown() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/enemy2_down.mp3");
    }

    public static void play() {
        new EnemyLevelTwoDown().start();
    }
}
