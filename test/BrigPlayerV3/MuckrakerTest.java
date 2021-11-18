package BrigPlayerV3;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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
    RobotInfo enemyBotMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));
    RobotInfo friendBotSland = new RobotInfo(1, friend, RobotType.SLANDERER, 10, 10, new MapLocation(0, 0));
    RobotInfo enemyBotSland = new RobotInfo(2, enemy, RobotType.SLANDERER, 10, 10, new MapLocation(1, 0));
    RobotInfo[] friendBotArr = new RobotInfo[2];
    RobotInfo[] enemyBotArr = new RobotInfo[2];

    @Mock
    private ArrayList<RobotInfo> nearbyEnemyMuckraker;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void senseNearbyRobotsTestNullArrayE() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, enemy)).thenReturn(robots);

        assertArrayEquals(null, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestNullArrayF() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, friend)).thenReturn(robots);

        assertArrayEquals(null, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void senseNearbyRobotsTestArrayLen() throws GameActionException {
        friendBotArr[0] =friendBotPoli;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(friendBotArr);

        assertEquals(friendBotArr.length, muckTest.senseNearbyRobotsInSenseRadius().length);
        assertArrayEquals(friendBotArr, muckTest.senseNearbyRobotsInSenseRadius());
    }

    @Test
    public void exposeEnemyTestTrue() throws GameActionException {
        enemyBotArr[0] = enemyBotSland;

        when(rcTest.canExpose(enemyBotSland.location)).thenReturn(true);

        assertTrue(muckTest.exposeEnemy(enemyBotSland));
    }

    @Test
    public void exposeEnemyTestFalse() throws GameActionException {
        when(rcTest.canExpose(enemyBotPoli.location)).thenReturn(false);

        assertFalse(muckTest.exposeEnemy(enemyBotPoli));
    }

    @Test
    public void handleEnemyPoliFalse() throws GameActionException {
        assertFalse(muckTest.handleEnemyPoli());
    }

    @Test
    public void handleEnemyEnemyECFalse() throws GameActionException {
        assertFalse(muckTest.handleEnemyEC());
    }

    @Test
    public void handleEnemySlandFalse() throws GameActionException {
        assertFalse(muckTest.handleEnemySlanderer());
    }

    @Test
    public void clearPreexistingListsTest(){
        when(nearbyEnemyMuckraker.get(0)).thenReturn(enemyBotMuck);
        nearbyEnemyMuckraker.add(enemyBotMuck);
        assertTrue(muckTest.clearPreexistingLists());
        assertEquals(0, nearbyEnemyMuckraker.size());
    }

    @Test
    @Ignore
    public void getNearbyEnemiesTest() throws GameActionException {
        enemyBotArr[0] = enemyBotPoli;
        List<RobotInfo> botList = new ArrayList<RobotInfo>();
        botList.add(enemyBotPoli);

//        when(enemyBotPoli.getTeam()).thenReturn(enemy);
//        when(enemyBotPoli.team.equals(enemy)).thenReturn(true);
        when(rcTest.getTeam()).thenReturn(friend);
//        when(enemyBotPoli.getTeam()).thenReturn(enemy);


        assertEquals(enemyBotArr.length, muckTest.getNearbyEnemies(enemyBotArr).size());
        assertEquals(botList, muckTest.getNearbyEnemies(enemyBotArr));
    }




}
