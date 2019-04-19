package ar.edu.itba.ati.model.Masks;

public class KirshMask extends TwoDirectionsMask {

   @Override
   public double[][] generateXMask() {
      double[][] mask = {
              {5, 5, 5},
              {-3, 0 ,-3},
              {-3, -3, -3}
      };

      return mask;
   }

   @Override
   public double[][] generateYMask() {
      double[][] mask = {
              {-3, -3, 5},
              {-3, 0, 5},
              {-3, -3, 5}
      };

      return mask;
   }
}
