package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.Utils;

public class LaplaceGaussCrossingZero extends ZeroCrossingMask {

    private double deviation;

    public LaplaceGaussCrossingZero(double deviation) {
        super(7);
        this.deviation = deviation;
    }

    @Override
    public double[][] getPoundedMask() {
        double[][] mask = new double[getMaskSize()][getMaskSize()];

        int borderLength = mask.length / 2;

        for(int x = -borderLength, i = 0; x <= borderLength; x++, i++) {
            for(int y = -borderLength, j = 0; y <= borderLength; y++, j++) {
                mask[i][j] = Utils.getLaplaceGaussValue(x, y, deviation);
            }
        }

        return mask;
    }

}
