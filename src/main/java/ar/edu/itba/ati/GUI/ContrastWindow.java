package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ContrastWindow extends VBox {

    @FXML
    Slider slider;

    Controller controller;

    public ContrastWindow(Controller controller) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Contrast.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.controller = controller;
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening HistogramWindow.fmxl");
            e.printStackTrace();
        }
    }


}
