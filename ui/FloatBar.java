package pers.euphoria.aircraftbattle.ui;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class FloatBar {
    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;
    private int controller;
    private boolean delay = false;
    private int x;
    private int y;

    FloatBar(String filePath, int imageWidth, int imageHeight) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        image = GameFactory.loadImage(filePath);
        this.controller = 0;
    }

    public void drawFloatBar(Hero hero, Graphics graphics) {
        if (delay) {
            x = hero.getX() + hero.flyingObjectWidth / 2 - imageWidth / 2;
            y = hero.getY() - imageHeight + 30;
            delay = false;
        } else {
            delay = true;
        }
        graphics.drawImage(image, x, y - controller, null);
        controller++;
    }

    public boolean controllerOutOfBound() {
        return this.controller >= 55;
    }

    public void resetController() {
        this.controller = 0;
    }
}
