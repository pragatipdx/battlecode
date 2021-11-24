package BrigPlayerV3;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Navigation {
    RobotController rc;

    public Navigation(RobotController _rc) {
        rc = _rc;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    boolean tryMove(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }

    // tries to move in the general direction of dir
    boolean goTo(Direction dir) throws GameActionException {
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight()};
        for (Direction d : toTry) {
            if (tryMove(d))
                return true;
        }
        return false;
    }

    // navigate towards a particular location
    boolean goTo(MapLocation destination) throws GameActionException {
        goTo(rc.getLocation().directionTo(destination));
        return true;
    }


//    boolean moveToDest(Direction route_to_dir) throws GameActionException {
//        Direction[] dirs = {route_to_dir, route_to_dir.rotateRight(), route_to_dir.rotateLeft(), route_to_dir.rotateRight().rotateRight(), route_to_dir.rotateLeft().rotateLeft()};
//        for (Direction dir : dirs) {
//            if (rc.canMove(dir)) {
//                rc.move(dir);
//                return true;
//            }
//        }
//        return false;
//    }


    //basic pathfinding bug
    //NEEDS TO BE TESTED - HAS NOT BEEN VERIFIED
//    /**
//     * Attempts to move to a given MapLocation, navigating around
//     * squares < passabilityThreshold.
//     *
//     * @param targt The intended MapLocation of movement
//     * @return true if a move was performed
//     * @throws GameActionException
//     */
//    static final double passabilityThreshold = 0.3;
//    static Direction bugDirection = null;
//
//    boolean basicBug(MapLocation target) throws GameActionException {
//        Direction dir = rc.getLocation().directionTo(target);
//        if (rc.getLocation().equals(target)) {
////            System.out.println("Bug reached target Dest");
//            return true;
//        } else {
//            if (bugDirection == null) {
//                bugDirection = dir.rotateRight();
//            }
//            for (int i = 0; i < 8; ++i) {
//                if (rc.canMove(bugDirection) && rc.sensePassability(rc.getLocation().add(bugDirection)) >= passabilityThreshold) {
//                    rc.move(bugDirection);
//                    return true;
//                }
//                bugDirection = bugDirection.rotateRight();
//            }
//            bugDirection = bugDirection.rotateLeft();
//        }
//        //if bug cannot move
//        return false;
//    }


    /**
     * Attempts to move to away from a given MapLocation
     *
     * @param location The intended MapLocation to move away from
     * @return true if a move was performed
     * @throws GameActionException
     */
    boolean moveAway(MapLocation location) throws GameActionException {

        Direction dirToLocation = rc.getLocation().directionTo(location);
        Direction dirAwayFromLocation = dirToLocation.opposite();

//        System.out.println("dirToLocation: " + dirToLocation + "dirAwayFromLoc : " + dirAwayFromLocation);

        if(rc.canMove(dirAwayFromLocation)) {

            rc.move(dirAwayFromLocation);
//            System.out.println("I AM MOVING    AWAY  ");
            return true;

        } else {

            if(goTo(dirAwayFromLocation)) {
//              System.out.println("I AM MOVING    AWAY  ");
                return true;
            } else {
//              System.out.println("COULD    NOT   MOVE    AWAY  !!!!");
                return false;
            }

        }
    }




}
