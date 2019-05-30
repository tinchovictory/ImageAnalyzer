package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class SiftWindow extends VBox {

    Controller controller;

    @FXML
    Button openFirst;

    @FXML
    Button openSecond;

    @FXML
    Button applySift;

    @FXML
    Button showFirst;

    @FXML
    Button showSecond;

    @FXML
    Button showBoth;

    @FXML
    TextField matchingDistance;




    private SiftWindow(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SiftWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Error opening TrackingWindow.fxml");
            e.printStackTrace();
        }
        this.controller = controller;


    }

    public void init() {
       openFirst.setOnAction(e -> {
           controller.getMainWindow().loadImage1();
           controller.getMainWindow().refreshImage();
       });
        openSecond.setOnAction(e -> {
            controller.getMainWindow().loadImage2();
            controller.getMainWindow().refreshImage();
        });
        applySift.setOnAction(e-> {
            controller.applySift(Integer.parseInt(matchingDistance.getCharacters().toString()));
            controller.getMainWindow().refreshImage();
        });

        showFirst.setOnAction(e-> {
            controller.showImage1();
            controller.getMainWindow().refreshImage();
        });

        showSecond.setOnAction(e->{
            controller.showImage2();
            controller.getMainWindow().refreshImage();
        });

        showBoth.setOnAction(e-> {
            controller.showImage3();
            controller.getMainWindow().refreshImage();
        });
    }


    public static SiftWindow openInNewWindow(Controller controller) {
        Stage newStage = new Stage();
        SiftWindow window = new SiftWindow(controller);
        newStage.setScene(new Scene(window));
        window.init();
        newStage.show();
        return window;

    }


}
