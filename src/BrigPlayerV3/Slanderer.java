package BrigPlayerV3;

import battlecode.common.*;

public class Slanderer extends Unit {


    public Slanderer(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        if (nav.tryMove(Util.randomDirection()))
            System.out.println("I moved!");

        Team enemy = rc.getTeam().opponent();


        final int senseRadius = 20;
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (robot.getType() == RobotType.MUCKRAKER) {
                MapLocation Mlocate = robot.getLocation();
                if (nav.runAwayFromMuck(Mlocate)) {
                    System.out.println("I ranAway!");
                }
            }
        }
        Team ally = rc.getTeam();
        int actionRadiusA = rc.getType().actionRadiusSquared;
        for (RobotInfo robotA : rc.senseNearbyRobots(actionRadiusA, ally)){
            if(robotA.getType() == RobotType.ENLIGHTENMENT_CENTER){
                MapLocation Mlocate = robotA.getLocation();
                Direction Dlocate = robotA.getLocation().directionTo(Mlocate);
                if(rc.canMove(Dlocate)){
                    if (nav.tryMove(Dlocate)){
                        rc.move(Dlocate);
                        System.out.println("!!!!!!!!!I am moving towards " + Dlocate + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(Dlocate));
                    }

                }
            }
        }
    }


}
