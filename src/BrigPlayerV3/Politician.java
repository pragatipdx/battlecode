package BrigPlayerV3;

import battlecode.common.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

public class Politician extends Unit {
    List<RobotInfo> nearbyEnemies = new ArrayList<>();
    List<RobotInfo> nearbyAlly = new ArrayList<>();
    List<RobotInfo> nearbyFriendlySlanderer = new ArrayList<>();
    List<RobotInfo> nearbyEnemyEC=new ArrayList<>();
    int senseRadius = 10;
    int actionRadius=10;

    public Politician(RobotController r) {
        super(r);
    }

    public Politician(RobotController r, Team enemy, Team friend)
    {
        super(r, enemy, friend);
    }

    public void takeTurn() throws GameActionException {

        super.takeTurn();
        if (nav.tryMove(Utility.randomDirection()))
            System.out.println("I moved!");


        RobotInfo[] robotInfosInSenseRadius = senseNearbyRobotsInSenseRadius();
        if(robotInfosInSenseRadius.length > 0){

            nearbyEnemies = getNearbyEnemies(robotInfosInSenseRadius);
            populateEnemyLists();
            nearbyAlly = getNearbyAlly(robotInfosInSenseRadius);
            populateAllyLists();
        }

//        // Protect slanderer from enemy
       protectSlanderer();

//        // Empower if enemy and neutral within range
        politicianEmpower();
//
//        //Find lowest influence EC
//        findWeekestInfluenceEC();

    }

    RobotInfo[] senseNearbyRobotsInSenseRadius() {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(senseRadius);
        return robotInfos;
    }

    boolean populateEnemyLists() {
        if(!nearbyEnemies.isEmpty()) {
            clearPreexistingLists();
            addRobotsToLists();
            return true;
        } else
            return false;
    }
    boolean populateAllyLists() {
        if(!nearbyAlly.isEmpty()) {
            clearPreexistingLists();
            addRobotsToLists();
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
            if (robot.type.equals(RobotType.SLANDERER)) {
                nearbyFriendlySlanderer.add(robot);
            }
        }
        for(RobotInfo robot : nearbyEnemies)
        {
            if(robot.type.equals(RobotType.ENLIGHTENMENT_CENTER))
            {
                nearbyEnemyEC.add(robot);
            }
        }
    }
    boolean clearPreexistingLists() {
        nearbyEnemyEC.clear();
        nearbyFriendlySlanderer.clear();
        return true;
    }

    boolean politicianEmpower() throws GameActionException {
        if (!nearbyEnemies.isEmpty()) {
            if (rc.canEmpower(actionRadius)) {
                System.out.println("\ne m p o w e r i n g");
                rc.empower(actionRadius);

            }
            }
        return true;
        }


    boolean protectSlanderer() throws GameActionException {
        if(!nearbyFriendlySlanderer.isEmpty()) {
            for (RobotInfo robot : nearbyFriendlySlanderer) {
                nav.goTo(robot.location);
            }
            return true;
        } else
            return false;

    }

//    boolean findWeekestInfluenceEC() {
//        int senseRadius = rc.getType().sensorRadiusSquared;
//        RobotInfo weak_Health_EC=null;
//        int weak_influence = (int)(Double.MAX_VALUE);
//
//        for (RobotInfo troop : rc.senseNearbyRobots(senseRadius, enemy)) {
//            if (troop.getType() == RobotType.ENLIGHTENMENT_CENTER) {
//                if (troop.getInfluence() < weak_influence) {
//                    weak_Health_EC = troop;
//                    weak_influence = troop.getInfluence();
//
//                }
//            }
//        }
//        return true;
//    }



}
