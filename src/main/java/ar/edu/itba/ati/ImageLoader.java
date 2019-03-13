package ar.edu.itba.ati;

import ar.edu.itba.ati.model.ImageExtension;
import ar.edu.itba.ati.model.ImageType;
import ar.edu.itba.ati.model.Image;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

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

}
