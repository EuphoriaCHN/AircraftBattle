package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.hero.Hero;

public class SecondBoss extends Boss {
    public SecondBoss(Hero hero) {
        super(471, 260,
                (GameFactory.FRAME_WIDTH - 471) / 2,
                -1 * 260 - 335,
                150 * (Math.min(hero.getDoubleFire(), 4) + 1));
    }

    static {
        bossBulletLocation = new int[][]{
                {60, 117}, {151, 117}, {320, 117}, {410, 117}, {130, 219}, {72, 243}, {399, 243}
        };
    }

    @Override
    public int[] getLaserAttribute() {
        return new int[] {x + 211, y + 230};
    }
}
