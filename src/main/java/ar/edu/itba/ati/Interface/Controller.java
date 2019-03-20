package ar.edu.itba.ati.Interface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public interface Controller {

    Color getPixelValue(int x , int y);

    void editPixelValue(int x, int y, Color newValue);

    BufferedImage loadImage(File image);
    
    BufferedImage loadRawImage(File image, int width, int height);

    BufferedImage getImage();

    void saveImage(File image);

    void cropImage(File image, Point p1, Point p2); // p1.x < p2.x

    BufferedImage createSquare();

    BufferedImage createCircle();

    BufferedImage createColorGradient();

    BufferedImage createGreyGradient();

    Color getPixelsMean(Point p1, Point p2); // p1.x < p2.x

    BufferedImage getRedImage();

    BufferedImage getGreenImage();

    BufferedImage getBlueImage();

    BufferedImage getHueImage();

    BufferedImage getSaturationImage();

    BufferedImage getValueImage();

}
