package ar.edu.itba.ati.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrackingArea {

    private static int[][] Directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    private int[][] phi;
    private int[][] f;
    private Image image;
    private Set<Point> lIn;
    private Set<Point> lOut;
    private int[] objColorAccum;
    private int objPixelsAmount;
    private int[] bgColorAccum;
    private int bgPixelsAmount;

    public TrackingArea(List<Point> initialSelection,  Image image) {
        this.image = image;
        this.phi = new int[image.getWidth()][image.getHeight()];
        this.f = new int[image.getWidth()][image.getHeight()];
        this.objColorAccum = new int[3];
        this.bgColorAccum = new int[3];
        this.objPixelsAmount = 0;
        this.bgPixelsAmount = 0;
        this.lIn = new HashSet<>();
        this.lOut = new HashSet<>();

        initPhi(initialSelection);
        lInit();
    }

    private void initPhi(List<Point> initialSelection) {
        for(Point p : initialSelection) {
            phi[p.x][p.y] = -3;
            objColorAccum[0] += image.getPixelColor(p.x, p.y).getRed();
            objColorAccum[1] += image.getPixelColor(p.x, p.y).getGreen();
            objColorAccum[2] += image.getPixelColor(p.x, p.y).getBlue();
            objPixelsAmount++;
        }

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(phi[x][y] != -3) {
                    phi[x][y] = 3;
                    bgColorAccum[0] += image.getPixelColor(x, y).getRed();
                    bgColorAccum[1] += image.getPixelColor(x, y).getGreen();
                    bgColorAccum[2] += image.getPixelColor(x, y).getBlue();
                    bgPixelsAmount++;
                }
            }
        }
    }

    private void lInit() {
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(phi[x][y] == -3) {
                    for(int[] dir : Directions) {
                        int newX = x + dir[0];
                        int newY = y + dir[1];

                        if(!isOutOfBounds(newX, newY) && phi[newX][newY] == 3) {
                            phi[x][y] = -1;
                            lIn.add(new Point(x, y));
                        }
                    }
                }
            }
        }
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(phi[x][y] == 3) {
                    for(int[] dir : Directions) {
                        int newX = x + dir[0];
                        int newY = y + dir[1];

                        if(!isOutOfBounds(newX, newY) && phi[newX][newY] == -1) {
                            phi[x][y] = 1;
                            lOut.add(new Point(x, y));
                        }
                    }
                }
            }
        }
    }

    private boolean isOutOfBounds(int x, int y) {
        if(x < 0 || y < 0) {
            return true;
        }
        if(x >= image.getWidth() || y >= image.getHeight()) {
            return true;
        }
        return false;
    }

    public Image findBorder() {
        List<Point> movePoints = new ArrayList<>();

        int counter = 0;
        while(!finishIteration() && counter < 1000000) {
            for(Point p : lOut) {
                if(f(p) > 0) {
//                    System.out.println("Switch in");
                    movePoints.add(p);
                }
            }

            for(Point p : movePoints) {
                switchPointToLIn(p);
            }

            checkLin();
            movePoints.clear();

            for(Point p : lIn) {
                if(f(p) < 0) {
//                    System.out.println("Switch out");
                    movePoints.add(p);
                }
            }
            for(Point p : movePoints) {
                switchPointToLOut(p);
            }
            checkLout();
            movePoints.clear();

            counter++;
        }

        // Paint outer border line to show selection
        Image newImage = image.cloneImage();
        for(Point p : lOut) {
            newImage.setPixelColor(p.x, p.y, Color.GREEN);
        }
        return newImage;
    }

    private void switchPointToLIn(Point p) {
        lOut.remove(p);
        lIn.add(p);
        phi[p.x][p.y] = -1;

        Color color = image.getPixelColor(p.x, p.y);
        objColorAccum[0] += color.getRed();
        objColorAccum[1] += color.getGreen();
        objColorAccum[2] += color.getBlue();
        objPixelsAmount++;
        bgColorAccum[0] -= color.getRed();
        bgColorAccum[1] -= color.getGreen();
        bgColorAccum[2] -= color.getBlue();
        bgPixelsAmount--;


        for(int[] dir : Directions) {
            int newX = p.x + dir[0];
            int newY = p.y + dir[1];

            if(!isOutOfBounds(newX, newY) && phi[newX][newY] == 3) {
                phi[newX][newY] = 1;
                lOut.add(new Point(newX, newY));
            }
        }
    }

    private void checkLin() {
        List<Point> objPoints = new ArrayList<>();

        for(Point p : lIn) {
            int count = 0;
            for(int[] dir : Directions) {
                int newX = p.x + dir[0];
                int newY = p.y + dir[1];

                if(!isOutOfBounds(newX, newY) && phi[newX][newY] < 0) {
                    count++;
                }
            }
            if(count == 4) {
                objPoints.add(p);
            }
        }

        for(Point p : objPoints) {
            lIn.remove(p);
            phi[p.x][p.y] = -3;
        }
    }

    private void switchPointToLOut(Point p) {
        lIn.remove(p);
        lOut.add(p);
        phi[p.x][p.y] = 1;

        Color color = image.getPixelColor(p.x, p.y);
        objColorAccum[0] -= color.getRed();
        objColorAccum[1] -= color.getGreen();
        objColorAccum[2] -= color.getBlue();
        objPixelsAmount--;
        bgColorAccum[0] += color.getRed();
        bgColorAccum[1] += color.getGreen();
        bgColorAccum[2] += color.getBlue();
        bgPixelsAmount++;


        for(int[] dir : Directions) {
            int newX = p.x + dir[0];
            int newY = p.y + dir[1];

            if(!isOutOfBounds(newX, newY) && phi[newX][newY] == -3) {
                phi[newX][newY] = -1;
                lIn.add(new Point(newX, newY));
            }
        }
    }

    private void checkLout() {
        List<Point> bgPoints = new ArrayList<>();

        for(Point p : lOut) {
            int count = 0;
            for(int[] dir : Directions) {
                int newX = p.x + dir[0];
                int newY = p.y + dir[1];

                if(!isOutOfBounds(newX, newY) && phi[newX][newY] > 0) {
                    count++;
                }
            }
            if(count == 4) {
                bgPoints.add(p);
            }
        }

        for(Point p : bgPoints) {
            lOut.remove(p);
            phi[p.x][p.y] = 3;
        }
    }

    private double f(Point p) {
        Color color = image.getPixelColor(p.x, p.y);
        double objAvgRed = (double) objColorAccum[0] / objPixelsAmount;
        double objAvgGreen = (double) objColorAccum[1] / objPixelsAmount;
        double objAvgBlue = (double) objColorAccum[2] / objPixelsAmount;

        double bgAvgRed = (double) bgColorAccum[0] / bgPixelsAmount;
        double bgAvgGreen = (double) bgColorAccum[1] / bgPixelsAmount;
        double bgAvgBlue = (double) bgColorAccum[2] / bgPixelsAmount;

        double a = Math.sqrt(Math.pow(objAvgRed - color.getRed(), 2) + Math.pow(objAvgGreen - color.getGreen(), 2) + Math.pow(objAvgBlue - color.getBlue(), 2));
        double b = Math.sqrt(Math.pow(bgAvgRed - color.getRed(), 2) + Math.pow(bgAvgGreen - color.getGreen(), 2) + Math.pow(bgAvgBlue - color.getBlue(), 2));

        return Math.log(a / b);
    }

    private boolean finishIteration() {
        for(Point p : lOut) {
            if(f(p) > 0) {
                return false;
            }
        }
        for(Point p : lIn) {
            if(f(p) < 0) {
                return false;
            }
        }
        return true;
    }


    public static List<Point> generateSelection(Point p1, Point p2) {
        List<Point> list = new ArrayList<>();

        for(int i = p1.x ; i < p2.x; i++) {
            for(int j = p1.y; j < p2.y; j++) {
                list.add(new Point(i, j));
            }
        }
/*
        for(int i = p1.y; i < p2.y; i++) {
            list.add(new Point(p1.x, i));
            list.add(new Point(p2.x, i));
        }
        for(int i = p1.x; i < p2.x; i++) {
            list.add(new Point(i, p1.y));
            list.add(new Point(i, p2.y));
        }
*/
        return list;
    }
}
