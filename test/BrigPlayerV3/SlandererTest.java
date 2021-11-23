package BrigPlayerV3;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SlandererTest {
    static RobotController rc;
    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Slanderer slandererTest = new Slanderer(rcTest, enemy, friend);
    int senseRadius = 30;

    RobotInfo enemyMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));

    RobotInfo friendBotEC = new RobotInfo(1, friend, RobotType.ENLIGHTENMENT_CENTER, 10, 10, new MapLocation(1, 0));
    RobotInfo[] friendBotArr = new RobotInfo[2];
    RobotInfo[] enemyBotArr = new RobotInfo[2];
    ArrayList<RobotInfo> nearbyEnemyMuckrakerNotMock = new ArrayList<>();
    ArrayList<RobotInfo> nearbyAllyECNotMock = new ArrayList<>();

    @Mock
    private ArrayList<RobotInfo> nearbyEnemyMuckraker;
    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];

    @Mock
    private ArrayList<RobotInfo> nearbyEC;
    private RobotInfo[] AllyInfoArr = new RobotInfo[1];

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void senseNearbyRobotsTestNullArrayE() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, enemy)).thenReturn(robots);

        assertArrayEquals(null, slandererTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestNullArrayF() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, friend)).thenReturn(robots);

        assertArrayEquals(null, slandererTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestArrayLen() throws GameActionException {
        friendBotArr[0] = friendBotEC;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(friendBotArr);

        assertEquals(friendBotArr.length, slandererTest.senseNearbyRobotsInSenseRadius().length);
        assertArrayEquals(friendBotArr, slandererTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyEnemyRobotsTestArrayLen() throws GameActionException {
        enemyBotArr[0] = enemyMuck;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(enemyBotArr);

        assertEquals(enemyBotArr.length, slandererTest.senseNearbyRobotsInSenseRadius().length);
        assertArrayEquals(enemyBotArr, slandererTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void handleEnemyMuckFalse() throws GameActionException {
        assertFalse(slandererTest.runFromEnemy());
    }

    @Test
    public void handleAllyEC() throws GameActionException {
        assertFalse(slandererTest.stayCLoseToHome());
    }

    @Test
    public void clearPreexistingListsTest() {
        when(nearbyEnemyMuckraker.get(0)).thenReturn(enemyMuck);
        nearbyEnemyMuckraker.add(enemyMuck);
        assertTrue(slandererTest.clearPreexistingLists());
        assertEquals(0, nearbyEnemyMuckraker.size());
        when(nearbyEC.get(0)).thenReturn(friendBotEC);
        nearbyEC.add((friendBotEC));
        assertTrue(slandererTest.clearPreexistingLists());
        assertEquals(0, nearbyEC.size());
    }

    @Test
    public void getNearbyEnemiesTest() throws GameActionException {
        nearbyEnemyMuckrakerNotMock.add(enemyMuck);
        EnemyBotInfoArr[0] = enemyMuck;
        assertEquals(nearbyEnemyMuckrakerNotMock, slandererTest.getNearbyEnemies(EnemyBotInfoArr));
    }

    @Test
    public void getNearbyAllyTest() throws GameActionException {
        nearbyAllyECNotMock.add(friendBotEC);
        AllyInfoArr[0] = friendBotEC;
        assertEquals(nearbyAllyECNotMock, slandererTest.getNearbyAlly(AllyInfoArr));
    }
}
