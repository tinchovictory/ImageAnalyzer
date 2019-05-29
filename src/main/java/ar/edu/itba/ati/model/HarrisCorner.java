package ar.edu.itba.ati.model;

import ar.edu.itba.ati.Utils;
import ar.edu.itba.ati.model.Masks.SobelMask;

import java.awt.*;

public class HarrisCorner {

    private static int GAUSS_MASK_SIZE = 7;
    private static double GAUSS_MASK_DEVIATION = 2;
    private static double HARRIS_K = 0.04;

    public Image applyTo(Image originalImage) {
        Image newImage = originalImage.cloneImage();
        newImage.toGrayScale();

        // Get Gx and Gy from sobel
        SobelMask sobelMask = new SobelMask();
        ImageColorChannel gx = sobelMask.applySingleMaskTo(newImage.getRedChannel(), sobelMask.generateXMask());
        ImageColorChannel gy = sobelMask.applySingleMaskTo(newImage.getRedChannel(), sobelMask.generateYMask());


        // Get Gx2 and Gy2
        ImageColorChannel gx2 = squareChannel(gx);
        ImageColorChannel gy2 = squareChannel(gy);
        ImageColorChannel gxy = multiplyChannels(gx, gy);

        // Apply gauss to the channels
        Mask gaussMask = new Mask(GAUSS_MASK_SIZE, Mask.Type.GAUSS, GAUSS_MASK_DEVIATION);
        gx2 = gaussMask.applyTo(gx2);
        gy2 = gaussMask.applyTo(gy2);
        gxy = gaussMask.applyTo(gxy);

        ImageColorChannel cim = getCim(gx2, gy2, gxy);

        removeBorders(cim);

        newImage.setRedChannel(cim.cloneChannel());
        newImage.setGreenChannel(cim.cloneChannel());
        newImage.setBlueChannel(cim.cloneChannel());

        newImage.normalizeImage(GAUSS_MASK_SIZE);

        return newImage;
    }

    public Image threshold(Image originalImage, int value) {
        Image harrisImage = applyTo(originalImage);
        Image thresholdImage = originalImage.cloneImage();

        for(int y = 0; y < originalImage.getWidth(); y++) {
            for(int x = 0; x < originalImage.getWidth(); x++) {
                if(harrisImage.getRedChannel().getPixel(x, y) > value) {
                    thresholdImage.setPixelColor(x, y, Color.MAGENTA);
                } else {
                    thresholdImage.setPixelColor(x, y, originalImage.getPixelColor(x, y));
                }
            }
        }

        return thresholdImage;
    }


    private ImageColorChannel squareChannel(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = 0; y < newChannel.getHeight(); y++) {
            for(int x = 0; x < newChannel.getWidth(); x++) {
                int originalColor = originalChannel.getPixel(x, y);
                newChannel.setPixel(x, y, (int) Math.pow(originalColor, 2));
            }
        }

        return newChannel;
    }

    private ImageColorChannel multiplyChannels(ImageColorChannel ch1, ImageColorChannel ch2) {
        ImageColorChannel newChannel = ch1.cloneChannel();

        for(int y = 0; y < newChannel.getHeight(); y++) {
            for(int x = 0; x < newChannel.getWidth(); x++) {
                int newColor = ch1.getPixel(x, y) * ch2.getPixel(x, y);
                newChannel.setPixel(x, y, newColor);
            }
        }

        return newChannel;
    }

    private ImageColorChannel getCim(ImageColorChannel gx2, ImageColorChannel gy2, ImageColorChannel gxy) {
        ImageColorChannel newChannel = gx2.cloneChannel();

        for(int y = 0; y < newChannel.getHeight(); y++) {
            for(int x = 0; x < newChannel.getWidth(); x++) {
                long gx2Color = (long) gx2.getPixel(x, y);
                long gy2Color = (long) gy2.getPixel(x, y);
                long gxyColor = (long) gxy.getPixel(x, y);

                int cim = (int) ((gx2Color * gy2Color - Math.pow(gxyColor, 2)) - HARRIS_K * Math.pow(gx2Color + gy2Color, 2));

                newChannel.setPixel(x, y, cim);
            }
        }

        return newChannel;
    }


    private void removeBorders(ImageColorChannel channel) {
        for(int x = 0; x < channel.getWidth(); x++) {
            for(int y = 0; y < channel.getHeight(); y++) {
                if(x < GAUSS_MASK_SIZE) {
                    channel.setPixel(x, y, 0);
                }
                if(x >= channel.getWidth() - GAUSS_MASK_SIZE) {
                    channel.setPixel(x, y, 0);
                }

                if(y < GAUSS_MASK_SIZE) {
                    channel.setPixel(x, y, 0);
                }
                if(y >= channel.getWidth() - GAUSS_MASK_SIZE) {
                    channel.setPixel(x, y, 0);
                }
            }
        }
    }

}
