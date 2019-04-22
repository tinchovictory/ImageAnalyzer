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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
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

    private double min2;

    private double max2;

    private double increment2;

    private double previousValue;

    private double previousValue2;

    private boolean doubleSlider;

    private String labelName;

    private String labelName2;

    private DFunction<Double,Double,BufferedImage> sliderDraggedD;

    private DConsumer<Double,Double> setClickedD;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public SliderWithImageWindow(Controller controller,Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment,String valueLabel) {
        this(controller,min,max,increment,false,valueLabel,min,max,increment,valueLabel);
        this.sliderDragged =sliderDragged;
        this.setClicked = setClicked;
        this.secondSlider.setVisible(false);
        this.secondValueLabel.setVisible(false);


    }

    private SliderWithImageWindow(Controller controller, double min, double max, double increment,boolean doubleSlider,String valueLabel,double min2,double max2,double increment2,String labelName2) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DoubleSliderImageWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.controller = controller;
        this.labelName = valueLabel;
        this.labelName2 = labelName2;
        this.min2 = min2;
        this.max2 = max2;
        this.increment2 = increment2;
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening DoubleSliderImageWindow.fmxl");
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
        this.valueLabel.setText(valueLabel);


        secondSlider.setBlockIncrement(increment2);
        secondSlider.setMax(max2);
        secondSlider.setMin(min2);
        secondSlider.setValue(min2);
        secondSlider.setShowTickMarks(true);
        secondSlider.setShowTickLabels(false);
        secondSlider.setSnapToTicks(true);
        secondSlider.setMajorTickUnit(increment2);
        secondSlider.setMinorTickCount(0);
        this.secondValueLabel.setText(labelName2);



        this.previousValue  = 0;
        this.previousValue2 = 0;
        this.min = min;
        this.increment = increment;
        this.doubleSlider = doubleSlider;

    }

    public SliderWithImageWindow(Controller controller, DFunction<Double,Double,BufferedImage> sliderDragged , DConsumer<Double,Double> setClicked, double min, double max, double increment,String labelName,double min2,double max2,double increment2, String label2Name){
        this(controller,min,max,increment,true,labelName,min2,max2,increment2,label2Name);
        this.sliderDraggedD=sliderDragged;
        this.setClickedD=setClicked;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static SliderWithImageWindow openInNewWindow(Controller controller, Function<Double,BufferedImage> sliderDragged , Consumer<Double> setClicked,double min, double max, double increment,String valueLabel){
        Stage newStage = new Stage();
        SliderWithImageWindow window =  new SliderWithImageWindow(controller,sliderDragged,setClicked,min,max,increment,valueLabel);
        window.setStage(newStage);
        newStage.setScene(new Scene(window ));
        window.initSimpleSlider();
        newStage.show();
        return window;

    }

    public static SliderWithImageWindow openDoubleInNewWindow(Controller controller, DFunction<Double,Double,BufferedImage> sliderDragged , DConsumer<Double,Double> setClicked, double min, double max, double increment,double min2,double max2,double increment2,String labelName, String label2Name){
        Stage newStage = new Stage();
        SliderWithImageWindow window =  new SliderWithImageWindow(controller,sliderDragged,setClicked,min,max,increment,labelName,min2,max2,increment2,label2Name);
        window.setStage(newStage);
        newStage.setScene(new Scene(window ));
        window.initDoubleSlider();
        newStage.show();
        return window;
    }



    private void initDoubleSlider(){
        slider.setOnMouseDragged(this::handle);

        secondSlider.setOnMouseDragged(this::handle);

        setButton.setOnAction(e -> {
            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            double value2 = (Math.round(secondSlider.getValue() / increment2) * increment2) + min2;
            System.out.println(value2);
            setClickedD.accept(value,value2);
            valueLabel.setText(labelName+": "+ df2.format(value));
            secondValueLabel.setText(labelName2+": "+df2.format(value2));
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
                valueLabel.setText(labelName+": " + df2.format(value));
                image.setImage(SwingFXUtils.toFXImage(tempimage, null));
                previousValue=value;
            }
        });

        setButton.setOnAction(e -> {
            double value = (Math.round(slider.getValue() / increment) * increment) + min;
            setClicked.accept(value);
            valueLabel.setText(labelName+": " + df2.format(value));
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

        image.setPreserveRatio(true);
        image.setFitHeight(300);
        image.setFitWidth(400);

        cancelButton.setOnAction(e-> stage.close());
    }


    private void handle(MouseEvent e) {
        double value = (Math.round(slider.getValue() / increment) * increment) + min;
        double value2 = (Math.round(secondSlider.getValue() / increment2) * increment2) + min2;
        if (value != previousValue || value2 != previousValue2) {
            BufferedImage tempimage = sliderDraggedD.apply(value, value2);
            valueLabel.setText(labelName + ": " + df2.format(value));
            secondValueLabel.setText(labelName2 + ": " + df2.format(value2));
            image.setImage(SwingFXUtils.toFXImage(tempimage, null));

        }
        previousValue = value;
        previousValue2 = value2;
    }
}
