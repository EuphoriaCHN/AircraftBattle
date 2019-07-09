package pers.euphoria.aircraftbattle.ui;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Warning {
    private static BufferedImage bufferedImage;

    private static int width = 453;
    private static int height = 92;
    private static int x = -1 * width;
    private static int y = (GameFactory.FRAME_HEIGHT - height) / 2 - 30;

    static {
        bufferedImage = GameFactory.loadImage("./static/img/plugins/warning.png");
    }

    public static boolean move(Graphics graphics) {
        graphics.drawImage(bufferedImage, x, y, null);
        if (x < (GameFactory.FRAME_WIDTH - width) / 2) {
            if (x > (-1 * width / 2)) {
                x += 3;
            } else {
                x += 6;
            }
        } else {
            if (x < GameFactory.FRAME_WIDTH / 2) {
                x += 3;
            } else {
                x += 6;
            }
        }
        return x < GameFactory.FRAME_WIDTH;
    }

    public static void clear() {
        x = -1 * width;
        y = (GameFactory.FRAME_HEIGHT - height) / 2;
    }

    public static BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
