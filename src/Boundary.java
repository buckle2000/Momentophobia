import g4p_controls.*;
import processing.core.*;
import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.*;
import shiffman.box2d.*;

import javax.swing.*;
import java.util.*;

public class Boundary {

    // A boundary is a simple rectangle with x,y,width,and height
    float x;
    float y;
    float w;
    float h;
    // But we also have to make a body for box2d to know about it
    Body b;

    Boundary(float x1, float y1, float x2, float y2) {
        x = (x1+x2)/2;
        y = (y1+y2)/2;
        w = x2-x1;
        h = y2-y1;

        // Define the polygon
        PolygonShape sd = new PolygonShape();
        // Figure out the Main.box2d coordinates
        float box2dW = Main.box2d.scalarPixelsToWorld(w);
        float box2dH = Main.box2d.scalarPixelsToWorld(h);
        // We're just a box
        sd.setAsBox(box2dW, box2dH);


        // Create the body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Main.box2d.coordPixelsToWorld(x, y));
        b = Main.box2d.createBody(bd);

        // Attached the shape to the body using a Fixture
        b.createFixture(sd, 1);
    }

    // Draw the boundary, if it were at an angle we'd have to do something fancier
    void draw() {
        Main pa = Main.instance;
        pa.noFill();
        pa.stroke(0);
        pa.strokeWeight(3);
        pa.rectMode(PConstants.CENTER);

        float a = b.getAngle();

        pa.pushMatrix();
        pa.translate(x, y);
        pa.rotate(-a);
        pa.rect(0, 0, w, h);
        pa.popMatrix();
    }
}