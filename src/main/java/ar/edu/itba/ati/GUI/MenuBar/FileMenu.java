package ar.edu.itba.ati.GUI.MenuBar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FileMenu extends Menu {




    public FileMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FileMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }


}
