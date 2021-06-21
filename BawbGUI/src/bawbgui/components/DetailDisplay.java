/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
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
public class DetailDisplay extends GuiComponent {
    
    private final float           height;
    private final float           width;    
    private final Spatial         backBoard;
    private final Spatial         board;
    private final BitmapText      titleText;
    private final BitmapText      nameText;
    private final BitmapText      detailsText;
    private final Spatial         titleBox;
    private final Clickable       closeButton;
    private final Spatial         buttonOutline;   
    private final Node            displayNode;
    private final BitmapText      shadowText;
    private final AssetManager    am;
    
    private String iconPath;
    
    public DetailDisplay(Node guiNode, AssetManager am) {
        
        super(guiNode);
        name            = "DetailDisplay";
        this.am         = am;
        width           = Display.getWidth()/3f;
        height          = Display.getHeight()/3f;        
        displayNode     = new Node("Display");
        Box bBox        = new Box(width,height,1);
        Box box         = new Box(width-15,height-15,1);    
        Box tBox        = new Box(width/2, height/6,1);
        Box nBox        = new Box(width/12f, width/12f,1);
        Box oBox        = new Box(width/10, width/10,1);    
        backBoard       = new Geometry("Box", bBox);
        board           = new Geometry("Box", box);                
        titleBox        = new Geometry("Box", tBox);  
        closeButton = new Clickable("Close", nBox) {
        
            @Override
            public void onClick() {
                hide();
            }
        
        };  
        
        buttonOutline = new Geometry("Box", oBox);
        titleText     = new BitmapText(font);    
        nameText      = new BitmapText(font);  
        detailsText   = new BitmapText(font); 
        shadowText    = new BitmapText(font);
        shadowText.setColor(ColorRGBA.Black);
        
        titleBox.setMaterial(boxTitleMat);     
        closeButton.setMaterial(closeButtonMat);    
        buttonOutline.setMaterial(stoneBoardMat);
        backBoard.setMaterial(stoneBoardMat);
        board.setMaterial(paperBoardMat);      

        titleBox.setLocalTranslation(0,height-height/12,0);
        titleText.setSize(Display.getWidth()/25);
        
        closeButton.setLocalTranslation(width+nBox.xExtent/1.8f, height-nBox.yExtent*1.2f, 0);
        
        buttonOutline.setLocalTranslation(closeButton.getLocalTranslation()); 
        
        detailsText.setSize(Display.getWidth()/35);
        detailsText.setBox(new Rectangle(0,0,width-44, height-titleText.getLineHeight()));
        detailsText.setLocalTranslation(20, height-height/4-detailsText.getLineHeight()*2, 0);
        
        shadowText.setSize(Display.getWidth()/35);
        shadowText.setBox(new Rectangle(0,0,width-44, height-titleText.getLineHeight()));
        shadowText.setLocalTranslation(20, height-height/4-detailsText.getLineHeight()*2, 0);        
        
        nameText.setSize(width/8);
        
        attachChildren();
        
    }
   
    @Override
    public void show() {
        displayItemDetails();
        super.show();
    }
    
    private void attachChildren() {
        
        attach(backBoard);        
        attach(board);        
        attach(titleBox);
        attach(titleText);  
        attach(buttonOutline);        
        attach(closeButton);   
        attach(displayNode);
        
        setPosition(Display.getWidth()/1.65f, Display.getHeight()/2, 0);  
        
    }    
    
    public void setTitle(String title) {
        titleText.setText(title);
        titleText.setLocalTranslation(-titleText.getLineWidth()/2,height-height/12 + titleText.getLineHeight()/1.5f,0);
    }    

    public void setDetails(String details) {
        detailsText.setText(details);
        shadowText.setText(details.replaceAll(COLOR_PATTERN.toString(),""));      
        shadowText.setLocalTranslation(detailsText.getLocalTranslation().add(-1f, 1f, 0));
    }
    
    public void setNameText(String name) {
        nameText.setText(name);
        nameText.setLocalTranslation(-nameText.getLineWidth()/2, height-height/4, 0);
    }
    
    public void displayItemDetails(String title, String iconPath, String details) {
        
        detach(displayNode);
        setTitle(title);
        setName(iconPath);
        setDetails(details);
        this.iconPath       = iconPath;
        Box        iBox     = new Box(width/2.25f, width/2.25f, 1);
        Geometry   icon     = new Geometry("Box", iBox);
        Material   iconMat  = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture    texture  = am.loadTexture("Textures/Icons/" + iconPath + "Icon.png");
        iconMat.setTexture("ColorMap",  texture);
        iconMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        icon.setMaterial(iconMat);
        displayNode.attachChild(nameText);
        displayNode.attachChild(detailsText);
        displayNode.attachChild(icon);
        icon.setLocalTranslation(-width/5, height-width/2.25f-height/5, 0);
        attach(displayNode);
        
    }    
    
    public void displayItemDetails(String title, String name, String iconPath, String details) {
        
        detach(displayNode);
        setTitle(title);
        setNameText(name);
        setDetails(details);
        this.iconPath       = iconPath;
        Box        iBox     = new Box(width/2.5f, width/2.5f, 1);
        Geometry   icon     = new Geometry("Box", iBox);
        Material   iconMat  = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture    texture  = am.loadTexture("Textures/Icons/" + iconPath + "Icon.png");
        iconMat.setTexture("ColorMap",  texture);
        iconMat.setParam("AlphaDiscardThreshold", VarType.Float, 0.1f);
        icon.setMaterial(iconMat);
        displayNode.attachChild(nameText);
        displayNode.attachChild(shadowText);        
        displayNode.attachChild(detailsText);
        displayNode.attachChild(icon);
        icon.setLocalTranslation(-width/2, height-width/2.25f-height/5, 0);
        attach(displayNode);
        
    }     
    
    public void displayItemDetails() {
        displayItemDetails(titleText.getText(), nameText.getText(), iconPath, detailsText.getText());
    }
    
}
