package ar.edu.itba.ati.model;

import java.util.Arrays;

public class ImageColorChannel {
    private int width;
    private int height;
    private int[][] pixels;

    public ImageColorChannel(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width][height];
    }

    public void setPixel(int x, int y, int color) {
        pixels[x][y] =  color;
    }

    public int getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void addToPixel(int x, int y, double value) {
        pixels[x][y] += value;

        if(pixels[x][y] > 255) {
            pixels[x][y] = 255;
        }
    }

    public void multiplyToPixel(int x, int y, double value) {
        pixels[x][y] *= value;

        if(pixels[x][y] > 255) {
            pixels[x][y] = 255;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "ImageColorChannel{" +
                "width=" + width +
                ", height=" + height +
                ", pixels=" + Arrays.toString(pixels) +
                " pixels dim:" + pixels.length + " " + pixels[0].length +
                '}';
    }

    public void add(ImageColorChannel other) {
        int[][] otherPixels = other.pixels;

        for(int i=0; i< height;i++){
            for(int j=0; j< width;j++){
                this.pixels[i][j] = (this.pixels[i][j] + otherPixels[i][j])/2;
            }
        }
    }

    public void substract(ImageColorChannel other){
        int[][] otherPixels = other.pixels;

        for(int i=0; i< pixels.length;i++){
            for(int j=0; j< pixels[0].length;j++){
                this.pixels[i][j] = (this.pixels[i][j] - otherPixels[i][j]+255)/2;
            }
        }
    }


    public void compressDynamicRange(){
        int max = -1;
        for(int i=0; i< pixels.length;i++){
            for(int j=0; j< pixels[0].length;j++){
                 max =Math.max(max,pixels[i][j]);
            }
        }
        double c = 254/Math.log(1+max);

        for(int i=0; i< pixels.length;i++){
            for(int j=0; j< pixels[0].length;j++){
                this.pixels[i][j] = (int)(c   * Math.log(1+this.pixels[i][j]));
            }
        }
    }

    public void powerFunction(double gamma) {
        double c = Math.pow(255, 1 - gamma);

        for(int i = 0; i < pixels.length; i++) {
            for(int j = 0; j < pixels[0].length; j++) {
                this.pixels[i][j] = (int) ( c * Math.pow(pixels[i][j], gamma) );
            }
        }
    }

    public void setNegative(){
        this.pixels=this.getNegative().pixels;
    }

    public ImageColorChannel getNegative(){

        ImageColorChannel imageColorChannel = new ImageColorChannel(this.width,this.height);
        for(int i=0; i< pixels.length;i++){
            for(int j=0; j< pixels[0].length;j++){
                imageColorChannel.setPixel(i,j,255-this.getPixel(i,j));
            }
        }
        return imageColorChannel;
    }

    public void applyContrast(int minContrast, int maxContrast) {
        double minM = 70.0 / minContrast;
        double maxM = 76.0 / ( 256 - maxContrast );
        double maxB = 256 - 256 * maxM;

        for(int i = 0; i < pixels.length; i++) {
            for(int j = 0; j < pixels[0].length; j++) {
                if(this.pixels[i][j] < minContrast) {
                    this.pixels[i][j] *= minM;
                } else if(this.pixels[i][j] > maxContrast) {
                    this.pixels[i][j] = (int) (this.pixels[i][j] * maxM + maxB);
                }
            }
        }
    }

    public void applyThreshold(int threshold) {
        for(int i = 0; i < pixels.length; i++) {
            for(int j = 0; j < pixels[0].length; j++) {
                if(this.pixels[i][j] <= threshold) {
                    this.pixels[i][j] = 0;
                } else {
                    this.pixels[i][j] = 255;
                }
            }
        }
    }

    public double[] getFrequency() {
        double[] frequency = new double[256];

        for(int i = 0; i < pixels.length; i++) {
            for(int j = 0; j < pixels[0].length; j++) {
                int colorValue = pixels[i][j];
                frequency[colorValue]++;
            }
        }

        for(int i = 0; i < frequency.length; i++) {
            frequency[i] = frequency[i] / (width * height);
        }

        return frequency;
    }

    public void updateWith(int[] colors) {
        for(int i = 0; i < pixels.length; i++) {
            for(int j = 0; j < pixels[0].length; j++) {
                int originalColor = pixels[i][j];
                int newColor = colors[originalColor];
                setPixel(i, j, newColor);
            }
        }
    }

    public ImageColorChannel cloneChannel() {
        ImageColorChannel newChannel = new ImageColorChannel(width, height);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                newChannel.setPixel(x, y, getPixel(x, y));
            }
        }

        return newChannel;
    }

    public int getHighestPixel() {
        int highest = 0;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(getPixel(x, y) > highest) {
                    highest = getPixel(x, y);
                }
            }
        }
        return highest;
    }

    public void multiplyBy(int value) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                pixels[x][y] *= value;
            }
        }
    }

    public void transformPixelsWithMax(int max) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                pixels[x][y] = (int) ( (double) pixels[x][y] / max * 256);
            }
        }
    }
}
