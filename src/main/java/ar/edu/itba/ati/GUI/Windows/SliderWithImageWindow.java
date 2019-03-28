package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.Interface.DConsumer;
import ar.edu.itba.ati.Interface.DFunction;
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
    private Label secondValueLabel;

    @FXML
    private Slider secondSlider;

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

    private double min;

    private double increment;

    private double previousValue;

    private boolean doubleSlider;

    private DFunction<Double,Double,BufferedImage> sliderDraggedD;

    private DConsumer<Double,Double> setClickedD;

    public SliderWithImageWindow(Controller controller,Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment) {
        this(controller,min,max,increment,false);

        this.sliderDragged =sliderDragged;
        this.setClicked = setClicked;
        this.secondSlider.setVisible(false);
        this.secondValueLabel.setVisible(false);


    }

    private SliderWithImageWindow(Controller controller, double min, double max, double increment,boolean doubleSlider) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DoubleSliderImageWindow.fxml"));
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
        slider.setValue(min);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(false);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(increment);
        slider.setMinorTickCount(0);
        slider.setValue(min);


        secondSlider.setBlockIncrement(increment);
        secondSlider.setMax(max);
        secondSlider.setMin(min);
        secondSlider.setValue(min);
        secondSlider.setShowTickMarks(true);
        secondSlider.setShowTickLabels(false);
        secondSlider.setSnapToTicks(true);
        secondSlider.setMajorTickUnit(increment);
        secondSlider.setMinorTickCount(0);
        secondSlider.setValue(min);

        this.previousValue = 0;
        this.min= min;
        this.increment = increment;
        this.doubleSlider = doubleSlider;

    }

    public SliderWithImageWindow(Controller controller, DFunction<Double,Double,BufferedImage> sliderDragged , DConsumer<Double,Double> setClicked, double min, double max, double increment){
        this(controller,min,max,increment,true);
        this.sliderDraggedD=sliderDragged;
        this.setClickedD=setClicked;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static SliderWithImageWindow openInNewWindow(Controller controller, Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment){
        Stage newStage = new Stage();
        SliderWithImageWindow window =  new SliderWithImageWindow(controller,sliderDragged,setClicked,min,max,increment);
        window.setStage(newStage);
        newStage.setScene(new Scene(window ));
        window.initSimpleSlider();
        newStage.show();
        return window;

    }

    public static SliderWithImageWindow openDoubleInNewWindow(Controller controller, DFunction<Double,Double,BufferedImage> sliderDragged , DConsumer<Double,Double> setClicked, double min, double max, double increment){
        Stage newStage = new Stage();
        SliderWithImageWindow window =  new SliderWithImageWindow(controller,sliderDragged,setClicked,min,max,increment);
        window.setStage(newStage);
        newStage.setScene(new Scene(window ));
        window.initDoubleSlider();
        newStage.show();
        return window;
    }



    private void initDoubleSlider(){
        slider.setOnMouseDragged((e) -> {

            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            double value2 = (Math.round(secondSlider.getValue() / increment) * increment) + min;
            if (value != previousValue) {
                BufferedImage tempimage = sliderDraggedD.apply(value,value2);
                valueLabel.setText("Value: " + value);
                secondValueLabel.setText("Value: "+value2);
                image.setImage(SwingFXUtils.toFXImage(tempimage, null));
            }
        });

        secondSlider.setOnMouseDragged((e) -> {

            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            double value2 = (Math.round(secondSlider.getValue() / increment) * increment) + min;
            if (value != previousValue) {
                BufferedImage tempimage = sliderDraggedD.apply(value,value2);
                valueLabel.setText("Value: " + value);
                secondValueLabel.setText("Value: "+value2);
                image.setImage(SwingFXUtils.toFXImage(tempimage, null));
            }
        });

        setButton.setOnAction(e -> {

            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            double value2 = (Math.round(secondSlider.getValue() / increment) * increment) + min;
            setClickedD.accept(value,value2);
            valueLabel.setText("Value: " + value);
            controller.getMainWindow().refreshImage();
            Stage stage = (Stage) setButton.getScene().getWindow();
            stage.close();
        });
    }

    private void initSimpleSlider(){
        slider.setOnMouseDragged((e) -> {

            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            if (value != previousValue) {
                BufferedImage tempimage = sliderDragged.apply(value);
                valueLabel.setText("Value: " + value);
                image.setImage(SwingFXUtils.toFXImage(tempimage, null));
                previousValue=value;
            }
        });

        setButton.setOnAction(e -> {
            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            setClicked.accept(value);
            valueLabel.setText("Value: " + value);
            controller.getMainWindow().refreshImage();
            Stage stage = (Stage) setButton.getScene().getWindow();
            stage.close();
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!doubleSlider) {

        }else{

        }

        cancelButton.setOnAction(e-> stage.close());
    }


}
