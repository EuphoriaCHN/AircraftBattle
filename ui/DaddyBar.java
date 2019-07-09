package pers.euphoria.aircraftbattle.ui;

public class DaddyBar extends FloatBar {
    public static DaddyBar open = new DaddyBar("./static/img/plugins/daddy1.png", 200, 32);
    public static DaddyBar close = new DaddyBar("./static/img/plugins/daddy2.png", 200, 32);

    private DaddyBar(String filePath, int imageWidth, int imageHeight) {
        super(filePath, imageWidth, imageHeight);
    }
}
