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
                int x2 = (x - (IMAGE_SIZE / 2)) * (x - (IMAGE_SIZE / 2));
                int y2 = (y  - (IMAGE_SIZE / 2)) * (y  - (IMAGE_SIZE / 2));
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

    public static Image buildGreyGradient() {
        Image image = new Image(256, 256, ImageType.GRAY_SCALE, ImageExtension.PGM);
        setGreyGradientToImage(image);
        return image;
    }

    private static void setGreyGradientToImage(Image image) {
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                image.setPixelColor(x, y, new Color(y, y, y));
            }
        }
    }

    public static Image buildColorGradient() {
        Image image = new Image(IMAGE_SIZE, IMAGE_SIZE, ImageType.RGB, ImageExtension.PPM);
        return image;
    }

    private static void setColorGradienttoImage(Image image) {
        int changingPos = image.getHeight() / 6;
        int m = (int) (255.0 / changingPos);

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                if(y < changingPos) {
                    int blue = m * y;
                    image.setPixelColor(x, y, new Color(255, 0, blue));
                } else if(y < 2 * changingPos) {
                    int red = -m * (y - changingPos) + 255;
                    image.setPixelColor(x, y, new Color(red, 0, 255));
                } else if(y < 3 * changingPos) {
                    int green = m * (y - 2 * changingPos);
                    image.setPixelColor(x, y, new Color(0, green, 255));
                } else if(y < 4 * changingPos) {
                    int blue = -m * (y - 3 * changingPos) + 255;
                    image.setPixelColor(x, y, new Color(0, 255, blue));
                } else if(y < 5 * changingPos) {
                    int red = m * (y - 4 * changingPos);
                    image.setPixelColor(x, y, new Color(red, 255, 0));
                } else {
                    int green = -m * (y - 5 * changingPos) + 255;
                    image.setPixelColor(x, y, new Color(255, green, 0));
                }
            }
        }
    }
}
