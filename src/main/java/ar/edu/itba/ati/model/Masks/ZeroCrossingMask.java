package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public abstract class ZeroCrossingMask extends Mask {

    public ZeroCrossingMask(int size) {
        super(size, Type.OTHER);
    }

    @Override
    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel updatedChannel = applySingleMaskTo(originalChannel, getPoundedMask());

        return crossingZeros(updatedChannel);
    }

    private ImageColorChannel applySingleMaskTo(ImageColorChannel originalChannel, double[][] poundedMask) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = getBorderLength(); y < originalChannel.getHeight() - getBorderLength(); y++) {
            for(int x = getBorderLength(); x < originalChannel.getWidth() - getBorderLength(); x++) {
                applyMaskToPixel(x, y, newChannel, originalChannel, poundedMask);
            }
        }

        return newChannel;
    }

    private ImageColorChannel crossingZeros(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = getBorderLength(); y < originalChannel.getHeight() - getBorderLength(); y++) {
            for(int x = getBorderLength() + 1; x < originalChannel.getWidth() - getBorderLength(); x++) {
                int currentPixel = originalChannel.getPixel(x, y);
                int prevPixel = originalChannel.getPixel(x - 1, y);
                Integer nextPixel = x + 1 < originalChannel.getWidth() - getBorderLength() ? originalChannel.getPixel(x + 1, y) : null;

                if(originalChannel.getPixel(x, y) == 0) {
                    /* Check + 0 - or - 0 + */
                    if(nextPixel != null) {
                        if(prevPixel > 0 && nextPixel < 0 || prevPixel < 0 && nextPixel > 0) {
                            newChannel.setPixel(x, y, Math.abs(prevPixel) + Math.abs(nextPixel));
                        } else {
                            newChannel.setPixel(x, y, 0);
                        }
                        newChannel.setPixel(x - 1, y, 0);
                    }

                } else {
                    /* Check + - or - + */
                    if(prevPixel > 0 && currentPixel < 0 || prevPixel < 0 && currentPixel > 0) {
                        newChannel.setPixel(x - 1, y, Math.abs(prevPixel) + Math.abs(currentPixel));
                    } else {
                        newChannel.setPixel(x - 1, y, 0);
                    }
                }
            }
            newChannel.setPixel(originalChannel.getWidth() - getBorderLength() - 1, y, 0);
        }

        return newChannel;
    }

    public abstract double[][] getPoundedMask();
}
