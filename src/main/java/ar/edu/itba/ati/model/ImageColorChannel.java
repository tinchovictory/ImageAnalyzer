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
}
