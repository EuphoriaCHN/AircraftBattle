package pers.euphoria.aircraftbattle.flash;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BossLaser {
    private static BufferedImage[] images;

    private static boolean start = false;
    private static boolean launched = false;

    private static final int WIDTH = 49;

    private static int delay = 0;
    private static int holdOn = 0;
    private static int index = 0;

    static {
        images = new BufferedImage[35];

        for (int i = 0; i < images.length; i++) {
            images[i] = GameFactory.loadImage("./static/img/flash/laser/" + i + ".png");
        }
    }

    public static void drawFlash(Graphics graphics) {
        if (start) {
            if (++delay == 10) {
                delay = 0;
                // 150毫秒变动一次
                holdOn++;
                if (holdOn > 66) { // 关闭激光动画
                    if (index < 21) {
                        index = 21;
                    } else {
                        if (index >= images.length - 1) {
                            // 关闭完成，结束激光发射
                            start = false;
                            holdOn = 0;
                            index = 0;
                            launched = false;
                        }
                    }
                } else {
                    // 激光发射中
                    if (launched) {
                        // 已经开始发射
                        if (index + 1 == 21) {
                            index = 12;
                        }
                    } else {
                        // 发射蓄力中
                        if (index + 1 == 13) {
                            // 发射激光柱
                            launched = true;
                        }
                    }
                }
                index++;
            }
            int[] laserAttribute = GameFactory.boss.getLaserAttribute();
            graphics.drawImage(images[Math.min(index, images.length - 1)], laserAttribute[0],
                    laserAttribute[1], null);
        }
    }

    public static void readyLaser() {
        start = true;
    }

    public static boolean onReady() {
        return start;
    }

    public static void cancelLaser() {
        start = false;
        holdOn = 0;
        index = 0;
        launched = false;
    }

    public static boolean hitHero(Hero hero) {
        if (holdOn <= 66 && launched) {
            int[] laserAttribute = GameFactory.boss.getLaserAttribute();
            // 激光已经射出
            // 更新碰撞算法
            if (hero.getX() + hero.flyingObjectWidth > laserAttribute[0] + 13) { // 英雄机不处于激光左侧
                return hero.getX() < laserAttribute[0] + WIDTH - 16; // 当英雄机的x小于等于激光x+激光宽度时，则被击中
            } else {
                // 英雄机处于激光左侧安全地区
                return false;
            }
        }
        return false;
    }
}
