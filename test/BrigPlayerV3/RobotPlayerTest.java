package BrigPlayerV3;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RobotPlayerTest {
    private RobotController rcTest = mock(RobotController.class);


    @Test
    @Ignore
    public void ECRunTest() throws GameActionException {

        RobotPlayer robotPlayer = new RobotPlayer();
//        Robot EC = new Robot(rcTest);
//        EnlightenmentCenter EC = new EnlightenmentCenter(rcTest);

        when(rcTest.getType()).thenReturn(RobotType.ENLIGHTENMENT_CENTER);

        robotPlayer.run(rcTest);

//        when(RobotPlayer.run(rcTest)).thenReturn();

    }



}
