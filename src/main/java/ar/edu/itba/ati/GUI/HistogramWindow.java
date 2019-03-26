package ar.edu.itba.ati.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HistogramWindow extends VBox {

    public HistogramWindow(double [] values) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HistogramWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening HistogramWindow.fmxl");
            e.printStackTrace();
        }
        this.getChildren().addAll(new Histogram(values));
    }
}
