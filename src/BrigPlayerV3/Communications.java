package BrigPlayerV3;
import battlecode.common.*;


public class Communications {


    static void sendLocation(RobotController rc) throws GameActionException {
        MapLocation location = rc.getLocation();
        int x = location.x, y = location.y;
        int encodedLocation = (x % 128) * 128 + (y % 128);
        if (rc.canSetFlag((encodedLocation))) {
            rc.setFlag(encodedLocation);
        }
    }


    static void sendLocationExtraInfo(RobotController rc, int extraInfo) throws GameActionException {
        MapLocation location = rc.getLocation();
        int x = location.x, y = location.y;
        int encodedLocationPlusInfo = (x % 128) * 128 + (y % 128) + extraInfo * 128 * 128;
        if (rc.canSetFlag((encodedLocationPlusInfo))) {
            rc.setFlag(encodedLocationPlusInfo);
        }
    }


    static MapLocation getLocationFromFlag(RobotController rc, int flag) {
        int y = flag % 128;
        int x = (flag / 128) % 128;
        int extraInformation = flag / 128 / 128;

        MapLocation currentLocation = rc.getLocation();
        int offsetX128 = currentLocation.x / 128;
        int offsetY128 = currentLocation.y / 128;
        MapLocation actualLocation = new MapLocation(offsetX128 * 128 + x, offsetY128 * 128 + y);

        MapLocation alternative = actualLocation.translate(-128, 0);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(128, 0);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(0, -128);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        alternative = actualLocation.translate(0, 128);
        if(rc.getLocation().distanceSquaredTo(alternative) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alternative;
        }
        return actualLocation;

    }


}
