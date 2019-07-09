package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BigBoomFlash extends Flash {
    private int index = 0;
    private boolean status = false;
    private static BufferedImage[] images;

    static {
        width = 151;
        height = 151;

        images = new BufferedImage[30];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/bigboom/boom" + i + ".png");
        }
    }

    public BigBoomFlash(int x, int y) {
        super(x, y);
    }

    public static BufferedImage[] getImages() {
        return images;
    }

    public void drawBossDied(Graphics g) {
        if (!status) {
            g.drawImage(images[index++], x, y, null);
            if (index >= 30) {
                status = true;
            }
        }
    }
}
