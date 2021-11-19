package BrigPlayerV3;

import battlecode.common.*;

import static org.mockito.Mockito.mock;

public class SlandererTest {

    Team friend = Team.A;
    Team enemy = Team.B;

    private RobotController rcTest = mock(RobotController.class);
    private Slanderer muckTest = new Slanderer(rcTest, enemy, friend);


    RobotInfo enemyBotMuck = new RobotInfo(2, enemy, RobotType.MUCKRAKER, 10, 10, new MapLocation(1, 0));


//    @Mock
//    private RobotInfo[] EnemyBotInfoArr = new RobotInfo[1];
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }







}
