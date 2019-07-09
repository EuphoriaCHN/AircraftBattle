package pers.euphoria.aircraftbattle.award;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public abstract class Bee extends FlyingObject implements Award {
    protected static BufferedImage rootImage;
    protected static BufferedImage[] boomImage;

    private int dx = 2 * (int) (Math.random() * 2) == 0 ? 1 : -1;
    private int dy = (int) (Math.random() * 2) + 1;
    private int deadIndex = 1;

    Bee(int width, int height) {
        super(width, height);
    }

    @Override
    public abstract String getAward();

    @Override
    public void flyingMove() {
        x += dx;
        y += dy;
        if (x <= 0 || x >= GameFactory.FRAME_WIDTH - this.flyingObjectWidth) {
            dx *= -1;
        }
        if (outOfBounds()) {
            flyingObjectStatus = REMOVE;
        }
    }

    @Override
    public BufferedImage getImage() {
        if (isAlive()) {
            return rootImage;
        } else {
            if (isDead()) {
                BufferedImage image = boomImage[deadIndex++];
                if (deadIndex == boomImage.length) {
                    flyingObjectStatus = REMOVE;
                }
                return image;
            }
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y >= GameFactory.FRAME_HEIGHT;
    }
}
