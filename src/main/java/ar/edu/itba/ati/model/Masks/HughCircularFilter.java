package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.lang.Math.sin;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;


public class HughCircularFilter {

    private final static double epsilon = 0.001;
    private int roBound;

    private final static int degreeBound = 90;
    private int[][][] A;

    public HughCircularFilter() {
    }

    public ImageColorChannel apply(ImageColorChannel image) {
        A = new int[image.getHeight()][image.getWidth()][min(image.getHeight(),image.getWidth())/2];


        return null;
    }


    private void accumulate(ImageColorChannel image){

        for (int x=0; x< image.getHeight(); x++){
            for(int y=0; y<image.getWidth(); y++){


                if(image.getPixel(x,y)==255){
                    for (int a=-degreeBound, j=0; a<degreeBound; a++,j++){
                        for(int ro=-roBound, i=0; ro<roBound;ro++, i++){
                            double thetaRad = Math.toRadians(a);
                            if(abs( ro - x * cos(thetaRad) - y * sin(thetaRad) ) < epsilon){
                                //A[i][j]++;
                            }
                        }
                    }
                }



            }
        }

    }

}