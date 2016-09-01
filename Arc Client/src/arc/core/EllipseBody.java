package arc.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import arc.math2D.Vector;
import arc.sprites.Ellipse;

public class EllipseBody extends Body {
    private double radius;
    
    public EllipseBody(Color color, int id, Vector loc,Vector vel, double mass, double radius){
	    this.sprite = new Ellipse(radius, color);
	    this.loc = loc;
	    this.vel = vel;
	    this.mass = mass;
	    this.radius = radius;
	    this.id = id;
	}

    @Override
    public void update() {
	//Update its sprites location with respect to its velocity.
	this.loc.translate(this.vel);
	this.sprite.getNode().setTranslateX(this.loc.getX());
	this.sprite.getNode().setTranslateY(this.loc.getY());
	
    }
		
}
