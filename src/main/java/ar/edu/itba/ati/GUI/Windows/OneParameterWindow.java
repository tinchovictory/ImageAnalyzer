package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class OneParameterWindow extends VBox {

    @FXML
    private Label label;

    @FXML
    private Button button;

    @FXML
    private TextField parameter;

    Controller controller;



    private OneParameterWindow(Controller controller){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OneParameterWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening DoubleSliderImageWindow.fmxl");
            e.printStackTrace();
        }
        this.controller = controller;



    }

    public void init(Consumer<Double> onButtonClick, String valueLabel){
        button.setOnAction((e)-> {
            Double number = Double.parseDouble(parameter.getCharacters().toString());

            onButtonClick.accept(number);
            controller.getMainWindow().refreshImage();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
        });

        this.label.setText(valueLabel);
    }




    public static OneParameterWindow openInNewWindow(Controller controller, Consumer<Double> setClicked, String valueLabel){
        Stage newStage = new Stage();
        OneParameterWindow window =  new OneParameterWindow(controller);
        newStage.setScene(new Scene(window ));
        window.init(setClicked,valueLabel);
        newStage.show();
        return window;

    }
}
