package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.abs;

public class HughFilter {

    private double epsilon = 0.001;
    private int roBound;

    private final static int degreeBound= 90;
    private int[][] A;

    public HughFilter() {
    }

    public ImageColorChannel apply(ImageColorChannel image, int numberOfLines){
        int D = Math.max(image.getHeight(),image.getWidth());

        roBound =(int) (Math.sqrt(2)*D);
        A = new int[2*roBound][2*degreeBound];

        accumulate(image);


        HashMap<Point, Integer> lines = new HashMap<>();
        for(int ro = -roBound, roInd=0; ro < roBound; ro++,roInd++) {
            for(int theta = -degreeBound, thetaInd  =0 ; theta < degreeBound; theta++,thetaInd++) {
                if(A[roInd][thetaInd]>0){
                    lines.put(new Point(ro ,theta), A[roInd][thetaInd]);
                }
            }
        }


        List<Point> sortedPoints = lines
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(numberOfLines)
                .collect(toList());

        //Drawing lines
        ImageColorChannel newChannel = new ImageColorChannel(image.getWidth(),image.getHeight());
        for(Point p: sortedPoints){
            double ro = p.x;
            double thetaRad = Math.toRadians(p.y);

            for(int i=0; i<image.getHeight();i++){
                for(int j=0; j<image.getWidth();j++){

                    if(abs(ro - i * cos(thetaRad) - j * sin(thetaRad) ) < epsilon){
                        newChannel.setPixel(i,j,255);
                    }

                }
            }
        }

    return newChannel;


    }


    private void accumulate(ImageColorChannel image){

        for (int x=0; x< image.getHeight(); x++){
            for(int y=0; y<image.getWidth(); y++){


                if(image.getPixel(x,y)==255){
                    for (int theta=-degreeBound, j=0; theta<degreeBound; theta++,j++){
                        for(int ro=-roBound, i=0; ro<roBound;ro++, i++){
                            double thetaRad = Math.toRadians(theta);
                            if(abs( ro - x * cos(thetaRad) - y * sin(thetaRad) ) < epsilon){
                                A[i][j]++;
                            }
                        }
                    }
                }



            }
        }

    }

}
