package pers.euphoria.aircraftbattle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Any Flying object interface
 * Declaration some method and attributes
 */

public abstract class FlyingObject {
    // Flying objects standard status
    private static final int LIFE = 0;
    private static final int DEAD = 1;
    protected static final int REMOVE = 2;
    protected int flyingObjectStatus = LIFE;

    // Flying objects size attribute
    public int flyingObjectWidth;
    public int flyingObjectHeight;
    protected int x;
    protected int y;

    // Make special constructor method
    // 1. Hero, Sky and Bullet
    protected FlyingObject(int width, int height, int x, int y) {
        this.flyingObjectHeight = height;
        this.flyingObjectWidth = width;
        this.x = x;
        this.y = y;
    }

    // 2. Other Flying objects
    protected FlyingObject(int width, int height) {
        this.flyingObjectWidth = width;
        this.flyingObjectHeight = height;
        Random random = new Random();
        this.x = random.nextInt(GameFactory.FRAME_WIDTH - width);
        this.y = -1 * height;
    }

    // Flying object move method
    public abstract void flyingMove();

    // Judge is alive?
    protected boolean isAlive() {
        return flyingObjectStatus == LIFE;
    }

    // Judge game over?
    protected boolean isDead() {
        return flyingObjectStatus == DEAD;
    }

    public abstract BufferedImage getImage();

    // Draw object
    public void paintObject(Graphics g) {
        g.drawImage(this.getImage(), x, y, null);
    }

    // Judge object flying out of frame
    public abstract boolean outOfBounds();

    // Hit algorithm
    // this: Enemy, other: Hero or Award
    public boolean objectHit(FlyingObject hitObject) {
        int[] rangeX = new int[]{
                this.x - hitObject.flyingObjectWidth,
                this.x + this.flyingObjectWidth
        };
        int[] rangeY = new int[]{
                this.y - hitObject.flyingObjectHeight,
                this.y + this.flyingObjectHeight
        };
        // Judge algorithm
        return hitObject.x >= rangeX[0] && hitObject.x <= rangeX[1] && hitObject.y >= rangeY[0] && hitObject.y <= rangeY[1];
    }

    // Let flying object died
    protected void makeDie() {
        this.flyingObjectStatus = DEAD;
    }

    /**
     * 画敌机血条
     *
     * @param g 画笔
     */
    public void paintHealthLine(Graphics g) {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
