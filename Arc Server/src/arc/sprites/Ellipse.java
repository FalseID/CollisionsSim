package arc.sprites;

import javafx.scene.shape.Circle;


public class Ellipse extends Sprite {
    protected double radius;
    
    public Ellipse(double radius){
	
    	Circle sphere = new Circle();
		sphere.setRadius(radius);
 
        // set javafx node to a circle
        this.node = sphere;
    }
    
    public Circle getAsCircle(){
    	return (Circle) this.node;
    }
    
}
