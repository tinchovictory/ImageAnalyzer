package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.Utils;

public class LoGMask extends SimpleMask {
    private double deviation;

    public LoGMask(double deviation) {
        super(3);
        this.deviation = deviation;
    }

    @Override
    public double[][] getPoundedMask() {
        double[][] mask = new double[getMaskSize()][getMaskSize()];

        for(int x = 0; x < mask.length; x++) {
            for(int y = 0; y < mask[0].length; y++) {
                mask[x][y] = Utils.getLaplaceGaussValue(x, y, deviation);
            }
        }

        return mask;
    }
}
