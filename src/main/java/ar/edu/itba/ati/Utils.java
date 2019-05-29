package ar.edu.itba.ati;

import net.sf.doodleproject.numerics4j.random.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Utils {

    private static RNG rng = new RandomRNG(System.currentTimeMillis());


    public static double randomExponentialNumber(double lambda) {
//        double x = Math.random();
//        return lambda * Math.exp(- lambda * x);
//        return - (1 / lambda) * Math.log(x);

        return ExponentialRandomVariable.nextRandomVariable(lambda, rng);
    }

    public static double randomRayeighNumber(double epsilon) {
//        double x = Math.random();
//       return ( x / (epsilon * epsilon) ) * Math.exp( - x * x / ( 2 * epsilon *  epsilon ) );
//        return epsilon * Math.sqrt( -2 * Math.log(1 - x) );

        return RayleighRandomVariable.nextRandomVariable(epsilon, rng);
    }

    public static double randomGaussNumber(double phi, double mu) {
//        double x = Math.random();

//        double exp = Math.exp( - Math.pow(x - mu, 2) / (2 * phi * phi) );

//        return 1 / Math.sqrt( 2 * Math.PI * phi ) * exp;

        return NormalRandomVariable.nextRandomVariable(mu, phi, rng);
    }

    public static int toRange(int value, int minValue, int maxValue) {
/*        BigDecimal minValueBig = BigDecimal.valueOf(minValue);
        BigDecimal maxValueBig = BigDecimal.valueOf(maxValue);

        BigDecimal m = BigDecimal.valueOf(255.0).divide( maxValueBig.subtract(minValueBig) );
        BigDecimal b = m.multiply(minValueBig);*/

        double m = 255.0 / ((long) maxValue - (long) minValue);
        double b = - m * minValue;

     //   System.out.println("m: " + m + " b: " + b + " value: " + value + " ans: " + (int) (m * value + b));

//        long minMax = (long)maxValue - (long)minValue;
//        System.out.println("max min " + minMax);

//        return 0;
        return (int) (m * value + b);

       // return m.multiply(BigDecimal.valueOf(value)).subtract(b).intValue();
    }


    public static double getLaplaceGaussValue(int x, int y, double deviation) {
        double a = 1 / ( Math.sqrt(2 * Math.PI) * Math.pow(deviation, 3) );
        double b = 2 - ( x * x + y * y) / ( deviation * deviation );
        double c = Math.exp( - ( x * x + y * y) / ( 2 * deviation * deviation) );

        return - a * b * c;
    }

    public static double getGaussianKernel(int x, int y, double t) {
        double exp = - ( x * x + y * y ) / (4 * t);
        return 1 / ( 4 * Math.PI * t ) * Math.exp(exp);
    }

    public static double leclercBorders(int x, double deviation) {
        return Math.exp(- x * x / (deviation * deviation));
    }

    public static double lorentzianoBorders(int x, double deviation) {
        return 1 / ( (x * x) / (deviation * deviation) + 1);
    }


    public enum AnisotroplicDiffusionType {
        LECLERC, LORENTZIANO;
    }
}
