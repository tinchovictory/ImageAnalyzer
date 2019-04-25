package ar.edu.itba.ati.model.Masks;

public class LaplaceCrossingZero extends ZeroCrossingMask {
    public LaplaceCrossingZero(int threshold) {
        super(3, threshold);
    }

    @Override
    public double[][] getPoundedMask() {
        double[][] mask = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };

        return mask;
    }
}
