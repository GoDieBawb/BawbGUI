/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

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
public class TextBoard extends GuiComponent {
    
    private final BitmapText   messageText;
    private final BitmapText   shadowText;
    private final BitmapText   titleText;
    private final Spatial      titleBox;
    private final Clickable    closeButton;
    private final Spatial      buttonOutline;
    private final Spatial      backBoard;
    private final Spatial      board; 
    
    private float        height;
    private float        width;
    private float        scale = 1;
     
    private boolean onClose;
    
    public TextBoard(Node guiNode) {
        super(guiNode);       
        name          = "TextBoard";
        width         = Display.getWidth()/4f;
        height        = Display.getHeight()/3f;
        Box bBox      = new Box(width,height,1);
        Box box       = new Box(width-15,height-15,1);
        Box tBox      = new Box(width/2, height/6,1);
        Box nBox      = new Box(width/12f, width/12f,1);
        Box oBox      = new Box(width/10, width/10,1);
        backBoard     = new Geometry("Box", bBox);
        board         = new Geometry("Box", box); 
        titleBox      = new Geometry("Box", tBox);   
        
        closeButton   = new Clickable("Close", nBox) {
            @Override
            public void onMouseOver() {
                super.onMouseOver();
            }
            @Override
            public void onClick() {
                super.onClick();
            }
        };
        
        buttonOutline = new Geometry("Box", oBox);
        messageText   = new BitmapText(font);
        shadowText    = new BitmapText(font);
        titleText     = new BitmapText(font);

        titleBox.setMaterial(boxTitleMat);     
        closeButton.setMaterial(closeButtonMat);    
        buttonOutline.setMaterial(stoneBoardMat);
        backBoard.setMaterial(stoneBoardMat);
        board.setMaterial(paperBoardMat);        
        
        attachChildren();
        
        titleBox.setLocalTranslation(0,height-height/12,0);
        titleText.setSize(Display.getWidth()/25);
        messageText.setSize(Display.getWidth()/25);
        messageText.setLineWrapMode(LineWrapMode.Word);
        messageText.setBox(new Rectangle(0,0,width*2-50,height-5));
        shadowText.setSize(Display.getWidth()/25);
        shadowText.setLineWrapMode(LineWrapMode.Word);
        shadowText.setBox(new Rectangle(0,0,width*2-50,height-5));     
        shadowText.setColor(ColorRGBA.Black);
        closeButton.setLocalTranslation(width+nBox.xExtent/1.8f, height-nBox.yExtent*1.2f, 0);
        buttonOutline.setLocalTranslation(closeButton.getLocalTranslation());
        messageText.setSize(Display.getWidth()/25);
        shadowText.setSize(Display.getWidth()/25);
        
    }
    
    private void attachChildren() {
        attach(backBoard);        
        attach(board);
        attach(titleBox);
        attach(titleText);
        attach(shadowText);
        attach(messageText);   
        attach(buttonOutline);        
        attach(closeButton);
        setPosition(width, Display.getHeight()-height*1.1f, .25f);
    }
    
    public void showMessage(String title, String message) {
        setText(title, message);
        show();
    }
    
    public void setText(String title, String message) {
        unscale();
        titleText.setText(title);
        messageText.setText(message);
        shadowText.setText(message.replaceAll(COLOR_PATTERN.toString(),""));
        titleText.setLocalTranslation(-titleText.getLineWidth()/2,height-height/12 + titleText.getLineHeight()/1.5f,0);
        messageText.setLocalTranslation(-messageText.getLineWidth()/2.1f, height-messageText.getLineHeight()*2,0);
        shadowText.setLocalTranslation(-messageText.getLineWidth()/2.09f, height-messageText.getLineHeight()*2.05f,0);
        scale(scale);
    }
    
    private void unscale() {
        super.scale(1/scale);
        height*=1/scale;
        width*=1/scale;
    } 
    
    @Override
    public void scale(float val) {
        super.scale(val);
        scale = val;
        height*=val;
        width*=val;
    }
    
    public float getHeight() {
        return height;
    }
    
    public float getWidth() {
        return width;
    }
    
}
