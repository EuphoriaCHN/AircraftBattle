package pers.euphoria.aircraftbattle.ui;

public class HPBar extends FloatBar {
    public static HPBar open = new HPBar("./static/img/plugins/hp1.png", 200, 24);
    public static HPBar close = new HPBar("./static/img/plugins/hp2.png", 200, 27);

    private HPBar(String filePath, int imageWidth, int imageHeight) {
        super(filePath, imageWidth, imageHeight);
    }
}
