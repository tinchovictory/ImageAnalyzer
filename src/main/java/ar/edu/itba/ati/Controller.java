package ar.edu.itba.ati;

import ar.edu.itba.ati.model.Image;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller implements ar.edu.itba.ati.Interface.Controller {
    Image image;

    @Override
    public Color getPixelValue(int x, int y) {
        return image.getPixelColor(x, y);
    }

    @Override
    public void editPixelValue(int x, int y, Color newValue) {
        image.setPixelColor(x, y, newValue);
    }

    @Override
    public BufferedImage loadImage(File imagePath) {
        try {
            image = ImageManager.loadImage(imagePath);
        } catch (IOException e) {
            //TODO: Show msg to user
            System.out.println("Unable to load image");
        } catch (ImageReadException e) {
            //TODO: Show msg to user
            System.out.println("Unable to load image");
        }

        return image.getBufferdImage();
    }

    @Override
    public BufferedImage getImage() {
        return image.getBufferdImage();
    }

    @Override
    public void saveImage(File imagePath) {
        try {
            ImageManager.saveImage(imagePath, image);
        } catch (IOException e) {
            //TODO: Show msg to user
            System.out.println("Unable to save image");
        } catch (ImageWriteException e) {
            //TODO: Show msg to user
            System.out.println("Unable to save image");
        }
    }

    @Override
    public BufferedImage createSquare() {
         image = ImageCreator.buildSquare();
         return image.getBufferdImage();
    }

    @Override
    public BufferedImage createCircle() {
        image = ImageCreator.buildCircle();
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage createColorGradient() {
        image =  ImageCreator.buildColorGradient();
        return image.getBufferdImage();
    }

    @Override
    public BufferedImage createGreyGradient() {
        image = ImageCreator.buildGreyGradient();
        return image.getBufferdImage();
    }

    public Color getPixelsMean(Point p1, Point p2) {
        return image.getPixelsMean(p1, p2);
    }
}
