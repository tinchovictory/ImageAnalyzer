package ar.edu.itba.ati.Interface;

import ar.edu.itba.ati.GUI.MainWindow;

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

    void cropImage( Point p1, Point p2); // p1.x < p2.x

    BufferedImage createSquare();

    BufferedImage createCircle();

    BufferedImage createColorGradient();

    BufferedImage createGreyGradient();

    BufferedImage createSolidImage(int r, int g, int b);

    Color getPixelsMean(Point p1, Point p2); // p1.x < p2.x

    BufferedImage getRedImage();

    BufferedImage getGreenImage();

    BufferedImage getBlueImage();

    BufferedImage getHueImage();

    BufferedImage getSaturationImage();

    BufferedImage getValueImage();

    void addImage(File image);

    void substractImage(File image);

    void compressDynamicRange();

    void getNegative();

    BufferedImage applyContrast(int minContrast, int maxContrast);

    void setContrast(int minContrast, int maxContrast);

    double[] getHisotgram();

    void setMainWindow(MainWindow mainWindow);

    MainWindow getMainWindow();

    void equalizeImage();

    BufferedImage applyThreshold(int threshold);

    void setThreshold(int threshold);

    BufferedImage applyAditiveGaussNoise(double phi, double mu);

    BufferedImage applyMultiplicativeRayleighNoise(double epsilon);

    BufferedImage applyMultiplicativeExponentialNoise(double lambda);

    BufferedImage applySaltAndPepperNoise(double deviation);

    void setAditiveGaussNoise(double phi, double mu);

    void setMultiplicativeRayleighNoise(double epsilon);

    void setMultiplicativeExponentialNoise(double lambda);

    void setSaltAndPepperNoise(double deviation);

    void applyMeanMask(int size);

    void applyMedianMask(int size);

    void applyGaussMask(int size);

    void applyBorderMask(int size);

}

