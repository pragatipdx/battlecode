package BrigPlayerV3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class EnlightenmentCenterTest {

    private RobotController rcTest = mock(RobotController.class);
    private EnlightenmentCenter ECtest = new EnlightenmentCenter(rcTest);


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
        assertEquals(1, ECtest.countEnemyPoliticians());
    }

    @Test
    public void testInvest() throws GameActionException {
        assertEquals(5, ECtest.invest(RobotType.MUCKRAKER));
        when (rcTest.getRoundNum()).thenReturn(1);
        assertEquals(50, ECtest.invest(RobotType.POLITICIAN));
        when (rcTest.getRoundNum()).thenReturn(1);
        assertEquals(50, ECtest.invest(RobotType.SLANDERER));
    }

    @Test
    public void testNextBuild() throws GameActionException {
        when (rcTest.getRoundNum()).thenReturn(1);
        assertEquals(RobotType.SLANDERER, ECtest.nextBuild());
        when (rcTest.getRoundNum()).thenReturn(309);
        assertEquals(RobotType.POLITICIAN, ECtest.nextBuild());
        when (rcTest.getRoundNum()).thenReturn(107);
        assertEquals(RobotType.MUCKRAKER, ECtest.nextBuild());
    }

    @Test
    public void testBid() throws GameActionException {
        when (rcTest.getRoundNum()).thenReturn(100);
        when (rcTest.getTeamVotes()).thenReturn(0);
        when (rcTest.getInfluence()).thenReturn(100);
        ECtest.startbidding();
        verify(rcTest, times(1)).canBid(10);

        when (rcTest.getRoundNum()).thenReturn(100);
        when (rcTest.getTeamVotes()).thenReturn(500);
        when (rcTest.getInfluence()).thenReturn(100);
        ECtest.startbidding();
        verify(rcTest, times(1)).canBid(6);
    }

    @Test
    public void testBuild() throws GameActionException {
        ECtest.startBuild(RobotType.SLANDERER, 50);
        verify(rcTest, times(1)).canBuildRobot(RobotType.SLANDERER, Direction.NORTH, 50);
    }
}
