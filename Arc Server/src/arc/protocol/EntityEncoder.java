package arc.protocol;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import arc.core.Body;
import arc.core.EllipseBody;
import arc.core.Entity;
import arc.sprites.Ellipse;

public class EntityEncoder {
	
	//!stands for start of a message.
    //Starts with 0 to recognise that it's an initalization string.
    //id:type:x:y:x_vel:y_vel:mass:radius, this info does not need to be sent repeatedly.
    public String entityInitEncode(List<Entity> entities){
    	DecimalFormat df = new DecimalFormat("#.####");
    	DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
    	StringBuilder s = new StringBuilder("!0 ");
    	for(Entity e : entities){
    		if(e instanceof EllipseBody){
    			s.append(e.getId()+":"+"Ellipse"+":"+df.format(e.getLoc().getX())+":"+
    					df.format(e.getLoc().getY())+":"+df.format(e.getVel().getX())+":"+ df.format(e.getVel().getY())+":"+
    					df.format(((Body)e).getMass())+":"+df.format(((Ellipse)e.getSprite()).getAsCircle().getRadius())+ " ");
    		}
    	}
    	s.deleteCharAt(s.length()-1);
    	s.append("#"); //We use this to mark the end of data.
    	return s.toString();
    	
    }
    //id:x:y:x_vel:y_vel, information must be sent repeatedly.
    public String entityUpdateEncode(List<Entity> entities){
    	DecimalFormat df = new DecimalFormat("#.####");
    	DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        df.setNegativePrefix("-");
    	StringBuilder s = new StringBuilder("!1 ");
    	for(Entity e : entities){
    		if(e instanceof EllipseBody){
    			s.append(e.getId()+":"+ df.format(e.getLoc().getX())+":"+
    					df.format(e.getLoc().getY())+":"+df.format(e.getVel().getX())+":"+
    					df.format(e.getVel().getY())+" ");
    		}
    	}
    	s.deleteCharAt(s.length()-1);
    	s.append("#"); //We use this to mark the end of data.
    	return s.toString();
    }

}
