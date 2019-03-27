package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.awt.image.BufferedImage;
import java.io.IOException;

public class ContrastWindow extends VBox {

    @FXML
    Slider slider;

    @FXML
    ImageView image;

    @FXML
    Button setButton;

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
        slider.setBlockIncrement(5.0);
        slider.setMax(255);
        slider.setMin(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setOnMouseDragged(e->{
            BufferedImage tempimage = controller.applyContrast((int)slider.getValue(),255-(int)slider.getValue());

            image.setImage(SwingFXUtils.toFXImage(tempimage, null));
        });

        setButton.setOnAction(e-> {
            controller.setContrast((int)slider.getValue(),255-(int)slider.getValue());
            controller.getMainWindow().refreshImage();
            Stage stage = (Stage) setButton.getScene().getWindow();
            stage.close();
        });

    }




}
