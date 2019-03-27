package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.Interface.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OperationsMenu extends Menu implements Initializable {

    Controller controller;


    public OperationsMenu(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OperationsMenu.fxml"));
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
    private void addImage(){
        Stage stage = controller.getMainWindow().getStage();
        FileChooser chooser = new FileChooser();

        File image = chooser.showOpenDialog(stage);
        if (image != null) {
            controller.addImage(image);
        }
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void substractImage(ActionEvent event){
        Stage stage = controller.getMainWindow().getStage();
        FileChooser chooser = new FileChooser();

        File image = chooser.showOpenDialog(stage);
        if (image != null) {
            controller.substractImage(image);
        }
        controller.getMainWindow().refreshImage();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
