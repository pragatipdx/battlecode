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


    public RobotPlayer(RobotController rc) {
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
        int currentInfluence = rc.getInfluence();

        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
            } else {
                break;
            }
        }

        // Bidding - Check is votes is upper threshold and start bidding after building influence
        if (rc.getTeamVotes() < 751 && rc.getRoundNum()>50) {
            if (rc.getTeamVotes() / rc.getRoundNum() < 0.4) {
                if (rc.canBid((int) (0.1 * currentInfluence))) {
                    rc.bid((int) (0.1 * currentInfluence));
                    System.out.println("Bid " + (int) (0.1 * currentInfluence));
                }
            } else {
                if (rc.canBid((int) (0.06 * currentInfluence))) {
                    rc.bid((int) (0.06 * currentInfluence));
                    System.out.println("Bid " + (int) (0.06 * currentInfluence));
                }
            }
        }
    }



    static void runPolitician() throws GameActionException {

        RobotInfo weak_Health_EC=null;
        int weak_influence = (int)(Double.MAX_VALUE);
        Team enemy = rc.getTeam().opponent();
        Team ally = rc.getTeam();

        if (tryMove(randomDirection()))
            System.out.println("I moved!");

        int actionRadius = rc.getType().actionRadiusSquared;
        int senseRadius = rc.getType().sensorRadiusSquared;

        // Protect slanderer from enemy

        for (RobotInfo robotA : rc.senseNearbyRobots(actionRadius, ally)) {
            if (robotA.getType() == RobotType.SLANDERER) {
                MapLocation Mlocate = robotA.getLocation();
                Direction Dlocate = robotA.getLocation().directionTo(Mlocate);
                if (rc.canMove(Dlocate)) {
                    if (tryMove(Dlocate)) {
                        rc.move(Dlocate);
                        System.out.println("!!!!!!!!!I am moving towards " + Dlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(Dlocate));
                    }

                }
            }
        }

        // Empower if enemy and neutral within range

        politicianEmpower(actionRadius);

        //Find lowest influence EC

        for (RobotInfo troop : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (troop.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                if (troop.getInfluence() < weak_influence) {
                    weak_Health_EC = troop;
                    weak_influence = troop.getInfluence();

                }
            }
        }

    }

    public static boolean politicianEmpower(int actionRadius) throws GameActionException
    {
        boolean val=true;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, rc.getTeam().opponent());
        RobotInfo[] neutrals = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);

        if (attackable.length != 0 || neutrals.length != 0) {
            if (rc.canEmpower(actionRadius)) {
                System.out.println("empowering...");
                rc.empower(actionRadius);
                System.out.println("empowered");
                val=true;

            }
        }
        else{
            val=false;
        }
        return val;
    }


    static void runSlanderer() throws GameActionException {

        if (tryMove(randomDirection()))
            System.out.println("I moved!");
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                if (robot.getType() == RobotType.MUCKRAKER) {
                    MapLocation Mlocate = robot.getLocation();
                    Direction Dlocate = robot.getLocation().directionTo(Mlocate);
                    Direction OppDlocate = Dlocate.opposite();
                    if (rc.canMove(OppDlocate)) {
                        if (tryMove(OppDlocate)) {
                            rc.move(OppDlocate);
                            System.out.println("!!!!!!!!!!I am moving away from muckraker towards " + OppDlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(OppDlocate));
                        }

                    }
                }
            }
        }
        Team ally = rc.getTeam();
        int actionRadiusA = rc.getType().actionRadiusSquared;
        for (RobotInfo robotA : rc.senseNearbyRobots(actionRadiusA, ally)){
            if(robotA.getType() == RobotType.ENLIGHTENMENT_CENTER){
                MapLocation Mlocate = robotA.getLocation();
                Direction Dlocate = robotA.getLocation().directionTo(Mlocate);
                if(rc.canMove(Dlocate)){
                    if (tryMove(Dlocate)){
                        rc.move(Dlocate);
                        System.out.println("!!!!!!!!!I am moving towards " + Dlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(Dlocate));
                    }

                }
            }
        }
    }




    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        Team friend = rc.getTeam();
        int actionRadius = 12;
        int senseRadius = 30;
        int detectionRadius = 40;


        //get and store home location and id
        //only want to do this once on first turn
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius,friend) ) {
            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                homeLoc = robot.location;
                homeID = robot.ID;
                break;
            }
        }

        //get distance to every robot within detection radius
        //lower the number returned, farther the distance away
        //but can only get location of bots - NOT team, type or other properties
        //not sure yet what use this has - if any.....
