package pers.euphoria.aircraftbattle;

import pers.euphoria.aircraftbattle.award.*;
import pers.euphoria.aircraftbattle.enemy.Airplane;
import pers.euphoria.aircraftbattle.enemy.BigAirplane;
import pers.euphoria.aircraftbattle.enemy.BigBigAirplane;
import pers.euphoria.aircraftbattle.enemy.Boss;
import pers.euphoria.aircraftbattle.flash.*;
import pers.euphoria.aircraftbattle.hero.Bullet;
import pers.euphoria.aircraftbattle.hero.Hero;
import pers.euphoria.aircraftbattle.music.BackgroundPlayer;
import pers.euphoria.aircraftbattle.ui.PersonalFont;
import pers.euphoria.aircraftbattle.ui.Warning;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public abstract class GameFactory {
    // BEGIN EGG

    static StringBuilder eggStringBuilder = new StringBuilder();

    ////////////////////////////////////////////////////////////////////////

    // BEGIN GOLD FINGER -- AUTO GAME
    static boolean AUTO_GAME = false;
    static final String AUTO_GAME_CODE = "euphoria";
    static BufferedImage autoAimImage;
    static final int autoAimImageWidth = 128;
    static final int autoAimImageHeight = 128;

    static {
        autoAimImage = loadImage("./static/img/plugins/aim.png");
    }
    // END GOLD FINGER -- AUTO GAME

    ////////////////////////////////////////////////////////////////////////

    // BEGIN GOLD FINGER -- SUPER FIRE
    public static boolean SUPER_FIRE = false;
    static final String SUPER_FIRE_CODE = "whosyourdaddy";
    // END GOLD FINGER -- SUPER FIRE

    ////////////////////////////////////////////////////////////////////////

    // BEGIN GOLD FINGER -- INFINITE LIFE
    static boolean INFINITE_LIFE = false;
    static final String INFINITE_LIFE_CODE = "greedisgood";
    // END GOLD FINGER -- INFINITE LIFE

    ////////////////////////////////////////////////////////////////////////

    // BEGIN GOLD FINGER -- GOD POWER
    static boolean GOD_POWER = false;
    static final String GOD_POWER_CODE = "xust";
    // END GOLD FINGER -- GOD POWER

    ////////////////////////////////////////////////////////////////////////

    // BEGIN GOLD FINGER -- SUICIDE
    static final String SUICIDE_CODE = "lifeisshort";
    // END GOLD FINGER -- SUICIDE

    ////////////////////////////////////////////////////////////////////////

    // END EGG

    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int FRAME_HEIGHT = 768;
    public static final int FRAME_WIDTH = 512;
    static final int ORIGIN_X = (SCREEN_WIDTH - FRAME_WIDTH) / 2;
    static final int ORIGIN_Y = (SCREEN_HEIGHT - FRAME_HEIGHT) / 2;

    // 游戏字体
    private static final String FONT_FILE_PATH = "../static/font/font.ttf";
    static final Font gameFont = new PersonalFont().getPersonalFont(FONT_FILE_PATH);

    static final Font highScoreFont = new Font("", Font.BOLD, 20);
    // BEGIN DEBUG DETAILS
    static Font detailsFont = new Font("", Font.PLAIN, 15);
    // END DEBUG DETAILS

    // 游戏刷新率（毫秒）
    static int GAME_SPEED = 15;
    // 生成敌人时间控制（GAME_SPEED * ENEMY_ENTER_SPEED = 进入一个敌人的时间 ms）
    static int ENEMY_ENTER_SPEED = 120;
    // 生成子弹时间控制（GAME_SPEED * BULLET_ENTER_SPEED = 发射一颗子弹的时间 ms）
    static int BULLET_ENTER_SPEED = 50;
    // BOSS生成子弹时间（GAME_SPEED * BOSS_BULLET_ENTER_SPEED = 发射一颗子弹的时间 ms）
    static int BOSS_BULLET_ENTER_SPEED = 60;
    // 动画刷新频率（）
    static final int FLASH_REFRESH_TIME = 15;
    static final int BOSS_END_FLASH_DELAY = 300; // 击败BOSS后播放(300 * GAME_SPEED) ms动画
    static int bossEndFlashDelayCounter = 0; // 总击败BOSS动画计数器

    // 定义小型敌机血量上限
    public static int airPlaneMaxHealthPoint = 1;
    // 定义大型敌机血量上限
    public static int bigAirPlaneMaxHealthPoint = 2;
    // 定义超级敌机血量上限
    public static int bigBigAirPlaneMaxHealthPoint = 3;
    // 定义当前难度
    static int healthControl = 1; // 敌机血量系数
    static int speedControl = 1; // 敌机刷新率系数
    public static int level = 1; // 关卡数
    public static final int MAX_LEVEL = 2; // 最大关卡数
    static int hitEnemyNumber = 0; // 击毁的敌机数量
    public static Boss boss = null; // 定义BOSS对象
    static boolean cleanUp = false; // 清屏奖励逻辑

    // 悬浮栏逻辑
    static boolean getLevelUp = false;
    static boolean openEuphoria = false;
    static boolean closeEuphoria = false;
    static boolean openDaddy = false;
    static boolean closeDaddy = false;
    static boolean openHP = false;
    static boolean closeHP = false;
    static boolean openGod = false;
    static boolean closeGod = false;
    static boolean openKill = false;
    static boolean warning = false;

    // 定义排行榜最高显示数量
    static final int MAX_HIGH_SCORE = 5;

    /*
    游戏状态控制
    开始 / 运行 / 暂停 / 结束
    默认为 “开始状态"
    */
    static final int START = 0;
    static final int RUN = 1;
    static final int PAUSE = 2;
    static final int END = 3;

    // 特殊状态：BOSS战
    static boolean fightBoss = false;

    private static int gameStatus = START; // 当前游戏状态

    static void setGameStatus(int status) {
        gameStatus = status;
    }

    static int getGameStatus() {
        return gameStatus;
    }

    // 背景图片
    static BufferedImage pauseImage;
    static BufferedImage gameOverImage;
    static BufferedImage startBackGroundImage;
    static BufferedImage informationBarImage;
    static BufferedImage[] titlesImage;

    // 初始化背景图片
    static {
        pauseImage = loadImage("./static/img/plugins/pause.png");
        gameOverImage = loadImage("./static/img/plugins/gameover.png");

        startBackGroundImage = loadImage("./static/img/background/startbg.png");
        informationBarImage = loadImage("./static/img/background/bar.png");

        titlesImage = new BufferedImage[GameFactory.MAX_LEVEL];
        for (int i = 0; i < titlesImage.length; i++) {
            titlesImage[i] = loadImage("./static/img/background/title" + (i + 1) + ".png");
        }
    }

    /**
     * 获取图片集
     *
     * @param filePath 文件路径
     * @return 图片属性
     */
    public static BufferedImage loadImage(String filePath) {
        BufferedImage image;
        try {
            // 反射方法，在同包下获取
            if (FlyingObject.class.getResource(filePath) == null) {
                System.err.println(filePath + " Not Exist!");
//                System.exit(0);
            }
            image = ImageIO.read(FlyingObject.class.getResource(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return image;
    }

    /**
     * 伪随机方法生成下一个飞行物目标
     *
     * @return 一个新飞行物
     */
    static FlyingObject getNextOne() {
        Random random = new Random();
        /*
        加子弹射速：3%，如果满三级速度，即BULLET_ENTER_SPEED == 30，则将此奖励算作子弹威力
        加血蜜蜂：1%
        加子弹威力蜜蜂：5%
        加子弹数量蜜蜂：5%，如果满五级数量，则将此奖励算作子弹威力
        清屏蜜蜂：1%
        小敌机：55%
        大敌机：20%
        超大敌机：10%
         */
        int type = random.nextInt(100) + 1;
        if (type <= 15) {
            if (type <= 4) {
                if (type == 1) {
                    return new HealthBee();
                } else {
                    if (BULLET_ENTER_SPEED > 30) {
                        return new SpeedBee();
                    } else {
                        return new PowerBee();
                    }
                }
            } else {
                if (type <= 10) {
                    if (type == 5) {
                        return new WeaponBee();
                    } else {
                        return new PowerBee();
                    }
                } else {
                    return new NumberBee();
                }
            }
        } else {
            // 生成敌人
            if (type <= 70) {
                return new Airplane();
            } else {
                if (type <= 90) {
                    return new BigAirplane();
                } else {
                    return new BigBigAirplane();
                }
            }
        }
    }

    /**
     * 重置敌机属性
     */
    static void resetEnemyAirplane() {
        GameFactory.bigBigAirPlaneMaxHealthPoint = 3; // 重置超级敌机生命
        GameFactory.bigAirPlaneMaxHealthPoint = 2; // 重置大型敌机生命
        GameFactory.airPlaneMaxHealthPoint = 1; // 重置小型敌机生命
        GameFactory.ENEMY_ENTER_SPEED = 120; // 重置刷敌速度
        resetBossAttribute(); // 重置BOSS属性
    }

    /**
     * 重置子弹属性
     */
    static void resetBulletAttribute() {
        Bullet.bulletPower = 1; // 重置子弹威力
        GameFactory.BULLET_ENTER_SPEED = 50; // 重置子弹射速
    }

    /**
     * 重置金手指
     */
    static void resetGoldFinger() {
        GameFactory.AUTO_GAME = false; // 关闭自动游戏
        GameFactory.SUPER_FIRE = false; // 关闭超级火力
        GameFactory.INFINITE_LIFE = false; // 关闭无限生命
        GameFactory.GOD_POWER = false; // 关闭神力
        GameFactory.getLevelUp = false; // 重置升级标记
        GameFactory.openEuphoria = false; // 重置自动游戏开启标记
        GameFactory.closeEuphoria = false; // 重置自动游戏关闭标记
        GameFactory.openDaddy = false; // 重置超级火力开启标记
        GameFactory.closeDaddy = false; // 重置超级火力关闭标记
        GameFactory.openHP = false; // 重置无限血量开启标记
        GameFactory.closeHP = false; // 重置无限血量关闭标记
        GameFactory.openGod = false; // 重置神力开启标记
        GameFactory.closeGod = false; // 重置神力关闭标记
        GameFactory.openKill = false; // 重置自杀标记
    }

    /**
     * 重置游戏属性
     */
    static void resetGameAttribute() {
        GameFactory.hitEnemyNumber = 0; // 重置击毁敌机数
        GameFactory.level = 1; // 重置关卡数
        BackgroundPlayer.play(); // 重放背景音乐
    }

    /**
     * 重置BOSS
     */
    private static void resetBossAttribute() {
        GameFactory.BOSS_BULLET_ENTER_SPEED = 60; // 重置BOSS子弹发射速度
        GameFactory.fightBoss = false; // 重置BOSS战
        GameFactory.warning = false; // 重置warning横幅
        GameFactory.bossEndFlashDelayCounter = 0; // 重置击败BOSS播放动画控制器
        Boss.bossBullets = new ArrayList<>(); // 重置BOSS发射的子弹
        BossLaser.cancelLaser(); // 重置BOSS激光
    }

    public static void loader() {
        new Airplane();
        new BigAirplane();
        new BigBigAirplane();

        Boss.initialize();
    }
}
