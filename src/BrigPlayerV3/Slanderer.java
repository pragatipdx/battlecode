package BrigPlayerV3;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.List;

public class Slanderer extends Unit {
    List<RobotInfo> nearbyEnemyMuckraker = new ArrayList<>();
    List<RobotInfo> nearbyEC = new ArrayList<>();
    List<RobotInfo> nearbyEnemies = new ArrayList<>();
    List<RobotInfo> nearbyAlly = new ArrayList<>();
    int senseRadius = 30;
    MapLocation currentLocation;
    public Slanderer(RobotController r) {
        super(r);
    }

    public Slanderer(RobotController r, Team enemy, Team friend) {
        super(r, enemy, friend);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        MapLocation location = rc.getLocation();
        RobotInfo[] robotInfosInSenseRadius = senseNearbyRobotsInSenseRadius();
        if(robotInfosInSenseRadius.length > 0){

            nearbyEnemies = getNearbyEnemies(robotInfosInSenseRadius);
            populateEnemyLists();
            nearbyAlly = getNearbyAlly(robotInfosInSenseRadius);
            populateAllyLists();
        }
        runFromEnemy();
        stayCLoseToHome();

        if (nav.tryMove(Utility.randomDirection()))
            System.out.println("I moved!");
    }
    RobotInfo[] senseNearbyRobotsInSenseRadius() {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(senseRadius);
        return robotInfos;
    }
    Boolean populateEnemyLists() {
        if(!nearbyEnemies.isEmpty()) {
            clearPreexistingLists();
            addRobotsToLists();
            return true;
        } else
            return false;
    }
    Boolean populateAllyLists() {
        if(!nearbyAlly.isEmpty()) {
            clearPreexistingLists();
            addRobotsToLists();
            return true;
        } else
            return false;
    }

    public boolean runFromEnemy() throws GameActionException {
        /*try{

        Team enemy = rc.getTeam().opponent();
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (robot.getType() == RobotType.MUCKRAKER) {
                MapLocation Mlocate = robot.getLocation();
                if (nav.moveAway(Mlocate)) {
                    System.out.println("I ranAway!");
                }
            }
        }
    //}
       /* catch(NullPointerException e){
            System.out.println("NullPointerException thrown!");

    }
        return true;*/
        if(!nearbyEnemyMuckraker.isEmpty()) {
            for (RobotInfo robot : nearbyEnemyMuckraker) {
                System.out.println("\nFound Enemy MUCKRACKER!!!!! -   MOVING   AWAY!!!!    DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
                nav.moveAway(robot.location);
            }
            return true;
        }
        return false;

    }


    public boolean stayCLoseToHome() throws GameActionException {
        /*Team ally = rc.getTeam();
        int actionRadiusA = rc.getType().actionRadiusSquared;
        try {
        for (RobotInfo robotA : rc.senseNearbyRobots(actionRadiusA, ally)) {
                if (robotA.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    MapLocation Mlocate = robotA.getLocation();
                    Direction Dlocate = robotA.getLocation().directionTo(Mlocate);
                    if (rc.canMove(Dlocate)) {
                        if (nav.tryMove(Dlocate)) {
                            rc.move(Dlocate);
                            System.out.println("!!!!!!!!!I am moving towards " + Dlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(Dlocate));
                        }

                    }
                }
            } }catch (NullPointerException e) {
                System.out.println("NullPointerException thrown!");
            }


        return true; */
        if(!nearbyEC.isEmpty()) {
            for (RobotInfo robot : nearbyEC) {
                System.out.println("\nFound Friend EC - MOVING CLOSER    DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
                nav.goTo(robot.location);
            }
            return true;
        } else
            return false;
    }
    List<RobotInfo> getNearbyEnemies(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyEnemies = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(enemy)) {
                nearbyEnemies.add(robot);
            }
        }
        return nearbyEnemies;
    }
    List<RobotInfo> getNearbyAlly(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyAlly = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(friend)) {
                nearbyAlly.add(robot);
            }
        }
        return nearbyAlly;
    }

    private void addRobotsToLists() {
        for (RobotInfo robot : nearbyAlly) {
            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                nearbyEC.add(robot);
            }
            if (robot.type.equals(RobotType.MUCKRAKER)) {
                nearbyEnemyMuckraker.add(robot);
            }
        }
    }
    Boolean clearPreexistingLists() {
        nearbyEnemyMuckraker.clear();
        nearbyEC.clear();
        return true;
    }
}