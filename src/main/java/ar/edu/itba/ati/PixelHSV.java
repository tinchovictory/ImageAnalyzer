package ar.edu.itba.ati;

public class PixelHSV implements Pixel {


    // TODO check if pixels should be double or byte
    private final double hue;
    private final double sat;
    private final double value;


    public PixelHSV(double hue, double sat, double value) {
        this.hue = hue;
        this.sat = sat;
        this.value = value;
    }

    public double getH() {
        return hue;
    }

    public double getS() {
        return sat;
    }

    public double getV() {
        return value;
    }
}
