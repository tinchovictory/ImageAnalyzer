package ar.edu.itba.ati.GUI;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SelectableAreaFactory {
    private ResizableRectangle resizableRectangle;
    private Point rectStart;
    private boolean isSelecting;
    private Group selectionGroup;
    private MainWindow mainWindow;

    public SelectableAreaFactory(MainWindow mainWindow) {
        resizableRectangle = null;
        rectStart = null;
        isSelecting = false;
        selectionGroup = new Group();
        this.mainWindow = mainWindow;
        mainWindow.addToStackPane(selectionGroup);

        // Set event handlers
        setAllHandlers();
    }

    public void startSelection() {
        isSelecting = true;
    }

    public void stopSelection() {
        isSelecting = false;
        // clear selection
        selectionGroup.getChildren().clear();
    }

    public List<Point> getSelection() {
        List<Point> ans = new ArrayList<>();

        Bounds bounds = resizableRectangle.getBoundsInParent();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        double xScale = mainWindow.getImageView().getLayoutBounds().getWidth() / mainWindow.getImageView().getImage().getWidth();
        double yScale = mainWindow.getImageView().getLayoutBounds().getHeight() / mainWindow.getImageView().getImage().getHeight();

        Point p1 = new Point((int) (bounds.getMinX() / xScale), (int) (bounds.getMinY() / yScale));
        Point p2 = new Point((int) ((bounds.getMinX() + width) / xScale), (int) ((bounds.getMinY() + height) / yScale));

        ans.add(p1);
        ans.add(p2);
        return ans;
    }



    /* Event handlers */
    private void setAllHandlers() {
        this.mainWindow.getImageView().addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        this.mainWindow.getImageView().addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        this.mainWindow.getImageView().addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        if (event.isSecondaryButtonDown())
            return;
        if(!isSelecting) {
            return;
        }

        rectStart = new Point(event.getX(), event.getY());

        selectionGroup.getChildren().clear();

        resizableRectangle = new ResizableRectangle(rectStart.x, rectStart.y, 0, 0, selectionGroup);

        darkenOutsideRectangle(resizableRectangle);
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        if (event.isSecondaryButtonDown())
            return;

        if(!isSelecting) {
            return;
        }

        double offsetX = event.getX() - rectStart.x;
        double offsetY = event.getY() - rectStart.y;

        if (offsetX > 0) {
            if (event.getX() > mainWindow.getImageView().getFitWidth())
                resizableRectangle.setWidth(mainWindow.getImageView().getFitWidth() - rectStart.x);
            else
                resizableRectangle.setWidth(offsetX);
        } else {
            if (event.getX() < 0)
                resizableRectangle.setX(0);
            else
                resizableRectangle.setX(event.getX());
            resizableRectangle.setWidth(rectStart.x - resizableRectangle.getX());
        }

        if (offsetY > 0) {
            if (event.getY() > mainWindow.getImageView().getFitHeight())
                resizableRectangle.setHeight(mainWindow.getImageView().getFitHeight() - rectStart.y);
            else
                resizableRectangle.setHeight(offsetY);
        } else {
            if (event.getY() < 0)
                resizableRectangle.setY(0);
            else
                resizableRectangle.setY(event.getY());
            resizableRectangle.setHeight(rectStart.y - resizableRectangle.getY());
        }

    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
/*        if (selectionRectangle != null)
            isAreaSelected = true;*/
    };


    private void darkenOutsideRectangle(Rectangle rectangle) {
        Color darkAreaColor = new Color(0, 0, 0, 0.5);

        Rectangle darkAreaTop = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaLeft = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaRight = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaBottom = new Rectangle(0, 0, darkAreaColor);

        darkAreaTop.widthProperty().bind(mainWindow.getContainer().widthProperty());
        darkAreaTop.heightProperty().bind(rectangle.yProperty());

        darkAreaLeft.yProperty().bind(rectangle.yProperty());
        darkAreaLeft.widthProperty().bind(rectangle.xProperty());
        darkAreaLeft.heightProperty().bind(rectangle.heightProperty());

        darkAreaRight.xProperty().bind(rectangle.xProperty().add(rectangle.widthProperty()));
        darkAreaRight.yProperty().bind(rectangle.yProperty());
        darkAreaRight.widthProperty().bind(mainWindow.getContainer().widthProperty().subtract(
                rectangle.xProperty().add(rectangle.widthProperty())));
        darkAreaRight.heightProperty().bind(rectangle.heightProperty());

        darkAreaBottom.yProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        darkAreaBottom.widthProperty().bind(mainWindow.getContainer().widthProperty());
        darkAreaBottom.heightProperty().bind(mainWindow.getContainer().heightProperty().subtract(
                rectangle.yProperty().add(rectangle.heightProperty())));

        // adding dark area rectangles before the selectionRectangle. So it can't overlap rectangle
        selectionGroup.getChildren().add(1, darkAreaTop);
        selectionGroup.getChildren().add(1, darkAreaLeft);
        selectionGroup.getChildren().add(1, darkAreaBottom);
        selectionGroup.getChildren().add(1, darkAreaRight);

        // make dark area container layer as well
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaRight.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaRight.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaRight.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }


    private class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

}
