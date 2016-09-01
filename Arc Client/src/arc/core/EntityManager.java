package arc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityManager {
    /** All the entity objects currently in play */
    private final static List<Entity> ARC_ACTORS = new ArrayList<Entity>();
 
    /** A global single threaded list used to check collision against other
     * entity objects.
     */
    private final static List<Entity> CHECK_COLLISION_LIST = new ArrayList<Entity>();
    
    /** A global single threaded list used to do collision against other
     * entity objects.
     */
    private final static Map<Entity,Entity> COLLISION_LIST = new HashMap<Entity,Entity>();
 
    /** A global single threaded set used to cleanup or remove entity objects
     * in play.
     */
    private final static Set<Entity> CLEAN_UP_ENTITIES = new HashSet<Entity>();
 
    /** */
    public List<Entity> getAllEntities() {
        return ARC_ACTORS;
    }
 
    /**
     * VarArgs of entity objects to be added to the simulation.
     * @param entitys
     */
    public void addEntities(Entity... entities) {
        ARC_ACTORS.addAll(Arrays.asList(entities));
    }
    
    public void addEntities(List<Entity> entities) {
        ARC_ACTORS.addAll(entities);
    }
 
    /**
     * VarArgs of entity objects to be removed from the simulation.
     * @param entitys
     */
    public void removeEntities(Entity... entities) {
        ARC_ACTORS.removeAll(Arrays.asList(entities));
    }
    
    public void removeEntities(List<Entity> entities) {
    	ARC_ACTORS.removeAll(entities);
		
	}
 
    /** Returns a set of entity objects to be removed from the ARC_ACTORS.
     * @return CLEAN_UP_entityS
     */
    public Set<Entity> getEntitiesToBeRemoved() {
        return CLEAN_UP_ENTITIES;
    }
 
 /**
     * Adds entity objects to be removed
     * @param entitys varargs of entity objects.
     */
    public void addEntitiesToBeRemoved(Entity... entities) {
        if (entities.length > 1) {
            CLEAN_UP_ENTITIES.addAll(Arrays.asList((Entity[]) entities));
        } else {
            CLEAN_UP_ENTITIES.add(entities[0]);
        }
    }
    
    public void addEntitiesToBeRemoved(List<Entity> entities) {
        CLEAN_UP_ENTITIES.addAll(entities);
    }
    
    /**
     * Adds entity objects to be collided.
     * Will check for duplicates and ignore them.
     * @param entitys varargs of entity objects.
     */
    public void addEntitiesToBeCollided(Entity a, Entity b) {
	if(!COLLISION_LIST.containsKey(b)){
        	COLLISION_LIST.put(a,b);
        }
    }
 
    /**
     * Returns a list of entity objects to assist in collision checks.
     * This is a temporary and is a copy of all current entity objects
     * (copy of ARC_ACTORS).
     * @return CHECK_COLLISION_LIST
     */
    public List<Entity> getCollisionsToCheck() {
        return CHECK_COLLISION_LIST;
    }
    
    /**
     * Returns a list of entity objects to assist in collisions.
     * This is a temporary and is a copy of all current entity objects
     * (copy of ARC_ACTORS).
     * @return CHECK_COLLISION_LIST
     */
    public Map<Entity,Entity> getCollisionsToDo() {
        return COLLISION_LIST;
    }
    
 
    /**
     * Clears the list of entity objects in the collision check collection
     * (CHECK_COLLISION_LIST).
     */
    public void resetCollisionsToCheck() {
        CHECK_COLLISION_LIST.clear();
        CHECK_COLLISION_LIST.addAll(ARC_ACTORS);
    }
 
    /**
     * Clears the list of entity objects in the collision collection
     * (CHECK_COLLISION_LIST).
     */
    public void resetCollisionsToDo() {
        COLLISION_LIST.clear();
    }
 
    /**
     * Removes entity objects and nodes from all
     * temporary collections such as:
     * CLEAN_UP_ENTITIES.
     * The entity to be removed will also be removed from the
     * list of all entity objects called (ARC_ACTORS).
     */
    public void cleanupEntities() {
 
        // remove from actors list
        ARC_ACTORS.removeAll(CLEAN_UP_ENTITIES);
 
        // reset the clean up entities.
        CLEAN_UP_ENTITIES.clear();
    }

	
}
