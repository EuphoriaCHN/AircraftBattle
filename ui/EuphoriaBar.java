package pers.euphoria.aircraftbattle.ui;

public class EuphoriaBar extends FloatBar {
    public static EuphoriaBar open = new EuphoriaBar("./static/img/plugins/euphoria1.png", 200, 32);
    public static EuphoriaBar close = new EuphoriaBar("./static/img/plugins/euphoria2.png", 200, 32);

    private EuphoriaBar(String filePath, int imageWidth, int imageHeight) {
        super(filePath, imageWidth, imageHeight);
    }
}
