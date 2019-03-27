package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.Interface.Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

public class SliderWithImageWindow extends VBox implements Initializable {

    @FXML
    private Label valueLabel;

    @FXML
    private Slider slider;

    @FXML
    private ImageView image;

    @FXML
    private Button setButton;

    @FXML
    private Button cancelButton;

    private Function<Double,BufferedImage> sliderDragged;

    private Consumer<Double> setClicked;

    private Controller controller;

    private Stage stage;

    public SliderWithImageWindow(Controller controller,Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Contrast.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.controller = controller;
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening Contrast.fmxl");
            e.printStackTrace();
        }
        slider.setBlockIncrement(increment);
        slider.setMax(max);
        slider.setMin(min);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        this.sliderDragged =sliderDragged;
        this.setClicked = setClicked;


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static SliderWithImageWindow openInNewWindow(Controller controller, Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment){
        Stage newStage = new Stage();
        SliderWithImageWindow window =  new SliderWithImageWindow(controller,sliderDragged,setClicked,min,max,increment);
        window.setStage(newStage);
        newStage.setScene(new Scene(window ));
        newStage.show();
        return window;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.setOnMouseDragged(e->{
            BufferedImage tempimage = sliderDragged.apply(slider.getValue());
            valueLabel.setText("Value: "+slider.getValue());
            image.setImage(SwingFXUtils.toFXImage(tempimage, null));
        });

        setButton.setOnAction(e-> {
            setClicked.accept(slider.getValue());
            valueLabel.setText("Value: "+slider.getValue());
            controller.getMainWindow().refreshImage();
            Stage stage = (Stage) setButton.getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(e-> stage.close());
    }


}
