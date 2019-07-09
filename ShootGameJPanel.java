package pers.euphoria.aircraftbattle;

import pers.euphoria.aircraftbattle.award.Award;
import pers.euphoria.aircraftbattle.enemy.*;
import pers.euphoria.aircraftbattle.flash.*;
import pers.euphoria.aircraftbattle.hero.Bullet;
import pers.euphoria.aircraftbattle.hero.Hero;
import pers.euphoria.aircraftbattle.music.*;
import pers.euphoria.aircraftbattle.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Wang Qinhong
 */

public class ShootGameJPanel extends JPanel {
    // BEGIN AUTO GAME DEBUG
    private int lineX = 0;
    private int lineY = 0;
    // END AUTO GAME DEBUG

    private Sky sky = new Sky(); // 创建天空对象
    private Hero hero = new Hero(); // 创建英雄机对象
    private ArrayList<FlyingObject> enemies = new ArrayList<>(); // 创建敌人数组（小型敌机、大型敌机与小蜜蜂奖励）
    private ArrayList<Bullet> bullets = new ArrayList<>(); // 创建子弹数组
    private ArrayList<FloatBar> awardBars = new ArrayList<>(); // 创建奖励悬浮栏

    private int score = 0; // 玩家的得分
    private int enemyEnterIndex = 0; // 敌机入场数量控制
    private int bulletEnterIndex = 0; // 子弹入场控制
    private int bossEnterBullet = 0; // BOSS子弹入场控制
    private int bossDeadFlash = 0; // BOSS死亡动画控制

    public static void main(String[] args) {
        GameFactory.loader(); // 直接加载部分图片

        JFrame jFrame = new JFrame("Shoot Game"); // 主窗体

        ShootGameJPanel world = new ShootGameJPanel(); // 主面板
        jFrame.add(world); // 将面板添加至窗体

        // 初始化窗体
        jFrame.setBounds(GameFactory.ORIGIN_X, GameFactory.ORIGIN_Y, GameFactory.FRAME_WIDTH, GameFactory.FRAME_HEIGHT);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        BackgroundPlayer.play(); // 播放背景音乐
        world.action(); // 启动程序
    }

