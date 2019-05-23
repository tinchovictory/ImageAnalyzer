package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.GUI.SelectableAreaFactory;
import ar.edu.itba.ati.Interface.Controller;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class TrackingWindow extends VBox {

    Controller controller;

    @FXML
    Button selectObject;

    @FXML
    Button confirmObject;

    @FXML
    Button selectBackground;

    @FXML
    Button confirmBackground;

    @FXML
    Button startTracking;

    @FXML
    Button nextFrame;


    SelectableAreaFactory selectableAreaFactory;

    List<Point> pointsObject;

    List<Point> pointsBackgorund;

    private final BlockingQueue<Runnable> updateQueue;
    private final AnimationTimer updateTimer;

    private TrackingWindow(Controller controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TrackingWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Error opening TrackingWindow.fmxl");
            e.printStackTrace();
        }
        this.controller = controller;
        this.selectableAreaFactory = new SelectableAreaFactory(controller.getMainWindow());


        this.updateQueue = new ArrayBlockingQueue<>(1);
        this.updateTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Runnable runnable = updateQueue.poll();
                if(runnable != null) {
                    runnable.run();
                }
            }
        };

    }

    public void init() {
        selectObject.setOnAction(e -> selectableAreaFactory.startSelection());

        confirmObject.setOnAction(e -> {
            this.pointsObject = selectableAreaFactory.getSelection();
            selectableAreaFactory.stopSelection();
        });

        selectBackground.setOnAction(e -> selectableAreaFactory.startSelection());

        confirmBackground.setOnAction(e -> {
            this.pointsBackgorund = selectableAreaFactory.getSelection();
            selectableAreaFactory.stopSelection();
        });

        startTracking.setOnAction(e -> {

            controller.setTrackArea(pointsObject, pointsBackgorund);
            controller.getMainWindow().refreshImage();
        });

        nextFrame.setOnAction(e -> {

            new Thread(() -> {
                updateTimer.start();

                long counter = 0;

                int frame = 1;
                while(frame < controller.getVideoFramesAmount()) {
                    long startTime = System.nanoTime();

                    controller.trackAreaInNextFrame();

                    counter += System.nanoTime() - startTime;

                    try {
                        updateQueue.put(() -> controller.getMainWindow().refreshImage());
                    } catch (Exception ex) {
                        System.out.println("execption trying to refresh image");
                    }

                    frame++;
                }

                updateTimer.stop();

                counter /= (controller.getVideoFramesAmount() - 1);
                System.out.println("Tracking took " + counter / 1000000 + " milliseconds");
            }).start();

        });

        if(!controller.isVideo()){
            nextFrame.setVisible(false);
        }

    }


    public static TrackingWindow openInNewWindow(Controller controller) {
        Stage newStage = new Stage();
        TrackingWindow window = new TrackingWindow(controller);
        newStage.setScene(new Scene(window));
        window.init();
        newStage.show();
        return window;

    }


}
