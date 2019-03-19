package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import ar.edu.itba.ati.model.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class ChannelBandsWindow implements Initializable {

    @FXML
    private ImageView redImage;
    @FXML
    private ImageView blueImage;
    @FXML
    private ImageView greenImage;
    @FXML
    private ImageView hueImage;
    @FXML
    private ImageView saturatonImage;
    @FXML
    private ImageView valueImage;

    private Controller controller;

    private Stage stage;


    public ChannelBandsWindow(){

    }
    public ChannelBandsWindow(Controller controller,Stage stage) {
        System.out.println("Document Module constructed.");


        this.controller = controller;

        stage.setTitle("Channel Bands");


    }

    public void setImages(){
        BufferedImage image = controller.getImage();
        setImage(controller.getRedImage(),redImage);
        setImage(controller.getBlueImage(),blueImage);
        setImage(controller.getGreenImage(),greenImage);
        setImage(controller.getHueImage(),hueImage);
        setImage(controller.getSaturationImage(),saturatonImage);
        setImage(controller.getValueImage(),valueImage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle("Channel Bands");
    }

    public static void  setImage(BufferedImage bufferedImage, ImageView iv){
        if(bufferedImage == null){
            System.out.println("La imagen es null");
        }
        if(iv == null){
            System.out.println("IV es null");
        }
        iv.setImage(SwingFXUtils.toFXImage(bufferedImage,null));
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing... ");
        //stage.show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
