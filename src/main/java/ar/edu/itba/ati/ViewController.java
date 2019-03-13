package ar.edu.itba.ati;

import ar.edu.itba.ati.Interface.Controller;

import java.awt.image.BufferedImage;
import java.io.File;

public class ViewController implements Controller {
    @Override
    public int getPixelValue(int x, int y) {
        return 0;
    }

    @Override
    public void editPixelValue(int x, int y, int newValue) {

    }

    @Override
    public BufferedImage loadImage(File image) {
        return null;
    }

    @Override
    public void saveImage(File image) {

    }
}
