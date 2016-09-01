package arc.main;

import arc.core.EllipseBody;
import arc.core.Entity;
import arc.core.EntityManager;
import arc.math2D.Vector;
import arc.protocol.ServerProtocol;
import arc.sprites.Ellipse;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArcServer {
    //Primary surface for server interface.
    private Scene arcSurface;
    //Nodes abstract presentation
    private Group arcNodes;
    
    //If true then the server protocol knows to send update information to clients.
    private static boolean UpdateNeccessary;
    
    //Game loop using javaFX API
    private static Timeline arcLoop;
    //Framerate.
    private final int fps;
    public final Vector windowSize;
    private final String windowTitle;
    //Entity Manager
    private final static EntityManager entityManager = new EntityManager();
    
    public ArcServer(final int fps,final Vector windowSize, 
    		final String windowTitle){
		this.fps=fps;
		this.windowSize=windowSize;
		this.windowTitle=windowTitle;
		this.UpdateNeccessary = false;
		
		initArcLoop();
    }
    
    protected final void initArcLoop(){
	final Duration oneFrameAmt = Duration.millis(1000/getFps());
	 final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
		 new EventHandler<ActionEvent>() {
	     		@Override
	                public void handle(ActionEvent event) {
	                    // update actors
	                    updateEntities();
	                    // check for collision
	                    checkCollisions();
	                    // do collisions
	                    DoCollisions();
	                    // removed dead things
	                    //cleanupSprites();
	                }
	        });
	 setGameLoop(TimelineBuilder.create()
	                .cycleCount(Animation.INDEFINITE)
	                .keyFrames(oneFrame)
	                .build());
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
        
        //Test, create entities.
        Scene gameSurface = getGameSurface();
        EllipseBody b = new EllipseBody(Color.RED,new Vector(150,150), new Vector(2,1), 10, 8.00);
        EllipseBody c = new EllipseBody(Color.GREEN,new Vector(142,43), new Vector(3,1), 10, 15.00);
        EllipseBody d = new EllipseBody(Color.BLUE,new Vector(500,361), new Vector(1,2), 30, 38.00);
        EllipseBody e = new EllipseBody(Color.YELLOW,new Vector(58,196), new Vector(-2,1), 10, 6.00);
        EllipseBody f = new EllipseBody(Color.GREY,new Vector(182,25), new Vector(3,-1), 10, 21.00);
        EllipseBody g = new EllipseBody(Color.BLACK,new Vector(217,413), new Vector(-1,-2), 10, 14.00);
        //Add it to the entity manager to be managed.
        //Each entity contains a sprite which contains a node.
        getEntityManager().addEntities(b,c,d,e,f,g);
        getSceneNodes().getChildren().add(0, b.getSprite().node);
        getSceneNodes().getChildren().add(0, c.getSprite().node);
        getSceneNodes().getChildren().add(0, d.getSprite().node);
        getSceneNodes().getChildren().add(0, e.getSprite().node);
        getSceneNodes().getChildren().add(0, f.getSprite().node);
        getSceneNodes().getChildren().add(0, g.getSprite().node);
    }
    
    public void updateEntities(){
		for (Entity e : entityManager.getAllEntities()){
		    e.update();
		    if(e instanceof EllipseBody){
			// bouncing off side walls.
			Circle circle = ((Ellipse)e.getSprite()).getAsCircle();
	             if (e.getSprite().getNode().getTranslateX() > (getGameSurface().getWidth()  -
	            	circle.getRadius()) || e.getSprite().getNode().getTranslateX() < circle.getRadius() ) {
	                 	//Sideways collisions, x coordinate is multiplied by -1.
	            	 	e.setVel(new Vector(e.getVel().getX()*-1, e.getVel().getY()));
	            	 	setUpdateNeccessary(true);
	             }
	             if (e.getSprite().getNode().getTranslateY() > getGameSurface().getHeight()-
	        	     circle.getRadius() || e.getSprite().getNode().getTranslateY() < circle.getRadius()) {
	                 	//Top-bottom collisions, y coordinate is multiplied by -1.
	                 	e.setVel(new Vector(e.getVel().getX(), e.getVel().getY()*-1));
	                 	setUpdateNeccessary(true);
	             }
	    	}
		    
		}
    }
    
    //Most performance expensive.
    public void checkCollisions(){
		entityManager.resetCollisionsToCheck();
        for (Entity e1:entityManager.getCollisionsToCheck()){
            for (Entity e2:entityManager.getAllEntities()){
	        	if((!e1.equals(e2))&&e1.check_collide(e2)){
	        	    entityManager.addEntitiesToBeCollided(e1, e2);
	        	}
            }
        }
    }
    
    public void DoCollisions(){
    	if(!entityManager.getCollisionsToDo().isEmpty()){
	        for (Entity e1:entityManager.getCollisionsToDo().keySet()){
	        	e1.collide(entityManager.getCollisionsToDo().get(e1));
	        }
	        setUpdateNeccessary(true);
    	}
        entityManager.resetCollisionsToDo();
    }
    
   
    /**
     * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
     * onto the game surface(Scene) is a JavaFX Group object.
     * @param sceneNodes The root container having many children nodes
     * to be displayed into the Scene area.
     */
    protected void setSceneNodes(Group sceneNodes) {
        this.arcNodes = sceneNodes;
    }
    
    protected void setArcSurface(Scene arcSurface) {
        this.arcSurface = arcSurface;
    }
    
    protected static void setGameLoop(Timeline arcLoop) {
        ArcServer.arcLoop = arcLoop;
    }
    
    private String getWindowTitle() {
	return this.windowTitle;
    }
    /**Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     */
    
    public int getFps(){
    	return this.fps;
    }
    
    private Scene getGameSurface() {
    	return this.arcSurface;
    }
    
    private Timeline getGameLoop() {
    	return this.arcLoop;
    }
    
    private EntityManager getEntityManager(){
    	return this.entityManager;
    }
    
    //All objects that are rendered.
    public Group getSceneNodes() {
    	return arcNodes;
    }
    
    protected Timeline getArcLoop(){
    	return this.arcLoop;
    }

	public static EntityManager getEntitymanager() {
		return entityManager;
	}

	public synchronized static boolean isUpdateNeccessary() {
		return UpdateNeccessary;
	}

	public synchronized static void setUpdateNeccessary(boolean UpdateNeccessary) {
		ArcServer.UpdateNeccessary = UpdateNeccessary;
	}
    
}
