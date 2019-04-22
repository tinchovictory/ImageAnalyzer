package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public abstract class TwoDirectionsMask extends Mask {

    private static int MASK_SIZE = 3;
    private static int MASK_BORDER = (MASK_SIZE - 1) / 2;

    public TwoDirectionsMask() {
        super(MASK_SIZE, Type.OTHER);
    }

    @Override
    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel xChannel = applySingleMaskTo(originalChannel, generateXMask());
        ImageColorChannel yChannel = applySingleMaskTo(originalChannel, generateYMask());

        return joinMasks(xChannel, yChannel);
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

    private ImageColorChannel joinMasks(ImageColorChannel channel1, ImageColorChannel channel2) {
        ImageColorChannel newChannel = channel1.cloneChannel();

        for(int y = MASK_BORDER; y < newChannel.getHeight() - MASK_BORDER; y++) {
            for(int x = MASK_BORDER; x < newChannel.getWidth() - MASK_BORDER; x++) {
                int chanel1Color = channel1.getPixel(x, y);
                int chanel2Color = channel2.getPixel(x, y);

                int color = (int) Math.sqrt( chanel1Color * chanel1Color + chanel2Color * chanel2Color );
                newChannel.setPixel(x, y, color);
            }
        }

        return newChannel;
    }

    public abstract double[][] generateXMask();

    public abstract double[][] generateYMask();
}
