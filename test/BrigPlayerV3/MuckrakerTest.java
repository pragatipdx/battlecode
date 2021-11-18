package BrigPlayerV3;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MuckrakerTest {

    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);

    private Muckraker muckTest = new Muckraker(rcTest, enemy, friend);

    int senseRadius = 30;


    RobotInfo friendBotPoli = new RobotInfo(1, friend, RobotType.POLITICIAN, 10, 10, new MapLocation(0, 0));
    RobotInfo enemyBotPoli = new RobotInfo(2, enemy, RobotType.POLITICIAN, 10, 10, new MapLocation(1, 0));
    RobotInfo friendBotSland = new RobotInfo(1, friend, RobotType.SLANDERER, 10, 10, new MapLocation(0, 0));
    RobotInfo enemyBotSland = new RobotInfo(2, enemy, RobotType.SLANDERER, 10, 10, new MapLocation(1, 0));
    RobotInfo[] friendBotArr = new RobotInfo[2];
    RobotInfo[] enemyBotArr = new RobotInfo[2];


    @Test
    public void senseNearbyRobotsTestNullArrayE() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);

        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, enemy)).thenReturn(robots);

        assertArrayEquals(null, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestNullArrayF() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);

        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, friend)).thenReturn(robots);

        assertArrayEquals(null, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestArrayLen() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);

        friendBotArr[0] =friendBotPoli;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(friendBotArr);

        assertEquals(friendBotArr.length, muckTest.senseNearbyRobotsInSenseRadius().length);
        assertArrayEquals(friendBotArr, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void exposeEnemyTestTrue() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);

        enemyBotArr[0] = enemyBotSland;

        when(rcTest.canExpose(enemyBotSland.location)).thenReturn(true);

        assertTrue(muckTest.exposeEnemy(enemyBotSland));
    }

    @Test
    public void exposeEnemyTestFalse() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);

        when(rcTest.canExpose(enemyBotPoli.location)).thenReturn(false);

        assertFalse(muckTest.exposeEnemy(enemyBotPoli));
    }






}
