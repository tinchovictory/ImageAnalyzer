package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public abstract class ZeroCrossingMask extends Mask {
    private static int MASK_SIZE = 3;
    private static int MASK_BORDER = (MASK_SIZE - 1) / 2;

    public ZeroCrossingMask() {
        super(MASK_SIZE, Type.OTHER);
    }

    @Override
    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel updatedChannel = applySingleMaskTo(originalChannel, getPoundedMask());

        return crossingZeros(updatedChannel);
    }

    private ImageColorChannel applySingleMaskTo(ImageColorChannel originalChannel, double[][] poundedMask) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = MASK_BORDER; y < originalChannel.getHeight() - MASK_BORDER; y++) {
            for(int x = MASK_BORDER; x < originalChannel.getWidth() - MASK_BORDER; x++) {
                applyMaskToPixel(x, y, newChannel, originalChannel, poundedMask);
            }
        }

        return newChannel;
    }

    private ImageColorChannel crossingZeros(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = MASK_BORDER; y < originalChannel.getHeight() - MASK_BORDER; y++) {
            for(int x = MASK_BORDER + 1; x < originalChannel.getWidth() - MASK_BORDER; x++) {
                int currentPixel = originalChannel.getPixel(x, y);
                int prevPixel = originalChannel.getPixel(x - 1, y);
                Integer nextPixel = x + 1 < originalChannel.getWidth() - MASK_BORDER ? originalChannel.getPixel(x + 1, y) : null;

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
        }

        return newChannel;
    }

    public abstract double[][] getPoundedMask();
}
