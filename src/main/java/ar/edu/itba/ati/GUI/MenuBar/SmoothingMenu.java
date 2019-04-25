package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.GUI.Windows.SliderWithImageWindow;
import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.Interface.DConsumer;
import ar.edu.itba.ati.Interface.DFunction;
import ar.edu.itba.ati.Interface.Thunk;
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
    private void bilateralFilter(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value, value2)->controller.applyBilateralFilter(7,value.intValue(),value2);
        DConsumer<Double,Double> setClicked = (value, value2) -> controller.setBilateralFilter(7,value.intValue(),value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,1,15.0,0.5,0,50,2,"Space","Color");
    }



    @FXML
    private void prewittMask(){
//        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyPrewittMask();
//        Consumer<Double> setClicked = (value) -> controller.setPrewittMask();
//        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
        applyFunction(controller::setPrewittMask);
    }

    @FXML
    private void sobelMask(){
       applyFunction(controller::setSobelMask);
    }

    @FXML
    private void fiveAMask(){
//        Function<Double, BufferedImage> sliderDragged = (value)->controller.apply5aMask();
//        Consumer<Double> setClicked = (value) -> controller.set5aMask();
//        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
        applyFunction(controller::set5aMask);
    }

    @FXML
    private void kirshMask(){
//        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyKirshMask();
//        Consumer<Double> setClicked = (value) -> controller.setKirshMask();
//        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
        applyFunction(controller::setKirshMask);
    }

    @FXML
    private void laplaceMask(){
//        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyLaplaceMask();
//        Consumer<Double> setClicked = (value) -> controller.setLaplaceMask();
//        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
        applyFunction(controller::setLaplaceMask);
    }

    @FXML
    private void loGMask(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyLoGMask(value);
        Consumer<Double> setClicked = (value) -> controller.setLoGMask(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.2,2,0.2,"Deviation");
    }

    @FXML
    private void laplaceCrossingZeroMask(){
//        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyLaplaceCrossingZeroMask();
//        Consumer<Double> setClicked = (value) -> controller.setLaplaceCrossingZeroMask();
//        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,1,15.0,2,"Mask size");
        applyFunction(controller::setLaplaceCrossingZeroMask);
    }

    @FXML
    private void loGCrossingZeroMask(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyLoGCrossingZeroMask(value);
        Consumer<Double> setClicked = (value) -> controller.setLoGCrossingZeroMask(value);
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0.2,2,0.2,"Deviation");
    }

    @FXML
    private void isotropicDiffusion(){
        Function<Double, BufferedImage> sliderDragged = (value)->controller.applyIsotropicDiffusion(value.intValue());
        Consumer<Double> setClicked = (value) -> controller.setIsotropicDiffusion(value.intValue());
        SliderWithImageWindow.openInNewWindow(controller,sliderDragged,setClicked,0,50,2,"Iterations");
    }

    @FXML
    private void anisotropicDiffusionLeclerc(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value, value2)->controller.applyAnisotropicDiffusionLeclerc(value.intValue(), value2);
        DConsumer<Double,Double> setClicked = (value, value2) -> controller.setAnisotropicDiffusionLeclerc(value.intValue(), value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,1,80,1,0,50,2,"Iterations","Deviation");
    }

    @FXML
    private void anisotropicDiffusionLorentziano(){
        DFunction<Double,Double, BufferedImage> sliderDragged = (value, value2)->controller.applyAnisotropicDiffusionLorentziano(value.intValue(), value2);
        DConsumer<Double,Double> setClicked = (value, value2) -> controller.setAnisotropicDiffusionLorentziano(value.intValue(), value2);
        SliderWithImageWindow.openDoubleInNewWindow(controller,sliderDragged,setClicked,1,80,1,0,50,2,"Iterations","Deviation");
    }



    private void applyFunction(Thunk thunk){
            thunk.apply();
            controller.getMainWindow().refreshImage();
    }
}
