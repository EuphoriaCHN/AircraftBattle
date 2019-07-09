package pers.euphoria.aircraftbattle.ui;

public class KillBar extends FloatBar {
    public static KillBar open = new KillBar("./static/img/plugins/kill.png", 200, 32);

    private KillBar(String filePath, int imageWidth, int imageHeight) {
        super(filePath, imageWidth, imageHeight);
    }
}
