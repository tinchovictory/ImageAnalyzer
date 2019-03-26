package ar.edu.itba.ati.GUI;

import javafx.scene.chart.*;

import java.util.Arrays;
import java.util.Random;

public class Histogram extends BarChart {


    int DATA_SIZE = 1000;
    int data[] = new int[DATA_SIZE];
    int group[] = new int[10];


    public Histogram(double[] values){
        this(values,1);
    }
    public Histogram(double[] values,int intervalSize) {

        super(new CategoryAxis(), new NumberAxis());

        this.getXAxis().setLabel("Color");
        this.getYAxis().setLabel("Amount");

        XYChart.Series series = new XYChart.Series();

//        series.setName("Histogram");


        for(int i=0; i< values.length;i++){
            double accumulated =0;
            int counter =0;
            while(counter++ < intervalSize && i < values.length){
                accumulated += values[i++];
            }
            series.getData().add(new XYChart.Data<String,Double>("",accumulated));
        }

        this.getData().add(series);


    }


    private void prepareData() {

        Random random = new Random();
        for (int i = 0; i < DATA_SIZE; i++) {
            data[i] = random.nextInt(100);
        }
    }

    //count data population in groups
    private void groupData() {
        for (int i = 0; i < 10; i++) {
            group[i] = 0;
        }
        for (int i = 0; i < DATA_SIZE; i++) {
            if (data[i] <= 10) {
                group[0]++;
            } else if (data[i] <= 20) {
                group[1]++;
            } else if (data[i] <= 30) {
                group[2]++;
            } else if (data[i] <= 40) {
                group[3]++;
            } else if (data[i] <= 50) {
                group[4]++;
            } else if (data[i] <= 60) {
                group[5]++;
            } else if (data[i] <= 70) {
                group[6]++;
            } else if (data[i] <= 80) {
                group[7]++;
            } else if (data[i] <= 90) {
                group[8]++;
            } else if (data[i] <= 100) {
                group[9]++;
            }
        }
    }
}
