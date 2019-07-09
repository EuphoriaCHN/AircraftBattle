package pers.euphoria.aircraftbattle.hero;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject {
    public static BufferedImage images[] = new BufferedImage[2];
    public static final int BULLET_HEIGHT = 35;
    public static final int BULLET_WIDTH = 12;

    private int dy;

    // Personal option
    public static int bulletPower = 1;

    static {
        images[0] = GameFactory.loadImage("./static/img/bullet/blue.png");
        images[1] = GameFactory.loadImage("./static/img/bullet/purple.png");
    }

    private Bullet(int x, int y) {
        super(BULLET_WIDTH, BULLET_HEIGHT, x, y);
        this.dy = -3;
    }

    @Override
    public void flyingMove() {
        y += dy;
    }

    @Override
    public BufferedImage getImage() {
        if (isAlive()) {
            if (Hero.doubleFire < 4) {
                return images[0];
            } else {
                return images[1];
            }
        } else {
            if (isDead()) {
                flyingObjectStatus = REMOVE;
            }
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y < -1 * flyingObjectHeight;
    }

    /**
     * 获取单个子弹
     *
     * @param hero 英雄当前状态
     * @return 子弹集合
     */
    static Bullet[] getSingleBullet(Hero hero) {
        return new Bullet[]{
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 - BULLET_WIDTH / 2, hero.getY() - BULLET_HEIGHT)
        };
    }

    /**
     * 获取两个子弹
     *
     * @param hero 英雄当前状态
     * @return 子弹集合
     */
    static Bullet[] getDoubleBullet(Hero hero) {
        return new Bullet[]{
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 - BULLET_WIDTH * 3, hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 + BULLET_WIDTH * 2, hero.getY() - BULLET_HEIGHT)
        };
    }

    /**
     * 获取三个子弹
     *
     * @param hero 英雄当前状态
     * @return 子弹集合
     */
    static Bullet[] getTripleBullet(Hero hero) {
        return new Bullet[]{
                new Bullet(hero.getX(), hero.getY()),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 - BULLET_WIDTH / 2, hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth - BULLET_WIDTH, hero.getY())
        };
    }

    /**
     * 获取四个子弹
     *
     * @param hero 英雄当前状态
     * @return 子弹集合
     */
    static Bullet[] getQuadrupleBullet(Hero hero) {
        return new Bullet[]{
                new Bullet(hero.getX(), hero.getY()),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 - BULLET_WIDTH * 3, hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 + BULLET_WIDTH * 2, hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth - BULLET_WIDTH, hero.getY())
        };
    }

    /**
     * 获取五个子弹
     *
     * @param hero 英雄当前状态
     * @return 子弹集合
     */
    static Bullet[] getSuperBullet(Hero hero) {
        return new Bullet[]{
                new Bullet(hero.getX(), hero.getY()),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 4 - BULLET_WIDTH / 2, hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 - BULLET_WIDTH / 2,
                        hero.getY() - BULLET_HEIGHT - BULLET_HEIGHT / 2),
                new Bullet(hero.getX() + hero.flyingObjectWidth / 2 + hero.flyingObjectWidth / 4 - BULLET_WIDTH / 2,
                        hero.getY() - BULLET_HEIGHT),
                new Bullet(hero.getX() + hero.flyingObjectWidth - BULLET_WIDTH, hero.getY())
        };
    }
}