//        for (MapLocation location : rc.detectNearbyRobots() ) {
//            int distance = rc.getLocation().compareTo(location);
//        }


        //look for robots in action radius and expose them if possible
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location)) {
//                    System.out.println("\ne x p o s e d\n");
                    rc.expose(robot.location);
//                    return;//why return instead of moving after?not possible?
                }
            }
        }

        //sense nearby enemies
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius, enemy)) {
//            System.out.println("This loop should only run if enemy is detected");
            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                // It's an Enemy EC! Make your way there to SWARM
                basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.SLANDERER)) {
                // It's a slanderer! advance to EXPOSE!
                basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.MUCKRAKER)) {
                basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.POLITICIAN))  {
//                runAway(robot.location);
                break;
            } else {
                continue;
            }
        }

        //try to move away from Home EC, hopefully toward enemy
//        tryMoveAwayFromHome();

    }

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    static void buildSpawnableRobot(int val,int influence) throws GameActionException
    {for(Direction dir: directions)
    {if(rc.canBuildRobot(spawnableRobot[val],dir,influence));
        {rc.buildRobot(spawnableRobot[val],dir,influence);}}}


    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }


    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
//        System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }


    public static boolean moveToDest(Direction route_to_dir) throws GameActionException {
        Direction[] dirs = {route_to_dir, route_to_dir.rotateRight(), route_to_dir.rotateLeft(), route_to_dir.rotateRight().rotateRight(), route_to_dir.rotateLeft().rotateLeft()};
        for(Direction dir : dirs) {
            if(rc.canMove(dir)) {
                rc.move(dir);
                return true;
            }
        }
        return false;
    }


    //basic pathfinding bug
    //NEEDS TO BE TESTED - HAS NOT BEEN VERIFIED
    /**
     * Attempts to move to a given MapLocation, navigating around
     * squares < passabilityThreshold.
     *
     * @param targt The intended MapLocation of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static final double passabilityThreshold = 0.3;
    static Direction bugDirection = null;
    static boolean basicBug(MapLocation target) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(target);
        if (rc.getLocation().equals(target)) {
            System.out.println("Bug reached target Dest");
            return true;
        } else {
            if (bugDirection == null) {
                bugDirection = dir.rotateRight();
            }
            for (int i = 0; i < 8; ++i) {
                if (rc.canMove(bugDirection) && rc.sensePassability(rc.getLocation().add(bugDirection)) >= passabilityThreshold) {
                    rc.move(bugDirection);
                    return true;
                }
                bugDirection = bugDirection.rotateRight();
            }
            bugDirection = bugDirection.rotateLeft();
        }
        //if bug cannot move
        return false;
    }


    /**
     * Attempts to move to away from a given MapLocation
     *
     * @param location The intended MapLocation to move away from
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean moveAway(MapLocation location) throws GameActionException {
        double possiblePass = 0.0;
        Direction homeDir = rc.getLocation().directionTo(location);
        Direction moveDir = randomDirection();
        List<Direction> bestPossDirs = new ArrayList<>();
        for (Direction possibleDir : directions) {
            if (rc.canMove(possibleDir) && possibleDir != homeDir) {
                bestPossDirs.add(possibleDir);
            }
        }
        if (bestPossDirs.isEmpty()) {
            moveDir = randomDirection();
        } else {
            Collections.shuffle(bestPossDirs);
            moveDir = bestPossDirs.get(0);
        }
        if (rc.canMove(moveDir)) {
            rc.move(moveDir);
            return true;
        } else return false;
    }



}