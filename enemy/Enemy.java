package pers.euphoria.aircraftbattle.enemy;

/**
 * Enemy interface
 */

public interface Enemy {
    // Personal option
    int getHealthPoint();

    void subHealthPoint(int minus);

    // Get score when attack enemy
    int getScore();
}
