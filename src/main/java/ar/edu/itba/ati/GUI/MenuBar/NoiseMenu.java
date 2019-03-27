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

public class NoiseMenu extends Menu {

    private Controller controller;

    public NoiseMenu(Controller controller) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu/NoiseMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void aditiveGaussNoise(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyAditiveGaussNoise(value,0);
        Consumer<Double> setClicked = (value) -> controller.setAditiveGaussNoise(value,0);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05);
    }

    @FXML
    private void multiplicativeRayleighNoise(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyMultiplicativeRayleighNoise(value);
        Consumer<Double> setClicked = (value) -> controller.setMultiplicativeRayleighNoise(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05);
    }

    @FXML
    private void multiplicativeExponentialNoise(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyMultiplicativeExponentialNoise(value);
        Consumer<Double> setClicked = (value) -> controller.setMultiplicativeExponentialNoise(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05);
    }

    @FXML
    private void saltAndPepper(){
        System.out.println("Salt And pepper");
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applySaltAndPepperNoise(value);
        Consumer<Double> setClicked = (value) -> controller.setSaltAndPepperNoise(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05);
    }
}
