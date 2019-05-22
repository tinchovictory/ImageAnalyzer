package ar.edu.itba.ati.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Video {
    private List<Image> frames;
    private Iterator<Image> frameIterator;
    private int currentFrame;

    public Video() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    public void addNextFrame(Image frame) {
        frames.add(frame);
    }

    public Image getNextFrame() {
        if(frameIterator == null) {
            frameIterator = frames.iterator();
        }

        if(!frameIterator.hasNext()) {
            frameIterator = null;
            return null;
        }

        currentFrame++;
        return frameIterator.next();
    }

    public Image getFrame(int idx) {
        return frames.get(idx);
    }

    public int getFramesAmount() {
        return frames.size();
    }

    public Image getCurrentFrame() {
        return getFrame(currentFrame);
    }

    public void replaceCurrentFrame(Image frame) {
        frames.set(currentFrame, frame);
    }

}
