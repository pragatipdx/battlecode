package BrigadePlayer;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RobotPlayerTest {

        private RobotController rcTest = mock(RobotController.class);
        private RobotPlayer rpTest = new RobotPlayer(rcTest);


    @Test
    public void politicianTest() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.POLITICIAN);
        when(rcTest.getTeam()).thenReturn(Team.NEUTRAL);

        RobotInfo[] robots={};
        when(rcTest.senseNearbyRobots(1,Team.NEUTRAL)).thenReturn(robots);
        when(rcTest.canEmpower(1)).thenReturn(false);

        assertFalse(rpTest.politicianEmpower(1));
    }

    @Test
    public void runECTest() throws GameActionException {
        if (rcTest.getType() == RobotType.ENLIGHTENMENT_CENTER)
        {assertEquals(RobotType.ENLIGHTENMENT_CENTER, rcTest.getType());}
        if (rcTest.getType() == RobotType.POLITICIAN)
        {assertEquals(RobotType.POLITICIAN, rcTest.getType());}
        if (rcTest.getType() == RobotType.SLANDERER)
        {assertEquals(RobotType.SLANDERER, rcTest.getType());}
        if (rcTest.getType() == RobotType.MUCKRAKER)
        {assertEquals(RobotType.MUCKRAKER, rcTest.getType());}}


    @Test
    public void testMoveToDest() throws GameActionException {
        when(rcTest.getType()).thenReturn(RobotType.POLITICIAN);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);

        assertTrue(rpTest.moveToDest(Direction.NORTH));
    }

    @Test
    public void testMoveAway() throws GameActionException {
        MapLocation testLoc = new MapLocation(10,10);

        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);

        assertTrue(rpTest.moveAway(testLoc));
    }

    @Test
    public void testBasicBug() throws GameActionException {
        MapLocation testLoc = new MapLocation(0,0);
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);

        assertTrue(rpTest.basicBug(testLoc));

    }

    @Test
    public void testRunAwayFromMuck() throws GameActionException {
        //tests bot can move away from testLoc
        MapLocation testLoc = new MapLocation(10,10);

        when(rcTest.getType()).thenReturn(RobotType.SLANDERER);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));

//        when(directionTo(testLoc)).thenReturn(Direction.NORTH);

        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);

        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);

        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);

        assertFalse(rpTest.runAwayFromMuck(testLoc));

    }


    @Test
    public void testBasicBug2() throws GameActionException {
        //tests that bug can move to testLoc with appropriate passability
        Double testDouble = new Double(6.0);
        MapLocation testLoc = new MapLocation(1,0);
        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);
        when(rcTest.sensePassability(testLoc)).thenReturn(testDouble);
        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);

        assertTrue(rpTest.basicBug(testLoc));
    }

    //test that enemy politicians in sense radius are counted correctly
    @Test
    public void testCountEnemyPoliticians() throws GameActionException {
        RobotInfo bot1 = new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, new MapLocation(0, 0));
        RobotInfo bot2 = new RobotInfo(2, Team.A, RobotType.POLITICIAN, 10, 10, new MapLocation(1, 0));
        RobotInfo[] arr1 = new RobotInfo[2];
        arr1[0] = bot1;
        arr1[1] = bot2;
        when(rcTest.senseNearbyRobots()).thenReturn(arr1);
        when(rcTest.getTeam()).thenReturn(Team.A);

        assertEquals(1, rpTest.countEnemyPoliticians());
    }

}

