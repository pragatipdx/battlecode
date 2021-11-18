package BrigPlayerV3;
import battlecode.common.*;


public class Unit extends Robot {

    Navigation nav;
    MapLocation hqLoc;
    Integer hqID;
    Team enemy;
    Team friend;

    public Unit(RobotController r) {
        super(r);
        nav = new Navigation(rc);
        enemy = rc.getTeam().opponent();
        friend = rc.getTeam();
    }


    public Unit(RobotController r, Team enemy, Team friend) {
        super(r);
        nav = new Navigation(rc);
        this.enemy = enemy;
        this.friend = friend;
    }


    public void takeTurn() throws GameActionException {
        super.takeTurn();

        findHQ();
    }

    public void findHQ() throws GameActionException {
        if (hqLoc == null) {
            // search surroundings for HQ
            RobotInfo[] robots = rc.senseNearbyRobots();
            for (RobotInfo robot : robots) {
                if (robot.type == RobotType.ENLIGHTENMENT_CENTER && robot.team == rc.getTeam()) {
                    hqLoc = robot.location;
                    hqID = robot.ID;
                }
            }
        }
    }





}
