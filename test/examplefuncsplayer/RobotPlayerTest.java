package examplefuncsplayer;

import BrigadePlayer.RobotPlayer;
import battlecode.common.*;
import org.mockito.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import org.junit.Test;

public class RobotPlayerTest {

	private RobotController rcTest = mock(RobotController.class);
	private RobotPlayer rpTest = new RobotPlayer(rcTest);

	@Test
	public void testSanity() {
		assertEquals(2, 1+1);
	}

}
