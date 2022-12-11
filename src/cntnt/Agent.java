package cntnt;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Agent {

    double angle;
    double[] pos;
    double movementSpeed;

    Node rectangle;

    public Agent(double angle, double[] pos, double movementSpeed) {
        this.angle = angle;
        this.pos = pos;
        this.movementSpeed = movementSpeed;
        rectangle = new Rectangle(0, 0, 4,4);
        ((Rectangle)rectangle).setFill(Color.GREEN);
    }


    double getX(){
        return pos[0];
    }
    double getY(){
        return pos[1];
    }

}
