/* =========================================================
 * ====                   WARNING                        ===
 * =========================================================
 * The code in this tab has been generated from the GUI form
 * designer and care should be taken when editing this file.
 * Only add/edit code inside the event handlers i.e. only
 * use lines between the matching comment tags. e.g.

 void myBtnEvents(GButton button) { //_CODE_:button1:12356:
     // It is safe to enter your event code here  
 } //_CODE_:button1:12356:
 
 * Do not rename this tab!
 * =========================================================
 */

public void cfg_collapse(GPanel source, GEvent event) { //_CODE_:cfgPanel:628848:
  println("cfgPanel - GPanel >> GEvent." + event + " @ " + millis());
} //_CODE_:cfgPanel:628848:

public void btnPause_click(GButton source, GEvent event) { //_CODE_:btnPause:357494:
  println("btnPause - GButton >> GEvent." + event + " @ " + millis());
} //_CODE_:btnPause:357494:

public void knobGravity_turn(GKnob source, GEvent event) { //_CODE_:knobGravity:763520:
  println(source.get);
} //_CODE_:knobGravity:763520:

public void chkGravity_check(GCheckbox source, GEvent event) { //_CODE_:chkGravity:638919:
  println("chkGravity - GCheckbox >> GEvent." + event + " @ " + millis());
} //_CODE_:chkGravity:638919:

public void btnReset_click(GButton source, GEvent event) { //_CODE_:btnReset:441256:
  println("btnReset - GButton >> GEvent." + event + " @ " + millis());
} //_CODE_:btnReset:441256:

public void varResitution_change(GCustomSlider source, GEvent event) { //_CODE_:varResitution:649653:
  println("custom_slider1 - GCustomSlider >> GEvent." + event + " @ " + millis());
} //_CODE_:varResitution:649653:

public void varFriction_change(GCustomSlider source, GEvent event) { //_CODE_:varFriction:578259:
  println("custom_slider1 - GCustomSlider >> GEvent." + event + " @ " + millis());
} //_CODE_:varFriction:578259:



// Create all the GUI controls. 
// autogenerated do not edit
public void createGUI(){
  G4P.messagesEnabled(false);
  G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
  G4P.setCursor(ARROW);
  surface.setTitle("Sketch Window");
  cfgPanel = new GPanel(this, 0, 0, 150, 600, "设置");
  cfgPanel.setDraggable(false);
  cfgPanel.setText("设置");
  cfgPanel.setOpaque(true);
  cfgPanel.addEventHandler(this, "cfg_collapse");
  btnPause = new GButton(this, 20, 530, 110, 30);
  btnPause.setText("暂停");
  btnPause.setTextBold();
  btnPause.addEventHandler(this, "btnPause_click");
  knobGravity = new GKnob(this, 0, 270, 150, 150, 0.8);
  knobGravity.setTurnRange(110, 70);
  knobGravity.setTurnMode(GKnob.CTRL_ANGULAR);
  knobGravity.setShowArcOnly(false);
  knobGravity.setOverArcOnly(true);
  knobGravity.setIncludeOverBezel(false);
  knobGravity.setShowTrack(true);
  knobGravity.setLimits(10.0, 0.0, 50.0);
  knobGravity.setNbrTicks(6);
  knobGravity.setShowTicks(true);
  knobGravity.setOpaque(false);
  knobGravity.addEventHandler(this, "knobGravity_turn");
  lblGravity = new GLabel(this, 0, 240, 150, 30);
  lblGravity.setOpaque(true);
  chkGravity = new GCheckbox(this, 10, 420, 130, 30);
  chkGravity.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  chkGravity.setText("重力");
  chkGravity.setOpaque(false);
  chkGravity.addEventHandler(this, "chkGravity_check");
  btnReset = new GButton(this, 50, 560, 50, 30);
  btnReset.setText("重置");
  btnReset.setLocalColorScheme(GCScheme.RED_SCHEME);
  btnReset.addEventHandler(this, "btnReset_click");
  varResitution = new GCustomSlider(this, 0, 30, 150, 60, "blue18px");
  varResitution.setShowValue(true);
  varResitution.setLimits(1.0, 0.0, 1.0);
  varResitution.setNumberFormat(G4P.DECIMAL, 3);
  varResitution.setOpaque(false);
  varResitution.addEventHandler(this, "varResitution_change");
  lblResitution = new GLabel(this, 30, 70, 90, 30);
  lblResitution.setText("弹性");
  lblResitution.setOpaque(false);
  varFriction = new GCustomSlider(this, 0, 110, 150, 60, "blue18px");
  varFriction.setLimits(0.0, 0.0, 1.0);
  varFriction.setNumberFormat(G4P.DECIMAL, 2);
  varFriction.setOpaque(false);
  varFriction.addEventHandler(this, "varFriction_change");
  lblFriction = new GLabel(this, 30, 150, 90, 30);
  lblFriction.setText("摩擦系数");
  lblFriction.setOpaque(false);
  lblWarn0 = new GLabel(this, 0, 190, 150, 40);
  lblWarn0.setText("以上选项仅对后来放的球有关");
  lblWarn0.setLocalColorScheme(GCScheme.RED_SCHEME);
  lblWarn0.setOpaque(false);
  chkDebug = new GCheckbox(this, 10, 450, 130, 30);
  chkDebug.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  chkDebug.setText("显示详细信息");
  chkDebug.setOpaque(false);
  chkDebug.setSelected(true);
  chkPicture = new GCheckbox(this, 10, 480, 130, 30);
  chkPicture.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  chkPicture.setText(" :)");
  chkPicture.setOpaque(false);
  cfgPanel.addControl(btnPause);
  cfgPanel.addControl(knobGravity);
  cfgPanel.addControl(lblGravity);
  cfgPanel.addControl(chkGravity);
  cfgPanel.addControl(btnReset);
  cfgPanel.addControl(varResitution);
  cfgPanel.addControl(lblResitution);
  cfgPanel.addControl(varFriction);
  cfgPanel.addControl(lblFriction);
  cfgPanel.addControl(lblWarn0);
  cfgPanel.addControl(chkDebug);
  cfgPanel.addControl(chkPicture);
}

// Variable declarations 
// autogenerated do not edit
GPanel cfgPanel; 
GButton btnPause; 
GKnob knobGravity; 
GLabel lblGravity; 
GCheckbox chkGravity; 
GButton btnReset; 
GCustomSlider varResitution; 
GLabel lblResitution; 
GCustomSlider varFriction; 
GLabel lblFriction; 
GLabel lblWarn0; 
GCheckbox chkDebug; 
GCheckbox chkPicture; 