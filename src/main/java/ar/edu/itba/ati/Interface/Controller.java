package ar.edu.itba.ati.Interface;

import java.awt.image.BufferedImage;
import java.io.File;

public interface Controller {

    int getPixelValue(int x ,int y);

    void editPixelValue(int x, int y, int newValue);

     BufferedImage loadImage(File image);

     void saveImage(File image);

}
