package BrigPlayerV3;
import battlecode.common.*;

public class Robot {
    RobotController rc;
    Communications comms;

    int turnCount = 0;

    public Robot(RobotController r) {
        this.rc = r;
        comms = new Communications(rc);
    }

    public void takeTurn() throws GameActionException {
        turnCount += 1;
    }

}
