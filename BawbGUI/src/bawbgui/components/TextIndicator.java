/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
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
public class TextIndicator extends GuiComponent {
    
    private float     width;
    private float     height;    
    private String    value;
    private boolean   fromRight;
    
    private final Spatial    icon;
    private final Spatial    backBoard;
    private final BitmapText text;
    
    public TextIndicator(Node guiNode, AssetManager assetManager, String iconName) {
        
        super(guiNode);
        name             = "TestIndicator";
        width            = Display.getWidth()/12f;
        height           = Display.getHeight()/20;        
        Box bBox         = new Box(width,height,1);
        Box iBox         = new Box(Display.getWidth()/15,Display.getWidth()/15,1);
        icon             = new Geometry("Icon", iBox);
        backBoard        = new Geometry("BackBoard", bBox);
        text             = new BitmapText(font);
        Material iconMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture  texture = assetManager.loadTexture("Textures/Icons/" + iconName + "Icon.png"); 
        
        iconMat.setTexture("ColorMap",  texture);
        iconMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);  
        backBoard.setMaterial(paperBoardMat);
        icon.setMaterial(iconMat);
        icon.setLocalTranslation(-width,0,.02f);
        text.setLocalTranslation(0,0,.01f);        
        text.setSize(width/2.5f);

        attachChildren();
        
    }    
    
    private void attachChildren() {
        attach(backBoard);
        attach(text);
        attach(icon);
    }    
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
        text.setText(value);
        if (fromRight)
            text.setLocalTranslation(-text.getLineWidth()/1.7f, text.getLineHeight()/2,.01f);
        else
            text.setLocalTranslation(-text.getLineWidth()/2.5f, text.getLineHeight()/2,.01f);
    }
    
    public void setFromRight(boolean fromRight) {
        if(fromRight) {
            icon.setLocalTranslation(width,0,.02f);
            setPosition(Display.getWidth()-width*1.75f,Display.getHeight()-height*2,0);
        }
        else {
            icon.setLocalTranslation(-width,0,.02f);
            setPosition(width*1.75f,Display.getHeight()-height*2,0);
        }
        this.fromRight = fromRight;
        setValue(value);
    }
    
    public boolean isFromRight() {
        return fromRight;
    }
    
}
