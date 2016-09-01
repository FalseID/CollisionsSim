package arc.math2D;

public class Util {
    
    public static boolean isLinearlyIndependent(Vector a, Vector b){
	
	/*
	 * |<x1,y1>| Find determinant to check if rows are linearly independent.
	 * |<x2,y2>|
	 */
	if (a.getX()*b.getY() - b.getX()*a.getY() == 0){
	    return false;
	}
	else return true;
    }
    
    
    //Faster at the expense of accuracy.
    public static double fast_isqrt(double x){
	    double xhalf = 0.5d*x;
	    long i = Double.doubleToLongBits(x);
	    i = 0x5fe6ec85e7de30daL - (i>>1);
	    x = Double.longBitsToDouble(i);
	    x = x*(1.5d - xhalf*x*x);
	    return x;
    }

}
