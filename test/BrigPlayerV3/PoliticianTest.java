package BrigPlayerV3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;


public class PoliticianTest {
    static RobotController rc;
    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Politician politicianTest = new Politician(rcTest, enemy, friend);
    int senseRadius = 10;
    RobotInfo[] friendBotArr = new RobotInfo[1];
   // RobotInfo[] enemyBotArr = new RobotInfo[1];
    ArrayList<RobotInfo> nearbyAlly = new ArrayList<>();

    RobotInfo friendBotSlanderer = new RobotInfo(1, friend, RobotType.SLANDERER, 10, 10, new MapLocation(1, 0));
    RobotInfo enemyEC = new RobotInfo(2, enemy, RobotType.ENLIGHTENMENT_CENTER, 10, 10, new MapLocation(1, 0));

    @Mock
    private ArrayList<RobotInfo> nearbySlanderer;
    private RobotInfo[] AllyInfoArr = new RobotInfo[1];

    @Mock

    private ArrayList<RobotInfo> nearbyEC;
    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void politicianEmpowerTest() throws GameActionException {
        assertTrue(politicianTest.politicianEmpower());
    }

    @Test
    public void politicianProtector() throws GameActionException {
        assertFalse(politicianTest.protectSlanderer());
    }
    @Test
    public void senseNearbyRobotsTestNullArrayE() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, enemy)).thenReturn(robots);

        assertArrayEquals(null, politicianTest.EmpoweringRadiusBots());
    }

    @Test
    public void politicianToActionTest() throws GameActionException {
        nearbyAlly.add(friendBotSlanderer);
        AllyInfoArr[0] = friendBotSlanderer;
        assertEquals(nearbyAlly, politicianTest.politicianToAction(AllyInfoArr));
    }

    @Test
    public void politicianActionTest() throws GameActionException {
      //  nearbyAlly.add(friendBotSlanderer);
        AllyInfoArr[0] = friendBotSlanderer;
        RobotInfo[] robots = {};

        assertEquals(nearbyAlly, politicianTest.politicianBotsList(robots));
    }



    @Test
    public void senseNearbyRobotsTestNullArrayF() throws GameActionException {
        RobotInfo[] robots = {};
        when(rcTest.senseNearbyRobots(senseRadius, friend)).thenReturn(robots);

        assertArrayEquals(null,politicianTest.EmpoweringRadiusBots());
    }

    @Test
    public void senseNearbyRobotsTestArrayLen() throws GameActionException {
        friendBotArr[0] = friendBotSlanderer;

        when(rcTest.senseNearbyRobots(senseRadius)).thenReturn(friendBotArr);
        assertEquals(friendBotArr.length, politicianTest.EmpoweringRadiusBots().length);
        assertArrayEquals(friendBotArr, politicianTest.EmpoweringRadiusBots());
    }

    @Test
    public void fetchBots() throws GameActionException {
        assertTrue(politicianTest.fetchBots());
    }

    @Test
     public void botsFlagE() throws GameActionException
    {
       assertFalse(politicianTest.enemyBotsflagCouter());
    }

    @Test
      public void botsFlagA()
    {
        assertFalse(politicianTest.allyBotsFlagCounter());
    }


    @Test
    public void findWeekestEC()
    {
        assertTrue(politicianTest.findWeekestInfluenceEC());
    }
    @Test
    public void resetFlagCounterTest() {
        when(nearbyEC.get(0)).thenReturn(enemyEC);
        nearbyEC.add(enemyEC);
        assertTrue(politicianTest.resetFlagCounter());
        assertEquals(0, nearbyEC.size());
        when(nearbySlanderer.get(0)).thenReturn(friendBotSlanderer);
        nearbySlanderer.add((friendBotSlanderer));
        assertTrue(politicianTest.resetFlagCounter());
        assertEquals(0, nearbySlanderer.size());
    }

}

