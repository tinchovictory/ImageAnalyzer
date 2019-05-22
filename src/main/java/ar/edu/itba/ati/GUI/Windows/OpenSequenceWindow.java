package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OpenSequenceWindow extends VBox {

    Controller controller;

    @FXML
    TextField numberOfImages;

    @FXML
    TextField extension;

    @FXML
    TextField prefix;

    @FXML
    Button button;

    private OpenSequenceWindow(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OpenSequenceWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Error opening OpenSequenceWindow.fmxl");
            e.printStackTrace();
        }
        this.controller = controller;


    }

    public void init(File path) {

        button.setOnAction(event -> {


            int numberOfImages = Integer.parseInt(this.numberOfImages.getCharacters().toString());
            String extension = this.extension.getCharacters().toString();
            String prefix = this.prefix.getCharacters().toString();
            getFilesList(path, numberOfImages, prefix, extension);

        });

    }

    private List<File> getFilesList(File path, int numberOfImages, String prefix, String extension) {
        List<File> list = new ArrayList<>();
        for (int i = 1; i < numberOfImages+1; i++) {
            String filePath = path.getAbsolutePath() + "/" + prefix + i +"."+ extension;
            System.out.println(filePath);
            list.add(new File(filePath));
        }
        return list;
    }


    public static OpenSequenceWindow openInNewWindow(Controller controller, File path) {
        Stage newStage = new Stage();
        OpenSequenceWindow window = new OpenSequenceWindow(controller);
        newStage.setScene(new Scene(window));
        window.init(path);
        newStage.show();
        return window;

    }


}
