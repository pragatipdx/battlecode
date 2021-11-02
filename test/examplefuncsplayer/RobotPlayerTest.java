package examplefuncsplayer;

import battlecode.common.*;
import org.mockito.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import org.junit.Test;

public class RobotPlayerTest {

	@Mock
	RobotInfo[] tempRobotInfo;
	@Mock
//	your rcmock should be assigned with the RobotController mock
	RobotController RCmock = mock(RobotController.class);


//	// mock creation
//	List mockedList = mock(List.class);
//
//	// using mock object - it does not throw any "unexpected interaction" exception
//	mockedList.add("one");
//	mockedList.clear();
//
//	// selective, explicit, highly readable verification
//	verify(mockedList).add("one");
//	verify(mockedList).clear();

	@Test
	public void testSanity() {
		assertEquals(2, 1+1);
	}

	@Test
	public void testSenseNearbyRobots() {
		when(RCmock.senseNearbyRobots()).thenReturn(tempRobotInfo);
//		assertArrayEquals(tempRobotInfo, RCmock.senseNearbyRobots());

	}

}
