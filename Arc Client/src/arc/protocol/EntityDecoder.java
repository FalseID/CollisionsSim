package arc.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;
import arc.core.EllipseBody;
import arc.core.Entity;
import arc.main.ArcClient;
import arc.math2D.Vector;

/*
 * Decodes code by EntityEncoder.
 */
public class EntityDecoder {
	
	public List<Entity> entityInitDecode(String code){
		ArrayList<Entity> entity_list = new ArrayList<Entity>();
    	for (String object : code.substring(3) .split(" ")){
    		String[] split = object.split(":");
    		if(split[1].equals("Ellipse")){
    			
    			int id = Integer.parseInt(split[0]);
    			Vector loc = new Vector(Double.parseDouble(split[2]),Double.parseDouble(split[3]));
    			Vector vel = new Vector(Double.parseDouble(split[4]), Double.parseDouble(split[5]));
    			double mass = Double.parseDouble(split[6]);
    			double radius = Double.parseDouble(split[7]);
    			
	    		entity_list.add(new EllipseBody(Color.RED, id, loc, vel, mass, radius));
    		}
    	}
    	return entity_list;
		
	}
    	
    Map<Integer, List<Vector>> entityUpdateDecode(String code){
    	HashMap<Integer, List<Vector>> entity_update = new HashMap<Integer, List<Vector>>();
		for (String object : code.substring(3).split(" ")){
			String[] split = object.split(":");
			
			int id = Integer.parseInt(split[0]);
			Vector loc = new Vector(Double.parseDouble(split[1]), Double.parseDouble(split[2]));
			Vector vel = new Vector(Double.parseDouble(split[3]), Double.parseDouble(split[4]));
			
			entity_update.put(id, Arrays.asList(loc, vel));
		}
		return entity_update;
    }
}
