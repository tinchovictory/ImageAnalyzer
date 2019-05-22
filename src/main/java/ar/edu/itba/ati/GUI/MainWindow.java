package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainWindow {


    private Stage stage;

    private ImageView imageView;

    private Controller controller;

    private BorderPane container;

    private StackPane stackPane;

    private boolean isAreaSelected;

    private BufferedImage mainImage;

    private final AreaSelection areaSelection;

    private final Group selectionGroup;


    public ImageView getImageView() {
        return imageView;
    }

    public BorderPane getContainer() {
        return container;
    }

    public MainWindow(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        container = new BorderPane();
        this.isAreaSelected = false;
        controller.setMainWindow(this);

        container.setPrefHeight(600);
        container.setPrefWidth(800);

        imageView.fitHeightProperty().bind(container.heightProperty());
        imageView.fitWidthProperty().bind(container.widthProperty());

        areaSelection = new AreaSelection();
        selectionGroup = new Group();

        stackPane = new StackPane();

        selectionGroup.getChildren().add(imageView);
        stackPane.getChildren().addAll(selectionGroup);

        MenuBar menuBar = getMenuBar();
        menuBar.getMenus().add(getSelectionMenu());
        container.setTop(menuBar);
        container.setCenter(stackPane);


        stage.setScene(new Scene(container, 800, 600));

    }

    public void addToStackPane(Group g) {
        stackPane.getChildren().add(g);
    }

    private void clearSelection(Group group) {
        //deletes everything except for base container layer
        isAreaSelected = false;
        group.getChildren().remove(1, group.getChildren().size());

    }

    public MenuBar getMenuBar() {
        return new ar.edu.itba.ati.GUI.MenuBar.MenuBar(controller);

    }

    private Menu getSelectionMenu() {
        Menu selection = new Menu("Selection");
        MenuItem select = new MenuItem("Select area");
        select.setOnAction(event -> areaSelection.selectArea(selectionGroup));

        MenuItem clear = new MenuItem("Clear selection");
        clear.setOnAction(event -> {
            clearSelection(selectionGroup);
        });
        clear.disableProperty().bind(Bindings.createBooleanBinding(() -> isAreaSelected));


        MenuItem crop = new MenuItem("Crop image");
        crop.setOnAction(e -> {
            if (isAreaSelected)
                cropImage();
        });
        crop.disableProperty().bind(Bindings.createBooleanBinding(() -> isAreaSelected));
        selection.getItems().addAll(select, clear, crop);
        return selection;
    }

    private void cropImage() {

        Bounds bounds = areaSelection.selectArea(selectionGroup).getBoundsInParent();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        double xScale = imageView.getLayoutBounds().getWidth() / imageView.getImage().getWidth();
        double yScale = imageView.getLayoutBounds().getHeight() / imageView.getImage().getHeight();

        Point p1 = new Point((int) (bounds.getMinX() / xScale), (int) (bounds.getMinY() / yScale));
        Point p2 = new Point((int) ((bounds.getMinX() + width) / xScale), (int) ((bounds.getMinY() + height) / yScale));

        System.out.println("P1" + p1);
        System.out.println("P2" + p2);
        controller.cropImage(p1, p2);
        clearSelection(selectionGroup);
        refreshImage();


    }


    public void openRawImage(int width, int height, File image) {
        controller.loadRawImage(image, width, height);
        refreshImage();
    }

    public void loadImage() {
        FileChooser chooser = new FileChooser();

        File image = chooser.showOpenDialog(stage);
        if (image != null) {
            try {
                controller.loadImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Load Image failed");
                return;
            }
            refreshImage();
        }
    }

    public Stage getStage(){
        return stage;
    }
    public void saveFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(stage);
        controller.saveImage(file);
    }


    public ImageView getImage(BufferedImage bufferedImage) {
        ImageView iv = new ImageView();
        iv.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        return iv;
    }



    public void refreshImage() {
        mainImage = controller.getImage();
        imageView.setImage(SwingFXUtils.toFXImage(mainImage, null));
    }


    private class AreaSelection {

        private Group group;

        private ResizableRectangle selectionRectangle = null;
        private double rectangleStartX;
        private double rectangleStartY;
        private javafx.scene.paint.Paint darkAreaColor = javafx.scene.paint.Color.color(0, 0, 0, 0.5);

        private ResizableRectangle selectArea(Group group) {
            this.group = group;

            // group.getChildren().get(0) == mainImageView. We assume image view as base container layer
            if (imageView != null && mainImage != null) {
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
            }

            return selectionRectangle;
        }

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            if (event.isSecondaryButtonDown())
                return;

            rectangleStartX = event.getX();
            rectangleStartY = event.getY();

            clearSelection(group);

            selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, group);

            darkenOutsideRectangle(selectionRectangle);

        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            if (event.isSecondaryButtonDown())
                return;

            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;

            if (offsetX > 0) {
                if (event.getX() > imageView.getFitWidth())
                    selectionRectangle.setWidth(imageView.getFitWidth() - rectangleStartX);
                else
                    selectionRectangle.setWidth(offsetX);
            } else {
                if (event.getX() < 0)
                    selectionRectangle.setX(0);
                else
                    selectionRectangle.setX(event.getX());
                selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
            }

            if (offsetY > 0) {
                if (event.getY() > imageView.getFitHeight())
                    selectionRectangle.setHeight(imageView.getFitHeight() - rectangleStartY);
                else
                    selectionRectangle.setHeight(offsetY);
            } else {
                if (event.getY() < 0)
                    selectionRectangle.setY(0);
                else
                    selectionRectangle.setY(event.getY());
                selectionRectangle.setHeight(rectangleStartY - selectionRectangle.getY());
            }

        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            if (selectionRectangle != null)
                isAreaSelected = true;
        };


        private void darkenOutsideRectangle(Rectangle rectangle) {
            Rectangle darkAreaTop = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaLeft = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaRight = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaBottom = new Rectangle(0, 0, darkAreaColor);

            darkAreaTop.widthProperty().bind(container.widthProperty());
            darkAreaTop.heightProperty().bind(rectangle.yProperty());

            darkAreaLeft.yProperty().bind(rectangle.yProperty());
            darkAreaLeft.widthProperty().bind(rectangle.xProperty());
            darkAreaLeft.heightProperty().bind(rectangle.heightProperty());

            darkAreaRight.xProperty().bind(rectangle.xProperty().add(rectangle.widthProperty()));
            darkAreaRight.yProperty().bind(rectangle.yProperty());
            darkAreaRight.widthProperty().bind(container.widthProperty().subtract(
                    rectangle.xProperty().add(rectangle.widthProperty())));
            darkAreaRight.heightProperty().bind(rectangle.heightProperty());

            darkAreaBottom.yProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
            darkAreaBottom.widthProperty().bind(container.widthProperty());
            darkAreaBottom.heightProperty().bind(container.heightProperty().subtract(
                    rectangle.yProperty().add(rectangle.heightProperty())));

            // adding dark area rectangles before the selectionRectangle. So it can't overlap rectangle
            group.getChildren().add(1, darkAreaTop);
            group.getChildren().add(1, darkAreaLeft);
            group.getChildren().add(1, darkAreaBottom);
            group.getChildren().add(1, darkAreaRight);

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
    }
}
