package pers.euphoria.aircraftbattle.flash;

import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * Flash动画类，在窗体中任意一点的动画的抽象
 */

public abstract class Flash {
    protected static int width;
    protected static int height;
    protected static BufferedImage[] images;
    private int controlIndex;
    protected int x;
    protected int y;

    public Flash(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 绘画方法
     *
     * @param graphics 画笔
     */
    public void drawFlash(Graphics graphics) {
        graphics.drawImage(images[controlIndex++], x, y, null);
        if (controlIndex >= images.length) {
            controlIndex = images.length - 1;
        }
    }

    /**
     * 判断当前动画有无播放完毕
     *
     * @return 布尔值
     */
    public boolean finished() {
        return controlIndex == images.length - 1;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
