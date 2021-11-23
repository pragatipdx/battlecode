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

public class SlandererTest  {
    static RobotController rc;
    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Slanderer slandererTest = new Slanderer(rcTest, enemy, friend);
    int senseRadius = 30;

    RobotInfo enemyMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));

    RobotInfo friendBotEC = new RobotInfo(1, friend, RobotType.ENLIGHTENMENT_CENTER, 10, 10, new MapLocation(1, 0));
    RobotInfo enemyBotPoli = new RobotInfo(2, enemy, RobotType.POLITICIAN, 10, 10, new MapLocation(1, 0));
   // RobotInfo enemyBotMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));
    RobotInfo friendBotSland = new RobotInfo(1, friend, RobotType.SLANDERER, 10, 10, new MapLocation(0, 0));
    RobotInfo enemyBotSland = new RobotInfo(2, enemy, RobotType.SLANDERER, 10, 10, new MapLocation(1, 0));
    RobotInfo[] friendBotArr = new RobotInfo[2];
    RobotInfo[] enemyBotArr = new RobotInfo[2];
    ArrayList<RobotInfo> nearbyEnemyMuckrakerNotMock = new ArrayList<>();
    ArrayList<RobotInfo> nearbyAllyECNotMock = new ArrayList<>();

    @Mock
    private ArrayList<RobotInfo> nearbyEnemyMuckraker;
    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];
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
        friendBotArr[0] =friendBotEC;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(friendBotArr);

        assertEquals(friendBotArr.length, slandererTest.senseNearbyRobotsInSenseRadius().length);
        assertArrayEquals(friendBotArr, slandererTest.senseNearbyRobotsInSenseRadius());
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
    public void clearPreexistingListsTest(){
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
        EnemyBotInfoArr[0]=enemyMuck;
        assertEquals(nearbyEnemyMuckrakerNotMock, slandererTest.getNearbyEnemies(EnemyBotInfoArr));
    }
    @Test
    public void getNearbyAllyTest() throws GameActionException {
        nearbyAllyECNotMock.add(friendBotEC);
        AllyInfoArr[0]=friendBotEC;
        assertEquals(nearbyAllyECNotMock, slandererTest.getNearbyAlly(AllyInfoArr));
    }


    /*@Test
    public void testTurn() throws GameActionException {
        RobotInfo bot1 = new RobotInfo(1, Team.B, RobotType.MUCKRAKER, 10, 10, new MapLocation(0, 0));
        RobotInfo bot2 = new RobotInfo(2, Team.A, RobotType.ENLIGHTENMENT_CENTER, 10, 10, new MapLocation(1, 0));
        RobotInfo[] arr1 = new RobotInfo[2];
        arr1[0] = bot1;
        arr1[1] = bot2;
        MapLocation testLoc = new MapLocation(10,10);
        when(slandererTest.runFromEnemy(testLoc)).thenReturn(true);
        when(slandererTest.stayCLoseToHome(testLoc)).thenReturn(true);
        slandererTest.takeTurn();
    }
    @Test
    public void testRunTowardsEC() throws GameActionException {
        MapLocation testLoc = new MapLocation(10,10);
        when(rcTest.getType()).thenReturn(RobotType.ENLIGHTENMENT_CENTER);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);
        assertTrue(slandererTest.stayCLoseToHome(testLoc));
    }
    @Test
    public void testRunAwayFromMuck() throws GameActionException {
        MapLocation testLoc = new MapLocation(10,10);
        when(rcTest.getType()).thenReturn(RobotType.SLANDERER);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);
        assertTrue(slandererTest.runFromEnemy(testLoc));

    }*/
//    @Mock
//    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

}
