package BrigPlayerV3;

import battlecode.common.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class Politician extends Unit {


    public Politician(RobotController r) {
        super(r);
    }

    public Politician(RobotController r, Team enemy, Team friend) {
        super(r, enemy, friend);
    }

    public void takeTurn() throws GameActionException {

        if (nav.tryMove(Utility.randomDirection()))
            System.out.println("I moved!");

        int actionRadius = rc.getType().actionRadiusSquared;
        // Protect slanderer from enemy
        protectSlanderer(actionRadius);

        // Empower if enemy and neutral within range
        politicianEmpower(actionRadius);

        //Find lowest influence EC
        findWeekestInfluenceEC();

    }

    Boolean protectSlanderer(int actionRadius) throws GameActionException {
        Team ally = rc.getTeam();
        for (RobotInfo robotA : rc.senseNearbyRobots(actionRadius, ally)) {
            if (robotA.getType() == RobotType.SLANDERER) {
                MapLocation Mlocate = robotA.getLocation();
                Direction Dlocate = robotA.getLocation().directionTo(Mlocate);
                if (rc.canMove(Dlocate)) {
                    if (nav.tryMove(Dlocate)) {
                        rc.move(Dlocate);
                        System.out.println("!!!!!!!!!I am moving towards " + Dlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(Dlocate));
                    }

                }
            }
        }
        return true;
    }

    boolean findWeekestInfluenceEC() {
        int senseRadius = rc.getType().sensorRadiusSquared;
        RobotInfo weak_Health_EC=null;
        int weak_influence = (int)(Double.MAX_VALUE);

        for (RobotInfo troop : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (troop.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                if (troop.getInfluence() < weak_influence) {
                    weak_Health_EC = troop;
                    weak_influence = troop.getInfluence();

                }
            }
        }
        return true;
    }


    Boolean politicianEmpower(int actionRadius) throws GameActionException
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

}
