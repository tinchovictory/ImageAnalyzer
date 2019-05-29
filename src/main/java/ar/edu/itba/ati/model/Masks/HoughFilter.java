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

public class HoughFilter {

    private double epsilon = 0.4;
    private int roBound;

    private final static int degreeBound= 90;
    private int[][] A;

    public HoughFilter() {
    }

    public ImageColorChannel apply(ImageColorChannel image, int numberOfLines){
        int D = Math.max(image.getHeight(),image.getWidth());

        roBound =(int) (Math.sqrt(2)*D);
        A = new int[2*roBound][2*degreeBound];

        accumulate(image);

    int counter =0;
        HashMap<Point, Integer> lines = new HashMap<>();
        for(int ro = -roBound, roInd=0; ro < roBound; ro++,roInd++) {
            for(int theta = -degreeBound, thetaInd  =0 ; theta < degreeBound; theta++,thetaInd++) {
                if(A[roInd][thetaInd]>0){
                    lines.put(new Point(ro ,theta), A[roInd][thetaInd]);
                }
                if(A[roInd][thetaInd] !=0){
                    System.out.println(A[roInd][thetaInd]);
                    counter ++;
                }
            }
        }

        System.out.println("Lines "+counter);

        List<Point> sortedPoints = lines
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(numberOfLines)
                .collect(toList());
        System.out.println(lines.size());
        //Drawing lines
        ImageColorChannel newChannel = image.cloneChannel();
        for(Point p: sortedPoints){
            double ro = p.x;
            double thetaRad = Math.toRadians(p.y);

            for(int i=0; i<image.getHeight();i++){
                for(int j=0; j<image.getWidth();j++){

                    if(abs(ro - i * cos(thetaRad) - j * sin(thetaRad) ) < epsilon){
                        newChannel.setPixel(i,j,120);
                    }

                }
            }
        }

    return newChannel;


    }


    private void accumulate(ImageColorChannel image){

        for (int x=0; x< image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){


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
