package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class BossBullet extends FlyingObject implements Enemy {
    protected static BufferedImage[][] images;
    BufferedImage[] draws;
    private int index = 0;
    private boolean delay = true;

    static {
        images = new BufferedImage[2][];

        images[0] = new BufferedImage[16];
        for (int i = 0; i < images[0].length; i++) {
            images[0][i] = GameFactory.loadImage("./static/img/bullet/light/light" + i + ".png");
        }

        images[1] = new BufferedImage[16];
        for (int i = 0; i < images[1].length; i++) {
            images[1][i] = GameFactory.loadImage("./static/img/bullet/magic/magic" + i + ".png");
        }
    }

    BossBullet(int[] location) {
        super(48, 48, location[0], location[1]);
    }

    // 获取一个BOSS子弹对象
    public static BossBullet getBossBullet(int[] location, double percent) {
        if (percent < 0.5) {
            return new ThunderBullet(location);
        } else {
            return new MagicBullet(location);
        }
    }

    @Override
    public void flyingMove() {
        this.y += 2;

        if (outOfBounds()) {
            this.flyingObjectStatus = REMOVE;
        }
    }

    @Override
    public BufferedImage getImage() {
        delay = !delay;
        if (delay) {
            return draws[index = index + 1 >= draws.length ? 0 : index + 1];
        } else {
            return draws[index];
        }
    }

    @Override
    public boolean outOfBounds() {
        return this.y >= GameFactory.FRAME_HEIGHT;
    }

    /**
     * BOSS发射的子弹生命值为1
     *
     * @return 无意义
     */
    @Override
    public int getHealthPoint() {
        return 0;
    }

    /**
     * BOSS发射的子弹生命为1
     *
     * @param minus 无意义
     */
    @Override
    public void subHealthPoint(int minus) {
    }

    @Override
    public int getScore() {
        return 0;
    }
}
