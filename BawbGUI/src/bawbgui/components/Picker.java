/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bawbgui.components;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bob
 */
public class Picker {
    
    public InputManager inputManager;
    
    public Picker(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    public boolean checkContact(Spatial s) {
        
        float x = inputManager.getCursorPosition().x;
        float y = inputManager.getCursorPosition().y;
        
        CollisionResults results = new CollisionResults();
        Ray              ray     = new Ray(new Vector3f(x,y,0), new Vector3f(0,0,1));        
        
        s.collideWith(ray, results);
        
        boolean hit = results.size() > 0;
        
        return hit;        
        
    }
    
    private Geometry findGeometry(Spatial s) {
        
        if (s instanceof Geometry) return (Geometry) s;
        
        if (s instanceof Node) for (int i = 0; i < ((Node) s).getChildren().size(); i++) {
            Spatial c = (Node) ((Node) s).getChild(i);
            
            if (c instanceof Geometry) return (Geometry) c;
            if (c instanceof Node) return findGeometry(c);
            
        }
        
        return null;
        
    }
    
}
