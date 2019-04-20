package ar.edu.itba.ati.model.Masks;

public class LaplaceGaussCrossingZero extends ZeroCrossingMask {

    private double deviation;

    public LaplaceGaussCrossingZero(double deviation) {
        super();
        this.deviation = deviation;
    }

    @Override
    public double[][] getPoundedMask() {
        double[][] mask = new double[getMaskSize()][getMaskSize()];

        for(int x = 0; x < mask.length; x++) {
            for(int y = 0; y < mask[0].length; y++) {
                mask[x][y] = getLaplaceGaussValue(x, y);
            }
        }

        return mask;
    }

    private double getLaplaceGaussValue(int x, int y) {
        double a = 1 / ( Math.sqrt(2 * Math.PI) * Math.pow(deviation, 3) );
        double b = 2 - ( x * x + y * y) / ( deviation * deviation );
        double c = Math.exp( - ( x * x + y * y) / ( 2 * deviation * deviation) );

        return a * b * c;
    }
}
