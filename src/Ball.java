import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.*;

class Ball {
    // We need to keep track of a Body and a width and height
    Body body;
    float r;
    String info = "";
    int lastupdate = -200;

    // Constructor
    Ball(float x, float y, float r_) {
        r = r_;
        makeBody(new Vec2(x, y), r);
    }

    // This function removes the particle from the Main.box2d world
    void killBody() {
        Main.box2d.destroyBody(body);
    }

    boolean contains(float x, float y) {
        Vec2 worldPoint = Main.box2d.coordPixelsToWorld(x, y);
        Fixture f = body.getFixtureList();
        boolean inside = f.testPoint(worldPoint);
        return inside;
    }

    static final float SOMERATIO = 0.2f;

    // Drawing the ball
    void draw(boolean usepicture) {
        Main pa = Main.instance;
        // We look at each body and get its screen position
        Vec2 pos = Main.box2d.getBodyPixelCoord(body);
        // Get its angle of rotation
        float a = body.getAngle();
        pa.pushMatrix();
        pa.translate(pos.x, pos.y);
        if (!usepicture) {
            pa.ellipseMode(PConstants.RADIUS);
            pa.strokeWeight(2);
            if (body.isAwake()) {
                pa.fill(255, 242, 0);
                pa.stroke(255, 128, 0);
            } else {
                pa.fill(204);
                pa.stroke(130);
            }
            pa.ellipse(0, 0, r, r);
        }
        pa.pushMatrix();
        pa.rotate(-a);
        if (usepicture) {
            pa.imageMode(PConstants.CENTER);
            pa.image(Main.fillImage, 0, 0, r * 2, r * 2);
        } else {
            //画参考线
            pa.strokeWeight(1);
            pa.stroke(0);
            pa.line(0, 0, r, 0);
        }
        pa.popMatrix();
        pa.popMatrix();
    }

    void drawDebug() {
        Main pa = Main.instance;
        // We look at each body and get its screen position
        Vec2 pos = Main.box2d.getBodyPixelCoord(body);
        // Get its angle of rotation
        float a = body.getAngle();
        pa.pushMatrix();
        pa.translate(pos.x, pos.y);
        pa.pushMatrix();
        Vec2 vv = body.getLinearVelocity();
        float v = vv.length();
        pa.rotate(-(float) Math.atan2(vv.y, vv.x));
        pa.stroke(159, 163, 18);
        pa.fill(89, 43, 230);
        float l = v * 3;
        float m = body.getMass();
        pa.strokeWeight(l * 0.08f);
        //画箭头
        pa.line(0, 0, l, 0);
        pa.line(l, 0, l * (1 - SOMERATIO), l * -SOMERATIO * 0.6f);
        pa.line(l, 0, l * (1 - SOMERATIO), l * SOMERATIO * 0.6f);
        pa.popMatrix();
        pa.textAlign(PConstants.CENTER, PConstants.BOTTOM);
        if (pa.millis() - lastupdate > 200) {
            lastupdate = pa.millis();
            info = String.format("p=%7.2f\nv=%7.2f\nm=%7.2f", v * m, v, m);
        }
        pa.textFont(Main.font16);
        pa.text(info, 0, 0);
        pa.popMatrix();
    }

    // This function adds the rectangle to the Main.box2d world
    void makeBody(Vec2 center, float radius) {
        // Define and create the body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(Main.box2d.coordPixelsToWorld(center));
        body = Main.box2d.createBody(bd);

        // Define a polygon (this is what we use for a rectangle)
        CircleShape shape = new CircleShape();
        shape.setRadius(Main.box2d.scalarPixelsToWorld(radius));
        // Define a fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = Main.FRICTION;
        fd.restitution = Main.RESTITUTION;
        body.createFixture(fd);
        //body.m_mass=;

        // Give it some initial random velocity
        //body.setLinearVelocity(new Vec2(random(-5, 5), random(2, 5)));
        //body.setAngularVelocity(random(-5, 5));
    }

}