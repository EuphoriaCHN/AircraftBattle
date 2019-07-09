package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.FlyingObject;
import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.flash.BigBoomFlash;
import pers.euphoria.aircraftbattle.flash.Flash;
import pers.euphoria.aircraftbattle.hero.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Boss extends FlyingObject implements Enemy {
    protected static BufferedImage[] images;
    private static BufferedImage[] names;
    static int[][] bossBulletLocation;
    private int healthPoint;
    private int maxHealthPoint;
    private boolean notReady = true;
    private int dx = 1;

    // 存储BOSS死亡FLASH
    public static ArrayList<BigBoomFlash> flashes = new ArrayList<>();

    // BOSS发射的子弹
    public static ArrayList<BossBullet> bossBullets = new ArrayList<>();

    Boss(int width, int height, int x, int y, int startHP) {
        super(width, height, x, y);
        healthPoint = Bullet.bulletPower * startHP;
        maxHealthPoint = healthPoint;
    }

    private BufferedImage getNameImage() {
        return names[GameFactory.level - 1];
    }


    public boolean notInAttackStage() {
        return this.notReady;
    }

    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }

    public int[] newBulletAttribute() {
        int index = (int) (Math.random() * bossBulletLocation.length);
        int[] temp = bossBulletLocation[index];
        return new int[]{this.x + temp[0], this.y + temp[1]};
    }

    public abstract int[] getLaserAttribute();

    /**
     * 游戏最开始时，随着GameFactory.loader()被调用，预加载图片
     */
    public static void initialize() {
        images = new BufferedImage[GameFactory.MAX_LEVEL];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/enemy/boss/boss" + i + ".png");
        }

        names = new BufferedImage[GameFactory.MAX_LEVEL];
        for (int i = 0; i < names.length; i++) {
            names[i] = GameFactory.loadImage("./static/img/enemy/boss/name" + i + ".png");
        }
    }

    @Override
    public void paintHealthLine(Graphics g) {
        if (healthPoint <= 0) {
            return;
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(180, 14, 286, 39);
        g.setColor(Color.RED);
        int drawWidth = 284 * healthPoint / maxHealthPoint;
        g.fillRect(181, 15, drawWidth, 37);
        g.setColor(Color.BLACK);

        // 画BOSS名字
        g.drawImage(this.getNameImage(), 37, 8, null);
    }

    @Override
    public void flyingMove() {
        if (isAlive()) {
            if (y < 60) {
                y++;
            } else {
                if (notReady) {
                    notReady = false;
                    healthPoint = maxHealthPoint;
                }
                x += dx;
                if (x >= GameFactory.FRAME_WIDTH - flyingObjectWidth || x <= 0) {
                    dx *= -1;
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        return images[GameFactory.level - 1];
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    @Override
    public int getHealthPoint() {
        return healthPoint;
    }

    @Override
    public void subHealthPoint(int minus) {
        this.healthPoint -= minus;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
