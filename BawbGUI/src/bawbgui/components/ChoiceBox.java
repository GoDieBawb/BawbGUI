/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.font.BitmapText;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class ChoiceBox extends GuiComponent {
    
    private final ArrayList<String>  choices;
    
    private TextBoard  textBoard;
    
    public ChoiceBox( Node guiNode, ArrayList<String>  choices) {
        super(guiNode);
        this.choices  = choices;
        createButtons();
    }
    
    public ChoiceBox( Node guiNode, ArrayList<String>  choices, TextBoard textBoard) {
        super(guiNode);
        this.choices  = choices;
        createButtons();
        this.textBoard = textBoard;
        setPosition(textBoard.getPosition().x, textBoard.getPosition().y-textBoard.getHeight()*1.42f, 1);        
    }    
    
    private void createButtons() {
        name                 = "ChoiceBox";
        float   width        = Display.getWidth()/4f;
        float   height       = (Display.getHeight()/22+4)*3;
        Box     backBox      = new Box(width,height,1);
        Spatial backBoard    = new Geometry("Box", backBox);
        float   buttonWidth  = width/2.25f-15;
        float   buttonHeight = Display.getHeight()/22;
        backBoard.setMaterial(stoneBoardMat);
        
        attach(backBoard);
        setPosition(width,height,.25f);
        
        for (int i = 0; i < choices.size(); i++) {
            
            BitmapText b   = new BitmapText(font);
            String     s   = choices.get(i);
            Box        box = new Box(buttonWidth, buttonHeight,1);
            
            Clickable  g   = new Clickable(s, box) {
                
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
            
            float      x   = -buttonWidth*1.1f;        
            if (i>2)   x   = buttonWidth*1.1f;
            float      y   = height*.65f-(buttonHeight*2+5)*i;
            if (i>2)   y   = height*.65f-(buttonHeight*2+5)*(i-3);
            
            attach(g);
            attach(b);
            g.setLocalTranslation(x, y, 0);
            g.setMaterial(buttonMat);
            g.setName(s);
            b.setText(s);
            b.setLocalTranslation(g.getLocalTranslation().add(-b.getLineWidth()/2, b.getLineHeight(), .1f));
            
        }
        
    }
    
}
