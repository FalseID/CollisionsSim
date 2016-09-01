package arc.core;

import java.util.concurrent.atomic.AtomicInteger;

import arc.math2D.Vector;
import arc.sprites.Sprite;
import javafx.scene.Node;

public abstract class Entity {
	//Unique Id for the entity.
	protected static AtomicInteger NextId = new AtomicInteger();
	protected int Id;
	
    //Graphical represenation of entity.
    public Sprite sprite;
    
    //Location vector
    protected Vector loc;
    
    //Velocity vector
    protected Vector vel;
    
    //Is the the entity dead.
    public boolean isdead = false;
    
    public abstract void update();
    
    public abstract boolean check_collide(Entity e);
    public abstract void collide(Entity e);
    
    public Sprite getSprite() {
        return sprite;
    }
    
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    
    public Vector getLoc() {
        return loc;
    }
    
    public void setLoc(Vector loc) {
        this.loc = loc;
    }
    
    public Vector getVel() {
        return vel;
    }
    
    public void setVel(Vector vel) {
        this.vel = vel;
    }
    
    public boolean isIsdead() {
        return isdead;
    }
    
    public void setIsdead(boolean isdead) {
        this.isdead = isdead;
    }

	public int getId() {
		return this.Id;
	}
    
    

}
