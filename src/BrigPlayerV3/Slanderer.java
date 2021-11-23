package BrigPlayerV3;

import battlecode.common.*;

public class Slanderer extends Unit {

    public Slanderer(RobotController r) {
        super(r);
    }

    public Slanderer(RobotController r, Team enemy, Team friend) {
        super(r, enemy, friend);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        MapLocation location = rc.getLocation();
        runFromEnemy(location);
        stayCLoseToHome(location);

        if (nav.tryMove(Utility.randomDirection()))
            System.out.println("I moved!");
    }

    public boolean runFromEnemy(MapLocation avoid) throws GameActionException {
        final int senseRadius = 20;
        try{
        Team enemy = rc.getTeam().opponent();
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (robot.getType() == RobotType.MUCKRAKER) {
                MapLocation Mlocate = robot.getLocation();
                if (nav.moveAway(Mlocate)) {
                    System.out.println("I ranAway!");
                }
            }
        }

    }
        catch(NullPointerException e){
            System.out.println("NullPointerException thrown!");

    }
        return true;
    }


    public boolean stayCLoseToHome(MapLocation avoid) throws GameActionException {
        Team ally = rc.getTeam();
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


        return true;
    }}