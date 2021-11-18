package BrigPlayerV3;
import static org.junit.Assert.*;
import battlecode.common.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SlandererTest {
    private RobotController rcTest = mock(RobotController.class);
    private BrigPlayerV3.Navigation rpTest = new Navigation(rcTest);
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
        assertFalse(rpTest.runTowardsEC(testLoc));
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
        assertFalse(rpTest.runAwayFromMuck(testLoc));

    }
}
