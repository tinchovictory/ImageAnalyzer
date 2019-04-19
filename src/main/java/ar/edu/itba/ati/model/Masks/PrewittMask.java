package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public class PrewittMask extends Mask {

    private static int MASK_SIZE = 3;
    private static int MASK_BORDER = (MASK_SIZE - 1) / 2;

    public PrewittMask() {
        super(MASK_SIZE, Type.OTHER);
    }

    @Override
    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel xChannel = applySingleMaskTo(originalChannel, generatePrewittXMask());
        ImageColorChannel yChannel = applySingleMaskTo(originalChannel, generatePrewittYMask());

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

    private double[][] generatePrewittXMask() {
        double [][] pounds = new double[MASK_SIZE][MASK_SIZE];
        int halfSize = MASK_SIZE / 2;

        for(int i = 0; i < MASK_SIZE; i++) {
            for(int j = 0; j < MASK_SIZE; j++) {
                pounds[i][j] = i - halfSize;
            }
        }

        return pounds;
    }

    private double[][] generatePrewittYMask() {
        double [][] pounds = new double[MASK_SIZE][MASK_SIZE];
        int halfSize = MASK_SIZE / 2;

        for(int i = 0; i < MASK_SIZE; i++) {
            for(int j = 0; j < MASK_SIZE; j++) {
                pounds[i][j] = j - halfSize;
            }
        }

        return pounds;
    }
}
