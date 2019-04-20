package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public abstract class SimpleMask extends Mask {
    public SimpleMask(int size) {
        super(size, Type.OTHER);
    }

    @Override
    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        int maskBorder  = (getMaskSize() - 1) / 2;
        double[][] poundedMask = getPoundedMask();

        for(int y = maskBorder; y < originalChannel.getHeight() - maskBorder; y++) {
            for(int x = maskBorder; x < originalChannel.getWidth() - maskBorder; x++) {
                applyMaskToPixel(x, y, newChannel, originalChannel, poundedMask);
            }
        }

        return newChannel;
    }

    public abstract double[][] getPoundedMask();
}
