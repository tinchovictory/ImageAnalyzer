package ar.edu.itba.ati.Interface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public interface Controller {

    Color getPixelValue(int x , int y);

    void editPixelValue(int x, int y, Color newValue);

     BufferedImage loadImage(File image);

     BufferedImage getImage();

     void saveImage(File image);

     BufferedImage createSquare();

     BufferedImage createCircle();

}
