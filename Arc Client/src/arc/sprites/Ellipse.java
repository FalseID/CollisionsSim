package arc.sprites;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;


public class Ellipse extends Sprite {
    protected double radius;
    
    public Ellipse(double radius, Color fill){
	
	Circle sphere = new Circle();
	sphere.setRadius(radius);
	sphere.setVisible(true);
        sphere.setFill(fill);
 
        // set javafx node to a circle
        this.node = sphere;
    }
    
    public Circle getAsCircle(){
	return (Circle) this.node;
    }
    
}
