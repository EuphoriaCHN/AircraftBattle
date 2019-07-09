package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class SmallBoomFlash extends Flash {
    static {
        width = 77;
        height = 77;

        images = new BufferedImage[20];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/smallboom/boom" + i + ".png");
        }
    }

    public SmallBoomFlash(int x, int y) {
        super(x, y);
    }

    public static BufferedImage[] getImages() {
        return images;
    }
}
