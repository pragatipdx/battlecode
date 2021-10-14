package BrigadePlayer;

import battlecode.common.*;

public strictfp class RobotPlayer {
    static RobotController rc;

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

    static int turnCount;
    static MapLocation ec_Location;
    static Direction target;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        BrigadePlayer.RobotPlayer.rc = rc;

        turnCount = 0;

        System.out.println("I'm a " + rc.getType() + " and I just got created!");

        RobotInfo[] sensable = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
        for (RobotInfo robot : sensable) {
            if (robot.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                ec_Location = robot.getLocation();
            }
        }
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
                    case MUCKRAKER:            runMuckraker();           break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    static void runEnlightenmentCenter() throws GameActionException {
        RobotType toBuild = randomSpawnableRobotType();
        int influence = 50;
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);

            } else {
                break;
            }
        }

    }

    static void runPolitician() throws GameActionException {

        RobotInfo weak_Health_EC=null;
        int weak_influence = (int)(Double.MAX_VALUE);
        Team enemy = rc.getTeam().opponent();

        int actionRadius = rc.getType().actionRadiusSquared;
        int senseRadius=rc.getType().sensorRadiusSquared;

        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutrals=rc.senseNearbyRobots(actionRadius,Team.NEUTRAL);

        if (attackable.length != 0 || neutrals.length !=0) {
            if (rc.canEmpower(actionRadius)) {
                System.out.println("empowering...");
                rc.empower(actionRadius);
                System.out.println("empowered");
            }
        }


            for (RobotInfo troop : rc.senseNearbyRobots(senseRadius, enemy)) {
                if (troop.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    if (troop.getInfluence() < weak_influence) {
                        weak_Health_EC = troop;
                        weak_influence = troop.getInfluence();

                    }
                }
            }

            if (weak_Health_EC != null) {
                target = rc.getLocation().directionTo(weak_Health_EC.getLocation());
                moveToDest(target);
            }

            if (!rc.getLocation().isWithinDistanceSquared(ec_Location, rc.getType().sensorRadiusSquared)) {
                moveToDest(rc.getLocation().directionTo(ec_Location));
            }

//        while (!tryMove(target) && rc.isReady()){
//            target = directions[(int) (Math.random() * directions.length)];
//        }


        if (tryMove(randomDirection()))
            System.out.println("I moved!");
    }

    static void runSlanderer() throws GameActionException {

        if (tryMove(randomDirection()))
            System.out.println("I moved!");

    }

    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;

        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {

            if (robot.type.canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location)) {
                    System.out.println("e x p o s e d");
                    rc.expose(robot.location);

                    return;
                }
            }
        }
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }

    static void buildSpawnableRobot(int val,int influence) throws GameActionException
    {for(Direction dir: directions)
        {if(rc.canBuildRobot(spawnableRobot[val],dir,influence));
            {rc.buildRobot(spawnableRobot[val],dir,influence);}}}

    static boolean moveToDest(Direction route_to_dir) throws GameActionException {

        Direction[] dirs = {route_to_dir, route_to_dir.rotateRight(), route_to_dir.rotateLeft(), route_to_dir.rotateRight().rotateRight(), route_to_dir.rotateLeft().rotateLeft()};

        for(Direction dir : dirs) {
            if(rc.canMove(dir)) {
                rc.move(dir);
                return true;
            }
        }

        return false;
    }

}
