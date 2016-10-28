import g4p_controls.*;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import shiffman.box2d.Box2DProcessing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main extends PApplet {
    public static final float DEFAULTRESTITUTION = 1.0f, DEFAULTGRAVITY = 10.0f, DEFAULTFRICTION = 0.0f;
    public static Main instance;
    public static float FRICTION, RESTITUTION;
    public static Box2DProcessing box2d;
    public static PFont font20, font16;
    public static boolean fullScreen = false, nosmooth = false;
    public static int dwidth = 800, dheight = 600;
    public static PImage fillImage;
    float holdingRadius = 50;
    boolean pause, holdingMouse;
    int lastupdate;
    String draginfo = "";
    Spring spring;
    ArrayList<Ball> balls;
    ArrayList<Boundary> boundaries;

    public static void main(String[] args) {
        ArrayList<String> argsArrayList = new ArrayList<>();
        for (String arg : args) {
            arg = arg.trim().toLowerCase();
            if (arg.contains("-?") || arg.contains("/?")) {
                System.out.println("Momentum(Physics) demo program");
                System.out.println("    by buckle200");
                System.out.println();
                System.out.println("Parameters:");
                System.out.println("           {-? | /?} show this help");
                System.out.println("           full      fullscreen");
                System.out.println("           nosmooth  disable vsync");
                System.out.println("           <other>   see processing.org");
                System.out.println("           <width>x<height>");
                System.out.println("                specify window size");
                System.out.println("                e.g. 1024x768");
                System.out.println("                default is 800x600");
                System.exit(0);
            } else if (arg.equals("full"))
                fullScreen = true;
            else if (arg.equals("nosmooth"))
                nosmooth = true;
            else if (arg.contains("x")) {
                String[] size = arg.split("x");
                int w = 0, h = 0;
                if (size.length != 2)
                    continue;
                try {
                    w = Integer.parseInt(size[0]);
                    h = Integer.parseInt(size[1]);
                } catch (Exception e) {
                    System.out.println("参数不正确，请使用 -? 查看帮助");
                    System.exit(-1);
                }
                dwidth = max(w, 400);
                dheight = max(h, 600);

            } else if (arg.equals("present"))
                argsArrayList.add("--present");
            else argsArrayList.add(arg);
        }
        String[] argsArray = new String[argsArrayList.size()];
        for (int i = 0; i < argsArrayList.size(); ++i) {
            argsArray[i] = argsArrayList.get(i);
        }
        PApplet.main("Main", argsArray);
    }

    public void settings() {
        instance = this;
        if (fullScreen)
            fullScreen();
        else
            size(dwidth, dheight);
        if (nosmooth)
            noSmooth();
    }

    public void setup() {
        surface.setTitle("动量守恒演示程序");
        font20 = createFont("simkai.ttf", 20);
        font16 = createFont("simkai.ttf", 16);
        if (font20 == null || font16 == null) {
            JOptionPane.showMessageDialog(null, "加载字体 \"simkai.tff\" 失败!", "错误", JOptionPane.ERROR_MESSAGE);
            exitActual();
        }
        textFont(font20);
        fillImage = null;
        fillImage = loadImage("fill.png");
        if (fillImage != null) {
            PGraphics tempMask = createGraphics(fillImage.width, fillImage.height);
            tempMask.beginDraw();
            tempMask.background(0, 0);
            tempMask.ellipseMode(CORNER);
            tempMask.noStroke();
            tempMask.fill(255);
            tempMask.ellipse(0, 0, fillImage.width, fillImage.height);
            tempMask.endDraw();
            fillImage.mask(tempMask);
        }

        createGUI();
        cfgPanel.setFont(font20.getFont());
        for (GAbstractControl ctrl : cfgPanel.getChildren()) {
            if (ctrl instanceof GTextBase) {
                GTextBase textbase = (GTextBase) ctrl;
                textbase.setFont(font20.getFont());
            }
        }
        init();
        reset();
    }

    public void init() {
        //varRestitution.setValue(DEFAULTRESTITUTION);
        //varFriction.setValue(DEFAULTFRICTION);
        chkGravity.setSelected(true);
        //chkDebug.setSelected(true);
    }

    public void reset() {
        knobGravity.setValue(DEFAULTGRAVITY);
        //chkGravity_check(chkGravity,null);
        varRestitution_change(varRestitution, null);
        varFriction_change(varFriction, null);
        pause = false;
        holdingMouse = false;
        balls = new ArrayList<>();
        spring = new Spring();
        box2d = new Box2DProcessing(this);
        box2d.createWorld();
        boundaries = new ArrayList<>();
        boundaries.add(new Boundary(150, 0, width - 1, 0));
        boundaries.add(new Boundary(150, 0, 150, height));
        boundaries.add(new Boundary(150, height, width, height));
        boundaries.add(new Boundary(width, 0, width, height));
        updateGravity(knobGravity.getValueF());
    }

    public void draw() {
        boolean debug = chkDebug.isSelected();
        background(255);
        if (millis() - lastupdate > 200) {
            lastupdate = millis();
            if (spring.mouseJoint != null && debug) {
                Vec2 vout = new Vec2();
                spring.mouseJoint.getReactionForce(0.01f, vout);
                textAlign(RIGHT, TOP);
                long degree = Math.round(Math.toDegrees(Math.atan2(vout.y, vout.x)));
                if (degree < 0) degree += 360;
                draginfo = String.format("F= %.2f@%3d°", vout.length(), degree);
            }
        }
        Vec2 screenCenter = new Vec2(width / 2, height / 2);
        float maxRenderDistance = max(width, height) * 2;
        for (Ball ball : (ArrayList<Ball>) balls.clone()) {
            if (box2d.coordWorldToPixels(ball.body.getPosition()).sub(screenCenter).length() > maxRenderDistance) {
                ball.killBody();
                balls.remove(ball);
                continue;
            }
        }
        if (!pause) box2d.step();
        spring.update();
        if (holdingMouse) {
            holdingRadius += 1.6;
            ellipseMode(RADIUS);
            fill(255, 255 - holdingRadius, 0);
            noStroke();
            ellipse(mouseX, mouseY, holdingRadius, holdingRadius);
        }
        for (Boundary boundary : boundaries) {
            boundary.draw();
        }
        boolean usepicture;
        if (chkPicture != null)
            usepicture = chkPicture.isSelected();
        else usepicture = false;
        for (Ball ball : balls)
            ball.draw(usepicture);
        if (debug)
            for (Ball ball : balls)
                ball.drawDebug();
        textFont(font20);
        if (spring.mouseJoint != null) {
            spring.display();
            if (debug) {
                fill(255, 0, 0);
                text(draginfo, mouseX, mouseY);
            }
        }
    }

    public void keyPressed() {
        switch (Character.toLowerCase(key)) {
            case ' ':
            case 'p':
                pause = !pause;
                break;
            case 'r':
                reset();
                break;
            case 'd':
                chkDebug.setSelected(!chkDebug.isSelected());
                break;
            case 'g':
                chkGravity.setSelected(!chkGravity.isSelected());
                chkGravity_check(chkGravity, null);
                break;
        }
    }

    public void mouseDragged() {
        if (mouseButton == CENTER)
            balls.add(new Ball(mouseX, mouseY, holdingRadius));
    }

    public void mousePressed() {
        //(pause && mouseButton != RIGHT) || 
        if (mouseX < 150) return;
        boolean overlap = false;
        Ball which = null;
        for (Ball ball : balls) {
            if (ball.contains(mouseX, mouseY)) {
                overlap = true;
                which = ball;
                break;
            }
        }
        if (!overlap)
            switch (mouseButton) {
                case LEFT:
                    holdingMouse = true;
                    holdingRadius = 0;
                    break;
                case RIGHT:
                    balls.add(new Ball(mouseX, mouseY, random(10, 60)));
                    break;
            }
        else switch (mouseButton) {
            case LEFT:
                spring.bind(mouseX, mouseY, which);
                break;
            case RIGHT:
                balls.remove(which);
                which.killBody();
                break;
        }
    }

    public void mouseReleased() {
        if (holdingMouse) {
            holdingMouse = false;
            balls.add(new Ball(mouseX, mouseY, holdingRadius));
        } else spring.destroy();
    }

    void updateGravity(float magnitude) {
        if (chkGravity.isSelected()) {
            box2d.setGravity(0, -magnitude);
            lblGravity.setText(String.format("g=%.2f", magnitude));
        } else {
            box2d.setGravity(0, 0);
            lblGravity.setText("无重力");
        }
    }

    public void btnPause_click(GButton source, GEvent event) { //_CODE_:btnPause:357494:
        pause = !pause;
    } //_CODE_:btnPause:357494:

    public void knobGravity_turn(GKnob source, GEvent event) { //_CODE_:knobGravity:763520:
        if (chkGravity.isSelected())
            updateGravity(source.getValueF());
    } //_CODE_:knobGravity:763520:

    public void chkGravity_check(GCheckbox source, GEvent event) { //_CODE_:chkGravity:638919:
        if (source.isSelected()) {
            updateGravity(knobGravity.getValueF());
            for (Ball ball : balls)
                ball.body.setAwake(true);
        } else updateGravity(0);
    } //_CODE_:chkGravity:638919:

    public void btnReset_click(GButton source, GEvent event) { //_CODE_:btnReset:441256:
        reset();
    } //_CODE_:btnReset:441256:

    public void varRestitution_change(GCustomSlider source, GEvent event) { //_CODE_:varRestitution:649653:
        RESTITUTION = source.getValueF();
    } //_CODE_:varRestitution:649653:

    public void varFriction_change(GCustomSlider source, GEvent event) { //_CODE_:varFriction:578259:
        FRICTION = source.getValueF();

    } //_CODE_:varFriction:578259:

    public void btnExit_click(GButton source, GEvent event) { //_CODE_:btnReset:441256:
        boolean pausing = pause;
        pause = false;
        int choice = JOptionPane.showConfirmDialog(null, "确定要退出吗", "退出", JOptionPane.YES_NO_OPTION);
        if (choice == 0) System.exit(0);
        pause = pausing;
    }

    public void createGUI() {
        G4P.messagesEnabled(false);
        G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
        G4P.setCursor(ARROW);

        cfgPanel = new MoreOpenG4PControl(this, 0, 0, 150, height, "设置");
        cfgPanel.setDraggable(false);
        cfgPanel.setCollapsible(false);
        cfgPanel.setOpaque(true);
        btnPause = new GButton(this, 20, 510, 110, 30);
        btnPause.setText("暂停(P/ )");
        btnPause.setTextBold();
        btnPause.addEventHandler(this, "btnPause_click");
        knobGravity = new GKnob(this, 0, 270, 150, 150, 0.8f);
        knobGravity.setTurnRange(110, 70);
        knobGravity.setTurnMode(GKnob.CTRL_ANGULAR);
        knobGravity.setShowArcOnly(false);
        knobGravity.setOverArcOnly(true);
        knobGravity.setIncludeOverBezel(false);
        knobGravity.setShowTrack(true);
        knobGravity.setLimits(DEFAULTGRAVITY, 0.0f, 50.0f);
        knobGravity.setNbrTicks(6);
        knobGravity.setShowTicks(true);
        knobGravity.setOpaque(false);
        knobGravity.addEventHandler(this, "knobGravity_turn");
        lblGravity = new GLabel(this, 0, 240, 150, 30);
        lblGravity.setOpaque(true);
        chkGravity = new GCheckbox(this, 10, 420, 130, 30);
        chkGravity.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
        chkGravity.setText("重力 (G)");
        chkGravity.setOpaque(false);
        chkGravity.addEventHandler(this, "chkGravity_check");
        btnReset = new GButton(this, 20, 550, 110, 30);
        btnReset.setText("重置 (R)");
        btnReset.setTextBold();
        btnReset.setLocalColorScheme(GCScheme.RED_SCHEME);
        btnReset.getLocalColorScheme();
        btnReset.addEventHandler(this, "btnReset_click");
        varRestitution = new GCustomSlider(this, 0, 30, 150, 60, "blue18px");
        varRestitution.setShowValue(true);
        varRestitution.setLimits(DEFAULTRESTITUTION, 0.0f, 1.0f);
        varRestitution.setNumberFormat(G4P.DECIMAL, 2);
        varRestitution.setOpaque(false);
        varRestitution.addEventHandler(this, "varRestitution_change");
        lblRestitution = new GLabel(this, 30, 70, 90, 30);
        lblRestitution.setText("弹性");
        lblRestitution.setOpaque(false);
        varFriction = new GCustomSlider(this, 0, 110, 150, 60, "blue18px");
        varFriction.setShowValue(true);
        varFriction.setLimits(DEFAULTFRICTION, 0.0f, 1.0f);
        varFriction.setNumberFormat(G4P.DECIMAL, 2);
        varFriction.setOpaque(false);
        varFriction.addEventHandler(this, "varFriction_change");
        lblFriction = new GLabel(this, 30, 150, 90, 30);
        lblFriction.setText("摩擦系数");
        lblFriction.setOpaque(false);
        lblWarn0 = new GLabel(this, 0, 190, 150, 40);
        lblWarn0.setOpaque(false);
        lblWarn0.setLocalColorScheme(GCScheme.CUSTOM);
        lblWarn0.setLocalColor(0, 0xff0000);
        lblWarn0.setText("以上选项仅对调整后放的球有用");
        chkDebug = new GCheckbox(this, 10, 450, 130, 30);
        chkDebug.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
        chkDebug.setText("详情 (D)");
        chkDebug.setOpaque(false);
        if (fillImage != null) {
            chkPicture = new GCheckbox(this, 10, 480, 130, 30);
            chkPicture.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
            chkPicture.setText("(⊙_⊙)");
            chkPicture.setOpaque(false);
            cfgPanel.addControl(chkPicture);
        }
        cfgPanel.addControl(btnPause);
        cfgPanel.addControl(knobGravity);
        cfgPanel.addControl(lblGravity);
        cfgPanel.addControl(chkGravity);
        cfgPanel.addControl(btnReset);
        cfgPanel.addControl(varRestitution);
        cfgPanel.addControl(lblRestitution);
        cfgPanel.addControl(varFriction);
        cfgPanel.addControl(lblFriction);
        cfgPanel.addControl(lblWarn0);
        cfgPanel.addControl(chkDebug);
        if (fullScreen) {
            btnExit = new GButton(this, 20, height - 50, 110, 30);
            btnExit.setText("退出");
            btnExit.setTextBold();
            btnExit.setLocalColorScheme(GCScheme.RED_SCHEME);
            btnExit.getLocalColorScheme();
            btnExit.addEventHandler(this, "btnExit_click");
            cfgPanel.addControl(btnExit);
        }
    }

    MoreOpenG4PControl cfgPanel;
    GButton btnPause;
    GKnob knobGravity;
    GLabel lblGravity;
    GCheckbox chkGravity;
    GButton btnReset;
    GCustomSlider varRestitution;
    GLabel lblRestitution;
    GCustomSlider varFriction;
    GLabel lblFriction;
    GLabel lblWarn0;
    GCheckbox chkDebug;
    GButton btnExit;
    GCheckbox chkPicture;
}

class MoreOpenG4PControl extends GPanel {
    public MoreOpenG4PControl(PApplet pApplet, float v, float v1, float v2, float v3) {
        super(pApplet, v, v1, v2, v3);
    }

    public MoreOpenG4PControl(PApplet pApplet, float v, float v1, float v2, float v3, String s) {
        super(pApplet, v, v1, v2, v3, s);
    }

    public LinkedList<GAbstractControl> getChildren() {
        return this.children;
    }
}
