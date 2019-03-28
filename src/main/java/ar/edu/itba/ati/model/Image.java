package ar.edu.itba.ati.model;

import ar.edu.itba.ati.Utils;

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

    public void toGrayScale() {
        HSVImage hsvImage = this.toHSV();
        ImageColorChannel valueChannel = hsvImage.getValueChannel();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int greyColor = valueChannel.getPixel(x, y);
                Color color = new Color(greyColor, greyColor, greyColor);
                setPixelColor(x, y, color);
            }
        }
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


        for(int height = 0; height < newImage.getHeight(); height++) {
            for(int width = 0; width < newImage.getWidth(); width++) {
                newImage.setPixelColor(width, height, getPixelColor(width, height));
            }
        }

        return newImage;
    }

    public void add(Image other){
        if(other.getWidth() != this.getWidth() || other.getHeight() != this.getHeight()){
            throw new IllegalArgumentException("Images must be of the same size");
        }
        this.redChannel.add(other.redChannel);
        this.greenChannel.add(other.greenChannel);
        this.blueChannel.add(other.blueChannel);

    }

    public void substract(Image other){
        if(other.getWidth() != this.getWidth() || other.getHeight() != this.getHeight()){
            throw new IllegalArgumentException("Images must be of the same size");
        }
        this.redChannel.substract(other.redChannel);
        this.greenChannel.substract(other.greenChannel);
        this.blueChannel.substract(other.blueChannel);
    }

    public void multiplyBy(int value) {
        this.redChannel.multiplyBy(value);
        this.greenChannel.multiplyBy(value);
        this.blueChannel.multiplyBy(value);

        int highestPixel = getHighestPixel();

        this.redChannel.transformPixelsWithMax(highestPixel);
        this.greenChannel.transformPixelsWithMax(highestPixel);
        this.blueChannel.transformPixelsWithMax(highestPixel);
    }

    private int getHighestPixel() {
        int highest = redChannel.getHighestPixel();
        int greenHighest = greenChannel.getHighestPixel();
        int blueHighest = blueChannel.getHighestPixel();
        if(greenHighest > highest) {
            highest = greenHighest;
        }
        if(blueHighest > highest) {
            highest = blueHighest;
        }
        return highest;
    }

    public void compressDynamicRange(){
        this.redChannel.compressDynamicRange();
        this.greenChannel.compressDynamicRange();
        this.blueChannel.compressDynamicRange();
    }

    public void powerFunction(double gamma) {
        this.redChannel.powerFunction(gamma);
        this.greenChannel.powerFunction(gamma);
        this.blueChannel.powerFunction(gamma);
    }

    public void setNegative(){
        this.redChannel.setNegative();
        this.greenChannel.setNegative();
        this.blueChannel.setNegative();
    }

    public void applyContrast(int minContrast, int maxContrast) { // TODO: Get r1, r2 from image
        this.redChannel.applyContrast(minContrast, maxContrast);
        this.greenChannel.applyContrast(minContrast, maxContrast);
        this.blueChannel.applyContrast(minContrast, maxContrast);
    }

    public void applyThreshold(int threshold) {
        this.redChannel.applyThreshold(threshold);
        this.greenChannel.applyThreshold(threshold);
        this.blueChannel.applyThreshold(threshold);
    }

    public double[] getGreyFrequency() {
        Image copyImage = this.cloneImage();
        copyImage.toGrayScale();
        return copyImage.redChannel.getFrequency();
    }

    public void equalizeFrequencies() {
        equalizeColor(redChannel);
        equalizeColor(greenChannel);
        equalizeColor(blueChannel);
    }

    private void equalizeColor(ImageColorChannel colorChannel) {
        int[] equalizedColors = getEqualizedColors(colorChannel);
        colorChannel.updateWith(equalizedColors);
    }

    private int[] getEqualizedColors(ImageColorChannel colorChannel) {
        int[] equalizedColors = new int[256];

        double[] accumFrequency = getAccumFrequencies(colorChannel);
        double minFreq = min(accumFrequency);
        for(int i = 0; i < equalizedColors.length; i++) {
            equalizedColors[i] = (int) Math.floor( (accumFrequency[i] - minFreq) / (1.0 - minFreq) * 255 + 0.5 );
        }

        return equalizedColors;
    }

    private double[] getAccumFrequencies(ImageColorChannel colorChannel) {
        double[] accumFrequency = new double[256];
        double[] relativeFrequency = colorChannel.getFrequency();
        double accum = 0;

        for(int i = 0; i < accumFrequency.length; i++) {
            accum += relativeFrequency[i];
            accumFrequency[i] = accum;
        }

        return accumFrequency;
    }

    private double min(double[] array) {
        double min = Double.MAX_VALUE;
        for(int i = 0; i < array.length; i++) {
            if(array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }


    public Image cloneImage() {
        Image image = new Image(width, height, imageType, imageExtension);

        for(int height = 0; height < this.height; height++) {
            for(int width = 0; width < this.width; width++) {
                image.setPixelColor(width, height, this.getPixelColor(width, height));
            }
        }

        return image;
    }

    public void applyAditiveGaussNoise(double phi, double mu) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {

                //if(Math.random() < 0.5) {
                    double noise = Utils.randomGaussNumber(phi * 100, 0);
                    addAllBandsPixel(x, y, noise);
                //}
            }
        }
    }

    public void applyMultiplicativeRayleighNoise(double epsilon) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                //if(Math.random() < 0.5) {
                    double noise = Utils.randomRayeighNumber(epsilon);
                    multiplyAllBandsPixel(x, y, noise);
                //}
            }
        }
    }

    public void applyMultiplicativeExponentialNoise(double lambda) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                //if(Math.random() < 0.5) {
                    double noise = Utils.randomExponentialNumber(lambda);
                    multiplyAllBandsPixel(x, y, noise);
                //}
            }
        }
    }

    private void addAllBandsPixel(int x, int y, double noise) {
        redChannel.addToPixel(x, y, noise);
        greenChannel.addToPixel(x, y, noise);
        blueChannel.addToPixel(x, y, noise);

        int minPixel = minPixel();
        int maxPixel = maxPixel();

        redChannel.normalizePixels(minPixel, maxPixel);
        greenChannel.normalizePixels(minPixel, maxPixel);
        blueChannel.normalizePixels(minPixel, maxPixel);
    }

    private void multiplyAllBandsPixel(int x, int y, double noise) {
        redChannel.multiplyToPixel(x, y, noise);
        greenChannel.multiplyToPixel(x, y, noise);
        blueChannel.multiplyToPixel(x, y, noise);

        int minPixel = minPixel();
        int maxPixel = maxPixel();

        redChannel.normalizePixels(minPixel, maxPixel);
        greenChannel.normalizePixels(minPixel, maxPixel);
        blueChannel.normalizePixels(minPixel, maxPixel);
    }

    public void applySaltAndPepperNoise(double deviation) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                double rand = Math.random();

                if(rand < 0.5 - deviation/2) {
                    setAllBandsPixel(x, y, 0);
                }
                if(rand > 0.5 + deviation/2) {
                    setAllBandsPixel(x, y, 255);
                }
            }
        }
    }

    private void setAllBandsPixel(int x, int y, int color) {
        redChannel.setPixel(x, y, color);
        greenChannel.setPixel(x, y, color);
        blueChannel.setPixel(x, y, color);
    }


    public void applyMask(Mask mask) {
        redChannel = mask.applyTo(redChannel);
        greenChannel = mask.applyTo(greenChannel);
        blueChannel = mask.applyTo(blueChannel);
    }

    private int minPixel() {
        int minPixel = redChannel.minPixel();
        int blueMinPixel = blueChannel.minPixel();
        int greenMinPixel = greenChannel.minPixel();

        if(minPixel > blueMinPixel) {
            minPixel = blueMinPixel;
        }

        if(minPixel > greenMinPixel) {
            minPixel = greenMinPixel;
        }
        return minPixel;
    }

    private int maxPixel() {
        int maxPixel = redChannel.maxPixel();
        int blueMaxPixel = blueChannel.maxPixel();
        int greenMaxPixel = greenChannel.maxPixel();

        if(maxPixel < blueMaxPixel) {
            maxPixel = blueMaxPixel;
        }

        if(maxPixel < greenMaxPixel) {
            maxPixel = greenMaxPixel;
        }
        return maxPixel;
    }


    @Override
    public String toString() {
        return redChannel.toString();
    }
}
