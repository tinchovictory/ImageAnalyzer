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
}
