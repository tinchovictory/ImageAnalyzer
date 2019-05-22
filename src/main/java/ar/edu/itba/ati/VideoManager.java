package ar.edu.itba.ati;

import ar.edu.itba.ati.model.Image;
import ar.edu.itba.ati.model.Video;
import org.apache.sanselan.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VideoManager {

    public static Video loadVideo(List<File> files) throws ImageReadException, IOException {
        Video video = new Video();

        for(File f : files) {
            Image frame = ImageManager.loadImage(f);
            video.addNextFrame(frame);
        }

        return video;
    }
}
