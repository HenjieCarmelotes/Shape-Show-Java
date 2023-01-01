package shapeshow;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Shape {
	   // This class represents rectangle shapes.
    void draw(GraphicsContext g) {
        g.setFill(color);
        g.fillRect(left,top,width,height);
        g.setStroke(Color.BLACK);
        g.strokeRect(left,top,width,height);
    }

} //end of class Rectangle
