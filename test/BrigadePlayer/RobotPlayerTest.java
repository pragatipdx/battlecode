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

    }

