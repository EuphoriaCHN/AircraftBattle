package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class MiddleBoomFlash extends Flash {
    static {
        width = 116;
        height = 116;

        images = new BufferedImage[20];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/midboom/boom" + i + ".png");
        }
    }

    public MiddleBoomFlash(int x, int y) {
        super(x, y);
    }

    public static BufferedImage[] getImages() {
        return images;
    }
}
