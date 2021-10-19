package sprint1Bot;
import battlecode.common.*;

import java.sql.SQLSyntaxErrorException;
import java.util.*;

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

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        turnCount = 0;

        System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation() + " I was created on turncount: " + turnCount);
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
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        if (attackable.length != 0 && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
    }

    static void runSlanderer() throws GameActionException {
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
    }

    static MapLocation homeLoc = null;
    static int homeID = 0;
    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        Team friend = rc.getTeam();

        int actionRadius = 12;
        int senseRadius = 30;
        int detectionRadius = 40;
        int detectionRadiusSqr = rc.getType().detectionRadiusSquared;
        System.out.println("\nDetection Radius const: 40    DetectionRadius Squared: " + detectionRadiusSqr);

        //get ID and Location of EC that spawned bot and store
        //still needs to be fully implemented - can't think of:
        //1. whether this is necessary
        // (I think it is for sending flags to EC, which I believe will act as a hub or coordination)
        //2. how to run this only once, as soon as the bot is spawned, then store the location and ID
        System.out.println("\n Sensing Nearby Bots... " + "\n");
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
        System.out.println("\n Detecting Nearby Bots... " + "\n");

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

                if (tryMove())
                    System.out.println("\nMOVED/MOVING AWAY FROM ENEMY POLITICIAN");

                break;

            }

        }


        //protect friendly slanderers???
        //prioritize attack vs. defend?


        System.out.println("TEST3");

        //couldn't find any enemies to move toward or friendly slanderers to protect
        //move randomly, prioritizing squares with high passibility score
//        if (tryMoveAwayFromHome())
//            System.out.println("Tally Ho!");

        if (tryMove(randomDirection()))
            System.out.println("Tally Ho!");

        System.out.println("TEST4");



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



    static boolean tryMove() throws GameActionException {
        Direction dir = randomDirection();
        double bestOption = 0.0;
        double temp = 0.0;
        for (Direction possibility : directions) {
            if (rc.canMove(possibility)) {
                temp = rc.sensePassability(rc.getLocation().add(possibility));
                System.out.println("Temp pass: " + possibility + " " + temp + " Best option: " + bestOption);
                if (temp > bestOption) {
                    bestOption = temp;
                    dir = possibility;
                    System.out.println("\nFound better option: " + bestOption + " at " + possibility);
                }
            }
        }
        System.out.println("I am trying to move " + dir + "; Is ready? " + rc.isReady() + " Cooldown Turns:" + rc.getCooldownTurns() + " CanMove in Dir?" + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
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
//                possiblePass = rc.sensePassability(rc.getLocation().add(possibleDir));
            } else {
                continue;
            }

            System.out.println("Checking all possible directions....");
            if (rc.canMove(possibleDir) && possibleDir != homeDir && possiblePass > 0.5) {
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


    static void sendLocation() throws  GameActionException {
        MapLocation location = rc.getLocation();
        int x = location.x, y = location.y;
        int encodedLocation = (x % 128) * (y % 128);
        if (rc.canSetFlag(encodedLocation)) {
            rc.setFlag(encodedLocation);
        }
    }

    static void sendLocation(int extraInformation) throws  GameActionException {
        MapLocation location = rc.getLocation();
        int x = location.x, y = location.y;
        int extraInfoCap = 2^10;
        int encodedLocation = (x % 128) * (y % 128) + extraInformation * 128 * 128;
        if (rc.canSetFlag(encodedLocation) && extraInformation < extraInfoCap) {
            rc.setFlag(encodedLocation);
        }
    }

    static MapLocation getLocationFromFlag(int flag){
        int y = flag % 128;
        int x = (flag /128) % 128;
        int extraInfo = flag / 128 / 128;

        //compare values to current location to
        //figure out where it is in relation to our current offset

        MapLocation currentLocation = rc.getLocation();
        int offsetX128 = currentLocation.x/128;
        int offsetY128 = currentLocation.y/128;
        MapLocation actualLocation = new MapLocation(offsetX128 * 128 + x, offsetY128 * 128 + y);

        //check distances
        MapLocation alternative = actualLocation.translate(-128,0);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(128,0);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(0,-128);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(0,128);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }

        return actualLocation;
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
