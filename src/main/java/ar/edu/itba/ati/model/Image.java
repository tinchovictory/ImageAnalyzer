package ar.edu.itba.ati.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Image {
    private ImageType imageType;
    private ImageExtension imageExtension;
    private ImageColorChannel redChannel;
    private ImageColorChannel greenChannel;
    private ImageColorChannel blueChannel;
    private int width;
    private int height;

    public Image(int width, int height, ImageType imageType, ImageExtension imageExtension) {
        this.imageType = imageType;
        this.imageExtension = imageExtension;
        this.redChannel = new ImageColorChannel(width, height);
        this.greenChannel = new ImageColorChannel(width, height);
        this.blueChannel = new ImageColorChannel(width, height);
        this.width = width;
        this.height = height;
    }

    public Image(BufferedImage bufferedImage, ImageType imageType, ImageExtension imageExtension) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight(), imageType, imageExtension);

        for(int height = 0; height < bufferedImage.getHeight(); height++) {
            for(int width = 0; width < bufferedImage.getWidth(); width++) {
                Color color = new Color(bufferedImage.getRGB(width, height));
                setPixelColor(width, height, color);
            }
        }
    }

    public BufferedImage getBufferdImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public ImageExtension getImageExtension() {
        return imageExtension;
    }

    private int getBufferedImageType() {
        if(imageType == ImageType.GRAY_SCALE) {
            return BufferedImage.TYPE_BYTE_GRAY;
        }
        return BufferedImage.TYPE_INT_RGB;
    }

    public Color getPixelColor(int x, int y) {
        return new Color(redChannel.getPixel(x, y), greenChannel.getPixel(x, y), blueChannel.getPixel(x, y));
    }

    public void setPixelColor(int x, int y, Color color) {
        redChannel.setPixel(x, y, color.getRed());
        greenChannel.setPixel(x, y, color.getGreen());
        blueChannel.setPixel(x, y, color.getBlue());
    }

    public Color getPixelsMean(Point p1, Point p2) { // p1.x < p2.x
        double red = 0, green = 0, blue = 0;

        if(p2.getY() < p1.getY()) {
            return getPixelsMean(p2, p1);
        }

        /* p1.x < p2.x && p1.y < p2.y */

        for(int x = (int) p1.getX(); x < (int) p2.getX(); x++) {
            for(int y = (int) p1.getY(); y < (int) p2.getY(); y++) {
                red +=  redChannel.getPixel(x, y);
                green += greenChannel.getPixel(x, y);
                blue += blueChannel.getPixel(x, y);
            }
        }

        int pixelsAmount = (int) ((p2.getX() - p1.getX()) * (p2.getY() - p1.getY()));

        return new Color((int)(red/pixelsAmount), (int)(green/pixelsAmount), (int)(blue/pixelsAmount));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public HSVImage toHSV() {
        return new HSVImage(this);
    }

    public BufferedImage getRedImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color redColor = new Color(color.getRed(), 0, 0);
                bufferedImage.setRGB(x, y, redColor.getRGB());
            }
        }

        return bufferedImage;
    }

    public BufferedImage getGreenImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color greenColor = new Color(0, color.getGreen(), 0);
                bufferedImage.setRGB(x, y, greenColor.getRGB());
            }
        }

        return bufferedImage;
    }


    public BufferedImage getBlueImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = getPixelColor(x, y);
                Color blueColor = new Color(0, 0, color.getBlue());
                bufferedImage.setRGB(x, y, blueColor.getRGB());
            }
        }

        return bufferedImage;
    }

    public Image cropImage(Point p1, Point p2) {
        int ymin = (int) p1.getY();
        int ymax = (int) p2.getY();
        if(ymax < ymin) {
            int aux = ymax;
            ymax = ymin;
            ymin = aux;
        }

        Image newImage = new Image((int) (p2.getX() - p1.getX()), ymax - ymin, imageType, imageExtension);

        for(int x = (int) p1.getX(); x < (int) p2.getX(); x++) {
            for(int y = ymin; y < ymax; y++) {
                newImage.setPixelColor(x, y, getPixelColor(x, y));
            }
        }

        return newImage;
    }

    @Override
    public String toString() {
        return redChannel.toString();
    }
}
