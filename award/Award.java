package pers.euphoria.aircraftbattle.award;

/**
 * Award interface
 * 1. Double fire
 * 2. Life up
 */

public interface Award {
    // 设定基本奖励
    // 1. 奖励血量
    String HEALTH_BEE = "health";

    // 2. 奖励子弹威力
    String BULLET_POWER = "power";

    // 3. 奖励子弹数量
    String BULLET_NUMBER = "number";

    // 4. 加快子弹射速
    String BULLET_SPEED = "speed";

    // 5. 清屏
    String CLEAR_SCREEN = "clear";

    String getAward();
}
