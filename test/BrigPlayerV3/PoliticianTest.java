package BrigPlayerV3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class PoliticianTest {

    Team friend = Team.A;
    Team enemy = Team.B;


    private RobotController rcTest = mock(RobotController.class);
    private BrigPlayerV3.Politician rpTest = new Politician(rcTest,enemy,friend);


    RobotInfo enemyBotMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));


//    @Mock
//    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }


//    @Test
//    public void testEmpower() throws GameActionException {
//        RobotInfo bot1 = new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, new MapLocation(0, 0));
//        RobotInfo bot2 = new RobotInfo(2, Team.A, RobotType.POLITICIAN, 10, 10, new MapLocation(1, 0));
//        RobotInfo[] arr1 = new RobotInfo[2];
//        arr1[0] = bot1;
//        arr1[1] = bot2;
//        when(rcTest.senseNearbyRobots()).thenReturn(arr1);
//        when(rcTest.getTeam()).thenReturn(Team.A);
//
//        assertEquals(1, rpTest.politicianEmpower(5));
//    }
//
//    @Test
//    public void testProtectSlanderer() throws GameActionException {
//        MapLocation testLoc = new MapLocation(10,10);
//        when(rcTest.getType()).thenReturn(RobotType.SLANDERER);
//        when(rcTest.getLocation()).thenReturn(new MapLocation(0,0));
//        when(rcTest.canMove(Direction.NORTH)).thenReturn(false);
//        when(rcTest.canMove(Direction.NORTHEAST)).thenReturn(false);
//        when(rcTest.canMove(Direction.EAST)).thenReturn(true);
//        when(rcTest.canMove(Direction.SOUTHEAST)).thenReturn(true);
//        when(rcTest.canMove(Direction.SOUTH)).thenReturn(true);
//        when(rcTest.canMove(Direction.SOUTHWEST)).thenReturn(false);
//        when(rcTest.canMove(Direction.WEST)).thenReturn(false);
//        when(rcTest.canMove(Direction.NORTHWEST)).thenReturn(false);
//
////        EnemyBotInfoArr[0]=enemyBotMuck;
//        assertFalse(rpTest.protectSlanderer(1));
//    }

}

