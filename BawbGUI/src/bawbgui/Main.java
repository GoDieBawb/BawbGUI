package bawbgui;

import bawbgui.components.AlertBox;
import bawbgui.components.BarIndicator;
import bawbgui.components.ChoiceBox;
import bawbgui.components.DetailDisplay;
import bawbgui.components.GuiAppState;
import bawbgui.components.IconIndicator;
import bawbgui.components.TextBoard;
import bawbgui.components.TextIndicator;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        setDisplayFps(false);
        flyCam.setEnabled(false);
        getViewPort().setBackgroundColor(ColorRGBA.Green);
        stateManager.attach(new GuiAppState(inputManager, assetManager));
        //testBarIndicator();
        //testTextIndicator();
        testChoiceBox();
        //testAlertBox();
        //testSlideOut();
        //testDetailDisplay();
    }

    private void testDetailDisplay() {
        DetailDisplay d = new DetailDisplay(guiNode, assetManager);
        String details = "Weight: 50 lbs\n\nMeat: 20 lbs\n\nEdible: 5 lbs";
        d.displayItemDetails("Daughter", "Anne", "Char19", details);
        d.show();
    }
    
    private void testSlideOut() {
        IconIndicator s = new IconIndicator(guiNode, assetManager);
        s.setTop(false);
        s.addItem("Axe");
        s.addItem("Pick");
        s.addItem("Shovel");
        s.show();
    }
    
    private void testAlertBox() {
        AlertBox box = new AlertBox(guiNode);
        box.showMessage("Test", "This is a test message.", "Okay");
        box.scale(.25f);
    }
    
    BarIndicator bar;
    private void testBarIndicator() {
        bar = new BarIndicator(guiNode, assetManager, "Flag");
        bar.setFromRight(false);
        bar.setColor(ColorRGBA.Yellow);
        bar.show();
        bar.setBlink(true);
    }
    
    private void testTextIndicator() {
        TextIndicator ind = new TextIndicator(guiNode, assetManager, "Coin");
        ind.setValue("$50000");
        ind.setFromRight(false);
        ind.show();
    }    
    
    private void testChoiceBox() {
        
        TextBoard textBoard = new TextBoard(guiNode);
        textBoard.showMessage("Test", "A Test Message");
        textBoard.scale(.5f);
        
        ArrayList<String> x = new ArrayList<>(Arrays.asList("Agree", "Disagree", "Walk", "Run", "Eat", "Drink"));        
        ChoiceBox choiceBox = new ChoiceBox(guiNode, x, textBoard);
        choiceBox.show();
        choiceBox.scale(.5f);
        
    }
    
    boolean up;
    int x = 0;
    @Override
    public void simpleUpdate(float tpf) {
        
        if (bar != null) {
            
            if (bar.getValue() <= 0 && !up) {
                up = true;
            }

            if (bar.getValue() >= bar.getMaxValue() && up) {
                up = false;
                x++;
            }

            if (up) bar.setValue(bar.getValue()+.1f);
            else    bar.setValue(bar.getValue()-.1f);
            
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
