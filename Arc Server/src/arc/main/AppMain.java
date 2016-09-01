package arc.main;
import java.io.IOException;

import arc.math2D.Vector;
import arc.protocol.EntityEncoder;
import arc.protocol.ServerProtocol;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application {
  
    
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	ArcServer arcWorld = new ArcServer(60, new Vector(640, 580), "Arc Server");
    	EntityEncoder encoder = new EntityEncoder();
    	ServerProtocol arcProtocol = null;
		try {
			arcProtocol = new ServerProtocol("127.0.0.1", 4444, arcWorld, encoder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
    	
        arcWorld.initialize(primaryStage);
        Thread serverthread = new Thread(arcProtocol);
        serverthread.setDaemon(true);
        serverthread.start();
        
        // kick off the game loop
        arcWorld.beginArcLoop();
 
        // display window
        primaryStage.show();
    }
}
