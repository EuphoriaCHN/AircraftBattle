package pers.euphoria.aircraftbattle.hero;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
    private static BufferedImage[] images;

    private int life;
    static int doubleFire;

    private static int[][] heroPictureAttribute = new int[][]{{117, 92}, {117, 94}, {120, 100}, {128, 105}, {160, 80}};

    static {
        images = new BufferedImage[5];
        for (int i = 1; i <= images.length; i++) {
            images[i - 1] = GameFactory.loadImage("./static/img/hero/hero" + i + ".png");
        }
    }

    public Hero() {
        super(117, 92, 150, 500);
        this.life = 3;
        doubleFire = 0;
    }

    public void heroMove(int mouseX, int mouseY) {
        this.x = mouseX - this.flyingObjectWidth / 2;
        this.y = mouseY - this.flyingObjectHeight / 2;
    }

    /**
     * EGG: AUTO GAME
     */
    public void heroAutoMove(int locationX) {
        this.x = locationX - this.flyingObjectWidth / 2;
    }

    /**
     * EGG: INFINITE LIFE
     */
    public void infiniteLife() {
        this.life = Integer.MAX_VALUE;
    }

    /**
     * EGG: UNSET INFINITE LIFE
     */
    public void limitedLife() {
        this.life = 3;
    }

    /**
     * EGG: SUICIDE
     */
    public void heroSuicide() {
        this.life = 0;
    }

    /**
     * 返回当前英雄机发射的子弹
     *
     * @return 子弹数组
     */
    public Bullet[] shootBullet() {

        if (GameFactory.SUPER_FIRE) {
            return Bullet.getSuperBullet(this);
        }

        // 如果为双倍火力
        switch (doubleFire) {
            case 0:
                return Bullet.getSingleBullet(this);
            case 1:
                return Bullet.getDoubleBullet(this);
            case 2:
                return Bullet.getTripleBullet(this);
            case 3:
                return Bullet.getQuadrupleBullet(this);
            default:
                return Bullet.getSuperBullet(this);
        }
    }

    /**
     * 获取当前英雄机的生命
     *
     * @return 当前英雄机的生命
     */
    public int getLife() {
        return life;
    }

    /**
     * 增加英雄机生命
     */
    public void addLive() {
        this.life++;
    }

    /**
     * 英雄机损失生命
     */
    public void subLive() {
        this.life--;
    }

    /**
     * 英雄机增加火力，如果火力值到达上限，则增加子弹威力
     */
    public void addDoubleFire() {
        if (doubleFire < 5) {
            doubleFire++;
            this.flyingObjectWidth = heroPictureAttribute[doubleFire < 5 ? doubleFire : 4][0];
            this.flyingObjectHeight = heroPictureAttribute[doubleFire < 5 ? doubleFire : 4][1];
        } else {
            Bullet.bulletPower++;
        }
    }

    /**
     * 清空当前英雄机的火力值
     */
    public void clearDoubleFire() {
        doubleFire = 0;
    }

    @Override
    public void flyingMove() {
        // None
    }

    @Override
    public BufferedImage getImage() {
        if (isAlive()) {
            return images[doubleFire < 5 ? doubleFire : 4];
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        // 英雄机永不越界
        return false;
    }

    /**
     * 获取当前英雄机的火力值
     *
     * @return 当前英雄机的火力值
     */
    public int getDoubleFire() {
        return Math.min(doubleFire, 5);
    }
}
