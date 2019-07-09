package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.flash.MiddleBoomFlash;
import pers.euphoria.aircraftbattle.music.EnemyLevelTwoDown;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Big enemy airplane
 */

public class BigAirplane extends FlyingObject implements Enemy {
    private static BufferedImage[] boomImages;
    private static BufferedImage[] bigPlaneImages;
    private BufferedImage nowImage;

    private int speed;
    private int deadIndex = 1;

    // Personal option
    private int healthPoint = GameFactory.bigAirPlaneMaxHealthPoint;

    static {
        boomImages = MiddleBoomFlash.getImages();
        bigPlaneImages = new BufferedImage[4];
        for (int i = 0; i < bigPlaneImages.length; i++) {
            bigPlaneImages[i] = GameFactory.loadImage("./static/img/enemy/level_two/bigplane0" + (i + 1) + ".png");
        }
    }

    public BigAirplane() {
        super(105, 68);
        speed = 2;
        nowImage = bigPlaneImages[(int) (Math.random() * 4)];
    }

    @Override
    public int getHealthPoint() {
        return this.healthPoint;
    }

    @Override
    public void subHealthPoint(int minus) {
        this.healthPoint -= minus;
    }

    @Override
    public int getScore() {
        EnemyLevelTwoDown.play();
        return (GameFactory.FRAME_HEIGHT - this.y) / 6;
    }

    @Override
    public void flyingMove() {
        y += speed;
        if (outOfBounds()) {
            flyingObjectStatus = REMOVE;
        }
    }

    @Override
    public BufferedImage getImage() {
        if (isAlive()) {
            return nowImage;
        } else {
            if (isDead()) {
                BufferedImage image = boomImages[deadIndex++];
                if (deadIndex == boomImages.length) {
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

    @Override
    public void paintHealthLine(Graphics g) {
        if (healthPoint <= 0) {
            return;
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(this.x - 1, this.y - 12, this.flyingObjectWidth + 2, 7);
        int drawWidth = this.flyingObjectWidth * healthPoint / GameFactory.bigAirPlaneMaxHealthPoint;
        if (drawWidth * 1.0 / (float)this.flyingObjectWidth < 0.3) {
            g.setColor(Color.RED);
        } else {
            if (drawWidth * 1.0 / (float)this.flyingObjectWidth < 0.6) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.GREEN);
            }
        }
        g.fillRect(this.x, this.y - 11, drawWidth, 5);
        g.setColor(Color.BLACK);
    }
}
