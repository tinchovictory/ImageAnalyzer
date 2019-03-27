package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.GUI.Windows.SliderWithImageWindow;
import ar.edu.itba.ati.Interface.Controller;
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
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2);

    }

    @FXML
    private void medianFilter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyMedianMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setMedianMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2);
    }

    @FXML
    private void gaussFilter(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyGaussMask(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setGaussMask(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2);
    }


}
