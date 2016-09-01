package arc.sprites;

import javafx.scene.Node;

//Graphical representation of entities.

public abstract class Sprite {
    public Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    
    

}
