package BrigPlayerV3;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommunicationsTest {

    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Communications communicationTest = new Communications(rcTest);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testflag() throws GameActionException {
          when(rcTest.getLocation()).thenReturn(new MapLocation(0, 0));
          int x,y=1;
        int encodedLocation=1;
        when(rcTest.canSetFlag(encodedLocation)).thenReturn(true);
          rcTest.setFlag(1);

          assertTrue(communicationTest.sendLocation(rcTest));


     }

     @Test
     public void testCommunication() throws GameActionException {
         when(rcTest.getLocation()).thenReturn(new MapLocation(0, 0));
         int x,y=1;
         int encodedLocation=1;
         when(rcTest.canSetFlag(encodedLocation)).thenReturn(true);
         rcTest.setFlag(1);
         assertTrue(communicationTest.sendLocationExtraInfo(rcTest,1));
     }

}
