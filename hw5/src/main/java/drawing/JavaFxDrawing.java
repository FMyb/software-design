package drawing;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxDrawing extends Application implements Drawing {
    private final int width = 500;
    private final int height = 500;

    private static GraphicsContext graphicsContext;

    public JavaFxDrawing() {
        if (graphicsContext == null) {
            graphicsContext = new Canvas(width, height).getGraphicsContext2D();
        }
    }

    @Override
    public long width() {
        return width;
    }

    @Override
    public long height() {
        return height;
    }

    @Override
    public void drawCircle(long x, long y, long r) {
        graphicsContext.fillOval(x - (double) r / 2, y - (double) r / 2, r, r);
    }

    @Override
    public void drawLine(long x1, long y1, long x2, long y2) {
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("graph");
        Group root = new Group();
        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        root.getChildren().add(graphicsContext.getCanvas());
        stage.setScene(new Scene(root));
        stage.show();
    }
}
