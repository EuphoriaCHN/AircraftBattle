package pers.euphoria.aircraftbattle.enemy;

class ThunderBullet extends BossBullet {
    ThunderBullet(int[] location) {
        super(location);
        draws = images[0];
    }
}
