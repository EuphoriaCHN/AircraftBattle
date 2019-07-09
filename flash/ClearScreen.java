package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClearScreen {
    private static final int LENGTH = 20;
    private static BufferedImage[] images = new BufferedImage[LENGTH];
    private static int controlIndex = 0;
    private static int controlSpeed = 0;

    static {
        for (int i = 0; i < LENGTH; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/clear/clear" + i + ".png");
        }
    }

    private static void drawFlash(Graphics graphics) {
        graphics.drawImage(images[controlIndex], 63, 28, null);
    }

    public static boolean drawLevel(Graphics graphics) {
        controlSpeed++;
        if (controlSpeed % 6 == 0) {
            controlSpeed = 0;
            controlIndex++;
            if (controlIndex == LENGTH - 1) {
                controlIndex = 0;
                return false;
            }
        }
        drawFlash(graphics);
        return true;
    }
}
