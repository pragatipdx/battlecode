package BrigPlayerV3;

import battlecode.common.*;

public class EnlightenmentCenter extends Robot{

    public EnlightenmentCenter(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        RobotType toBuild = randomSpawnableRobotType();
        int influence = 50;
        int currentInfluence = rc.getInfluence();

        //sense nearby enemy politicans
        int enemyPolCount = countEnemyPoliticians();

        //set an alert flag if enemy politicians are present
        if (enemyPolCount >= 1) {
            rc.setFlag(555);
        } else {
            rc.setFlag(0);
        }

        for (Direction dir : Util.directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
            } else {
                break;
            }
        }

        // Bidding - Check is votes is upper threshold and start bidding after building influence
        if (rc.getTeamVotes() < 751 && rc.getRoundNum()>50) {
            if (rc.getTeamVotes() / rc.getRoundNum() < 0.4) {
                if (rc.canBid((int) (0.1 * currentInfluence))) {
                    rc.bid((int) (0.1 * currentInfluence));
                    System.out.println("Bid " + (int) (0.1 * currentInfluence));
                }
            } else {
                if (rc.canBid((int) (0.06 * currentInfluence))) {
                    rc.bid((int) (0.06 * currentInfluence));
                    System.out.println("Bid " + (int) (0.06 * currentInfluence));
                }
            }
        }
    }



    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    public RobotType randomSpawnableRobotType() {
        return Util.spawnableRobot[(int) (Math.random() * Util.spawnableRobot.length)];
    }

    public void buildSpawnableRobot(int val,int influence) throws GameActionException
    {for(Direction dir: Util.directions)
    {if(rc.canBuildRobot(Util.spawnableRobot[val],dir,influence));
        {rc.buildRobot(Util.spawnableRobot[val],dir,influence);}}}


    //count number of enemy politicians in sensor radius
    public int countEnemyPoliticians() throws GameActionException {
        RobotInfo[] nearbyBots = rc.senseNearbyRobots();
        int count = 0;
        for (RobotInfo bot : nearbyBots) {
            if (bot.team == rc.getTeam().opponent() && bot.type == RobotType.POLITICIAN) {
                count += 1;
            }
        }
        return count;
    }

}
