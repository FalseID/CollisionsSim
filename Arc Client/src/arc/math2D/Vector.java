package arc.math2D;

public class Vector {
    public double x;
    public double y;
    
    public Vector(double x,double y){
	this.x=x;
	this.y=y;
    }
    
    public Vector(int x,int y){
	this.x=(double)x;
	this.y=(double)y;
    }
    
    public double magnitude_squared(){
	return dot_product(this);
    }
    
    //Much slower.
    public double magnitude(){
	return Math.sqrt(dot_product(this));
    }
    
    public boolean equals(Vector v){
	if (this.x == v.getX() && this.y == v.getY()){
	    return true;
	}
	return false;
    }
    
    //Very fast normalize.
    public void Normalize(){
	double c = arc.math2D.Util.fast_isqrt(magnitude_squared());
	scale(c);
    }
    
    public Vector getCopy(){
	return new Vector(this.x, this.y);
    }
    
    public double dot_product(Vector other){
	return this.x*other.getX() + this.y*other.getY();
    }
    
    public void translate(Vector v){
	this.x += v.getX();
	this.y += v.getY();
    }
    
    public void scale(double c){
	this.x *= c;
	this.y *= c;
    }
    

    @Override
    public String toString() {
	return "Vector [x=" + x + ", y=" + y + "]";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    
}
