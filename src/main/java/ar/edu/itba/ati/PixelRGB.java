package ar.edu.itba.ati;

public class PixelRGB implements Pixel {

    //TODO check if pixels should be a byte or a double
    private final double red;
    private final double blue;
    private final double green;


    public PixelRGB(byte red, byte blue, byte green) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public double getR() {
        return red;
    }

    public double getB() {
        return blue;
    }

    public double getG() {
        return green;
    }
}
