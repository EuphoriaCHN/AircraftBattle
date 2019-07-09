package pers.euphoria.aircraftbattle.music;

public class EnemyLevelThreeDown extends MusicPlayer {
    private EnemyLevelThreeDown() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/enemy3_down.mp3");
    }

    public static void play() {
        new EnemyLevelThreeDown().start();
    }
}
