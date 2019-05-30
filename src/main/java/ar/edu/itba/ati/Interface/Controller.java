package ar.edu.itba.ati.Interface;

import ar.edu.itba.ati.GUI.MainWindow;

import java.awt.Point;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

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

    BufferedImage getGreyImage();

    void addImage(File image);

    void substractImage(File image);

    void multiplyBy(int value);

    BufferedImage multiplyByBuffered(int value);

    void compressDynamicRange();

    void powerFunction(double gamma);

    BufferedImage powerFunctionBuffered(double gamma);

    void getNegative();

    BufferedImage applyContrast(int minContrast, int maxContrast);

    void setContrast(int minContrast, int maxContrast);

    double[] getHisotgram();

    void setMainWindow(MainWindow mainWindow);

    MainWindow getMainWindow();

    void equalizeImage();

    BufferedImage applyThreshold(int threshold);

    void setThreshold(int threshold);

    BufferedImage applyGlobalThreshold();

    void setGlobalThreshold();

    BufferedImage applyOtsuThreshold();

    void setOtsuThreshold();

    BufferedImage applyAditiveGaussNoise(double phi, double mu, double contamination);

    BufferedImage applyMultiplicativeRayleighNoise(double epsilon,double contamination);

    BufferedImage applyMultiplicativeExponentialNoise(double lambda,double contamination);

    BufferedImage applySaltAndPepperNoise(double deviation);

    void setAditiveGaussNoise(double phi, double mu,double contamination);

    void setMultiplicativeRayleighNoise(double epsilon,double contamination);

    void setMultiplicativeExponentialNoise(double lambda,double contamination);

    void setSaltAndPepperNoise(double deviation);

    BufferedImage applyMeanMask(int size);

    BufferedImage applyMedianMask(int size);

    BufferedImage applyWeightedMedianMask(int size);

    BufferedImage applyGaussMask(int size, double deviation);

    BufferedImage applyBorderMask(int size);

    BufferedImage applyPrewittMask();

    BufferedImage applySobelMask();

    BufferedImage apply5aMask();

    BufferedImage applyKirshMask();

    BufferedImage applyLaplaceMask();

    BufferedImage applyLoGMask(double deviation);

    BufferedImage applyLaplaceCrossingZeroMask(int threshold);

    BufferedImage applyLoGCrossingZeroMask(double deviation, int threshold);

    BufferedImage applyIsotropicDiffusion(int iterations);

    BufferedImage applyAnisotropicDiffusionLeclerc(int iterations, double deviation);

    BufferedImage applyAnisotropicDiffusionLorentziano(int iterations, double deviation);

    BufferedImage applyBilateralFilter(int size, double spaceSigma, double colorSigma);

    void setMeanMask(int size);

    void setMedianMask(int size);

    void setWeightedMedianMask(int size);

    void setGaussMask(int size, double deviation);

    void setBorderMask(int size);

    void setPrewittMask();

    void setSobelMask();

    void set5aMask();

    void setKirshMask();

    void setLaplaceMask();

    void setLoGMask(double deviation);

    void setLaplaceCrossingZeroMask(int threshold);

    void setLoGCrossingZeroMask(double deviation, int threshold);

    void setIsotropicDiffusion(int iterations);

    void setAnisotropicDiffusionLeclerc(int iterations, double deviation);

    void setAnisotropicDiffusionLorentziano(int iterations, double deviation);

    void setBilateralFilter(int size, double spaceSigma, double colorSigma);

    void setSusanFilter();

    void setSusanCornerFilter();

    BufferedImage applyCannyFilter(int t1, int t2);

    void setCannyFilter(int t1, int t2);

    BufferedImage applyHoughFilter(int lines);

    void setHoughFilter(int lines);

    void setHoughCircularFilter();

    void setTrackArea(List<Point> objectPoints, List<Point> backgroundPoints);



    boolean isVideo();

    void loadVideo(List<File> frames);

    BufferedImage getCurrentVideoFrame();

    void loadVideoNextFrame();

    void startTrackVideoArea(List<Point> objectPoints, List<Point> backgroundPoints);

    void trackAreaInNextFrame();

    int getVideoFramesAmount();


    BufferedImage applyHarris(int threshold);
    void setHarris(int threshold);




    void loadImage1(File image);

    void loadImage2(File image);

    void showImage1();

    void showImage2();

    void showImage3();

    void applySift();
}

