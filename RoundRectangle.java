package shapeshow;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RoundRectangle extends Shape {
	
		// This class represents rectangle shapes with rounded corners.
	    // (Note that it uses the inherited version of the 
	    // containsPoint(x,y) method, even though that is not perfectly
	    // accurate when (x,y) is near one of the corners.)
	
	void draw(GraphicsContext g) {
	    g.setFill(color);
	    g.fillRoundRect(left,top,width,height,width/3,height/3);
	    g.setStroke(Color.BLACK);
	    g.strokeRoundRect(left,top,width,height,width/3,height/3);
	}
	

}
