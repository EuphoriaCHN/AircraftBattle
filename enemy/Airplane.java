package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.flash.SmallBoomFlash;
import pers.euphoria.aircraftbattle.music.EnemyLevelOneDown;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Small enemy airplane
 */

public class Airplane extends FlyingObject implements Enemy {
    private static BufferedImage[] boomImages;
    private static BufferedImage[] airplaneImages;
    private BufferedImage nowAirplaneImage;

    private int speed;
    private int deadIndex = 1;
    private int healthPoint = GameFactory.airPlaneMaxHealthPoint;

    static {
        boomImages = new BufferedImage[11];
        airplaneImages = new BufferedImage[4];
        for (int i = 0; i < airplaneImages.length; i++) {
            airplaneImages[i] = GameFactory.loadImage("./static/img/enemy/level_one/airplane0" + (i + 1) + ".png");
        }
        boomImages = SmallBoomFlash.getImages();
    }

    public Airplane() {
        super(55, 43);
        speed = 3;
        nowAirplaneImage = airplaneImages[(int) (Math.random() * 4)];
    }

    @Override
    public int getScore() {
        EnemyLevelOneDown.play();
        return (GameFactory.FRAME_HEIGHT - this.y) / 10;
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
            return nowAirplaneImage;
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
    public int getHealthPoint() {
        return this.healthPoint;
    }

    @Override
    public void subHealthPoint(int minus) {
        this.healthPoint -= minus;
    }

    @Override
    public void paintHealthLine(Graphics g) {
        if (healthPoint <= 0) {
            return;
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(this.x - 1, this.y - 12, this.flyingObjectWidth + 2, 7);
        int drawWidth = this.flyingObjectWidth * healthPoint / GameFactory.airPlaneMaxHealthPoint;
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
