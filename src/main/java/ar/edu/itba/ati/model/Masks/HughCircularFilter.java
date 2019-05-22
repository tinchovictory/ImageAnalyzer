package ar.edu.itba.ati.model.Masks;

import ar.edu.itba.ati.model.ImageColorChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;


public class HughCircularFilter {

    private final static double epsilon = 0.1;
    private int[][][] A;

    public HughCircularFilter() {
    }

    public ImageColorChannel apply(ImageColorChannel image) {
        A = new int[image.getWidth()][image.getHeight()][min(image.getHeight(), image.getWidth()) / 2];

        accumulate(image);


        HashMap<Point, Integer> lines = new HashMap<>();
        int max=0;
        for(int a = 0; a < A.length; a++) {
            for(int b = 0; b < A[0].length; b++) {
                for(int r = 0; r < A[0][0].length; r++) {
                    if(A[a][b][r]>max){
                        max= A[a][b][r];
                    }
                    if(A[a][b][r] > 0)
                        lines.put(new Point(a,b,r), A[a][b][r]);
                }
            }
        }

        System.out.println(max);

        List<Point> points = lines
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(1)
                .collect(toList());

        //System.out.println(lines);

        for(Point p : points){
            System.out.println(p +" "+lines.get(p));
        }

        ImageColorChannel newChannel = new ImageColorChannel(image.getWidth(),image.getHeight());
        for(Point p: points) {
            System.out.println(p);
            for(int x = 0; x < image.getWidth(); x++){
                for(int y = 0; y < image.getHeight(); y++){

                    if (isInCircle(p,x,y)) {
                        newChannel.setPixel(x,y,255);
                    }
                }
            }
        }
        return newChannel;
    }


    private void accumulate(ImageColorChannel image) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                if (image.getPixel(x, y) == 255) {
                    for (int a = 0; a < A.length; a++) {
                        for (int b = 0; b < A[0].length; b++) {
                            for (int r = 0; r < A[0][0].length; r++) {
                                if (isInCircle(new Point(a, b, r), x, y)) {
                                    A[a][b][r]++;
                                }
                            }
                        }
                    }
                }


            }
        }
    }

    private boolean isInCircle(Point p,int x, int y){
        return abs((p.r*p.r) - (x - p.a) * (x - p.a) - (y - p.b) * (y - p.b)) < epsilon;
    }

    private class Point {
        int a, b, r;
        Point(int a, int b, int r) {
            this.a = a;
            this.b = b;
            this.r = r;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "a=" + a +
                    ", b=" + b +
                    ", r=" + r +
                    '}';
        }
    }

}