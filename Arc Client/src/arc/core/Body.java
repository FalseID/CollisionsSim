package arc.core;

public abstract class Body extends Entity {
    protected double mass;

	public double getMass() {
	    return mass;
	}

	public void setMass(double mass) {
	    this.mass = mass;
	}
}
