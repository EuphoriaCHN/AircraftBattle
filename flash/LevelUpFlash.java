package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelUpFlash {
    private static final int LENGTH = 7;
    private static BufferedImage[] images = new BufferedImage[LENGTH];
    private static int[][] imageAttribute = new int[][]{
            {94, 112}, {142, 152}, {156, 172}, {136, 170}, {142, 130}, {156, 148}, {162, 162}
    };
    private static int controlIndex = 0;
    private static int controlSpeed = 0;

    static {
        for (int i = 0; i < LENGTH; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/levelup/levelup" + i + ".png");
        }
    }

    private static void drawFlash(Hero hero, Graphics graphics) {
        int dx, dy;
        dx = hero.getX() + hero.flyingObjectWidth / 2 - imageAttribute[controlIndex][0] / 2;
        dy = hero.getY() + hero.flyingObjectHeight / 2 - imageAttribute[controlIndex][1] / 2;
        graphics.drawImage(images[controlIndex], dx, dy, null);
    }

    public static boolean drawLevel(Hero hero, Graphics graphics) {
        controlSpeed++;
        if (controlSpeed % 8 == 0) {
            controlSpeed = 0;
            controlIndex++;
            if (controlIndex == LENGTH - 1) {
                controlIndex = 0;
                return false;
            }
        }
        drawFlash(hero, graphics);
        return true;
    }
}
