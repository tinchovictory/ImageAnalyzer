package ar.edu.itba.ati.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.awt.image.BufferedImage;
import java.io.File;

public class OpenRawImage {
    @FXML
    Button openImage;

    @FXML
    TextField widthField,heightField;

    MainWindow mainWindow;

    Stage stage;

    File image;

    public OpenRawImage() {
    }

    public void initData(MainWindow mainWindow, Stage stage){
        FileChooser chooser = new FileChooser();

        image =  chooser.showOpenDialog(stage);
        if(image != null){
            this.mainWindow = mainWindow;
            this.stage = stage;
            stage.setTitle("Open Raw Image");
            stage.show();
        }else{
            stage.close();
        }

    }

    @FXML
    public void openImageClicked(ActionEvent e){
        int width = Integer.parseInt(widthField.getCharacters().toString());
        int height = Integer.parseInt(heightField.getCharacters().toString());
        mainWindow.openRawImage(width,height,image);
        stage.close();
    }
}
