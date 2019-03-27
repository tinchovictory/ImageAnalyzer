package ar.edu.itba.ati.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HSVImage {
    private ImageType imageType;
    private ImageExtension imageExtension;
    private ImageColorChannel hue;
    private ImageColorChannel saturation;
    private ImageColorChannel value;
    private int width;
    private int height;


    public HSVImage(Image image) {
        this.imageType = image.getImageType();
        this.imageExtension = image.getImageExtension();
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.hue = new ImageColorChannel(width, height);
        this.saturation = new ImageColorChannel(width, height);
        this.value = new ImageColorChannel(width, height);

        fillColorChannelsFrom(image);
    }

    private void fillColorChannelsFrom(Image image) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Color c = image.getPixelColor(x, y);
                float[] hsv = new float[3];
                hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);

                hue.setPixel(x, y, (int) (hsv[0] * 255));
                saturation.setPixel(x, y, (int) (hsv[1] * 255));
                value.setPixel(x, y, (int) (hsv[2] * 255));
            }
        }

    }

    private int getBufferedImageType() {
        if(imageType == ImageType.GRAY_SCALE) {
            return BufferedImage.TYPE_BYTE_GRAY;
        }
        return BufferedImage.TYPE_INT_RGB;
    }

    public BufferedImage getHueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int hueColor = hue.getPixel(x, y);
                Color color = new Color(hueColor, hueColor, hueColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }

    public BufferedImage getSaturationImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int saturationColor = saturation.getPixel(x, y);
                Color color = new Color(saturationColor, saturationColor, saturationColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }


    public BufferedImage getValueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int valueColor = value.getPixel(x, y);
                Color color = new Color(valueColor, valueColor, valueColor);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }

    public ImageColorChannel getValueChannel() {
        return this.value;
    }
}
