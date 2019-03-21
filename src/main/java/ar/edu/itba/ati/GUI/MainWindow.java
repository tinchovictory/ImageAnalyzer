package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import java.io.IOException;

public class MainWindow {


    private Stage stage;

    private ImageView imageView;

    private Controller controller;

    private BorderPane container;

    private ScrollPane stackPane;

    private boolean isAreaSelected;

    private BufferedImage mainImage;

    final AreaSelection areaSelection;

    final Group selectionGroup;



    public MainWindow(Stage stage, Controller controller) {
        this.stage = stage;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        container = new BorderPane();
        this.isAreaSelected = false;


        container.setPrefHeight(600);
        container.setPrefWidth(800);

        imageView.fitHeightProperty().bind(container.heightProperty());
        imageView.fitWidthProperty().bind(container.widthProperty());

        areaSelection = new AreaSelection();
        selectionGroup = new Group();


        stackPane = new ScrollPane();

        selectionGroup.getChildren().add(imageView);
        stackPane.setContent(selectionGroup);

        MenuBar menuBar=  getMenuBar();
        container.setTop(menuBar);
        container.setCenter(stackPane);
        this.controller = controller;


        stage.setScene(new Scene(container,800,600));

    }

    private void clearSelection(Group group) {
        //deletes everything except for base container layer
        isAreaSelected = false;
        group.getChildren().remove(1,group.getChildren().size());

    }

