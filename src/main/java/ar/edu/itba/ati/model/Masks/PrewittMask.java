package ar.edu.itba.ati.model.Masks;

public class PrewittMask extends TwoDirectionsMask {

   @Override
   public double[][] generateXMask() {
      double [][] pounds = new double[getMaskSize()][getMaskSize()];
      int halfSize = getMaskSize() / 2;

      for(int i = 0; i < getMaskSize(); i++) {
         for(int j = 0; j < getMaskSize(); j++) {
            pounds[i][j] = i - halfSize;
         }
      }

      return pounds;
   }

   @Override
   public double[][] generateYMask() {
      double [][] pounds = new double[getMaskSize()][getMaskSize()];
      int halfSize = getMaskSize() / 2;

      for(int i = 0; i < getMaskSize(); i++) {
         for(int j = 0; j < getMaskSize(); j++) {
            pounds[i][j] = j - halfSize;
         }
      }

      return pounds;
   }
}
