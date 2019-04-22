package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;
import ar.edu.itba.ati.model.Mask;

public class BilateralFilter extends Mask {

    double spaceSigma;
    double colorSigma;

    public BilateralFilter(int size,double spaceSigma, double colorSigma) {
        super(size, Type.OTHER);
        this.spaceSigma = spaceSigma;
        this.colorSigma = colorSigma;
    }

    public ImageColorChannel applyTo(ImageColorChannel originalChannel) {
        ImageColorChannel xChannel = applySingleMaskTo(originalChannel);

        return xChannel;
    }

    private ImageColorChannel applySingleMaskTo(ImageColorChannel originalChannel) {
        ImageColorChannel newChannel = originalChannel.cloneChannel();

        for(int y = getBorderLength(); y < originalChannel.getHeight() - getBorderLength(); y++) {
            for(int x = getBorderLength(); x < originalChannel.getWidth() - getBorderLength(); x++) {
                applyMaskToPixel(x, y, newChannel, originalChannel);
            }
        }

        return newChannel;
    }

    private void applyMaskToPixel(int xCenter, int yCenter, ImageColorChannel newChannel, ImageColorChannel originalChannel){
        double upper = 0;
        double lower = 0;

        for(int y = 0; y < this.getMaskSize(); y++) {
            for(int x = 0; x < this.getMaskSize(); x++) {
                int xPos = xCenter + ( x - this.getBorderLength() );
                int yPos = yCenter + ( y - this.getBorderLength() );

                double wResult = wFunction(xCenter, yCenter,xPos,yPos,spaceSigma,colorSigma,originalChannel);
                upper += originalChannel.getPixel(xPos, yPos) * wResult;
                lower += wResult;
            }
        }

        newChannel.setPixel(xCenter, yCenter, (int) (upper/lower));
    }

    private double wFunction(int i, int j, int k, int l, double spaceSigma, double colorSigma, ImageColorChannel original){
        double colorIJ = original.getPixel(i, j);
        double colorKL = original.getPixel(k,l);
        double exponent= - ( (i-k) * (i-k) + (j-l) * (j-l) ) / (2 * spaceSigma * spaceSigma) - (colorIJ - colorKL) * (colorIJ -  colorKL) / (2 * colorSigma * colorSigma);
        return Math.exp(exponent);
    }
}
