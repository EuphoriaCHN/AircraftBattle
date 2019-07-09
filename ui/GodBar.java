package pers.euphoria.aircraftbattle.ui;

public class GodBar extends FloatBar {
    public static GodBar open = new GodBar("./static/img/plugins/xust1.png", 200, 32);
    public static GodBar close = new GodBar("./static/img/plugins/xust2.png", 200, 32);

    private GodBar(String filePath, int imageWidth, int imageHeight) {
        super(filePath, imageWidth, imageHeight);
    }
}
