package ar.edu.itba.ati;

import static ar.edu.itba.ati.Utitlites.RGBtoHSV;

public class ImageRGB extends Image {

    public ImageRGB(int widht, int lenght, PixelRGB[][] pixels) {
        this.width = widht;
        this.length = lenght;
        this.pixels = pixels;
    }



    public ImageHSV getAsHSV(){
        PixelHSV[][] pixels= new PixelHSV[width][length];

        for (int i =0 ; i< width ; i++){
            for (int j =0 ; j<length; j++){
                pixels[i][j] = RGBtoHSV((PixelRGB) this.pixels[width][length]);
            }
        }
        return new ImageHSV(width,length,pixels);
    }

}
