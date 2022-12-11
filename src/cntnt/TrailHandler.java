package cntnt;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cntnt.Controller.trails;

public class TrailHandler {

    private final double VISIBLE_TRAILS_KEY_OFFSET = getTrailMapKeyOffset();
    private final Map<Double, int[]> visibleTrails = new HashMap<>();
    private final List<int[]> toDelete = new ArrayList<>();
    Group visible = new Group();


    void initTrails() {

        for (int i = 0; i < trails.length; i++) {

            for (int j = 0; j < trails[0].length; j++) {

                trails[i][j] = new Trail(new int[] {i * Main.AGENT_SIZE, j * Main.AGENT_SIZE}, 0);
                visible.getChildren().add(trails[i][j].node);

            }

        }

    }

    void addTrail(double[] newPos, double angle) {

        int[] newPosToTrailMap = new int[] {(int) (newPos[0] / Main.AGENT_SIZE), (int) (newPos[1] / Main.AGENT_SIZE)};

        if (newPosToTrailMap[0] < 0 || newPosToTrailMap[0] > Main.WINDOW_WIDTH || newPosToTrailMap[1] < 0 || newPosToTrailMap[1] > Main.WINDOW_HEIGHT) {
            return;
        }

        blur(newPosToTrailMap, angle);
    }

    void updateTrails(long elapsedTime) {

        for (int[] t : visibleTrails.values()) {
            trails[t[0]][t[1]].update(elapsedTime);

            if(trails[t[0]][t[1]].live == 0){
                toDelete.add(t);
                trails[t[0]][t[1]].angle = 0;
                trails[t[0]][t[1]].layers = 1;
            }
        }

        toDelete.stream().map(t -> t[0] * VISIBLE_TRAILS_KEY_OFFSET + t[1]).forEach(visibleTrails::remove);

        toDelete.clear();

    }

    void decideSenseTrail(Agent agent) {



        agent.angle = sense(agent, 0, Main.AGENT_SIZE * 3);

    }

    double sense(Agent agent, double offset, double sensorDist) {

        double sensorAngle = agent.angle + offset;
        double[] sensorDir = new double[] {Math.cos(sensorAngle), Math.sin(sensorAngle)};

        double[] sensorCenter = new double[] {agent.getX() + sensorDir[0] * sensorDist, agent.getY() + sensorDir[1] * sensorDist};


        if ((int) sensorCenter[0] / Main.AGENT_SIZE > trails.length - 1 || (int) sensorCenter[1] / Main.AGENT_SIZE > trails[0].length - 1 || (int) sensorCenter[0] / Main.AGENT_SIZE < 0 || (int) sensorCenter[1] / Main.AGENT_SIZE < 0) {
            return Math.random() * 2 * Math.PI;
        }

//        ((Rectangle)trails[(int)sensorCenter[0]/Main.AGENT_SIZE][(int)sensorCenter[1]/Main.AGENT_SIZE].node).setFill(Color.GREEN);
//        trails[(int)sensorCenter[0]/Main.AGENT_SIZE][(int)sensorCenter[1]/Main.AGENT_SIZE].node.setOpacity(1);


        if(trails[(int) sensorCenter[0] / Main.AGENT_SIZE][(int) sensorCenter[1] / Main.AGENT_SIZE].node.getOpacity() > 0)
            return trails[(int) sensorCenter[0] / Main.AGENT_SIZE][(int) sensorCenter[1] / Main.AGENT_SIZE].angle + ((Math.random() - 0.5) * Main.TRAIL_OFFSET);
        else
            return agent.angle;
    }



    void blur(int[] newPosToTrailMap, double angle) {

        Trail trail = trails[newPosToTrailMap[0]][newPosToTrailMap[1]];

        trail.live = Main.TRAIL_DURATION;
        visibleTrails.put(newPosToTrailMap[0] * VISIBLE_TRAILS_KEY_OFFSET + newPosToTrailMap[1], new int[] {newPosToTrailMap[0], newPosToTrailMap[1]});
        trail.angle = angle;
        trail.layers++;



    }


    double getTrailMapKeyOffset() {
        return Math.pow(10, String.valueOf(Main.WINDOW_WIDTH).length() - 1);
    }

}
