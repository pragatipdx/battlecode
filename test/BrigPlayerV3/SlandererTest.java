package BrigPlayerV3;

import battlecode.common.*;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SlandererTest  {
    static RobotController rc;
    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Slanderer slandererTest = new Slanderer(rcTest, enemy, friend);


    RobotInfo enemyBotMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));

    @Test
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

    }
//    @Mock
//    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

}
