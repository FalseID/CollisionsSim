package arc.main;
import arc.math2D.Vector;
import arc.protocol.ClientProtocol;
import arc.protocol.EntityDecoder;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application {
  
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	ArcClient arcClient = new ArcClient(60, new Vector(640, 580), "Arc Client");
    	EntityDecoder decoder = new EntityDecoder();
        ClientProtocol arcProtocol = new ClientProtocol("127.0.0.1", 4444, arcClient, decoder);
        arcClient.initialize(primaryStage);
        
        // setup communication with server
        Thread clientthread = new Thread(arcProtocol);
        clientthread.setDaemon(true);
        clientthread.start();
        
        // kick off the game loop
        arcClient.beginArcLoop();
 
        // display window
        primaryStage.show();
    }
}
