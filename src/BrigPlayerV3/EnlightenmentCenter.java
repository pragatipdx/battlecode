package BrigPlayerV3;

import battlecode.common.*;

public class EnlightenmentCenter extends Robot{

    public EnlightenmentCenter(RobotController r) {
        super(r);
    }


    public void takeTurn() throws GameActionException {
        RobotType toBuild = nextBuild();
        int influence = invest(toBuild);

        //sense nearby enemy politicans
        int enemyPolCount = countEnemyPoliticians();

        //set an alert flag if enemy politicians are present

        startBuild(toBuild, influence);

        // Bidding - Check is votes is upper threshold and start bidding after building influence
             startbidding();
    }

    public void startbidding() throws GameActionException {
        int currentInfluence = rc.getInfluence();
        if (rc.getTeamVotes() < 751 && rc.getRoundNum()>50) {
            if (rc.getTeamVotes() / rc.getRoundNum() < 0.4) {
             bidless(currentInfluence);
            } else {
               bidmore(currentInfluence);
            }
        }
    }

    public void bidmore(int currentInfluence) throws GameActionException {
        if (rc.canBid((int) (0.06 * currentInfluence))) {
            rc.bid((int) (0.06 * currentInfluence));
            System.out.println("Bid " + (int) (0.06 * currentInfluence));
        }
    }

    public void bidless(int currentInfluence) throws GameActionException {
        if (rc.canBid((int) (0.1 * currentInfluence))) {
            rc.bid((int) (0.1 * currentInfluence));
            System.out.println("Bid " + (int) (0.1 * currentInfluence));
        }
    }


    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    public RobotType randomSpawnableRobotType() {
        return Utility.spawnableRobot[(int) (Math.random() * Utility.spawnableRobot.length)];
    }

    public void buildSpawnableRobot(int val,int influence) throws GameActionException
    {for(Direction dir: Utility.directions)
    {if(rc.canBuildRobot(Utility.spawnableRobot[val],dir,influence));
        {rc.buildRobot(Utility.spawnableRobot[val],dir,influence);}}}


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

    //returns the next robot to be built based on round
    public RobotType nextBuild() {
        int currentRound = rc.getRoundNum();
        int cycle = currentRound % 10;
        switch (cycle) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: return RobotType.SLANDERER;
            case 7: return RobotType.MUCKRAKER;
            case 8: //prioritize slanderers early game
            case 9: if(currentRound < 300) return RobotType.SLANDERER;
        }
        return RobotType.POLITICIAN;
    }

    //determines how much influence to invest in a robot based on its type
    public int invest(RobotType toBuild) {
        switch (toBuild) {
            case MUCKRAKER: return 5;
            case SLANDERER: if (rc.getRoundNum() > 400) return 150;
            case POLITICIAN:
        }
        return 50;
    }

    public void startBuild(RobotType toBuild, int influence) throws GameActionException {
        for (Direction dir : Utility.directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
                System.out.println(toBuild.name());
            } else {
                break;
            }
        }
    }

}
