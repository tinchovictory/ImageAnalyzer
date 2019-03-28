package ar.edu.itba.ati.GUI.MenuBar;


import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

import java.io.IOException;

public class GenerateMenu extends Menu {

    Controller controller;

    public GenerateMenu(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GenerateMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.controller = controller;
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Could not open GenerateMenu.fxml");
            e.printStackTrace();
        }

    }
    @FXML
    private void createCircle(){
        controller.createCircle();
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void crateSquare(){
        controller.createSquare();
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void createColorGradient(){
        controller.createColorGradient();
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void createGreyGradient(){
        controller.createGreyGradient();
        controller.getMainWindow().refreshImage();
    }

    @FXML void createSyntethicImage(){
        controller.createSolidImage(225,32,101);
        controller.getMainWindow().refreshImage();
    }

}
