package ar.edu.itba.ati.model;

import ar.edu.itba.ati.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    }

    public void multiplyToPixel(int x, int y, double value) {
        pixels[x][y] *= value;
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

    public void applyGlobalThreshold() {
        int prevThreshold = 0;
        int threshold = 128;

        int dT = 1;

        while(Math.abs(prevThreshold - threshold) > dT) {
            int m1 = 0, n1 = 0;
            int m2 = 0, n2 = 0;

            for(int i = 0; i < pixels.length; i++) {
                for(int j = 0; j < pixels[0].length; j++) {
                    if(this.pixels[i][j] <= threshold) {
                        m1 += this.pixels[i][j];
                        n1++;
                    } else {
                        m2 += this.pixels[i][j];
                        n2++;
                    }
                }
            }

            m1 /= n1;
            m2 /= n2;

            prevThreshold = threshold;
            threshold = (m1 + m2) / 2;
        }

        applyThreshold(threshold);
    }

    public void applyOtsuThreshold() {
        double[] freq = getFrequency();

        double[] accumFreq = new double[256];
        double[] accumMean = new double[256];

        double accum = 0;
        double accumM = 0;

        for(int i = 0; i < freq.length; i++) {
            accum += freq[i];
            accumM += (freq[i] * i);

            accumFreq[i] = accum;
            accumMean[i] = accumM;
        }

        double mg = accumMean[255];

        double[] variance = getOtsuVariance(accumFreq, accumMean, mg);

        int max = getAllOtsuVarianceMax(variance);

        applyThreshold(max);
    }

    private double[] getOtsuVariance(double[] accumFreq, double[] accumMean, double mg) {
        double[] variance = new double[256];

        for(int i = 0; i < variance.length; i++) {
            variance[i] = Math.pow(mg * accumFreq[i] - accumMean[i], 2) / ( accumFreq[i] * ( 1 -  accumFreq[i]) );
        }

        return variance;
    }

    private int getAllOtsuVarianceMax(double[] variance) {
        double max = 0;
        Set<Integer> maxIdxs = new HashSet<>();

        for(int i = 0; i < variance.length; i++) {
            if(variance[i] > max) {
                max = variance[i];
                maxIdxs.clear();
                maxIdxs.add(i);
            } else if(variance[i] == max) {
                maxIdxs.add(i);
            }
        }

        int sum = 0;
        int ammount = 0;
        for(Integer i : maxIdxs) {
            sum += i;
            ammount++;
        }

        return sum / ammount;
    }

    public void applyIsotropicDiffusion(int iterations) {
        for(int i = 0; i < iterations; i++) {
            ImageColorChannel originalChannel = this.cloneChannel();
            applyIsotropicDiffusion(originalChannel);
        }
    }

    private void applyIsotropicDiffusion(ImageColorChannel originalChannel) {
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                int DnIij = 0, DsIij = 0, DeIij = 0, DwIij = 0;
                int centerPixel = originalChannel.getPixel(x, y);

                if(x > 0) {
                    DwIij = originalChannel.getPixel(x - 1, y) - centerPixel;
                }
                if(x < getWidth() - 1) {
                    DeIij = originalChannel.getPixel(x + 1, y) - centerPixel;
                }
                if(y > 0) {
                    DnIij = originalChannel.getPixel(x, y - 1) - centerPixel;
                }
                if(y < getWidth() - 1) {
                    DsIij = originalChannel.getPixel(x, y + 1) - centerPixel;
                }

                double Cnij = 1, Csij = 1, Ceij = 1, Cwij = 1;

                double newColor = centerPixel + 0.25 * ( DnIij * Cnij + DsIij * Csij + DeIij * Ceij + DwIij * Cwij);

                setPixel(x, y, (int) newColor);
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
                pixels[x][y] = (int) ( (double) pixels[x][y] / max * 255);
            }
        }
    }

    public int minPixel(int startX, int startY, int endX, int endY) {
        int min = 255;
        for(int  y = startY; y < endY; y++) {
            for(int x = startX; x < endX; x++) {
                if(min > pixels[x][y]) {
                    min = pixels[x][y];
                }
            }
        }
        return min;
    }

    public int maxPixel(int startX, int startY, int endX, int endY) {
        int max = 0;
        for(int  y = startY; y < endY; y++) {
            for(int x = startX; x < endX; x++) {
                if(max < pixels[x][y]) {
                    max = pixels[x][y];
                }
            }
        }
        return max;
    }

    public void normalizePixels(int minPixel, int maxPixel, int border) {
        for(int y = border; y < height - border; y++) {
            for(int x = border; x < width - border; x++) {
                pixels[x][y] = Utils.toRange(pixels[x][y], minPixel, maxPixel);
            }
        }
    }
}
