package pers.euphoria.aircraftbattle.enemy;

import pers.euphoria.aircraftbattle.GameFactory;
import pers.euphoria.aircraftbattle.hero.Hero;

public class FirstBoss extends Boss {
    public FirstBoss(Hero hero) {
        super(323, 309,
                (GameFactory.FRAME_WIDTH - 323) / 2,
                -1 * 309 - 335,
                75 * (Math.min(hero.getDoubleFire(), 4) + 1));
    }

    static {
        bossBulletLocation = new int[][]{
                {56, 17}, {215, 17}, {137, 257}
        };
    }

    @Override
    public int[] getLaserAttribute() {
        return new int[] {x + 136, y + 280};
    }
}
