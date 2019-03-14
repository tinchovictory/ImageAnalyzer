package ar.edu.itba.ati.model;

import java.awt.*;

public class HSVImage {
    private ImageType imageType;
    private ImageExtension imageExtension;
    //private ImageColorChannel hue;
    //private ImageColorChannel saturation;
    //private ImageColorChannel value;
    private float[][] hue;
    private float[][] saturation;
    private float[][] value;
    private int width;
    private int height;


    public HSVImage(Image image) {
        this.imageType = image.getImageType();
        this.imageExtension = image.getImageExtension();
        this.width = image.getWidth();
        this.height = image.getHeight();
//        this.hue = new ImageColorChannel(width, height);
//        this.saturation = new ImageColorChannel(width, height);
//        this.value = new ImageColorChannel(width, height);
        this.hue = new float[height][width];
        this.saturation = new float[height][width];
        this.value = new float[height][width];

        fillColorChannelsFrom(image);
    }

    private void fillColorChannelsFrom(Image image) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Color c = image.getPixelColor(x, y);
                float[] hsv = new float[3];
                hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);

                hue[x][y] = hsv[0];
                saturation[x][y] = hsv[1];
                value[x][y] = hsv[2];
            }
        }
    }
}
