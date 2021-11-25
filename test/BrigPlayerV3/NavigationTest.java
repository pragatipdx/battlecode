package BrigPlayerV3;
import static org.junit.Assert.*;
import battlecode.common.*;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NavigationTest {



    private RobotController rcTest = mock(RobotController.class);
    private BrigPlayerV3.Navigation rpTest = new Navigation(rcTest);

     @Test
    public void navTest() throws GameActionException {
         assertFalse(rpTest.goTo(Direction.NORTH));
     }

     @Test
     @Ignore
     public void navTestGoto() throws GameActionException {
         when(rpTest.goTo(Direction.NORTH)).thenReturn(Boolean.FALSE);
         assertFalse(rpTest.goTo(new MapLocation(0,0)));

     }


}
