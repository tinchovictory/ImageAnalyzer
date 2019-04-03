package ar.edu.itba.ati.model;

import java.util.ArrayList;
import java.util.Collections;

public class Mask {
    public enum Type {
        MEAN, GAUSS, MEDIAN, BORDERS, WEIGHTED_MEDIAN, PREWITT_X, PREWITT_Y;
    }

    private int size;
    private Type type;
    private int borderLength;
    private double deviation;

    public Mask(int size, Type type) {
        this(size, type, 0);
    }

    public Mask(int size, Type type, double deviation) {
        this.size = size;
        this.type = type;
        this.borderLength = (size - 1) / 2;
        this.deviation = deviation;
    }

    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        double[][] poundedMask = getPoundedMask();

        for(int y = borderLength; y < originalChannel.getHeight() - borderLength; y++) {
            for(int x = borderLength; x < originalChannel.getWidth() - borderLength; x++) {
                if(this.type == Type.MEDIAN) {
                    applyMedianToPixel(x, y, newChannel, originalChannel);
                } else if(this.type == Type.WEIGHTED_MEDIAN) {
                    applyWeightedMedianToPixel(x, y, newChannel, originalChannel);
                } else {
                    applyMaskToPixel(x, y, newChannel, originalChannel, poundedMask);
                }
            }
        }

        return newChannel;
    }

    private void applyMaskToPixel(int xCenter, int yCenter, ImageColorChannel newChannel, ImageColorChannel originalChannel, double[][] poundedMask) {
        double newColor = 0;
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                int xPos = xCenter + ( x - borderLength );
                int yPos = yCenter + ( y - borderLength );

                newColor += originalChannel.getPixel(xPos, yPos) * poundedMask[y][x];
            }
        }

        newChannel.setPixel(xCenter, yCenter, (int) newColor);
    }

    private void applyMedianToPixel(int xCenter, int yCenter, ImageColorChannel newChannel, ImageColorChannel originalChannel) {
        ArrayList<Integer> list = new ArrayList<>();

        for(int y = yCenter - borderLength; y <= yCenter + borderLength; y++) {
            for(int x = xCenter - borderLength; x <= xCenter + borderLength; x++) {
                list.add(originalChannel.getPixel(x, y));
            }
        }

        Collections.sort(list);

        int medianPosition = (list.size() - 1) / 2;
        newChannel.setPixel(xCenter, yCenter, list.get(medianPosition));
    }

    private void applyWeightedMedianToPixel(int xCenter, int yCenter, ImageColorChannel newChanel, ImageColorChannel originalChannel) {
        ArrayList<Integer> list = new ArrayList<>();
        int [][] weightedMask = getMedianWightedMask();

        for(int y = yCenter - borderLength, i = 0; y <= yCenter + borderLength; y++, i++) {
            for(int x = xCenter - borderLength, j = 0; x <= xCenter + borderLength; x++, j++) {
                for(int counter = 0; counter < weightedMask[i][j]; counter++) {
                    list.add(originalChannel.getPixel(x, y));
                }
            }
        }

        Collections.sort(list);

        int medianPosition = (list.size() - 1) / 2;
        newChanel.setPixel(xCenter, yCenter, list.get(medianPosition));
    }


    private double[][] getPoundedMask() {
        switch (this.type) {
            case MEAN:
                return generateMeanPoundedMask();
            case MEDIAN:
                return null;
            case WEIGHTED_MEDIAN:
                return null;
            case GAUSS:
                return generateGaussPoundedMask(deviation);
            case BORDERS:
                return generateBordersPoundedMask();
            case PREWITT_X:
                return generatePrewittXMask();
            case PREWITT_Y:
                return generatePrewittYMask();
        }

        throw new IllegalStateException();
    }

    private double[][] generateMeanPoundedMask() {
        double[][] pounds = new double[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                pounds[i][j] = 1.0 / (size * size);
            }
        }

        return pounds;
    }

    private double[][] generateBordersPoundedMask() {
        double[][] pounds = new double[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                pounds[i][j] = -1 / Math.pow(size, 2);

                if(i == (size -1) / 2 && j == i) {
                    pounds[i][j] = ( Math.pow(size, 2) - 1 ) / Math.pow(size, 2);
                }
            }
        }

        return pounds;
    }

    private double[][] generateGaussPoundedMask(double deviation) {
        double[][] pounds = new double[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                pounds[i][j] = getGaussianWights(i - 1, j - 1, deviation);
            }
        }

        return pounds;
    }

    private double getGaussianWights(int x, int y, double deviation) {
        double a = 1.0 / ( 2 * Math.PI * Math.pow(deviation, 2));
        double b = ( Math.pow(x, 2) + Math.pow(y, 2) ) / Math.pow(deviation, 2);
        return a * Math.exp( - b );
    }

    private double[][] generatePrewittXMask() {
        double [][] pounds = new double[size][size];
        int halfSize = size / 2;

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                pounds[i][j] = i - halfSize;
            }
        }

        return pounds;
    }

    private double[][] generatePrewittYMask() {
        double [][] pounds = new double[size][size];
        int halfSize = size / 2;

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                pounds[i][j] = j - halfSize;
            }
        }

        return pounds;
    }

    private int[][] getMedianWightedMask() {
        int[][] mask = new int[size][size];
        int halfSize = size / 2;

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                int exp1 = i;
                int exp2 = j;

                if(i > halfSize) {
                    exp1 = size - i - 1;
                }

                if(j < halfSize) {
                    exp2 = size - j - 1;
                }

                int exp = exp1 + exp2;

                mask[i][j] = (int) Math.pow(2, exp);
            }
        }

        return mask;
    }


    public int getBorderLength() {
        return borderLength;
    }
}
