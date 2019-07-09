package pers.euphoria.aircraftbattle.music;

public class EnemyLevelOneDown extends MusicPlayer {
    private EnemyLevelOneDown() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/enemy1_down.mp3");
    }

    public static void play() {
        new EnemyLevelOneDown().start();
    }
}
