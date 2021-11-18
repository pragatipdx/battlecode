package BrigPlayerV3;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ECTest {

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
}
