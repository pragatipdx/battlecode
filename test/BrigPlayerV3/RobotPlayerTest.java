package BrigPlayerV3;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RobotPlayerTest {

    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);


    @Test
    @Ignore
    public void muckRunTest() throws GameActionException {

        RobotPlayer robotPlayer = new RobotPlayer();


        when(rcTest.getType()).thenReturn(RobotType.MUCKRAKER);
        when(rcTest.getTeam()).thenReturn(friend);
        when(rcTest.getTeam().opponent()).thenReturn(enemy);

        robotPlayer.run(rcTest);

//        when(RobotPlayer.run(rcTest)).thenReturn();

    }



}
