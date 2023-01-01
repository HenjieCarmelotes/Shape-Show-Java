package shapeshow;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;

public class ShapeShow extends Application {
	
    private Shape[] shapes = new Shape[10];  // Contains shapes the user has drawn. It throws error when shapes drawn exceed 10.
    private int shapeCount = 0; // Number of shapes that the user has drawn.
    private Canvas canvas; // The drawing area where the user draws.
    private Color currentColor = Color.RED;  // Color to be used for new shapes.

    /**
     * A main routine that simply runs this application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    //--------------------- Methods for creating the GUI -------------------------
    
    /**
     * This method is required for any JavaFX Application.  It adds content to
     * the window (given by the parameter, stage) and shows the window.
     */
    public void start(Stage stage) {
        canvas = makeCanvas();
        paintCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setBottom(makeToolPanel(canvas));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Click buttons to add shapes; drag shapes with your mouse"); 
        stage.setResizable(false);
        stage.show();
    }

    private Canvas makeCanvas() {
            // Creates a canvas, and add mouse listeners to implement dragging.
            // The listeners are given by methods that are defined below.
        Canvas canvas = new Canvas(800,600);
        canvas.setOnMousePressed( this::mousePressed );
        canvas.setOnMouseReleased( this::mouseReleased );
        canvas.setOnMouseDragged( this::mouseDragged );
        return canvas;
    }

    private HBox makeToolPanel(Canvas canvas) {
            // Make a pane containing the buttons that are used to add shapes
            // and the pop-up menu (drop-down menu) for selecting the current color.
        Button ovalButton = new Button("Add an Oval");
        ovalButton.setOnAction( (e) -> addShape( new Oval() ) );
        Button rectButton = new Button("Add a Rect");
        rectButton.setOnAction( (e) -> addShape( new Rectangle() ) );
        Button roundRectButton = new Button("Add a RoundRect");
        roundRectButton.setOnAction( (e) -> addShape( new RoundRectangle() ) );
        ComboBox<String> combobox = new ComboBox<>();
        combobox.setEditable(false);
        Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, 
                Color.MAGENTA, Color.YELLOW, Color.BLACK, Color.WHITE };
        String[] colorNames = { "Red", "Green", "Blue", "Cyan", 
                "Magenta", "Yellow", "Black", "White" };
        combobox.getItems().addAll(colorNames);
        combobox.setValue("Red");
        combobox.setOnAction( 
                e -> currentColor = colors[combobox.getSelectionModel().getSelectedIndex()] );        
        HBox tools = new HBox(5); //set the distance of buttons
        tools.getChildren().add(ovalButton);
        tools.getChildren().add(rectButton);
        tools.getChildren().add(roundRectButton);
        tools.getChildren().add(combobox);
        tools.setStyle("-fx-border-width: 5px; -fx-border-color: transparent; -fx-background-color: lightgray");
        return tools;
    }

    private void paintCanvas() {
            // Redraw the shapes.  The entire list of shapes
            // is redrawn whenever the user adds a new shape
            // or moves an existing shape.
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE); // Fill with white background.
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (int i = 0; i < shapeCount; i++) {
            Shape s = shapes[i];
            s.draw(g);
        }
    }

    private void addShape(Shape shape) {
            // Add the shape to the canvas, and set its size/position and color.
            // The shape is added at the top-left corner, with size 80-by-50.
            // Then redraw the canvas to show the newly added shape.  This method
            // is used in the event listeners for the buttons in makeToolsPanel().
        shape.setColor(currentColor);
        shape.reshape(250,300,150,100);
        shapes[shapeCount] = shape;
        shapeCount++;
        paintCanvas();
    }

    
    // ------------ This part of the class defines methods to implement dragging -----------
    // -------------- These methods are added to the canvas as event listeners -------------

    private Shape shapeBeingDragged = null;  // This is null unless a shape is being dragged.
                                             // A non-null value is used as a signal that dragging
                                             // is in progress, as well as indicating which shape
                                             // is being dragged.

    private int prevDragX;  // During dragging, these record the x and y coordinates of the
    private int prevDragY;  //    previous position of the mouse.

    private void mousePressed(MouseEvent evt) {
            // User has pressed the mouse.  Find the shape that the user has clicked on, if
            // any.  If there is a shape at the position when the mouse was clicked, then
            // start dragging it.  If the user was holding down the shift key, then bring
            // the dragged shape to the front, in front of all the other shapes.
        int x = (int)evt.getX();  // x-coordinate of point where mouse was clicked
        int y = (int)evt.getY();  // y-coordinate of point 
        for ( int i = shapeCount - 1; i >= 0; i-- ) {  // check shapes from front to back
            Shape s = shapes[i];
            if (s.containsPoint(x,y)) {
                shapeBeingDragged = s;
                prevDragX = x;
                prevDragY = y;
                if (evt.isShiftDown()) { // s should be moved on top of all the other shapes
                    for (int j = i; j < shapeCount-1; j++) {
                            // move the shapes following s down in the list
                        shapes[j] = shapes[j+1];
                    }
                    shapes[shapeCount-1] = s;  // put s at the end of the list
                    paintCanvas();  // repaint canvas to show s in front of other shapes
                }
                return;
            }
        }
    }

    private void mouseDragged(MouseEvent evt) {
            // User has moved the mouse.  Move the dragged shape by the same amount.
        int x = (int)evt.getX();
        int y = (int)evt.getY();
        if (shapeBeingDragged != null) {
            shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
            prevDragX = x;
            prevDragY = y;
            paintCanvas();      // redraw canvas to show shape in new position
        }
    }

    private void mouseReleased(MouseEvent evt) {
            // User has released the mouse.  Move the dragged shape, then set
            // shapeBeingDragged to null to indicate that dragging is over.
        shapeBeingDragged = null;
    }
	
	

} //end of class ShapeShow
