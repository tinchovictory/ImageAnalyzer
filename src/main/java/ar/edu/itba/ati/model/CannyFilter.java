package ar.edu.itba.ati.model;

import ar.edu.itba.ati.model.Masks.SobelMask;

public class CannyFilter {

    private final static int GAUSS_MASK_SIZE = 11;
    private final static double GAUSS_MASK_DEVIATION = 2;

    private int t1;
    private int t2;

    public CannyFilter(int t1, int t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Image applyTo(Image originalImage) {
        Image newImage = originalImage.cloneImage();
        newImage.toGrayScale();

        // Apply gauss filter
        Mask gaussMask = new Mask(GAUSS_MASK_SIZE, Mask.Type.GAUSS, GAUSS_MASK_DEVIATION);
        newImage.applyMask(gaussMask);

        // Get Gx and Gy from sobel
        SobelMask sobelMask = new SobelMask();
        ImageColorChannel gx = sobelMask.applySingleMaskTo(newImage.getRedChannel(), sobelMask.generateXMask());
        ImageColorChannel gy = sobelMask.applySingleMaskTo(newImage.getRedChannel(), sobelMask.generateYMask());

        ImageColorChannel sobelChannel = sobelMask.joinMasks(gx, gy);

        ImageColorChannel cannyChannel = removeFakeMax(sobelChannel, gx, gy);

        removeBorders(cannyChannel);

        newImage.setRedChannel(cannyChannel);
        newImage.setGreenChannel(cannyChannel);
        newImage.setBlueChannel(cannyChannel);

        newImage.normalizeImage(GAUSS_MASK_SIZE);

        umbralizeCanny(newImage);

        return newImage;
    }

    private ImageColorChannel removeFakeMax(ImageColorChannel originalChannel, ImageColorChannel gx, ImageColorChannel gy) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int x = GAUSS_MASK_SIZE; x < originalChannel.getWidth() - GAUSS_MASK_SIZE; x++) {
            for(int y = GAUSS_MASK_SIZE; y < originalChannel.getHeight() - GAUSS_MASK_SIZE; y++) {
                double Gx = gx.getPixel(x, y);
                double Gy = gy.getPixel(x, y);

                double angle = 90;
                if(Gx != 0) {
                    angle = Math.atan(Gy / Gx);
                }

                int newColor = removeFakeMaxToPixel(x, y, originalChannel, getRealAngle(angle));
                newChannel.setPixel(x, y, newColor);
            }
        }

        return newChannel;
    }

    private int getRealAngle(double inputAngle) {
        double angle = inputAngle + Math.PI / 2;
        if(angle <= Math.PI / 8) {
            return 0;
        }
        if(angle <= Math.PI * 3.0 / 8) {
            return 45;
        }
        if(angle <= Math.PI * 5.0 / 8) {
            return 90;
        }
        if(angle <= Math.PI * 7.0 / 8) {
            return 135;
        }
        return 0;
    }

    private int removeFakeMaxToPixel(int x, int y, ImageColorChannel originalChannel, int angle) {
        int currentColor = originalChannel.getPixel(x, y);
        int pixelR = originalChannel.getPixel(x + 1, y);
        int pixelTR = originalChannel.getPixel(x + 1, y - 1);
        int pixelT = originalChannel.getPixel(x, y - 1);
        int pixelTL = originalChannel.getPixel(x - 1, y - 1);
        int pixelL = originalChannel.getPixel(x - 1, y);
        int pixelBL = originalChannel.getPixel(x - 1, y + 1);
        int pixelB = originalChannel.getPixel(x, y + 1);
        int pixelBR = originalChannel.getPixel(x + 1, y + 1);

        if(x == GAUSS_MASK_SIZE) {
            pixelTL = 0;
            pixelL = 0;
            pixelBL = 0;
        }
        if(x == originalChannel.getWidth() - GAUSS_MASK_SIZE - 1) {
            pixelTR = 0;
            pixelR = 0;
            pixelBR = 0;
        }
        if(y == GAUSS_MASK_SIZE) {
            pixelTL = 0;
            pixelT = 0;
            pixelTR = 0;
        }
        if(y == originalChannel.getWidth() - GAUSS_MASK_SIZE - 1) {
            pixelBL = 0;
            pixelB = 0;
            pixelBR = 0;
        }

        if(angle == 0 && (currentColor < pixelL || currentColor < pixelR)) {
            return 0;
        }
        if(angle == 45 && (currentColor < pixelTR || currentColor < pixelBL)) {
            return 0;
        }
        if(angle == 90 && (currentColor < pixelT || currentColor < pixelB)) {
            return 0;
        }
        if(angle == 135 && (currentColor < pixelTL || currentColor < pixelBR)) {
            return 0;
        }

        return currentColor;
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

    private Image umbralizeCanny(Image originalImage) {
        ImageColorChannel umbralizedChannel = umbralizeCanny(originalImage.getRedChannel());
        originalImage.setRedChannel(umbralizedChannel);
        originalImage.setGreenChannel(umbralizedChannel);
        originalImage.setBlueChannel(umbralizedChannel);

        return originalImage;
    }

    private ImageColorChannel umbralizeCanny(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int x = GAUSS_MASK_SIZE; x < newChannel.getWidth() - GAUSS_MASK_SIZE; x++) {
            for(int y = GAUSS_MASK_SIZE; y < newChannel.getHeight() - GAUSS_MASK_SIZE; y++) {
                int currentColor = originalChannel.getPixel(x, y);

                if(currentColor < t1) {
                    newChannel.setPixel(x, y, 0);
                }
                if(currentColor >= t2) {
                    newChannel.setPixel(x, y, 255);
                }
                if(currentColor >= t1 && currentColor < t2) {
                    if(checkNeighborBorder(x, y, originalChannel)) {
                        newChannel.setPixel(x, y, 255);
                    } else {
                        newChannel.setPixel(x, y, 0);
                    }
                }
            }
        }

        return newChannel;
    }

    private boolean checkNeighborBorder(int x, int y, ImageColorChannel channel) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(channel.getPixel(x+i, y+j) >= t2) {
                    return true;
                }
            }
        }

        return false;
    }
}
