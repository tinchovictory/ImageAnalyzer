package ar.edu.itba.ati;

import ar.edu.itba.ati.model.Image;
import ar.edu.itba.ati.model.ImageExtension;
import ar.edu.itba.ati.model.ImageType;

import java.awt.*;

public class ImageCreator {

    private static int IMAGE_SIZE = 200;

    public static Image buildCircle() {
        Image image = new Image(IMAGE_SIZE, IMAGE_SIZE, ImageType.GRAY_SCALE, ImageExtension.PGM);

        setCircleInImageOfRadius(IMAGE_SIZE / 4, image);

        return image;
    }

    private static void setCircleInImageOfRadius(int radius, Image image) {
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int x2 = x * x - (IMAGE_SIZE / 2) * (IMAGE_SIZE / 2);
                int y2 = y * y - (IMAGE_SIZE / 2) * (IMAGE_SIZE / 2);
                int r2 = radius * radius;
                if( x2 + y2 <= r2) {
                    image.setPixelColor(x, y, Color.white);
                }
            }
        }
    }

    public static Image buildSquare() {
        Image image = new Image(IMAGE_SIZE, IMAGE_SIZE, ImageType.GRAY_SCALE, ImageExtension.PGM);

        setSquareInImageOfLength(100, image);

        return image;
    }

    private static void setSquareInImageOfLength(int length, Image image) {
        int start = IMAGE_SIZE / 2 - length / 2;
        int end = IMAGE_SIZE - start;
        for(int x = start; x < end; x++) {
            for(int y = start; y < end; y++) {
                image.setPixelColor(x, y, Color.white);
            }
        }
    }
}
