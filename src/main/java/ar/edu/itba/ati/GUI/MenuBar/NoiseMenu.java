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
        DFunction<Double,Double, BufferedImage> sliderDragged = (value,value2)->controller.applyAditiveGaussNoise(value,0,value2);
        DConsumer<Double,Double> setClicked = (value,value2) -> controller.setAditiveGaussNoise(value,0,value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,0.0,5.0,0.05,0,1,0.05,"Standard deviation","Contamination");
    }

    @FXML
    private void multiplicativeRayleighNoise(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value,value2)->controller.applyMultiplicativeRayleighNoise(value,value2);
        DConsumer<Double,Double> setClicked = (value,value2) -> controller.setMultiplicativeRayleighNoise(value,value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05,0,1,0.05,"Epsilon","Contamination");
    }

    @FXML
    private void multiplicativeExponentialNoise(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value,value2)->controller.applyMultiplicativeExponentialNoise(value,value2);
        DConsumer<Double,Double> setClicked = (value,value2) -> controller.setMultiplicativeExponentialNoise(value,value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05,0,1,0.05,"Lambda","Contamination");
    }

    @FXML
    private void saltAndPepper(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applySaltAndPepperNoise(value);
        Consumer<Double> setClicked = (value) -> controller.setSaltAndPepperNoise(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.0,1.0,0.05,"Percentage of contamination");
    }
}
