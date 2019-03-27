package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.GUI.OpenRawImage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.io.IOException;

public class FileMenu extends Menu {

    Controller controller;


    public FileMenu(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FileMenu.fxml"));
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
    private void loadImage() {
        if(controller ==  null){
            System.out.println("CONTROLLER NULL");
        }else if (controller.getMainWindow() ==  null){
            System.out.println("MAIN WINODW NULL");
        }else{
            controller.getMainWindow().loadImage();
        }

    }

    @FXML
    private void saveFile() {
        controller.getMainWindow().saveFile();
    }


    @FXML
    private void openRawImage() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OpenRawImage.fxml"));
        Stage rawStage = new Stage();
        Parent root;
        try {
            root = loader.load();
            rawStage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Failed to load FXML");
            e.printStackTrace();
        }

        OpenRawImage rawImageController = loader.getController();
        rawImageController.initData(controller.getMainWindow(), rawStage);


    }


}
