package BrigPlayerV3;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.List;

public class Muckraker extends Unit {

    int actionRadius = 12;
    int senseRadius = 30;
    int detectionRadius = 40;

    MapLocation currentLocation;

    List<RobotInfo> nearbyEnemies = new ArrayList<>();
    List<RobotInfo> nearbyEnemyECs = new ArrayList<>();
    List<RobotInfo> nearbyEnemySlanderer = new ArrayList<>();
    List<RobotInfo> nearbyEnemyMuckraker = new ArrayList<>();
    List<RobotInfo> nearbyEnemyPolis = new ArrayList<>();

    Boolean flag = false;

    double coolDownTurns;

    public Muckraker(RobotController r) {
        super(r);
    }

    public Muckraker(RobotController r, Team enemy, Team friend) {
        super(r, enemy, friend);
    }


    public void takeTurn() throws GameActionException {

        super.takeTurn();

        if (rc.isReady()) {

            currentLocation = rc.getLocation();

            RobotInfo[] robotInfosInSenseRadius = senseNearbyRobotsInSenseRadius();

            nearbyEnemies.clear();

            clearPreexistingLists();

            if(robotInfosInSenseRadius.length > 0){

                List<RobotInfo> nearbyNeutralECs = getNearbyNeutralEC(robotInfosInSenseRadius);

                if(!nearbyNeutralECs.isEmpty()){
                    setFlagToNeutralECLoc();

                }

                nearbyEnemies = getNearbyEnemies(robotInfosInSenseRadius);

                populateEnemyLists();
            }

            handleEnemySlanderer();

            handleEnemyEC();

            handleEnemyPoli();

            move();

        }

    }

    void setFlagToNeutralECLoc() throws GameActionException {
        if(comms.sendLocation(rc)) {
            System.out.println("Set Flag to THIS LOCATION if NEUTRAL EC");
        } else {
            System.out.println("FAILED TO SET FLAG TO NEUTRAL EC LOCATION");
        }
    }


    void move() throws GameActionException {
        if (turnCount%2==0) {
            System.out.printf("Trying to move away from HOME");
            if(nav.moveAway(hqLoc))
                System.out.printf("MOVED away from HOME");
        }
        else{
            System.out.printf("Trying to move RANDOMLY");
            nav.goTo(Utility.randomDirection());
            System.out.printf("MOVED RANDOMLY");
        }

    }


    RobotInfo[] senseNearbyRobotsInSenseRadius() {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(senseRadius);
        return robotInfos;
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


    List<RobotInfo> getNearbyEnemies(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyEnemies = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(enemy)) {
                nearbyEnemies.add(robot);
            }
        }
        return nearbyEnemies;
    }


    List<RobotInfo> getNearbyNeutralEC(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyNeutralECs = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(Team.NEUTRAL) && robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                nearbyNeutralECs.add(robot);
            }
        }
        return nearbyNeutralECs;
    }


    Boolean handleEnemyPoli() throws GameActionException {
        if(!nearbyEnemyPolis.isEmpty()) {
            for (RobotInfo robot : nearbyEnemyPolis) {
                System.out.println("\nFound Enemy POLITICIAN!!!!! -   MOVING   AWAY!!!!    DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
                nav.moveAway(robot.location);
            }
            return true;
        }
        return false;
    }


    Boolean handleEnemyEC() throws GameActionException {
        if(!nearbyEnemyECs.isEmpty()) {
            for (RobotInfo robot : nearbyEnemyECs) {
                System.out.println("\nFound Enemy EC - MOVING CLOSER    DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
                nav.goTo(robot.location);
            }
            return true;
        } else
            return false;
    }


    Boolean handleEnemySlanderer() throws GameActionException {
        if(!nearbyEnemySlanderer.isEmpty()) {
            for (RobotInfo robot : nearbyEnemySlanderer) {
                System.out.println("\nFound Enemy SLANDERER - MOVING CLOSER   DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
                exposeEnemy(robot);
                nav.goTo(robot.location);
            }
            return true;
        } else
            return false;
    }


    Boolean populateEnemyLists() {
        if(!nearbyEnemies.isEmpty()) {
            addRobotsToLists();
            return true;
        } else
            return false;
    }

    private void addRobotsToLists() {
        for (RobotInfo robot : nearbyEnemies) {
            if (robot.type.equals(RobotType.ENLIGHTENMENT_CENTER)) {
                nearbyEnemyECs.add(robot);
            }
            if (robot.type.equals(RobotType.SLANDERER)) {
                nearbyEnemySlanderer.add(robot);
            }
            if (robot.type.equals(RobotType.MUCKRAKER)) {
                nearbyEnemyMuckraker.add(robot);
            }
            if (robot.type.equals(RobotType.POLITICIAN)) {
                nearbyEnemyPolis.add(robot);
            }
        }
    }

    Boolean clearPreexistingLists() {
        nearbyEnemyMuckraker.clear();
        nearbyEnemyPolis.clear();
        nearbyEnemySlanderer.clear();
        nearbyEnemyECs.clear();
        return true;
    }


}
