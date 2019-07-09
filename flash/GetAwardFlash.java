package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class GetAwardFlash extends Flash {
    static {
        width = 60;
        height = 60;

        images = new BufferedImage[9];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/getaward/boom" + i + ".png");
        }
    }

    public GetAwardFlash(int x, int y) {
        super(x, y);
    }

    public static BufferedImage[] getImages() {
        return images;
    }
}