    public MenuBar getMenuBar(){

        Menu file = new Menu("File");

        MenuItem openImage= new MenuItem("Open Image");
        openImage.setOnAction(e->loadImage());

        MenuItem openImageRAW = new MenuItem("Open RAW Image");
        openImageRAW.setOnAction(e-> openRawImage());
        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setOnAction(e-> saveFile());

        file.getItems().addAll(openImage,openImageRAW,saveImage);



        Menu tools = new Menu("Tools");

        MenuItem getPixel = new MenuItem("Get Pixel value");
        getPixel.setOnAction(e->showGetPixelModal());

        MenuItem modifyPixel= new MenuItem("Modify pixel value");
        modifyPixel.setOnAction(e-> showEditPixelModal());

        MenuItem getMean = new MenuItem("Get mean");
        getMean.setOnAction(e-> showMeanModal());

        MenuItem channelBands = new MenuItem("Show channel bands");
        channelBands.setOnAction(e-> showChannelBands());
        


        tools.getItems().addAll(getPixel,modifyPixel,getMean,channelBands);


        Menu create = new Menu("Generate");
        MenuItem circle = new MenuItem("Circle");
        circle.setOnAction(e-> {
            controller.createCircle();
            refreshImage();
        });
        MenuItem square = new MenuItem("Square");
        square.setOnAction(e->{
            controller.createSquare();
            refreshImage();
        });

        MenuItem colorGradient = new MenuItem("Color gradient");
        colorGradient.setOnAction(e-> {
            controller.createColorGradient();
            refreshImage();
        });
        MenuItem greyGradient = new MenuItem("Grey gradient");
        greyGradient.setOnAction(e ->{
            controller.createGreyGradient();
            refreshImage();
        });


        create.getItems().addAll(circle,square,colorGradient,greyGradient);

        Menu selection = new Menu("Selection");
        MenuItem select = new MenuItem("Select area");
        select.setOnAction(event -> areaSelection.selectArea(selectionGroup));

        MenuItem clear = new MenuItem("Clear selection");
        clear.setOnAction(event -> {
            clearSelection(selectionGroup);
        });
        clear.disableProperty().bind(Bindings.createBooleanBinding(()->isAreaSelected));


        MenuItem crop = new MenuItem("Crop image");
        crop.setOnAction(e-> {
            if (isAreaSelected)
                cropImage();
        });
        crop.disableProperty().bind(Bindings.createBooleanBinding(()->isAreaSelected));
        selection.getItems().addAll(select,clear,crop);



        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,create,tools,selection);
        return menuBar;

    }

    private void cropImage() {

        Bounds bounds =areaSelection.selectArea(selectionGroup).getBoundsInParent();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        double xScale = imageView.getLayoutBounds().getWidth() / imageView.getImage().getWidth();
        double yScale = imageView.getLayoutBounds().getHeight() / imageView.getImage().getHeight();

        Point p1 = new Point((int)(bounds.getMinX()/xScale),(int)(bounds.getMinY()/yScale));
        Point p2 = new Point((int)((bounds.getMinX()+width)/xScale), (int)((bounds.getMinY()+height)/yScale));

        System.out.println("P1"+p1);
        System.out.println("P2"+p2);
        controller.cropImage(p1,p2);
        refreshImage();
        


    }


    private void openRawImage(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OpenRawImage.fxml"));
        Stage rawStage = new Stage();
        Parent root;
        try {
            root = loader.load();
            rawStage.setScene(new Scene(root));
        }catch (IOException e ){
            System.out.println("Failed to load FXML");
            e.printStackTrace();
        }

        OpenRawImage rawImageController = loader.getController();
        rawImageController.initData(this,rawStage);


    }

    public void openRawImage(int width, int height,File image){
        controller.loadRawImage(image,height,width);
        refreshImage();
    }
    private void loadImage(){
        FileChooser chooser = new FileChooser();

        File image =  chooser.showOpenDialog(stage);
        if(image != null){
            try {
              controller.loadImage(image);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Load Image failed");
                return;
            }
            refreshImage();
        }
    }

    public void saveFile(){
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(stage);
        controller.saveImage(file);
    }

    public void showEditPixelModal(){
        Stage stage = new Stage();
        stage.setTitle("Edit pixel value");
        HBox first = new HBox(20);

        CoordenateBox xBox = new CoordenateBox("X: ");
        CoordenateBox yBox = new CoordenateBox("Y: ");

        first.getChildren().addAll(xBox.getBox(),yBox.getBox());
        first.setAlignment(Pos.CENTER);


        HBox second = new HBox(10);
        Label label = new Label("New Value");

        CoordenateBox rBox = new CoordenateBox("R: ");
        CoordenateBox gBox = new CoordenateBox("G: ");
        CoordenateBox bBox = new CoordenateBox("B: ");

        second.getChildren().addAll(label,rBox.getBox(),gBox.getBox(),bBox.getBox());
        second.setAlignment(Pos.CENTER);

        Button modifyButton = new Button("Modify");

        modifyButton.setOnAction(e->{
            int x = Integer.parseInt(xBox.getField().getCharacters().toString());
            int y = Integer.parseInt(yBox.getField().getCharacters().toString());

            int r = Integer.parseInt(rBox.getField().getCharacters().toString());
            int g = Integer.parseInt(gBox.getField().getCharacters().toString());
            int b = Integer.parseInt(bBox.getField().getCharacters().toString());
            controller.editPixelValue(x,y,new Color(r,g,b));
            refreshImage();


        });

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,second,modifyButton);
        stage.setScene(new Scene(container,400,120));

        stage.show();

    }

    public void showChannelBands(){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ImageBands.fxml"));
        Stage stage = new Stage();
        try {
            Parent root1 = (Parent) fxmlLoader.load();
            ChannelBandsWindow cb =  fxmlLoader.getController();
            cb.setController(controller);
            cb.setStage(stage);
            stage.setScene(new Scene(root1));
            stage.show();
            cb.setImages();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public ImageView getImage(BufferedImage bufferedImage){
        ImageView iv = new ImageView();
         iv.setImage(SwingFXUtils.toFXImage(bufferedImage,null));
         return iv;
    }

    public void showMeanModal(){
        Stage stage = new Stage();
        stage.setTitle("Get mean");

        CoordenateBox p1x = new CoordenateBox("X: ");
        CoordenateBox p1y = new CoordenateBox("Y: ");
        HBox p1 = new HBox(20);
        p1.getChildren().addAll(p1x.getBox(),p1y.getBox());
        p1.setAlignment(Pos.CENTER);

        CoordenateBox p2x = new CoordenateBox("X: ");
        CoordenateBox p2y = new CoordenateBox("Y: ");
        HBox p2 = new HBox(20);
        p2.getChildren().addAll(p2x.getBox(),p2y.getBox());
        p2.setAlignment(Pos.CENTER);

        Button getMean = new Button("Get mean");

        Label colorsLabel = new Label("");

        getMean.setOnAction(e-> {
           Color colors =  controller.getPixelsMean(new Point(p1x.parseInt(),p1y.parseInt()), new Point(p2x.parseInt(),p2y.parseInt()));
           colorsLabel.setText("R: "+colors.getRed()+" G: "+colors.getGreen()+" B: "+colors.getBlue());
        });

        VBox container = new VBox(20);
        container.getChildren().addAll(p1,p2,getMean,colorsLabel);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        stage.setScene(new Scene(container));
        stage.show();

    }

    public void showGetPixelModal(){
        Stage stage = new Stage();
        stage.setTitle("Get pixel value");
        HBox first = new HBox(20);

        CoordenateBox xBox = new CoordenateBox("X: ");
        CoordenateBox yBox = new CoordenateBox("Y: ");

        HBox colors = new HBox(20);

        Label rLabel = new Label("R: ");
        Label gLabel = new Label("G: ");
        Label bLabel = new Label("B: ");


        colors.getChildren().addAll(rLabel,gLabel,bLabel);
        colors.setAlignment(Pos.CENTER);


        Button getValue = new Button("Get Value");


        getValue.setOnAction(e-> {
            int x = Integer.parseInt(xBox.getField().getCharacters().toString());
            int y = Integer.parseInt(yBox.getField().getCharacters().toString());

            Color value = controller.getPixelValue(x,y);
            rLabel.setText("R: "+value.getRed());
            gLabel.setText("G: "+value.getGreen());
            bLabel.setText("B: "+value.getBlue());

        });


        first.getChildren().addAll(xBox.getBox(),yBox.getBox());
        first.setAlignment(Pos.CENTER);




        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,getValue,colors);
        stage.setScene(new Scene(container,400,200));

        stage.show();


    }

    public void refreshImage(){
        mainImage = controller.getImage();
        imageView.setImage(SwingFXUtils.toFXImage(mainImage,null));
    }

    private class CoordenateBox{
        Label yLabel;
        TextField yField;
        HBox yBox;

        public CoordenateBox(String label){
             yLabel = new Label(label);
             yField = new TextField();
            yField.setPrefWidth(60);
             yBox = new HBox(10);
            yBox.getChildren().addAll(yLabel,yField);
            yBox.setAlignment(Pos.CENTER);
        }


        public Label getLabel() {
            return yLabel;
        }

        public TextField getField() {
            return yField;
        }

        public HBox getBox() {
            return yBox;
        }

        public int parseInt(){
           return Integer.parseInt(getField().getCharacters().toString());
        }
    }



    private class AreaSelection {

        private Group group;

        private ResizableRectangle selectionRectangle = null;
        private double rectangleStartX;
        private double rectangleStartY;
        private javafx.scene.paint.Paint darkAreaColor = javafx.scene.paint.Color.color(0,0,0,0.5);

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
            Rectangle darkAreaTop = new Rectangle(0,0,darkAreaColor);
            Rectangle darkAreaLeft = new Rectangle(0,0,darkAreaColor);
            Rectangle darkAreaRight = new Rectangle(0,0,darkAreaColor);
            Rectangle darkAreaBottom = new Rectangle(0,0,darkAreaColor);

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
            group.getChildren().add(1,darkAreaTop);
            group.getChildren().add(1,darkAreaLeft);
            group.getChildren().add(1,darkAreaBottom);
            group.getChildren().add(1,darkAreaRight);

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
