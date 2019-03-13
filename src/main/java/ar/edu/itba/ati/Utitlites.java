package ar.edu.itba.ati;

public class Utitlites {



    //Code taken from slide 29 of ppt "Color"
    public static PixelHSV RGBtoHSV(PixelRGB p){
        double max = Math.max(p.getR(),Math.max(p.getB(),p.getG()));
        double min = Math.min(p.getR(),Math.min(p.getB(),p.getG()));

        double value =  max;

        double delta = max - min;

        double s = (max != 0) ? (delta/max) : 0.0;
        double hue;
        if(s == 0){
            hue = Double.MAX_VALUE;//TODO check UNDEFINED ?????????
        }
        else {
            if(p.getR() == max){
                hue = (p.getG() - p.getB()) / delta;
            }
            else if (p.getG() == max){
                hue = 2.0 + (p.getB()-p.getR())/delta;
            }
            else /*if (p.getB() == max)*/{ // as max is either blue red or green ,if it falls in this case max == p.getB is true
                hue = 4.0 + (p.getR() - p.getG())/delta;
            }
            hue = hue *60;

        }
        return new PixelHSV(hue,s,value);
    }
}
