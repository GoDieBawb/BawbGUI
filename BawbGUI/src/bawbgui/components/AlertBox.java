/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class AlertBox extends GuiComponent {
    
    private final Spatial      backBoard;
    private final Spatial      board;
    private final Spatial      titleBox;
    private final Clickable    okButton;
    private final BitmapText   titleText;
    private final BitmapText   messageText;
    private final BitmapText   shadowText;
    private final BitmapText   buttonText;
    
    private boolean button;
    private float   height;
    private float   width;
    
    public AlertBox(Node guiNode) {
        super(guiNode);
        name          = "AlertBox";
        width         = Display.getWidth()/3f;
        height        = Display.getHeight()/8;
        Box bBox      = new Box(width,height,1);
        Box box       = new Box(width-15,height-15,1);
        Box tBox      = new Box(width/3, height/3,1);
        Box nBox      = new Box(width/4, height/3,1);
        backBoard     = new Geometry("Box", bBox);
        board         = new Geometry("Box", box);
        titleBox      = new Geometry("Box", tBox);
        
        okButton      = new Clickable("Okay", nBox) {
            
            @Override 
            public void onMouseOver() {
                super.onMouseOver();
                setMaterial(pressedMat);
            }
            
            @Override
            public void onMouseExit() {
                super.onMouseExit();
                setMaterial(buttonMat);
            }
            
        };
        
        titleText     = new BitmapText(font);
        messageText   = new BitmapText(font);
        shadowText    = new BitmapText(font);
        buttonText    = new BitmapText(font);
           
        backBoard.setMaterial(stoneBoardMat);
        board.setMaterial(paperBoardMat);
        titleBox.setMaterial(boxTitleMat);
        okButton.setMaterial(buttonMat);
        
        titleText.setText("");
        messageText.setText("");
        
        attachChildren();
        
        titleBox.setLocalTranslation(0,height-height/12,0);
        titleText.setSize(Display.getWidth()/25);
        
        messageText.setSize(Display.getWidth()/25);
        messageText.setLineWrapMode(LineWrapMode.Word);
        messageText.setBox(new Rectangle(0,0,width*2-25,height-5));
        messageText.setAlignment(BitmapFont.Align.Center);
        shadowText.setSize(Display.getWidth()/25);
        shadowText.setLineWrapMode(LineWrapMode.Word);
        shadowText.setBox(new Rectangle(0,0,width*2-25,height-5));
        shadowText.setAlignment(BitmapFont.Align.Center);        
        shadowText.setColor(ColorRGBA.Black);
        okButton.setLocalTranslation(0,-height+height/6,0);
        buttonText.setSize(Display.getWidth()/30);
        buttonText.setText("Ok");
        buttonText.setLocalTranslation(-buttonText.getLineWidth()/2,-height+height/6+buttonText.getLineHeight()/1.5f,0);
        
    }
    
    private void attachChildren() {
        attach(backBoard);        
        attach(board);
        attach(titleBox);
        attach(titleText);
        attach(shadowText);
        attach(messageText);
        attach(okButton);
        attach(buttonText);
        setPosition(Display.getWidth()/2, Display.getHeight()*.15f,1f);
    }
    
    public void setWidth(float width) {
        this.width = width;
    }
    
    public float getWidth() {
        return width;
    }
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    public float getHeight() {
        return height;
    }
    
    public void showMessage(String title, String message, String button) {
        setMessage(title, message, button);
        show();
    }
    
    public void setMessage(String title, String message, String button) {
        setMessageText(message);
        setButtonText(button);
        setTitleText(title);
    }
    
    public void setTitleText(String text) {
        titleText.setText(text);
        titleText.setLocalTranslation(-titleText.getLineWidth()/2,height-height/12 + titleText.getLineHeight()/1.5f,0);
    }
    
    public void setButtonText(String text) {
        buttonText.setText(text);
        buttonText.setLocalTranslation(-buttonText.getLineWidth()/2,-height+height/6+buttonText.getLineHeight()/1.5f,0);
    }
    
    public void setMessageText(String text) {
        messageText.setText(text);
        shadowText.setText(text.replaceAll(COLOR_PATTERN.toString(),""));        
        messageText.setLocalTranslation(-messageText.getLineWidth()/2, height-messageText.getLineHeight(),0);
        shadowText.setLocalTranslation(-messageText.getLineWidth()/1.99f, height-messageText.getLineHeight()*1.05f,0);
    }
    
}
