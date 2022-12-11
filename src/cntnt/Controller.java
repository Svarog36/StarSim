package cntnt;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    long timeTickEnd, timeTickStart, now = System.currentTimeMillis(), timeWithinTickStart, timeWithinTickEnd;

    TrailHandler th = new TrailHandler();

    private final List<Agent> agents = new ArrayList<>();
    final static Trail[][] trails = new Trail[Main.WINDOW_WIDTH / Main.AGENT_SIZE][Main.WINDOW_HEIGHT / Main.AGENT_SIZE];

    Group agentPositions = new Group();

    Controller() {
//        Inward circle
//        for (int i = 0; i < Main.AGENT_NUMBER; i++){
//            double angle = Math.random() * 2 * Math.PI;
//            Agent agent = new Agent(angle, new double[] {Main.WINDOW_WIDTH / 2. + Math.cos(angle + Math.PI) * 200 * Math.random()*2, Main.WINDOW_HEIGHT / 2. + Math.sin(angle + Math.PI) * 200 * Math.random()*2}, Main.AGENT_SPEED);
//            agents.add(agent);
//            agentPositions.getChildren().add(agent.rectangle);
//        }

        //Center explosion
//        for (int i = 0; i < Main.AGENT_NUMBER; i++)
//            agents.add(new Agent(Math.random() * 2 * Math.PI, new double[] {Main.WINDOW_WIDTH / 2.0, Main.WINDOW_HEIGHT / 2.0}, Main.AGENT_SPEED));

//        Firework explosion
        for (int i = 0; i < Main.AGENT_NUMBER; i++)
            agents.add(new Agent(0, new double[] {Main.WINDOW_WIDTH / 2.0, Main.WINDOW_HEIGHT / 2.0}, Main.AGENT_SPEED));

        //Collision
//        for (int i = 0; i < Main.AGENT_NUMBER/2; i++)
//            agents.add(new Agent(0, new double[] {Main.WINDOW_WIDTH * 0.2, Main.WINDOW_HEIGHT / 2.0}, Main.AGENT_SPEED));
//
//        for (int i = 0; i < Main.AGENT_NUMBER/2; i++)
//            agents.add(new Agent(Math.PI, new double[] {Main.WINDOW_WIDTH * 0.8, Main.WINDOW_HEIGHT / 2.0 + Main.AGENT_SIZE * 3}, Main.AGENT_SPEED));


        th.initTrails();

        ticker.setCycleCount(Animation.INDEFINITE);

        ticker.play();

    }

    Timeline ticker = new Timeline(new KeyFrame(Duration.millis(Main.TICK_TIME), event -> {

        timeTickStart = System.currentTimeMillis();

        if (timeTickEnd != 0) {

            tick(timeTickStart - timeTickEnd);
        }

        timeTickEnd = System.currentTimeMillis();

    }));


    void tick(long elapsedTime) {

        timeWithinTickStart = System.currentTimeMillis();

        updateAgents(elapsedTime);

        th.updateTrails(elapsedTime);

        timeWithinTickEnd = System.currentTimeMillis();

        if (System.currentTimeMillis() - now > 1000) {
            System.out.println("Time for tick:" + (timeWithinTickEnd - timeWithinTickStart) + "/" + Main.TICK_TIME);
            now = System.currentTimeMillis();
        }


    }


    void updateAgents(long elapsedTime) {

        for (Agent agent : agents) {

            double[] direction = new double[] {Math.cos(agent.angle), Math.sin(agent.angle)};
            double[] newPos = new double[] {(agent.getX() + direction[0] * agent.movementSpeed * elapsedTime), (agent.getY() + direction[1] * agent.movementSpeed * elapsedTime)};

            th.decideSenseTrail(agent);

            if (newPos[0] < 0 || newPos[0] + Main.AGENT_SIZE > Main.WINDOW_WIDTH || newPos[1] < 0 || newPos[1] + Main.AGENT_SIZE > Main.WINDOW_HEIGHT) {


                if (newPos[0] < 0) {
                    newPos[0] = 0;
                } else if (newPos[0] + Main.AGENT_SIZE > Main.WINDOW_WIDTH) {
                    newPos[0] = Main.WINDOW_WIDTH - Main.AGENT_SIZE;
                }

                if (newPos[1] < 0) {
                    newPos[1] = 0;
                } else if (newPos[1] + Main.AGENT_SIZE > Main.WINDOW_HEIGHT) {
                    newPos[1] = Main.WINDOW_HEIGHT - Main.AGENT_SIZE;
                }

                agent.angle = Math.random() * 2 * Math.PI;

            }

            agent.pos = newPos;
            th.addTrail(agent.pos, agent.angle);

            agent.rectangle.setLayoutX(agent.pos[0]);
            agent.rectangle.setLayoutY(agent.pos[1]);

        }
    }

}
