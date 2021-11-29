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

        if(!nearbyEC.isEmpty()) {
            for (RobotInfo robot : nearbyEC) {
                System.out.println("\nFound Friend EC - MOVING CLOSER    DISTANCE: ");
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

    Boolean exposeEnemy(RobotInfo robot) throws GameActionException {
        if (rc.canExpose(robot.location)) {
            System.out.println("\ne x p o s e d\n");
            rc.expose(robot.location);
            return true;
        } else {
            return false;
        }
    }
}