package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.GUI.Windows.OpenRawImage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

    @FXML
    private void openLena() {
        File lena = Paths.get(getClass().getClassLoader().getResource("LENA.RAW").getFile()).toFile();
        controller.getMainWindow().openRawImage(256,256,lena);


    }


}
