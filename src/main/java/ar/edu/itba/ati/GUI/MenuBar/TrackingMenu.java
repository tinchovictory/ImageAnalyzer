package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.GUI.Windows.OpenRawImage;
import ar.edu.itba.ati.GUI.Windows.SiftWindow;
import ar.edu.itba.ati.GUI.Windows.TrackingWindow;
import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class TrackingMenu extends Menu {

    Controller controller;


    public TrackingMenu(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./Menu/TrackingMenu.fxml"));
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
    private void configureTracking(){
        TrackingWindow.openInNewWindow(controller);
    }

    @FXML
    private void configureSift() {
        SiftWindow.openInNewWindow(controller);
    }
}
