package pers.euphoria.aircraftbattle.award;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.flash.GetAwardFlash;

import java.awt.image.BufferedImage;

public class SpeedBee extends Bee {
    private static BufferedImage image;
    private static BufferedImage[] boomImages;
    private int deadIndex = 1;

    public SpeedBee() {
        super(60, 60);
    }

    static {
        image = GameFactory.loadImage("./static/img/award/speedBee.png");

        boomImages = GetAwardFlash.getImages();
    }

    @Override
    public String getAward() {
        return Award.BULLET_SPEED;
    }

    @Override
    public BufferedImage getImage() {
        if (isAlive()) {
            return image;
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
}
