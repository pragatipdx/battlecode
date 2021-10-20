package BrigadePlayer;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    //THESE NEED TO BE CHANGED
    static MapLocation homeLoc = null;
    static int homeID = 0;

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
        Team friend = rc.getTeam();

        int actionRadius = 12;
        int senseRadius = 30;
        int detectionRadius = 40;
        int detectionRadiusSqr = rc.getType().detectionRadiusSquared;

        System.out.println("\nDetection Radius const: 40    DetectionRadius Squared: " + detectionRadiusSqr);

        System.out.println("\n Sensing Nearby Friendly Bots... " + "\n");
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius,friend) ) {
            System.out.println("\nRobot type: " + robot.type + " Robot Id: "
                    + robot.ID + " Robot Loc: " +  robot.location + "\n");
            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                homeLoc = robot.location;
                homeID = robot.ID;
                System.out.println("Found Home! ID: " + homeID + " LOC: " + homeLoc);
                break;
            }
        }

        //detect ALL nearby robots and see how close/far they are
        System.out.println("\n Detecting ALL Nearby Bots... " + "\n");

        for (MapLocation location : rc.detectNearbyRobots() ) {

            //lower the number returned, farther the distance away
            int distance = rc.getLocation().compareTo(location);

            System.out.println("\nRobot detected at : " + location + "\nMy Location: " + rc.getLocation() +
                    "Distance: " + distance + "\n");
        }

        //sanity check //print info about self
        System.out.println("\nHome: ID: " + homeID + " LOC: " + homeLoc + " TurnCount: " + turnCount
                + " Player Cooldown: " + rc.getCooldownTurns() + " Player action/sense/detect radi: " +
                actionRadius + " / " + senseRadius + " / " + detectionRadius + "\n");

        System.out.println("TEST1");

        //look for robots in action radius and expose them if possible
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location)) {
                    System.out.println("\ne x p o s e d\n");
                    rc.expose(robot.location);
                    return;//why return instead of moving after?not possible?
                }
            }
        }

        System.out.println("TEST2");

        //sense nearby enemies
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius, enemy)) {
            System.out.println("This loop should only run if enemy is detected");

            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                // It's an Enemy EC! Make your way there to SWARM
                System.out.println("\nFOUND ENEMY EC at : " + robot.location);
                basicBug(robot.location);
                System.out.println("\nMOVED/MOVING TOWARD ENEMY EC");
                break;
            } else if (robot.type.equals(RobotType.SLANDERER)) {
                // It's a slanderer! advance to EXPOSE!
                System.out.println("\nFOUND ENEMY BOT at : " + robot.location);
                basicBug(robot.location);
                System.out.println("\nMOVED/MOVING TOWARD ENEMY BOT");
                break;
            } else if (robot.type.equals(RobotType.MUCKRAKER)) {
                // It's a slanderer! advance to EXPOSE!
                System.out.println("\nFOUND ENEMY BOT at : " + robot.location);
                basicBug(robot.location);
                System.out.println("\nMOVED/MOVING TOWARD ENEMY BOT");
                break;
            } else  {
                // It's an Enemy Politician! Avoid them lest lose your conviction
                System.out.println("\nFOUND ENEMY BOT at : " + robot.location);

                //needs to IMPLEMENT!!
                if (runAway(robot.location)){
                    System.out.println("\nMOVED/MOVING AWAY FROM ENEMY POLITICIAN");
                }

                return;

            }

        }

        if (tryMoveAwayFromHome())
            System.out.println("Tally Ho!");
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


    static boolean tryMoveAwayFromHome() throws GameActionException {
        System.out.println("Trying to move away from home");
        double bestOption = 0.0;
        double possiblePass = 0.0;
        Direction homeDir = rc.getLocation().directionTo(homeLoc);
        Direction tempDir = randomDirection();
        System.out.println("Home dir: " + homeDir);
        List<Direction> bestPossDirs = new ArrayList<>();
        for (Direction possibleDir : directions) {
            if (rc.canMove(possibleDir)) {
                System.out.println("TESTTT");
                possiblePass = rc.sensePassability(rc.getLocation().add(possibleDir));
            } else {
                continue;
            }

            System.out.println("Checking all possible directions....");
            if (rc.canMove(possibleDir) && possibleDir != homeDir && possiblePass > 0.2) {
//                possiblePass = rc.sensePassability(rc.getLocation().add(possibleDir));
//                System.out.println("Possible pass: " + possiblePass + " at " + possibleDir + "; Current Best option: " + bestOption);
                System.out.println("Possible pass: " + possiblePass + " at " + possibleDir + " ...adding to list");
                bestPossDirs.add(possibleDir);
//                if (possiblePass > bestOption) {
//                    bestOption = possiblePass;
//                    tempDir = possibleDir;
//                    System.out.println("\nFound better option: " + bestOption + " at " + possibleDir);
            }
        }
        if (bestPossDirs.isEmpty()) {
            tempDir = randomDirection();
        } else {
            Collections.shuffle(bestPossDirs);
            tempDir = bestPossDirs.get(0);
        }
        System.out.println("I am trying to move " + tempDir + "; Is ready? " + rc.isReady() + " Cooldown Turns:" + rc.getCooldownTurns() + " CanMove in Dir?" + rc.canMove(tempDir));
        if (rc.canMove(tempDir)) {
            rc.move(tempDir);
            return true;
        } else return false;
    }

    static boolean runAway(MapLocation location) throws GameActionException {
        System.out.println("Trying to move away from Enemy");
        double bestOption = 0.0;
        double possiblePass = 0.0;
        Direction homeDir = rc.getLocation().directionTo(location);
        Direction tempDir = randomDirection();
        System.out.println("Enemy dir: " + homeDir);
        List<Direction> bestPossDirs = new ArrayList<>();
        for (Direction possibleDir : directions) {
            if (rc.canMove(possibleDir)) {
                System.out.println("TEST");
                possiblePass = rc.sensePassability(rc.getLocation().add(possibleDir));
            } else {
                continue;
            }

            System.out.println("Checking all possible directions....");
            if (rc.canMove(possibleDir) && possibleDir != homeDir && possiblePass > 0.2) {
//                possiblePass = rc.sensePassability(rc.getLocation().add(possibleDir));
//                System.out.println("Possible pass: " + possiblePass + " at " + possibleDir + "; Current Best option: " + bestOption);
                System.out.println("Possible pass: " + possiblePass + " at " + possibleDir + " ...adding to list");
                bestPossDirs.add(possibleDir);
//                if (possiblePass > bestOption) {
//                    bestOption = possiblePass;
//                    tempDir = possibleDir;
//                    System.out.println("\nFound better option: " + bestOption + " at " + possibleDir);
            }
        }
        if (bestPossDirs.isEmpty()) {
            tempDir = randomDirection();
        } else {
            Collections.shuffle(bestPossDirs);
            tempDir = bestPossDirs.get(0);
        }
        System.out.println("I am trying to move " + tempDir + "; Is ready? " + rc.isReady() + " Cooldown Turns:" + rc.getCooldownTurns() + " CanMove in Dir?" + rc.canMove(tempDir));
        if (rc.canMove(tempDir)) {
            rc.move(tempDir);
            return true;
        } else return false;
    }


    //basic pathfinding bug
    //NEEDS TO BE TESTED - HAS NOT BEEN VERIFIED
    static final double passabilityThreshold = 0.6;
    static Direction bugDirection = null;
    static void basicBug(MapLocation target) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(target);
        if (rc.getLocation().equals(target)) {
            //do something
        } else {
            if (bugDirection == null) {
                bugDirection = dir.rotateRight();
            }
            for (int i = 0; i < 8; ++i) {
                if (rc.canMove(bugDirection) && rc.sensePassability(rc.getLocation().add(bugDirection)) >= passabilityThreshold) {
                    rc.move(bugDirection);
                    break;
                }
                bugDirection = bugDirection.rotateRight();
            }
            bugDirection = bugDirection.rotateLeft();
        }
    }








}
