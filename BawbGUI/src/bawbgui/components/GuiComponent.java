/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author Bob
 */
public class GuiComponent {
    
    public static ArrayList<GuiComponent> COMPONENTS = new ArrayList<>();    
    
    protected static Material boxTitleMat, closeButtonMat, stoneBoardMat, paperBoardMat, buttonMat, pressedMat;
    protected static BitmapFont font;
    protected static Picker     picker;
    protected static final Pattern COLOR_PATTERN = Pattern.compile("\\\\#([0-9a-fA-F]{8})#|\\\\#([0-9a-fA-F]{6})#|" +
"\\\\#([0-9a-fA-F]{4})#|\\\\#([0-9a-fA-F]{3})#");        
    
    protected boolean           cursor;
    protected boolean           visible;

    protected final  Node       guiNode;
    private   final  Node       body;
    
    protected final  ArrayList<Clickable>   clickables   = new ArrayList<>();
    protected final  ArrayList<MouseEffect> mouseEffects = new ArrayList<>();
    protected String name;
    
    public GuiComponent(Node guiNode) {
        this.guiNode = guiNode;
        body         = new Node();
        name         = "GuiComponent";
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public interface MouseEffect {
        public void    onMouseOver();
        public void    onMouseExit();
        public boolean isActive();
    }
    
    public class Clickable extends Geometry implements MouseEffect {
        
        private boolean active;
        private boolean enabled;
        
        public Clickable(String name, Mesh mesh) {
            super(name, mesh);
            enabled = true;
        }

        public void onClick() {
            if (!enabled) return;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        @Override
        public void onMouseOver() {
            if (active) return;
            active = true;
        }

        @Override
        public void onMouseExit() {
            if (!active) return;
            active = false;
        }
        
        @Override
        public boolean isActive() {
            return active;
        }
        
    }
    
    public static void initialize(AssetManager assetManager, InputManager inputManager) {
        boxTitleMat    = assetManager.loadMaterial("Materials/BoxTitle.j3m");     
        closeButtonMat = assetManager.loadMaterial("Materials/CloseButton.j3m");    
        stoneBoardMat  = assetManager.loadMaterial("Materials/StoneBoard.j3m");
        paperBoardMat  = assetManager.loadMaterial("Materials/PaperBoard.j3m");  
        buttonMat      = assetManager.loadMaterial("Materials/Button.j3m");  
        pressedMat     = assetManager.loadMaterial("Materials/ButtonPressed.j3m");
        font           = assetManager.loadFont("Interface/Fonts/Western.fnt");
        picker         = new Picker(inputManager);
    }
    
    public void show() {
        if (visible) return;
        COMPONENTS.add(this);        
        guiNode.attachChild(body);
        visible = true;
    }
    
    public void hide() {
        if (!visible) return;
        COMPONENTS.remove(this);        
        guiNode.detachChild(body);
        visible = false;        
    }
    
    public boolean isCursor() {
        return cursor;
    }
    
    public void setCursor(boolean cursor) {
        this.cursor = cursor;
    }    
    
    public boolean isVisible() {
        return visible;
    }
    
    public Vector3f getPosition() {
        return body.getLocalTranslation();
    }
    
    public void setPosition(float x, float y, float z) {
        body.setLocalTranslation(x, y, z);
    }
    
    public void setPosition(Vector3f position) {
        body.setLocalTranslation(position);
    }    
    
    public void setScale(float x, float y, float z) {
        body.setLocalScale(x, y, z);
    }    
    
    public void scale(float f) {
        body.scale(f);
    }
    
    public void attach(Spatial s) {
        
        if (s instanceof Node) {
            attachSubComponents((Node) s);
        }
        
        if (s instanceof Clickable) {
            clickables.add((Clickable)s);
        }
        
        if (s instanceof MouseEffect) {
            mouseEffects.add((MouseEffect)s);
        }
        
        body.attachChild(s);
        
    }
    
    public void detach(Spatial s) {
        
        if (s instanceof Node) {
            detachSubComponents((Node) s);
            ((Node) s).detachAllChildren();
        }
        
        if (s instanceof Clickable) {
            clickables.remove((Clickable)s);
        }
        
        if (s instanceof MouseEffect) {
            mouseEffects.remove((MouseEffect)s);
        }
        
        body.detachChild(s);
        
    }    
    
    private void attachSubComponents(Node n) {
    
        for (int i = 0; i < n.getChildren().size(); i++) {
            
            Spatial s = n.getChild(i);
            
            if (s instanceof Node) {
                attachSubComponents((Node) s);
            }
            
            else if (s instanceof Clickable) {
                clickables.add((Clickable) s);
            }
            
            if (s instanceof MouseEffect) {
                mouseEffects.add((MouseEffect)s);
            }            
            
        }
        
    }
    
    private void detachSubComponents(Node n) {
    
        for (int i = 0; i < n.getChildren().size(); i++) {
            
            Spatial s = n.getChild(i);
            
            if (s instanceof Node) {
                detachSubComponents((Node) s);
            }
            
            else if (s instanceof Clickable) {
                clickables.remove((Clickable) s);
            }
            
            if (s instanceof MouseEffect) {
                mouseEffects.remove((MouseEffect)s);
            }            
            
        }
        
    }    
    
    public boolean onClick() {
    
        for (int i = 0; i < clickables.size(); i++) {
            
            Clickable c       = clickables.get(i);
            boolean   contact = picker.checkContact(c);
            
            if (contact) {
                c.onClick();
                onClickableClicked(c);
                return true;
            }
            
        }
        
        return false;
        
    }
    
    public void onClickableClicked(Clickable clickable) {
    
    }
    
    private void checkMouseEffects() {
        
        for (int i = 0; i < mouseEffects.size(); i++) {
            
            MouseEffect e       = mouseEffects.get(i);
            boolean     contact = picker.checkContact((Spatial)e);
            
            if (contact) {
            
                if (!e.isActive()) {
                    e.onMouseOver();
                }
                
            }
            
            else {
            
                if (e.isActive()) {
                    e.onMouseExit();
                }
                
            }
            
        }
        
    }
    
    public void update(float tpf) {
        checkMouseEffects();
    }
    
}
