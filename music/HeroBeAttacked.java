package pers.euphoria.aircraftbattle.music;

public class HeroBeAttacked extends MusicPlayer {
    HeroBeAttacked() {
        super("./src/pers/euphoria/aircraftbattle/static/sound/me_down.mp3");
    }

    public static void play() {
        new HeroBeAttacked().start();
    }
}
