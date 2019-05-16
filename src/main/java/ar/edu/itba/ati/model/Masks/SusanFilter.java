package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.Image;
import ar.edu.itba.ati.model.ImageColorChannel;

public class SusanFilter {


    /**
     *      1 1 1
     *    1 1 1 1 1
     *  1 1 1 1 1 1 1
     *  1 1 1 1 1 1 1
     *  1 1 1 1 1 1 1
     *    1 1 1 1 1
     *      1 1 1
     *
     *  Circular mask
     *
     **/

    private final static int[][] mask = {
            {0,0,1,1,1,0,0},
            {0,1,1,1,1,1,0},
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},
            {0,1,1,1,1,1,0},
            {0,0,1,1,1,0,0},
    };

    private final static int MASK_PIXELS = 37;

    private final static int MASK_SIZE= 7;

    public static ImageColorChannel apply(ImageColorChannel image){
        ImageColorChannel channel = image.cloneChannel();

        for(int i = 7; i< channel.getHeight()-7; i++){
            for(int j = 7; j<channel.getWidth()-7; j++){
                int n = countLevelGray(image,i,j);
                double s = 1 - (double)n/MASK_PIXELS;
                if(s>0.33 && s<0.66){
                    channel.setPixel(i,j,255);
                }else{
                    channel.setPixel(i,j,0);
                }
            }
        }


        return channel;
    }


    public static ImageColorChannel applyCorner(ImageColorChannel image){
        ImageColorChannel channel = image.cloneChannel();

        for(int i = 7; i< channel.getHeight()-7; i++){
            for(int j = 7; j<channel.getWidth()-7; j++){
                int n = countLevelGray(image,i,j);
                double s = 1 - (double)n/MASK_PIXELS;
                if(s>0.5){
                    channel.setPixel(i,j,255);
                }else{
                    channel.setPixel(i,j,0);
                }
            }
        }


        return channel;
    }


    private static int countLevelGray(ImageColorChannel image, int i , int j){
        int center = image.getPixel(i,j);
        int count = 0;
        for(int x=0; x<MASK_SIZE ; x++){
            for(int y=0; y< MASK_SIZE; y++){
                int otherPixel =image.getPixel(i-3+x,j-3+y);
                if(mask[x][y]==1 && hasSameGrayLevel(otherPixel,center)){
                    count++;
                }
            }
        }

    return  count;
    }

    private static boolean hasSameGrayLevel(int color, int center){
        return Math.abs(color-center) < 27;
    }

    public static String get2DArrayPrint(int[][] matrix) {
        String output = new String();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                output = output + (matrix[i][j] + "\t");
            }
            output = output + "\n";
        }
        return output;
    }
}
