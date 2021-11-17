package BrigPlayerV3;

import battlecode.common.*;

public class Politician extends Unit {


    public Politician(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {

        RobotInfo weak_Health_EC=null;
        int weak_influence = (int)(Double.MAX_VALUE);
        Team enemy = rc.getTeam().opponent();
        Team ally = rc.getTeam();

        if (nav.tryMove(Util.randomDirection()))
            System.out.println("I moved!");

        int actionRadius = rc.getType().actionRadiusSquared;
        int senseRadius = rc.getType().sensorRadiusSquared;

        // Protect slanderer from enemy

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



    public boolean politicianEmpower(int actionRadius) throws GameActionException
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