    /**
     * 启动程序的执行方法
     */
    private void action() {
        // 创建鼠标适配器对象
        MouseAdapter mouseAdapter = new MouseAdapter() {
            /**
             * 重写mouseClicked()鼠标点击事件
             * @param e 鼠标事件
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
                switch (GameFactory.getGameStatus()) { // 根据当前游戏状态做不同业务逻辑
                    case GameFactory.START: // 游戏为启动状态
                        GameFactory.setGameStatus(GameFactory.RUN); // 将启动状态修改为运行状态
                        break;
                    case GameFactory.END: // 游戏为结束状态
                        score = 0; // 清除分数
                        sky = new Sky(); // 重绘天空
                        hero = new Hero(); // 重绘英雄机
                        enemies = new ArrayList<>(); // 清空敌人
                        bullets = new ArrayList<>(); // 清空子弹
                        awardBars = new ArrayList<>(); // 清空奖励悬浮栏

                        enemyEnterIndex = 0; // 重置敌机入场控制
                        bulletEnterIndex = 0; // 重置子弹入场控制
                        bossDeadFlash = 0; // 重置BOSS死亡动画控制
                        bossEnterBullet = 0; // 重置Boss子弹入场控制

                        // 重置敌机属性
                        GameFactory.resetEnemyAirplane();

                        // 重置子弹属性
                        GameFactory.resetBulletAttribute();

                        // 重置金手指
                        GameFactory.resetGoldFinger();

                        // 重置游戏属性
                        GameFactory.resetGameAttribute();

                        hero.clearDoubleFire(); // 清除双倍火力

                        GameFactory.setGameStatus(GameFactory.START); // 将当前状态修改为启动状态
                        break;
                }
            }

            /**
             * 重写mouseEntered()鼠标移入事件
             * @param e 鼠标事件
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                // 将处于暂停状态中的游戏，修改为运行状态
                if (GameFactory.getGameStatus() == GameFactory.PAUSE) {
                    GameFactory.setGameStatus(GameFactory.RUN);
                }
            }

            /**
             * 重写mouseExited()鼠标移出事件
             * @param e 鼠标事件
             */
            @Override
            public void mouseExited(MouseEvent e) {
                // 将处于运行状态中的游戏，修改为暂停状态
                System.gc();
                if (GameFactory.getGameStatus() == GameFactory.RUN) {
                    GameFactory.setGameStatus(GameFactory.PAUSE);
                }
            }

            /**
             * 重写mouseMoved()鼠标移动事件
             * @param e 鼠标事件
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                // 对于正在运行中的游戏，将英雄机的位置移动到鼠标的位置
                if (!GameFactory.AUTO_GAME && GameFactory.getGameStatus() == GameFactory.RUN) {
                    hero.heroMove(e.getX(), e.getY());
                }
            }
        };
        this.addMouseListener(mouseAdapter); // 为JPanel添加鼠标操作监听器
        this.addMouseMotionListener(mouseAdapter); // 为JPanel添加鼠标滑动监听器

        // BEGIN Personal option
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                GameFactory.eggStringBuilder.append(e.getKeyChar());

                // BEGIN GOLD FINGER -- AUTO GAME
                if (GameFactory.AUTO_GAME_CODE.startsWith(GameFactory.eggStringBuilder.toString())) {
                    if (GameFactory.AUTO_GAME_CODE.equals(GameFactory.eggStringBuilder.toString().toLowerCase())) {
                        if (GameFactory.AUTO_GAME) {
                            System.out.println("关闭金手指（关闭自动游戏！）");
                            CloseGoldFingerPlayer.play();
                            GameFactory.closeEuphoria = true;
                        } else {
                            System.out.println("开启金手指（开启自动游戏！）");
                            OpenGoldFingerPlayer.play();
                            GameFactory.openEuphoria = true;
                        }
                        GameFactory.AUTO_GAME = !GameFactory.AUTO_GAME;
                        GameFactory.eggStringBuilder.delete(0, GameFactory.eggStringBuilder.length());
                    }
                    return;
                }
                // END GOLD FINGER -- AUTO GAME

                // BEGIN GOLD FINGER -- SUPER FIRE
                if (GameFactory.SUPER_FIRE_CODE.startsWith(GameFactory.eggStringBuilder.toString())) {
                    if (GameFactory.SUPER_FIRE_CODE.equals(GameFactory.eggStringBuilder.toString().toLowerCase())) {
                        if (GameFactory.SUPER_FIRE) {
                            System.out.println("关闭金手指（关闭超级火力！）");
                            GameFactory.BULLET_ENTER_SPEED = 40;
                            CloseGoldFingerPlayer.play();
                            GameFactory.closeDaddy = true;
                        } else {
                            System.out.println("开启金手指（开启超级火力！）");
                            GameFactory.BULLET_ENTER_SPEED = 1;
                            OpenGoldFingerPlayer.play();
                            GameFactory.openDaddy = true;
                        }
                        GameFactory.SUPER_FIRE = !GameFactory.SUPER_FIRE;
                        GameFactory.eggStringBuilder.delete(0, GameFactory.eggStringBuilder.length());
                    }
                    return;
                }
                // END GOLD FINGER -- SUPER FIRE

                // BEGIN GOLD FINGER -- INFINITE LIFE
                if (GameFactory.INFINITE_LIFE_CODE.startsWith(GameFactory.eggStringBuilder.toString())) {
                    if (GameFactory.INFINITE_LIFE_CODE.equals(GameFactory.eggStringBuilder.toString().toLowerCase())) {
                        if (GameFactory.INFINITE_LIFE) {
                            System.out.println("关闭金手指（关闭无限生命！）");
                            hero.limitedLife();
                            CloseGoldFingerPlayer.play();
                            GameFactory.closeHP = true;
                        } else {
                            System.out.println("开启金手指（开启无限生命！）");
                            hero.infiniteLife();
                            OpenGoldFingerPlayer.play();
                            GameFactory.openHP = true;
                        }
                        GameFactory.INFINITE_LIFE = !GameFactory.INFINITE_LIFE;
                        GameFactory.eggStringBuilder.delete(0, GameFactory.eggStringBuilder.length());
                    }
                    return;
                }
                // END GOLD FINGER -- INFINITE LIFE

                // BEGIN GOLD FINGER -- GOD POWER
                if (GameFactory.GOD_POWER_CODE.startsWith(GameFactory.eggStringBuilder.toString())) {
                    if (GameFactory.GOD_POWER_CODE.equals(GameFactory.eggStringBuilder.toString().toLowerCase())) {
                        if (GameFactory.GOD_POWER) {
                            System.out.println("关闭金手指（关闭神秘力量！）");
                            CloseGoldFingerPlayer.play();
                            GameFactory.closeGod = true;
                        } else {
                            System.out.println("开启金手指（开启神秘力量！）");
                            OpenGoldFingerPlayer.play();
                            GameFactory.openGod = true;
                        }
                        GameFactory.GOD_POWER = !GameFactory.GOD_POWER;
                        GameFactory.eggStringBuilder.delete(0, GameFactory.eggStringBuilder.length());
                    }
                    return;
                }
                // END GOLD FINGER -- GOD POWER

                // BEGIN GOLD FINGER -- SUICIDE
                if (GameFactory.SUICIDE_CODE.startsWith(GameFactory.eggStringBuilder.toString())) {
                    if (GameFactory.SUICIDE_CODE.equals(GameFactory.eggStringBuilder.toString().toLowerCase())) {
                        System.out.println("Life is Short! I use Python!");
                        hero.heroSuicide();
                        GameFactory.openKill = true;
                    }
                    return;
                }
                // END GOLD FINGER -- SUICIDE

                GameFactory.eggStringBuilder.delete(0, GameFactory.eggStringBuilder.length());
            }
        };
        this.addKeyListener(keyAdapter);
        requestFocus(); // 重新获取监听焦点
        // END Personal option

        // 添加定时器对象timer
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 当游戏正在运行时
                if (GameFactory.getGameStatus() == GameFactory.RUN) {
                    if (checkFightBoss()) {
                        // BOSS就绪
                        if (!GameFactory.boss.notInAttackStage()) {
                            addBossBullet(); // BOSS发射子弹
                        }
                        // BOSS已经死亡，但现在还在BOSS战内，即播放动画
                        if (GameFactory.boss.isDead()) {
                            addBossDeadFlash(); // 处理动画添加逻辑
                            removeEndFlash(); // 移除完结动画
                        }
                    } else {
                        addEnemyHealthPoint(); // 处理难度系数--增加敌机血量
                        addEnemySpeed(); // 处理难度系数--增加敌机刷新速度
                        enemyEnterAction(); // 处理敌机，小蜜蜂添加逻辑
                    }
                    bulletEnterAction(); // 处理子弹逻辑
                    flyingObjectMove(); // 处理所有飞行物移动逻辑
                    removeOutOfBoundsObject(); // 移除所有越界飞行物
                    enemyDestroyed(); // 处理子弹击毁敌机逻辑
                    heroDestoryed(); // 处理英雄机被碰撞逻辑
                    checkGameOver(); // 处理游戏结束逻辑
                }
                repaint();
            }
        }, GameFactory.GAME_SPEED, GameFactory.GAME_SPEED);
    }

    /**
     * 绘画世界
     *
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g) {
        if (GameFactory.getGameStatus() == GameFactory.START) {
            // 如果为开始界面，展示主菜单
            g.drawImage(GameFactory.startBackGroundImage, 0, 0, null);
        } else {
            // 游戏运行中，展示标题及分数栏
            sky.paintObject(g); // 绘制天空
            // 如果当前非BOSS战
            if (!GameFactory.fightBoss) {
                g.drawImage(GameFactory.informationBarImage, 0, 0, null);
                g.drawImage(GameFactory.titlesImage[GameFactory.level - 1], 135, 668, null);
            }
            hero.paintObject(g); // 绘制英雄机
        }

        // 绘制清屏
        if (GameFactory.cleanUp) {
            GameFactory.cleanUp = ClearScreen.drawLevel(g);
        }

        // 绘制所有敌机
        for (FlyingObject enemy : enemies) {
            enemy.paintObject(g);
            if (enemy instanceof Enemy && enemy.isAlive()) {
                enemy.paintHealthLine(g);
            }
        }
        // BEGIN DEBUG AUTO GAME
        if (GameFactory.AUTO_GAME) {
//            g.drawLine(lineX, 0, lineX, GameFactory.FRAME_HEIGHT);
//            g.drawLine(0, lineY, GameFactory.FRAME_WIDTH, lineY);
            g.drawImage(GameFactory.autoAimImage, lineX, lineY, null);
            lineX = -1 * GameFactory.autoAimImageWidth;
            lineY = -1 * GameFactory.autoAimImageHeight;
        }
        // END DEBUG AUTO GAME

        if (GameFactory.fightBoss) {
            GameFactory.boss.paintObject(g);
            if (!GameFactory.boss.notInAttackStage()) {
                GameFactory.boss.paintHealthLine(g);
            }
            // 绘制BOSS激光
            if (BossLaser.onReady() && !GameFactory.boss.isDead()) {
                BossLaser.drawFlash(g);
            }
        } else {
            // BOSS战时，由于要显示BOSS名称，故取消显示分数和血量
            // 绘制分数
            if (GameFactory.getGameStatus() != GameFactory.START) {
                g.setFont(GameFactory.gameFont);
                // 绘制分数
                g.drawString("SCORE: " + score, 26, 52);
                // 绘制英雄机生命
                if (GameFactory.INFINITE_LIFE) {
                    // 如果当前状态为无限血量模式
                    g.drawString("LIFE: ??????", 26, 92);
                } else {
                    g.drawString("LIFE: " + hero.getLife(), 26, 92);
                }
//            g.drawString("HIT: " + GameFactory.hitEnemyNumber, 26, 132);
            }
        }

        // 绘制所有BOSS发射的子弹
        if (GameFactory.fightBoss && GameFactory.boss.isAlive()) {
            for (BossBullet bossBullet : Boss.bossBullets) {
                if (bossBullet.isAlive()) {
                    bossBullet.paintObject(g);
                }
            }
        }

        // 击败BOSS后，在等待时间内，播放动画
        if (GameFactory.fightBoss && GameFactory.boss.isDead()) {
            if (GameFactory.bossEndFlashDelayCounter >= GameFactory.BOSS_END_FLASH_DELAY) {
                // 重置音乐
                BossMusicPlayer.getBossMusic().stop();
                BackgroundPlayer.play();
                // 设置当前为非BOSS战
                GameFactory.fightBoss = false;
                // 重置BOSS射击频率
                GameFactory.BOSS_BULLET_ENTER_SPEED = 60;
                // 清空BOSS阵亡动画
                Boss.flashes = new ArrayList<>();
                // 清空总击败BOSS动画计数器
                GameFactory.bossEndFlashDelayCounter = 0;
                // 清空BOSS发射的所有子弹
                Boss.bossBullets = new ArrayList<>();
                // 重置BOSS激光
                BossLaser.cancelLaser();
                // 关卡数自增，但不超过最大关卡数
                GameFactory.level = Math.min(GameFactory.level + 1, GameFactory.MAX_LEVEL);
            } else {
                // 位于等待时期内，播放动画
                for (BigBoomFlash flash : Boss.flashes) {
                    flash.drawBossDied(g);
                }
                GameFactory.bossEndFlashDelayCounter++;
            }
        }

        // 绘制所有子弹
        for (Bullet bullet : bullets) {
            if (bullet.isAlive()) {
                bullet.paintObject(g);
            }
        }

        // 绘制升级光芒
        if (GameFactory.getLevelUp) {
            GameFactory.getLevelUp = LevelUpFlash.drawLevel(hero, g);
        }

        // 悬浮栏逻辑
        ///////////////////////////////////////////////////////////////////////////
        // 自瞄
        if (GameFactory.openEuphoria) {
            EuphoriaBar.open.drawFloatBar(hero, g);
            if (EuphoriaBar.open.controllerOutOfBound()) {
                GameFactory.openEuphoria = false;
                EuphoriaBar.open.resetController();
            }
        }
        if (GameFactory.closeEuphoria) {
            EuphoriaBar.close.drawFloatBar(hero, g);
            if (EuphoriaBar.close.controllerOutOfBound()) {
                GameFactory.closeEuphoria = false;
                EuphoriaBar.close.resetController();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        // 无限火力
        if (GameFactory.openDaddy) {
            DaddyBar.open.drawFloatBar(hero, g);
            if (DaddyBar.open.controllerOutOfBound()) {
                GameFactory.openDaddy = false;
                DaddyBar.open.resetController();
            }
        }
        if (GameFactory.closeDaddy) {
            DaddyBar.close.drawFloatBar(hero, g);
            if (DaddyBar.close.controllerOutOfBound()) {
                GameFactory.closeDaddy = false;
                DaddyBar.close.resetController();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        // 无限生命
        if (GameFactory.openHP) {
            HPBar.open.drawFloatBar(hero, g);
            if (HPBar.open.controllerOutOfBound()) {
                GameFactory.openHP = false;
                HPBar.open.resetController();
            }
        }
        if (GameFactory.closeHP) {
            HPBar.close.drawFloatBar(hero, g);
            if (HPBar.close.controllerOutOfBound()) {
                GameFactory.closeHP = false;
                HPBar.close.resetController();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        // 神力
        if (GameFactory.openGod) {
            GodBar.open.drawFloatBar(hero, g);
            if (GodBar.open.controllerOutOfBound()) {
                GameFactory.openGod = false;
                GodBar.open.resetController();
            }
        }
        if (GameFactory.closeGod) {
            GodBar.close.drawFloatBar(hero, g);
            if (GodBar.close.controllerOutOfBound()) {
                GameFactory.closeGod = false;
                GodBar.close.resetController();
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        // 奖励悬浮栏
        ArrayList<FloatBar> bars = new ArrayList<>();
        for (FloatBar floatBar : awardBars) {
            floatBar.drawFloatBar(hero, g);
            if (!floatBar.controllerOutOfBound()) {
                bars.add(floatBar);
            }
        }
        awardBars = bars;
        ///////////////////////////////////////////////////////////////////////////

        // 绘制warning横幅
        if (GameFactory.warning) {
            if (!Warning.move(g)) {
                GameFactory.warning = false;
                Warning.clear();
            }
        }

        // 根据不同状态业务逻辑，绘制背景
        switch (GameFactory.getGameStatus()) {
            case GameFactory.PAUSE:
                g.drawImage(GameFactory.pauseImage, 0, 0, null);
                break;
            case GameFactory.END:
                g.drawImage(GameFactory.gameOverImage, 0, 0, null);
                if (GameFactory.openKill) {
                    KillBar.open.drawFloatBar(hero, g);
                    if (KillBar.open.controllerOutOfBound()) {
                        GameFactory.openKill = false;
                        KillBar.open.resetController();
                    }
                }
                // 展示排行榜
                String[] highScore = UserDatabaseIO.select();
                g.setFont(GameFactory.highScoreFont);
                g.drawString("排行榜", 170, 320);
                int controlY = 360;
                g.setFont(GameFactory.detailsFont);
                for (int i = 0; i < GameFactory.MAX_HIGH_SCORE; i++) {
                    if (highScore[i] != null) {
                        g.drawString(highScore[i], 110, controlY);
                        controlY += 30;
                    }
                }
        }

        // BEGIN DEBUG DETAILS
//        g.setFont(GameFactory.detailsFont);
//        g.drawString("Small: " + GameFactory.airPlaneMaxHealthPoint, 0, GameFactory.FRAME_HEIGHT - 25 * 4);
//        g.drawString("Big: " + GameFactory.bigAirPlaneMaxHealthPoint, 0, GameFactory.FRAME_HEIGHT - 25 * 3);
//        g.drawString("DPS:" + Bullet.bulletPower * (hero.getDoubleFire() + 1), 0, GameFactory.FRAME_HEIGHT - 25 * 2);
        // END DEBUG DETAILS
    }

    /**
     * 生成BOSS子弹
     */
    private void addBossBullet() {
        bossEnterBullet++;
        double percent = 1;
        if (bossEnterBullet % GameFactory.BOSS_BULLET_ENTER_SPEED == 0) {
            percent = GameFactory.boss.getHealthPoint() * 1.0 / GameFactory.boss.getMaxHealthPoint();
            bossEnterBullet = 0;
            Boss.bossBullets.add(BossBullet.getBossBullet(GameFactory.boss.newBulletAttribute(), percent));
        }
        // 用BOSS血量来判断现在BOSS状态，是否提升发射子弹速度
        if (percent < 0.5) {
            GameFactory.BOSS_BULLET_ENTER_SPEED = 20;
            // 小于50%血量时BOSS发射激光
            BossLaser.readyLaser();
        }
    }

    /**
     * 判断BOSS战逻辑
     */
    private boolean checkFightBoss() {
        if (GameFactory.fightBoss) {
            return true;
        }
        if (GameFactory.hitEnemyNumber >= 50 * GameFactory.level) {
            GameFactory.hitEnemyNumber = 0;
            GameFactory.fightBoss = true;
            GameFactory.warning = true;
            BackgroundPlayer.getBackgroundPlayer().stop();
            BossMusicPlayer.play();

            // 初始化BOSS
            switch (GameFactory.level) {
                case 1:
                    GameFactory.boss = new FirstBoss(hero);
                    break;
                default:
                    GameFactory.boss = new SecondBoss(hero);
            }

            return true;
        }
        return false;
    }

    /**
     * 添加BOSS死亡动画
     */
    private void addBossDeadFlash() {
        bossDeadFlash++;
        if (bossDeadFlash % GameFactory.FLASH_REFRESH_TIME == 0) {
            bossDeadFlash = 0;
            Boss.flashes.add(new BigBoomFlash(
                    GameFactory.boss.x + (int) (Math.random() * GameFactory.boss.flyingObjectWidth - BigBoomFlash.getWidth()),
                    GameFactory.boss.y + (int) (Math.random() * GameFactory.boss.flyingObjectWidth - BigBoomFlash.getHeight())
            ));
//            switch ((int) (Math.random() * 3)) {
//                case 0:
//                    EnemyLevelOneDown.play();
//                    Boss.flashes.add(new SmallBoomFlash(
//                            GameFactory.boss.x + (int) (Math.random() * GameFactory.boss.flyingObjectWidth -
//                            SmallBoomFlash.getWidth()),
//                            GameFactory.boss.y + (int) (Math.random() * GameFactory.boss.flyingObjectHeight -
//                            SmallBoomFlash.getHeight())
//                    ));
//                    break;
//                case 1:
//                    EnemyLevelTwoDown.play();
//                    Boss.flashes.add(new MiddleBoomFlash(
//                            GameFactory.boss.x + (int) (Math.random() * GameFactory.boss.flyingObjectWidth -
//                            MiddleBoomFlash.getWidth()),
//                            GameFactory.boss.y + (int) (Math.random() * GameFactory.boss.flyingObjectWidth -
//                            MiddleBoomFlash.getHeight())
//                    ));
//                    break;
//                case 2:
//                    EnemyLevelThreeDown.play();
//                    Boss.flashes.add(new BigBoomFlash(
//                            GameFactory.boss.x + (int) (Math.random() * GameFactory.boss.flyingObjectWidth -
//                            BigBoomFlash.getWidth()),
//                            GameFactory.boss.y + (int) (Math.random() * GameFactory.boss.flyingObjectWidth -
//                            BigBoomFlash.getHeight())
//                    ));
//            }
        }
    }

    /**
     * 移除完结动画
     */
    private void removeEndFlash() {
        ArrayList<BigBoomFlash> flashes = new ArrayList<>();
        for (BigBoomFlash flash : Boss.flashes) {
            if (!flash.finished()) {
                flashes.add(flash);
            }
        }
        Boss.flashes = flashes;
    }

    /**
     * 敌人（小敌机、大敌机、小蜜蜂）入场
     *
     * @see GameFactory line 18
     */
    private void enemyEnterAction() {
        enemyEnterIndex++;
        // 生成敌人时间控制（GAME_SPEED * ENEMY_ENTER_SPEED = 进入一个敌人的时间 ms）
        if (enemyEnterIndex % GameFactory.ENEMY_ENTER_SPEED == 0) {
            enemyEnterIndex = 0;
            enemies.add(GameFactory.getNextOne()); // 添加一个敌人
        }
    }

    /**
     * 英雄机发射子弹
     *
     * @see GameFactory line 20
     */
    private void bulletEnterAction() {
        // BEGIN EGG: GOD POWER
        if (GameFactory.GOD_POWER) {
            return;
        }
        // END EGG: GOD POWER

        bulletEnterIndex++;
        // 生成子弹时间控制（GAME_SPEED * BULLET_ENTER_SPEED = 发射一颗子弹的时间 ms）
        if (bulletEnterIndex % GameFactory.BULLET_ENTER_SPEED == 0) {
            bulletEnterIndex = 0;
            // 获取当前英雄机发射的子弹，并将其添加进子弹组
            bullets.addAll(Arrays.asList(hero.shootBullet()));
        }
    }

    /**
     * 控制所有飞行物的移动，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     */
    private void flyingObjectMove() {
        sky.flyingMove();  //天空移动
        hero.flyingMove(); //英雄机移动

        // EGG: AUTO GAME
        int autoY = -1;
        int autoX = hero.x + hero.flyingObjectWidth / 2;

        for (FlyingObject enemy : enemies) { //遍历所有敌人
            enemy.flyingMove(); //敌人移动

            // EGG: AUTO GAME
            if (GameFactory.AUTO_GAME) {
                // 当前敌机处于屏幕内
                if (enemy.isAlive() && enemy.y > -1 * enemy.flyingObjectHeight && !enemy.outOfBounds()) {
                    // 当前敌机Y坐标最大
                    int autoEnemyY = enemy.y + enemy.flyingObjectHeight;
                    if (autoEnemyY > autoY) {
                        autoY = autoEnemyY;
                        autoX = enemy.x + enemy.flyingObjectWidth / 2;

                        // BEGIN DEBUG AUTO GAME
                        lineX = autoX - GameFactory.autoAimImageWidth / 2;
                        lineY = autoY - enemy.flyingObjectHeight / 2 - GameFactory.autoAimImageHeight / 2;
                        // END DEBUG AUTO GAME
                    }
                }
            }
        }
        for (Bullet bullet : bullets) { //遍历所有子弹
            bullet.flyingMove(); //子弹移动
        }

        // BOSS战
        if (GameFactory.fightBoss) {
            GameFactory.boss.flyingMove();
            // 遍历所有BOSS发射的子弹
            for (BossBullet bossBullet : Boss.bossBullets) {
                bossBullet.flyingMove();
            }
        }

        // EGG: AUTO GAME
        if (GameFactory.AUTO_GAME) {
            hero.heroAutoMove(autoX);
        }
    }

    /**
     * 删除越界的飞行物，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     * <p>
     * 遍历每一个敌人与每一个子弹，逐个判断其是否越界
     * 如果越界，则将其移除
     */
    private void removeOutOfBoundsObject() {
        ArrayList<FlyingObject> aliveFlyingObject = new ArrayList<>();
        ArrayList<Bullet> aliveBullet = new ArrayList<>();

        for (FlyingObject enemy : enemies) {
            if (!enemy.outOfBounds()) {
                aliveFlyingObject.add(enemy);
            }
        }
        for (Bullet bullet : bullets) {
            if (!bullet.outOfBounds()) {
                aliveBullet.add(bullet);
            }
        }

        enemies = aliveFlyingObject;
        bullets = aliveBullet;

        // BOSS战
        if (GameFactory.fightBoss) {
            // 处理BOSS发射的子弹
            ArrayList<BossBullet> aliveBossBullet = new ArrayList<>();
            for (BossBullet bossBullet : Boss.bossBullets) {
                if (!bossBullet.outOfBounds()) {
                    aliveBossBullet.add(bossBullet);
                }
            }
            Boss.bossBullets = aliveBossBullet;
        }
    }

    /**
     * 子弹碰撞到敌机，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     * <p>
     * 时间复杂度 n * m
     * 遍历所有子弹，对每一颗子弹，遍历所有当前正在飞行物检测碰撞
     */
    private void enemyDestroyed() {
        // BEGIN EGG: GOD POWER
        if (GameFactory.GOD_POWER) {
            if (GameFactory.fightBoss) {
                for (BossBullet bossBullet : Boss.bossBullets) {
                    bossBullet.makeDie();
                }
                GameFactory.boss.makeDie();
                Boss.bossBullets = new ArrayList<>(); // 清空BOSS发射的所有子弹
                score += GameFactory.boss.getScore();
            }
            for (FlyingObject flyingObject : enemies) {
                if (flyingObject.y > 0 && flyingObject.isAlive()) {
                    flyingObject.makeDie();
                    if (flyingObject instanceof Enemy) {
                        Enemy enemy = (Enemy) flyingObject;
                        GameFactory.hitEnemyNumber++;
                        score += enemy.getScore();
                    }
                }
            }
            return;
        }
        // END EGG: GOD POWER

        // 遍历所有子弹
        for (Bullet bullet : bullets) {
            // BOSS战
            if (GameFactory.fightBoss) {
                // 子弹射击到BOSS
                if (bullet.isAlive() && GameFactory.boss.isAlive() && GameFactory.boss.objectHit(bullet)) {
                    bullet.makeDie(); // 子弹死亡
                    if (!GameFactory.boss.notInAttackStage()) { // 当BOSS进入战斗阶段时
                        // 处理敌机扣血逻辑
                        GameFactory.boss.subHealthPoint(Bullet.bulletPower);
                        if (GameFactory.boss.getHealthPoint() <= 0) {
                            // 击败BOSS
                            GameFactory.boss.makeDie();
                            score += GameFactory.boss.getScore();
                            Boss.bossBullets = new ArrayList<>(); // 清空当前所有BOSS子弹
                            BossLaser.cancelLaser(); // 关闭BOSS激光
                        }
                    }
                }
                // 子弹碰撞到BOSS发射的子弹
                for (BossBullet bossBullet : Boss.bossBullets) {
                    if (bullet.isAlive() && bossBullet.isAlive() && bossBullet.objectHit(bullet)) {
                        // 英雄机发射的子弹碰撞到BOSS发射的子弹
                        bullet.makeDie();
                        bossBullet.makeDie();
                    }
                }
            }
            // 非BOSS战
            // 遍历所有当前飞行物
            for (FlyingObject flyingObject : enemies) {
                // 发生碰撞 或 当前为清屏奖励
                if (bullet.isAlive() && flyingObject.isAlive() && (GameFactory.cleanUp || flyingObject.objectHit(bullet))) {
                    // 设置该子弹与该飞行物死亡
//                    flyingObject.makeDie(); // 不计血量，所有敌机血量为1
                    bullet.makeDie();

                    // BEGIN Personal option
                    // 处理敌机扣血逻辑
                    if (flyingObject instanceof Enemy) {
                        Enemy enemy = (Enemy) flyingObject;
                        enemy.subHealthPoint(Bullet.bulletPower); // 敌机扣血
                        if (enemy.getHealthPoint() <= 0) {
                            flyingObject.makeDie();
                            GameFactory.hitEnemyNumber++;
                            score += enemy.getScore();
                        }
                    } else {
                        if (flyingObject instanceof Award) {
                            new UpgradePlayer().play();
                            flyingObject.makeDie();
                            Award award = (Award) flyingObject;
                            // 如果奖励为加血
                            if (award.getAward().equals(Award.HEALTH_BEE)) {
                                hero.addLive();
                                awardBars.add(new RedBeeBar()); // 添加一个奖励悬浮栏
                            } else {
                                // 如果奖励为加子弹数量
                                if (award.getAward().equals(Award.BULLET_NUMBER)) {
                                    hero.addDoubleFire();
                                    if (hero.getDoubleFire() < 5) {
                                        GameFactory.getLevelUp = true;
                                        awardBars.add(new GreenBeeBar()); // 添加一个奖励悬浮栏
                                    } else {
                                        // 如果飞机已经满级，则本次奖励会算作加子弹威力
                                        awardBars.add(new PurpleBeeBar()); // 添加一个奖励悬浮栏
                                    }
                                } else {
                                    // 如果奖励是加子弹威力
                                    if (award.getAward().equals(Award.BULLET_POWER)) {
                                        Bullet.bulletPower++;
                                        awardBars.add(new PurpleBeeBar()); // 添加一个奖励悬浮栏
                                    } else {
                                        // 如果奖励是清屏
                                        if (award.getAward().equals(Award.CLEAR_SCREEN)) {
                                            GameFactory.cleanUp = true; // 绘画清屏
                                        } else {
                                            // 奖励为增加射速
                                            if (GameFactory.BULLET_ENTER_SPEED >= 40) {
                                                GameFactory.BULLET_ENTER_SPEED -= 10;
                                                awardBars.add(new YellowBeeBar()); // 添加一个奖励悬浮栏
                                            } else {
                                                // 如果子弹射速已经满级，那么本次奖励会算作加子弹威力
                                                awardBars.add(new PurpleBeeBar()); // 添加一个奖励悬浮栏
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } // END Personal option
                }
            }
        }
    }

    /**
     * 检测英雄机与敌人的碰撞，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     * <p>
     * 时间复杂度 n
     * 对于每一个飞行物，检测是否与英雄机发生碰撞
     * 如果发生碰撞，移除被碰撞敌机，减少英雄机生命并具有清空所有火力值的惩罚
     */
    private void heroDestoryed() {
        // 英雄机与BOSS发生交互，但前提是当前不处于无限血量状态
        if (GameFactory.fightBoss && !GameFactory.INFINITE_LIFE) {
            // 英雄机碰触到BOSS
            if (hero.isAlive() && GameFactory.boss.isAlive() && GameFactory.boss.objectHit(hero)) {
                hero.heroSuicide();
                BossMusicPlayer.getBossMusic().stop(); // BOSS战音乐停止
                return;
            }
            // 英雄机碰到激光
            if (hero.isAlive() && GameFactory.boss.isAlive() && BossLaser.hitHero(hero)) {
                hero.heroSuicide();
                BossMusicPlayer.getBossMusic().stop(); // BOSS战音乐停止
                return;
            }
            // 英雄机碰撞到BOSS发射的子弹
            for (BossBullet bossBullet : Boss.bossBullets) {
                if (hero.isAlive() && GameFactory.boss.isAlive() && bossBullet.isAlive() && bossBullet.objectHit(hero)) {
                    bossBullet.makeDie();
                    hero.subLive();
                }
            }
        }
        // 遍历所有当前飞行物
        for (FlyingObject flyingObject : enemies) {
            if (hero.isAlive() && flyingObject.isAlive() && flyingObject.objectHit(hero)) {
                // 如果发生碰撞
                flyingObject.makeDie(); // 移除被碰撞敌机
                hero.subLive(); // 减少英雄机生命
                HeroBeAttacked.play(); // 播放英雄机被击中音乐
//                hero.clearDoubleFire(); // 清空火力值惩罚
            }
        }
    }

    /**
     * 检测游戏结束，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     */
    private void checkGameOver() {
        // 当英雄机生命值为0时，则游戏结束
        if (hero.getLife() == 0) {
            GameFactory.setGameStatus(GameFactory.END);
            if (GameFactory.fightBoss) {
                BossMusicPlayer.getBossMusic().stop();
            } else {
                BackgroundPlayer.getBackgroundPlayer().stop();
            }
            // 添加用户名和score
            UserDatabaseIO.insert("Euphoria", score);
        }
    }

    /**
     * 刷新游戏难度，增加敌机血量，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     */
    private void addEnemyHealthPoint() {
        // 如果英雄机单发子弹都足矣秒杀一个大型敌人
        if (Bullet.bulletPower >= GameFactory.bigAirPlaneMaxHealthPoint) {
            GameFactory.airPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 2; // 2倍
            GameFactory.bigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 30 / 20; // 3倍
            GameFactory.bigBigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 2; // 4倍
        }

        if ((score + 1) / 850 >= GameFactory.healthControl) {
            GameFactory.airPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 23 / 20; // 1.15倍
            GameFactory.bigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 30 / 20; // 1.725倍
            GameFactory.bigBigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 51 / 25; // 2.346倍
            GameFactory.healthControl++;
        }
    }

    /**
     * 刷新游戏难度，增加敌机出场速度，刷新频率见：
     *
     * @see GameFactory line 16: GAME_SPEED
     */
    private void addEnemySpeed() {
        // 如果英雄机一次性发射的子弹足矣击杀一个大型敌人
        if (Bullet.bulletPower * (hero.getDoubleFire() + 1) >= GameFactory.bigAirPlaneMaxHealthPoint) {
            GameFactory.airPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 110 / 100; // 1.1倍
            GameFactory.bigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 30 / 20; // 1.65倍
            GameFactory.bigBigAirPlaneMaxHealthPoint = GameFactory.airPlaneMaxHealthPoint * 89 / 50; // 1.95倍
            GameFactory.ENEMY_ENTER_SPEED = Math.max(40, GameFactory.ENEMY_ENTER_SPEED - 15);
        }

        if ((score + 1) / 1000 >= GameFactory.speedControl) {
            GameFactory.speedControl++;
            GameFactory.ENEMY_ENTER_SPEED = Math.max(40, GameFactory.ENEMY_ENTER_SPEED - 25);
        }
    }
}
