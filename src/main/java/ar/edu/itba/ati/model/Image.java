package ar.edu.itba.ati.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

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
                redChannel.setPixel(width, height, color.getRed());
                greenChannel.setPixel(width, height, color.getGreen());
                blueChannel.setPixel(width, height, color.getBlue());
            }
        }
    }

    public BufferedImage getBufferdImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, getBufferedImageType());

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = new Color((int) redChannel.getPixel(x, y), (int) greenChannel.getPixel(x, y), (int) blueChannel.getPixel(x, y));
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

    @Override
    public String toString() {
        return redChannel.toString();
    }
}
