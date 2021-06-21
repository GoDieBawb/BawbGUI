/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class IconIndicator extends GuiComponent {
    
    private final Clickable    openButton;
    private final Spatial      backBoard;
    private final AssetManager assetManager;
    private final Spatial      frame;

    private float   width;
    private float   height;
    private float   slideDistance;
    private boolean isOpen;
    private boolean top;
    private boolean displayOnly;
    private boolean slideOut;
    private String  value;
    
    private final ArrayList<Clickable> icons;
    private final ArrayList<String>    values;
    
    public IconIndicator(Node guiNode, AssetManager assetManager) {
        
        super(guiNode);
        name              = "IconIndicator";
        this.assetManager = assetManager;
        width             = Display.getWidth()/4;
        height            = Display.getWidth()/20;
        icons             = new ArrayList<>();
        values            = new ArrayList<>();
        Box abox          = new Box(Display.getWidth()/15,Display.getWidth()/15,1);
        Box bbox          = new Box(width, height, 1);
        Box fBox          = new Box(height-5, height-5, 1);
        backBoard         = new Geometry("Backboard", bbox);
        Material aMat     = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture  t        = assetManager.loadTexture("Textures/Icons/ArrowIcon.png");
        Material fMat     = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture  ft       = assetManager.loadTexture("Textures/Icons/RedFrameIcon.png");        
        frame             = new Geometry("Frame", fBox);

        
        openButton = new Clickable("Arrow", abox) {
            
            @Override
            public void onClick() {
                if (isOpen) return;
                open();
            }
            
        };
        
        aMat.setTexture("ColorMap", t);
        backBoard.setMaterial(paperBoardMat);
        aMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        openButton.setMaterial(aMat);
        attachChildren();
        openButton.setLocalTranslation(width/2-Display.getWidth()/4.5f, height, 0);
        openButton.scale(.75f);
        //if (!top) openButton.setLocalRotation(new Quaternion(0f,0f,180f,1f));
        openButton.setLocalTranslation(0,0,0);
        fMat.setTexture("ColorMap", ft);
        frame.setMaterial(fMat);
        fMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        
        
    }
    
    private void attachChildren() {
        
        attach(openButton);
        attach(backBoard);
        setPosition(Display.getWidth()/2, height, 0);
        
        int mult = -1;
        
        if (top) {
            mult = 1;
            setPosition(Display.getWidth()/2, Display.getHeight()-height, 0);
        }         
        
        if (isOpen) {
            backBoard.setLocalTranslation(0,0,0);
            openButton.setLocalTranslation(0,0,0);
        }
        else {
            backBoard.setLocalTranslation(0,mult*height*3,0);
            openButton.setLocalTranslation(0,mult*height*3,0);
        }    
        
        if (!slideOut) {
            attach(frame);
        }
        
    }
    
    public void addItem(String item) {
        
        setPosition(Display.getWidth()/2, height, 0);
        
        if (top) {
            setPosition(Display.getWidth()/2, Display.getHeight()-height, 0);
        }           
        
        Box        iBox  = new Box(height-5, height-5, 1);
        Material iconMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture  texture = assetManager.loadTexture("Textures/Icons/" + item + "Icon.png");
        
        Clickable  icon  = new Clickable(item, iBox) {
            @Override
            public void onClick() {
                if (displayOnly) return;
                if (isOpen) {
                    setValue(this);
                }
            }
        };
        
        iconMat.setTexture("ColorMap",  texture);
        iconMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        icon.setMaterial(iconMat);     
        icons.add(icon);
        if (values.isEmpty()) openButton.setMaterial(iconMat);
        values.add(item);
        attach(icon);
        
        Vector3f v;
        int      mult = -1;
        if (top) mult = 1;        
        
        for (int i = 0; i < icons.size(); i++) {
            if (isOpen)
                v = new Vector3f(-width+width/3f+(Display.getWidth()/7.5f+15)*i,0,.01f);
            else
                v = new Vector3f(0,mult*height*3,.1f);
            Clickable c = icons.get(i);
            c.setLocalTranslation(v);
        }
        
        if (values.size()==1) {
            if (!visible) {
                show();
            }
        }
        
    }
    
    public void removeItem(String item) {
        for (int i = 0; i < icons.size(); i++) {
            Clickable c = icons.get(i);
            if (c.getName().equals(item)) {
                icons.remove(c);
                detach(c);
            }
        }
        values.remove(item);
        if (values.isEmpty()){
            value = null;
            hide();
        }
        else setValue(values.get(0));
    }
    
    public void open() {
        
        if (isOpen) return;
        
        int      mult = -1;
        if (top) mult = 1;
        backBoard.setLocalTranslation(0,0,0);
        openButton.setLocalTranslation(0,mult*height*3,0);
        
        for (int i = 0; i < icons.size(); i++) {
            Vector3f v = new Vector3f(-width+width/3f+(Display.getWidth()/7.5f+15)*i,0,.01f);
            Clickable c = icons.get(i);
            c.setLocalTranslation(v);
            if (c.getName().equals(value)) frame.setLocalTranslation(c.getLocalTranslation());
        }
        
        isOpen = true;        
        
    }
    
    public void close() {
        
        if (!isOpen) return;
        
        int      mult = -1;
        if (top) mult = 1;
        
        backBoard.setLocalTranslation(0,mult*height*3,0);
        frame.setLocalTranslation(0, mult*height*3, 0);
        openButton.setLocalTranslation(0,0,0);
        
        for (int i = 0; i < icons.size(); i++) {
            Vector3f v = new Vector3f(0,mult*height*3,.01f);
            Clickable c = icons.get(i);
            c.setLocalTranslation(v);
        }
        
        isOpen = false;
        
    }
    
    @Override
    public void hide() {
        close();
        super.hide();
    }
    
    @Override
    public void show() {
        if (values.isEmpty()) return;
        if (value == null) setValue(values.get(0));
        if (!slideOut) open();
        super.show();
    }
    
    public void setTop(boolean top) {
        
        this.top = top;
        int mult = 1;
        
        if (top) {
            setPosition(Display.getWidth()/2, Display.getHeight()-height, 0);
            //openButton.setLocalRotation(new Quaternion(0f,0f,0f,1f));
        }       
        else {
            setPosition(Display.getWidth()/2, height, 0);
            //openButton.setLocalRotation(new Quaternion(0f,0f,180f,1f));
            mult=-1;
        }
        
        if (isOpen) {
            backBoard.setLocalTranslation(0,0,0);
            openButton.setLocalTranslation(0,mult*height*3,0);
        }
        else {
            backBoard.setLocalTranslation(0,mult*height*3,0);
            openButton.setLocalTranslation(0,0,0);
        }   
        
    }
    
    public void setDisplayOnly(boolean displayOnly) {
        this.displayOnly = displayOnly;
    }
    
    public boolean isDisplayOnly() {
        return displayOnly;
    }
    
    public boolean getTop() {
        return top;
    }

    public boolean isOpen() {
        return isOpen;
    }
    
    public ArrayList<String> getValues() {
        return values;
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean isSlideOut() {
        return slideOut;
    }
    
    public void setSlideOut(boolean slideOut) {
        this.slideOut = slideOut;
        if (slideOut) detach(frame);
        else          attach(frame);
    }
    
    private void setValue(Clickable c) {
        value = c.getName();
        if (slideOut) {
            openButton.setMaterial(c.getMaterial());      
            close();
        }
        else {
            frame.setLocalTranslation(c.getLocalTranslation());
        }
    }
    
    public void setValue(String item) {
        if (values.contains(item)) {
            for (int i = 0; i < icons.size(); i++) {
                if (icons.get(i).getName().equals(item))  {
                    setValue(icons.get(i));
                    return;
                }
            }
            System.out.println("ERROR");
        }
        else {
            addItem(item);
            setValue(item);
        }
    }
    
}
