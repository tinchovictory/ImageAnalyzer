package ar.edu.itba.ati.model.Masks;

public class FiveAMask extends TwoDirectionsMask {

   @Override
   public double[][] generateXMask() {
      double[][] mask = {
              {1, 1, 1},
              {1, -2 ,1},
              {-1, -1, -1}
      };

      return mask;
   }

   @Override
   public double[][] generateYMask() {
      double[][] mask = {
              {-1, 1, 1},
              {-1, -2, 1},
              {-1, 1, 1}
      };

      return mask;
   }
}
