package pers.euphoria.aircraftbattle.ui;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 天空类：飞行物
 *
 * @author Wang Qinhong
 */

public class Sky extends FlyingObject {
    // Declaration background image
    private static BufferedImage[] backgroundImages;

    // Static programming blocks - loading picture source
    static {
        backgroundImages = new BufferedImage[GameFactory.MAX_LEVEL];

        for (int i = 0; i < GameFactory.MAX_LEVEL; i++) {
            backgroundImages[i] = GameFactory.loadImage("./static/img/background/bg" + (i + 1) + ".jpg");
        }
    }

    private int speed;

    private int tempImageY;

    public Sky() {
        super(GameFactory.FRAME_WIDTH, GameFactory.SCREEN_HEIGHT, 0, 0);
        this.speed = 1;
        tempImageY = 0 - GameFactory.FRAME_HEIGHT;
    }

    @Override
    public void flyingMove() {
        y += speed;
        tempImageY += speed;
        if (y >= GameFactory.FRAME_HEIGHT) {
            y = 0 - GameFactory.FRAME_HEIGHT;
        }
        if (tempImageY >= GameFactory.FRAME_HEIGHT) {
            tempImageY = 0 - GameFactory.FRAME_HEIGHT;
        }
    }

    @Override
    public BufferedImage getImage() {
        return backgroundImages[GameFactory.level - 1];
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    @Override
    public void paintObject(Graphics g) {
        super.paintObject(g);
        g.drawImage(getImage(), x, y, null);
        g.drawImage(getImage(), x, tempImageY, null);
    }
}
