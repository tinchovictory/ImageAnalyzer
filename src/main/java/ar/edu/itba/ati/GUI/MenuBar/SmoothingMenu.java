package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.GUI.Windows.SliderWithImageWindow;
import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.Interface.DConsumer;
import ar.edu.itba.ati.Interface.DFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class SmoothingMenu extends Menu {

    Controller controller;

    public SmoothingMenu(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu/SmoothingMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.controller = controller;
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void meanFilter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyMeanMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setMeanMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask Size");

    }

    @FXML
    private void medianFilter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyMedianMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setMedianMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
    }


    @FXML
    private void weightedMedianFilter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyWeightedMedianMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setWeightedMedianMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller, sliderDragged, setClicked,1,15.0,2,"Mask size");
    }

    @FXML
    private void gaussFilter(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value, value2)->controller.applyGaussMask(value.intValue(),value2);
        DConsumer<Double,Double> setClicked = (value, value2) -> controller.setGaussMask(value.intValue(),value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,0,1,0.01,"Mask size","Standard deviation");
    }

    @FXML
    private void borderFIlter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyBorderMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setBorderMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
    }

    @FXML
    private void prewittMask(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyPrewittMask();
        Consumer<Double> setClicked = (value) -> controller.setPrewittMask();
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
    }

    @FXML
    private void sobelMask(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applySobelMask();
        Consumer<Double> setClicked = (value) -> controller.setSobelMask();
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
    }

}
