/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 *
 * @author Bob
 */
public class GuiAppState extends AbstractAppState implements RawInputListener {
    
    private final InputManager inputManager;
    
    public GuiAppState(InputManager inputManager, AssetManager assetManager) {
        this.inputManager = inputManager;
        GuiComponent.initialize(assetManager, inputManager);
        attachListener();
    }
    
    private void attachListener() {
        inputManager.addRawInputListener(this);
    }
    
    @Override
    public void update(float tpf) {
        
        for (int i = 0; i < GuiComponent.COMPONENTS.size(); i++) {
            GuiComponent c = GuiComponent.COMPONENTS.get(i);
            c.update(tpf);
        }
        
    }

    @Override
    public void beginInput() {

    }

    @Override
    public void endInput() {

    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {
        
    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {

    }

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {

    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        
        if (evt.getButtonIndex() == MouseInput.BUTTON_LEFT) {
            for (int i = 0; i < GuiComponent.COMPONENTS.size(); i++) {

                GuiComponent c = GuiComponent.COMPONENTS.get(i);
                
                if (evt.isReleased() && inputManager.isCursorVisible()) {
                    if (c.clickables.isEmpty()) continue;
                    if (c.onClick()) { 
                        evt.setConsumed(); 
                        return;
                    }
                }

            }
            
        }
        
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {

    }

    @Override
    public void onTouchEvent(TouchEvent evt) {
    }
    
}
