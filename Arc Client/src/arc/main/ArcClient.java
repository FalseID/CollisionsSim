package arc.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arc.core.EllipseBody;
import arc.core.Entity;
import arc.core.EntityManager;
import arc.math2D.Vector;
import arc.sprites.Ellipse;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArcClient {
    //Primary surface for the simulation.
    private Scene arcSurface;
    //Nodes displayed in the game window.
    private static Group arcNodes;
    //Framerate.
    private final int fps;
    public final Vector windowSize;
    private final String windowTitle;
    //Entity Manager
    private final static EntityManager entityManager = new EntityManager();
	private static Timeline arcLoop;
    
    public ArcClient(final int fps,final Vector windowSize, 
    		final String windowTitle){
		this.fps=fps;
		this.windowSize=windowSize;
		this.windowTitle=windowTitle;
		
		initArcLoop();
    }
    
    protected final void initArcLoop(){
	final Duration oneFrameAmt = Duration.millis(1000/getFps());
	 final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
		 new EventHandler<ActionEvent>() {
	     		@Override
	                public void handle(ActionEvent event) {
	     				//Update movement according to information from the server
	     				moveEntities();
	     				removeEntities();
	                }
	        });
	 setGameLoop(TimelineBuilder.create()
	                .cycleCount(Animation.INDEFINITE)
	                .keyFrames(oneFrame)
	                .build());
    }
    
    protected void removeEntities() {
		entityManager.cleanupEntities();
		
	}

	public void beginArcLoop() {
        getArcLoop().play();
    }
    
    public void initialize(final Stage primaryStage) {
        // Sets the window title
        primaryStage.setTitle(getWindowTitle());
 
        // Create the scene
        setSceneNodes(new Group());
        setArcSurface(new Scene(getSceneNodes(), windowSize.getX(), windowSize.getY()));
        primaryStage.setScene(getGameSurface());
    }
    
    public static void InitEntities(List<Entity> entity_list){
    	entityManager.addEntities(entity_list);
    	for(Entity e : entityManager.getAllEntities()){
    		getSceneNodes().getChildren().add(0, e.getSprite().node);
    	}
    }
    
    public void moveEntities(){
    	for (Entity e : entityManager.getAllEntities()){
    	    e.update();
        }
    }
    
    public static void updateEntities(Map<Integer, List<Vector>> entity_update){
    	//Updates entity velocity vector with information from the server.
    	for(Entity e : entityManager.getAllEntities()){
    		e.setLoc(entity_update.get(e.getId()).get(0));
    		e.setVel(entity_update.get(e.getId()).get(1));
    	}
    }
    
    public synchronized static void clearAll(){
    	entityManager.addEntitiesToBeRemoved(entityManager.getAllEntities());
    }
    
    protected void setSceneNodes(Group sceneNodes) {
        this.arcNodes = sceneNodes;
    }
    
    protected void setArcSurface(Scene arcSurface) {
        this.arcSurface = arcSurface;
    }
    
    protected static void setGameLoop(Timeline arcLoop) {
        ArcClient.arcLoop = arcLoop;
    }
    
    private String getWindowTitle() {
    	return this.windowTitle;
    }
    
    public int getFps(){
    	return this.fps;
    }
    
    private Scene getGameSurface() {
    	return this.arcSurface;
    }
    
    //All objects that are rendered.
    public static Group getSceneNodes() {
    	return arcNodes;
    }
    
    protected Timeline getArcLoop(){
    	return this.arcLoop;
    }
    
    
}
