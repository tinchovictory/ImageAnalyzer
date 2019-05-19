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
    private int[] objColorAvg;
    private int[] bgColorAvg;

    public TrackingArea(List<Point> objSelection, List<Point> bgSelection, Image image) {
        this.image = image;
        this.phi = new int[image.getWidth()][image.getHeight()];
        this.f = new int[image.getWidth()][image.getHeight()];

        this.objColorAvg = new int[3];
        this.bgColorAvg = new int[3];

        this.lIn = new HashSet<>();
        this.lOut = new HashSet<>();

        initPhi(objSelection, bgSelection);
        lInit();
    }

    private void initPhi(List<Point> objSelection, List<Point> bgSelection) {
        int count = 0;
        for(Point p : objSelection) {
            phi[p.x][p.y] = -3;
            objColorAvg[0] += image.getPixelColor(p.x, p.y).getRed();
            objColorAvg[1] += image.getPixelColor(p.x, p.y).getGreen();
            objColorAvg[2] += image.getPixelColor(p.x, p.y).getBlue();
            count++;
        }
        objColorAvg[0] /= count;
        objColorAvg[1] /= count;
        objColorAvg[2] /= count;

        count = 0;
        for(Point p : bgSelection) {
            bgColorAvg[0] += image.getPixelColor(p.x, p.y).getRed();
            bgColorAvg[1] += image.getPixelColor(p.x, p.y).getGreen();
            bgColorAvg[2] += image.getPixelColor(p.x, p.y).getBlue();
            count++;
        }
        bgColorAvg[0] /= count;
        bgColorAvg[1] /= count;
        bgColorAvg[2] /= count;

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(phi[x][y] != -3) {
                    phi[x][y] = 3;
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
        while(!finishIteration() && counter < 100) {
            for(Point p : lOut) {
                if(f(p) > 0) {
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

        double a = Math.sqrt(Math.pow(objColorAvg[0] - color.getRed(), 2) + Math.pow(objColorAvg[1] - color.getGreen(), 2) + Math.pow(objColorAvg[2] - color.getBlue(), 2));
        double b = Math.sqrt(Math.pow(bgColorAvg[0] - color.getRed(), 2) + Math.pow(bgColorAvg[1] - color.getGreen(), 2) + Math.pow(bgColorAvg[2] - color.getBlue(), 2));

        return Math.log(b / a);
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
        return list;
    }
}
