import processing.core.*;
import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

public class Spring {
    public MouseJoint mouseJoint;
    Ball binded;

    public Spring() {
        mouseJoint = null;
    }

    public void update() {
        if (mouseJoint != null) {
            Vec2 mouseWorld = Main.box2d.coordPixelsToWorld(Main.instance.mouseX, Main.instance.mouseY);
            mouseJoint.setTarget(mouseWorld);
        }
    }

    public void display() {
        Main pa=Main.instance;
        Vec2 mousePos = new Vec2(0, 0);
        mouseJoint.getAnchorA(mousePos);
        Vec2 jointPos = new Vec2(0, 0);
        mouseJoint.getAnchorB(jointPos);
        mousePos = Main.box2d.coordWorldToPixels(mousePos);
        jointPos = Main.box2d.coordWorldToPixels(jointPos);
        pa.fill(255, 0, 0);
        pa.noStroke();
        pa.ellipse(jointPos.x, jointPos.y, binded.r / 20, binded.r / 20);
        pa.stroke(255, 0, 0);
        pa.strokeWeight(3);
        pa.line(mousePos.x, mousePos.y, jointPos.x, jointPos.y);
    }

    public void bind(float x, float y, Ball ball) {
        binded=ball;
        MouseJointDef md = new MouseJointDef();
        md.bodyA = Main.box2d.getGroundBody();
        md.bodyB = ball.body;
        Vec2 mp = Main.box2d.coordPixelsToWorld(x, y);
        md.target.set(mp);
        md.maxForce = 1000.0f * ball.body.m_mass;
        md.frequencyHz = 1.0f;
        md.dampingRatio = 0.9f;
        md.collideConnected = true;
        mouseJoint = (MouseJoint) Main.box2d.world.createJoint(md);
    }

    public void destroy() {
        if (mouseJoint != null) {
            Main.box2d.world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
    }
}