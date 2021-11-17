package BrigPlayerV3;
import battlecode.common.*;

public class Muckraker extends Unit {


    public Muckraker(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();


        int actionRadius = 12;
        int senseRadius = 30;
        int detectionRadius = 40;

//        int testTurnCount = 0;
//        testTurnCount += 1;
//        System.out.println("Turncount: " + turnCount + " TestTurnCount: " + testTurnCount + "Robot ID: " + rc.getID());

        //get and store home location and id
        //only want to do this once on first turn
//        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius,friend) ) {
//            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
//                homeLoc = robot.location;
//                homeID = robot.ID;
//                break;
//            }
//        }

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
                nav.basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.SLANDERER)) {
                // It's a slanderer! advance to EXPOSE!
                nav.basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.MUCKRAKER)) {
                //move toward muckraker in hopes of finding Enemy EC
                nav.basicBug(robot.location);
                break;
            } else if (robot.type.equals(RobotType.POLITICIAN))  {
                nav.moveAway(robot.location);
                break;
            } else {
                continue;
            }
        }

        //try to move away from Home EC, hopefully toward enemy
        nav.moveAway(hqLoc);
    }



}
