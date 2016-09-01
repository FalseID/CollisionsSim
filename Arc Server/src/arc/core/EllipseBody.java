package arc.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import arc.math2D.Vector;
import arc.sprites.Ellipse;

public class EllipseBody extends Body {
    private double radius;
    
    public EllipseBody(Color color, Vector loc, Vector vel, double mass, double radius){
	    this.sprite = new Ellipse(radius);
	    this.loc = loc;
	    this.vel = vel;
	    this.mass = mass;
	    this.radius = radius;
	    this.Id = this.NextId.incrementAndGet();
	}

    @Override
    public void update() {
		//Update its sprites location with respect to its velocity.
		this.loc.translate(this.vel);
		this.sprite.getNode().setTranslateX(this.loc.getX());
		this.sprite.getNode().setTranslateY(this.loc.getY());
    }
    
    public boolean check_collide(Entity e){
	if (e instanceof EllipseBody){
	   //Subtracting this movement vector from e movement vector
	   //to account for movement of circles relative to each other.
	   Vector v = new Vector(e.getVel().getX() - this.getVel().getX(),
	   e.getVel().getY() - this.getVel().getY());
	   // determine it's size
       
       EllipseBody otherSphere = ((EllipseBody)e);
       EllipseBody thisSphere =  this;
       double v_sqr= v.magnitude_squared();
       //Vector between the centers of colliding circles.
       Vector d = new Vector(otherSphere.getLoc().getX() - thisSphere.getLoc().getX(), 
	       otherSphere.getLoc().getY() - thisSphere.getLoc().getY());
       //Distance between centers.
       double distance = d.magnitude();
       double sumRadii = otherSphere.getRadius() + this.radius;
       double minDist  = distance - sumRadii;
       
       //Checking if movement vector is long enough.
       if (v_sqr >= minDist*minDist){
		   Vector n1 = this.vel.getCopy();
		   n1.Normalize();
		   double dp = d.dot_product(n1);
		   //Checking if direction between movement and vector d indicates collision.
		   if (dp > 0){
		       //More accurate indication of collision.
		       //square root of f is the closest distance between center of a circle 
		       //and the movement vector of the other circle.
		       double f = distance*distance - dp*dp;
		       //is the closest distance enough for the circles to touch each other?
		       if(f < sumRadii*sumRadii){
				   //We have a very likely collision.
				   //sqrt(t) is a side of the right triangle orthogonal to sqrt(f)
				   double T = sumRadii*sumRadii - f;
				   //Can't squareroot a negative number.
				   if(T>=0){
	    			   //Distance one circle has to move along movement vector magnitude to collide.
	    			   //Expensive operation of sqrt.
	    			   double distance_to_collide = dp - Math.sqrt(T);
	    			   //Final test to account for direction
	    			   if(distance_to_collide*distance_to_collide <= this.vel.magnitude_squared()){
	    				   return true;
	    			   }
				   }
		       }
		       
		   }
       }
	}
	return false;
    }
    
    /**
     * Effects of collision are determined here.Rigid, frictionless perfect spheres.
     */
    public void collide(Entity e){
		Vector n1 = new Vector(this.getLoc().getX() - e.getLoc().getX(), 
			this.getLoc().getY() - e.getLoc().getY());
		n1.Normalize();
		Vector n2=n1.getCopy();
		       
		double a1 = this.vel.dot_product(n1);
		double a2 = e.getVel().dot_product(n1);
		double P = 2*(a1-a2)/(this.getMass() + ((Body)e).getMass());
		   
		n1.scale(P*((Body)e).getMass());
		n2.scale(P*this.getMass());
		       
		Vector v1= new Vector(this.vel.getX()-n1.getX(),this.vel.getY()-n1.getY());
		Vector v2= new Vector(e.getVel().getX()+n2.getX(),e.getVel().getY()+n2.getY());
		this.setVel(v1);
		e.setVel(v2);
    }

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
    
    

}
