package ar.edu.itba.ati.model.Masks;

import static ar.edu.itba.ati.Utils.getLaplaceGaussValue;

public class LaplaceGaussCrossingZero extends ZeroCrossingMask {

    private double deviation;

    public LaplaceGaussCrossingZero(double deviation) {
        super(7);
        this.deviation = deviation;
    }

    @Override
    public double[][] getPoundedMask() {
        double[][] mask = new double[getMaskSize()][getMaskSize()];

        for(int x = 0; x < mask.length; x++) {
            for(int y = 0; y < mask[0].length; y++) {
                mask[x][y] = getLaplaceGaussValue(x, y, deviation);
            }
        }

        return mask;
    }

}
