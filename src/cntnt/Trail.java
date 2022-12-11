package cntnt;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Trail {

    double live = 0;
    Node node;
    double angle;
    double layers = 1;


    public Trail(int[] pos, double angle){
        this.angle = angle;

        init(pos);
    }

    void init(int[] pos){
        node = new Rectangle(pos[0],pos[1], Main.AGENT_SIZE, Main.AGENT_SIZE);
        ((Rectangle)node).setFill(Color.rgb(0,0,0));

        node.setOpacity(0);
    }

    void update(long elapsedTime){

        if(live > 0) {
            live -= (double) elapsedTime / 1000;


            if (live < 0) {
                live = 0;
                node.setOpacity(0);
                return;
            }

            node.setOpacity(live / Main.TRAIL_DURATION);
            layers = node.getOpacity() * layers;
            ((Rectangle)node).setFill(Color.rgb((int)(200 * Math.pow(0.99, layers)),(int)(190 * Math.pow(0.9, layers)),(int)(190 * Math.pow(0.8, layers))));
            
        }

    }



}
