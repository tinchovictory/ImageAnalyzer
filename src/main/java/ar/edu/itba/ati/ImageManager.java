package ar.edu.itba.ati;

import ar.edu.itba.ati.model.ImageExtension;
import ar.edu.itba.ati.model.ImageType;
import ar.edu.itba.ati.model.Image;
import org.apache.sanselan.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManager {

    public static Image loadImage(File image) throws ImageReadException, IOException {
        BufferedImage bufferedImage = Sanselan.getBufferedImage(image);
        ImageInfo info = Sanselan.getImageInfo(image);
        ImageExtension extension;

        if(info.getFormat() == ImageFormat.IMAGE_FORMAT_PGM){
            extension = ImageExtension.PGM;
        } else if(info.getFormat() == ImageFormat.IMAGE_FORMAT_PPM){
            extension = ImageExtension.PPM;
        } else if(info.getFormat() == ImageFormat.IMAGE_FORMAT_UNKNOWN){
            throw new IllegalStateException("Unsupported image format");
        } else {
            throw new IllegalStateException("Unsupported image format");
        }

        if(bufferedImage.getType() == BufferedImage.TYPE_INT_RGB){
            return new Image(bufferedImage, ImageType.RGB, extension);
        } else if(bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY){
            return new Image(bufferedImage, ImageType.GRAY_SCALE, extension);
        } else {
            throw new IllegalStateException("Image wasn't RGB nor Grayscale");
        }

    }

    public static void saveImage(File imagePath, Image image) throws ImageWriteException, IOException {
        BufferedImage bufferedImage = image.getBufferdImage();
        ImageFormat imageFormat = getImageFormat(image);

        Sanselan.writeImage(bufferedImage, imagePath, imageFormat, null);
    }

    public static Image loadRawImage(File imagePath, int width, int height) throws IOException {
        Image image = new Image(width, height, ImageType.GRAY_SCALE, ImageExtension.RAW);

        byte[] imageData = loadBytesFromFile(imagePath);

        int i = 0;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int dataValue = imageData[i] < 0 ? imageData[i] + 256 : imageData[i];
                Color color = new Color(dataValue, dataValue, dataValue);
                image.setPixelColor(x, y, color);
                i++;

            }
        }
        return image;
    }

    private static ImageFormat getImageFormat(Image image) {
        if(image.getImageExtension() == ImageExtension.PGM) {
            return ImageFormat.IMAGE_FORMAT_PGM;
        } else if(image.getImageExtension() == ImageExtension.PPM) {
            return ImageFormat.IMAGE_FORMAT_PPM;
        }
        throw new IllegalArgumentException();
    }

    private static byte[] loadBytesFromFile(File image) throws IOException {
        InputStream inputStream = new FileInputStream(image);
        byte[] imageBytes = new byte[(int) image.length()];

        int pos = 0, reading = 0;
        while(pos < imageBytes.length && (reading = inputStream.read(imageBytes, pos, imageBytes.length - pos)) >= 0) {
            pos += reading;
        }

        if(pos < imageBytes.length) {
            throw new IOException();
        }

        inputStream.close();
        return imageBytes;
    }

}
