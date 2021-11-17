package BrigPlayerV3;
import battlecode.common.*;

public strictfp class RobotPlayerBetterCallSushi {

    static RobotController rc;
    static int turnCount;

    static Team myTeam;
    static Team oppTeam;

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };


    public RobotPlayerBetterCallSushi(RobotController rc) {
        this.rc = rc;
    }

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayerBetterCallSushi.rc = rc;
        turnCount = 0;
        myTeam = rc.getTeam();
        oppTeam = rc.getTeam().opponent();

        System.out.println("I'm a " + rc.getType() + " and I just got created!");


        //////////////////////////// /////what is this doing?///////////////////////////

//        RobotInfo[] sensable = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
//        for (RobotInfo robot : sensable) {
//            if (robot.getType() == RobotType.ENLIGHTENMENT_CENTER) {
//                ec_Location = robot.getLocation();
//            }
//        }


        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
//                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
//                    case POLITICIAN:           runPolitician();          break;
//                    case SLANDERER:            runSlanderer();           break;
//                    case MUCKRAKER:            runMuckraker();           break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

}

//./gradlew run -Pmaps=Andromeda -PteamA=examplefuncsplayer -PteamB=BrigadePlayer