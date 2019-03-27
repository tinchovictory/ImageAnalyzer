package ar.edu.itba.ati;

public class Utils {
    public static double randomExponentialNumber(double lambda) {
        double x = Math.random();
//        return lambda * Math.exp(- lambda * x);
        return - (1 / lambda) * Math.log(x);
    }

    public static double randomRayeighNumber(double epsilon) {
        double x = Math.random();
//       return ( x / (epsilon * epsilon) ) * Math.exp( - x * x / ( 2 * epsilon *  epsilon ) );
        return epsilon * Math.sqrt( -2 * Math.log(1 - x) );
    }

    public static double randomGaussNumber(double phi, double mu) {
        double x = Math.random();

        double exp = Math.exp( - Math.pow(x - mu, 2) / (2 * phi * phi) );

        return 1 / Math.sqrt( 2 * Math.PI * phi ) * exp;
    }
}
