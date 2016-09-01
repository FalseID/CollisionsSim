package arc.core;

public abstract class Body extends Entity {
    protected double mass;
    	
    public abstract boolean check_collide(Entity e);
    public abstract void collide(Entity e);

	public double getMass() {
	    return mass;
	}

	public void setMass(double mass) {
	    this.mass = mass;
	}
}
