package ar.edu.itba.ati.model.Masks;

public class SobelMask extends TwoDirectionsMask {

   @Override
   public double[][] generateXMask() {
      double[][] mask = {
              {-1, -2, -1},
              {0, 0, 0},
              {1, 2, 1}
      };

      return mask;
   }

   @Override
   public double[][] generateYMask() {
      double[][] mask = {
              {-1, 0, 1},
              {-2, 0, 2},
              {-1, 0, 1}
      };

      return mask;
   }
}
