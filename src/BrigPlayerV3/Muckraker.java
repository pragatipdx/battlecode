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

        //System.out.println("Turncount : " + turnCount);

        super.takeTurn();

        //System.out.println("Turncount after takeTurn : " + turnCount);

//        coolDownTurns = rc.getCooldownTurns();
//        System.out.printf("\ncooldownturns 1 :  " + coolDownTurns + "\n");

        if (rc.isReady()) {

            currentLocation = rc.getLocation();

//            System.out.printf("\ncooldownturns 2 :  " + coolDownTurns + "\n");

            //System.out.println("bytecode limit 1 : " + Clock.getBytecodeNum());

            RobotInfo[] robotInfosInSenseRadius = senseNearbyRobotsInSenseRadius();

            List<RobotInfo> nearbyNeutralECs = getNearbyNeutralEC(robotInfosInSenseRadius);

            nearbyEnemies = getNearbyEnemies(robotInfosInSenseRadius);


            //System.out.printf("cooldownturns AFTER EXPOSING :  " + coolDownTurns);

//        for(RobotInfo rb : robotInfosInSenseRadius) {
//            //System.out.println(rb);
//        }

            //System.out.println("bytecode limit 2 : " + Clock.getBytecodeNum());


//            if (!nearbyNeutralECs.isEmpty()) {
//                System.out.printf("\nFOUND NEUTRAL EC - SETTING FLAG at : " + nearbyNeutralECs.get(0).getLocation() +
//                        " \nDISTANCE : " + currentLocation.distanceSquaredTo(nearbyNeutralECs.get(0).getLocation()));
//            }


            populateEnemyLists();

            handleEnemySlanderer();

            handleEnemyEC();

            handleEnemyPoli();


//            if(!nearbyEnemyMuckraker.isEmpty()) {
//                for (RobotInfo robot : nearbyEnemyMuckraker) {
//                    System.out.println("\nFound Enemy MUCKRAKER - MOVING CLOSER DISTANCE: " + currentLocation.distanceSquaredTo(robot.getLocation()));
//                    nav.goTo(robot.location);
//                }
//            }

            nav.moveAway(hqLoc);
        }

    }



    public RobotInfo[] senseNearbyRobotsInSenseRadius() {
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

            clearPreexistingLists();

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
            return true;
        } else
            return false;
    }

    private void clearPreexistingLists() {
        nearbyEnemyMuckraker.clear();
        nearbyEnemyPolis.clear();
        nearbyEnemySlanderer.clear();
        nearbyEnemyECs.clear();
    }


//    Boolean exposeEnemy(RobotInfo[] robotInfos) throws GameActionException {
//        for (RobotInfo robot : robotInfos) {
//            if (robot.type.canBeExposed() && robot.team.equals(enemy)) { //&& COOLDOWNTURNS!
//                //System.out.println("\nCAN BE  ....  e x p o s e d\n");
//                //System.out.printf("cooldownturns :  " + coolDownTurns);
//                if (rc.canExpose(robot.location)) {
//                    //System.out.println("\ne x p o s e d\n");
//                    rc.expose(robot.location);
//                }
//            }
//        }
//        return true;
//    }


}
