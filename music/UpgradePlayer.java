package pers.euphoria.aircraftbattle.music;

public class UpgradePlayer extends MusicPlayer {
    public UpgradePlayer() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/upgrade.mp3");
    }

    public static void play() {
        new UpgradePlayer().start();
    }
}
