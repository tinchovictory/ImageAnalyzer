package ar.edu.itba.ati;

import net.sf.doodleproject.numerics4j.random.*;

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
}
