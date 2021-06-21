/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class BarIndicator extends GuiComponent {
    
    private float     maxValue;
    private float     currentValue;
    private float     width;
    private float     height;
    
    private boolean   fromRight;
    private boolean   blink;
    
    private final Spatial bar;
    private final Spatial back;
    private final Spatial icon;
    private final Spatial backBoard;
    
    public BarIndicator(Node guiNode, AssetManager assetManager, String iconName) {
        
        super(guiNode);
        name                = "BarIndicator";
        width               = Display.getWidth()/6f;
        height              = Display.getHeight()/20;
        Box bBox            = new Box(width,height,1);
        Box rBox            = new Box(width-10,height-10,1);
        Box iBox            = new Box(Display.getWidth()/15,Display.getWidth()/15,1);
        bar                 = new Geometry("Bar", rBox);
        back                = new Geometry("Container", rBox);
        backBoard           = new Geometry("BackBoard", bBox);
        icon                = new Geometry("Icon", iBox);
        Material barMat     = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material containMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material iconMat    = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture  texture    = assetManager.loadTexture("Textures/Icons/" + iconName + "Icon.png");
        
        iconMat.setTexture("ColorMap",  texture);
        iconMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        barMat.setColor("Color", ColorRGBA.Red);
        containMat.setColor("Color", ColorRGBA.Red.mult(.5f));
        
        backBoard.setMaterial(stoneBoardMat);
        back.setMaterial(containMat);
        bar.setMaterial(barMat);
        icon.setMaterial(iconMat);
        
        if (fromRight)
            icon.setLocalTranslation(width,0,.02f);
        else
            icon.setLocalTranslation(-width,0,.02f);
        
        bar.setLocalTranslation(0,0,.01f);
        
        maxValue     = 100;
        currentValue = 100;
        
        attachChildren();
        
    }
    
    private void attachChildren() {
        attach(backBoard);
        attach(bar);
        attach(back);
        attach(icon);
    }
    
    public void setMaxValue(float value) {
        maxValue = value;
    }
    
    public float getMaxValue() {
        return maxValue;
    }
    
    public void setValue(float value) {
        if      (value > maxValue) currentValue = maxValue;
        else if (value < 0)        currentValue = 0;
        else                       currentValue = value;
    }
    
    public float getValue() {
        return currentValue;
    }
    
    public void setColor(ColorRGBA color) {
        ((Geometry)bar).getMaterial().setColor("Color", color);
        ((Geometry)back).getMaterial().setColor("Color", color.mult(.5f));
    }
    
    public boolean isfromRight() {
        return fromRight;
    }
    
    public void setFromRight(boolean fromRight) {
        this.fromRight = fromRight;
        if (fromRight) {
            icon.setLocalTranslation(width+width/15,0,.02f);
            setPosition(Display.getWidth()-width*1.5f,Display.getHeight()-height*2,0);
        }
        else {
            icon.setLocalTranslation(-width*.9f,0,.02f);       
            setPosition(width*1.1f,Display.getHeight()-height*2,0);
        }
    }
    
    public void setBlink(boolean blink) {
        this.blink = blink;
    }
    
    public boolean getBlink() {
        return blink;
    }
    
    private Long    lastBlink = System.currentTimeMillis();
    private boolean on        = true;
    
    @Override
    public void update(float tpf) {
        
        super.update(tpf);
        float p = currentValue/maxValue;
        float x = (-width+10f)+(width-10f)*p;
        bar.setLocalScale(currentValue/maxValue, 1, 1);
        bar.setLocalTranslation(x, 0, .01f);
        
        if (blink) {
            
            float blinkScale = currentValue/maxValue;
            if (blinkScale < .1f) blinkScale = .1f;
            
            if (currentValue/maxValue > .25f) {
                if (!on) {
                    bar.setCullHint(Spatial.CullHint.Inherit); 
                    on = true;
                }
            }
            
            else if (System.currentTimeMillis() - lastBlink > 1000*blinkScale) {
                
                if (on) {
                    bar.setCullHint(Spatial.CullHint.Always); 
                    on = false;
                }
                else {
                    bar.setCullHint(Spatial.CullHint.Inherit); 
                    on = true;
                }
                
                lastBlink = System.currentTimeMillis();
                
            }
            
        }
        
    }
    
}
