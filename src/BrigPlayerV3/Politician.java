package BrigPlayerV3;

import battlecode.common.*;


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


        RobotInfo[] robotInfosInSenseRadius = EmpoweringRadiusBots();
        if(robotInfosInSenseRadius.length > 0){

            nearbyEnemies = politicianBotsList(robotInfosInSenseRadius);
            enemyBotsflagCouter();
            nearbyAlly = politicianToAction(robotInfosInSenseRadius);
            allyBotsFlagCounter();
        }

//        // Protect slanderer from enemy
       protectSlanderer();

//        // Empower if enemy and neutral within range
        politicianEmpower();
//
//        //Find lowest influence EC
        findWeekestInfluenceEC();

    }

    RobotInfo[] EmpoweringRadiusBots() {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(senseRadius);
        return robotInfos;
    }

    boolean enemyBotsflagCouter() {
        if(!nearbyEnemies.isEmpty()) {
            resetFlagCounter();
            fetchBots();
            return true;
        } else
            return false;
    }
    boolean allyBotsFlagCounter() {
        if(!nearbyAlly.isEmpty()) {
            resetFlagCounter();
            fetchBots();
            return true;
        } else
            return false;
    }
    List<RobotInfo> politicianBotsList(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyEnemies = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(enemy)) {
                nearbyEnemies.add(robot);
            }
        }
        return nearbyEnemies;
    }
    List<RobotInfo> politicianToAction(RobotInfo[] robotInfos) {
        List<RobotInfo> nearbyAlly = new ArrayList<>();
        for (RobotInfo robot : robotInfos) {
            if (robot.team.equals(friend)) {
                nearbyAlly.add(robot);
            }
        }
        return nearbyAlly;
    }


     boolean fetchBots() {
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
        return true;
    }

    boolean resetFlagCounter() {
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

    boolean findWeekestInfluenceEC() {
        RobotInfo weak_Health_EC=null;
        int weak_influence = (int)(Double.MAX_VALUE);
        if(!nearbyEnemies.isEmpty())
        {
            for(RobotInfo troop : nearbyEnemies)
            {    if (troop.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                if (troop.getInfluence() < weak_influence) {
                    weak_Health_EC = troop;
                    weak_influence = troop.getInfluence();

                }
            }
            }
        }

        return true;
    }



}
