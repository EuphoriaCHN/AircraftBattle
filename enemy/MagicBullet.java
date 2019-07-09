package pers.euphoria.aircraftbattle.enemy;

class MagicBullet extends BossBullet {
    MagicBullet(int[] location) {
        super(location);
        draws = images[1];
    }
}
