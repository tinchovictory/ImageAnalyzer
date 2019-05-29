package ar.edu.itba.ati.model;

import ar.edu.itba.ati.ImageManager;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sift {

    private static Sift instance;

    private static final String IMAGE_1_FILE_NAME = "image1KeyPoints";
    private static final String IMAGE_2_FILE_NAME = "image2KeyPoints";
    private static final String IMAGE_MATCHING_FILE_NAME = "imageMatchingKeyPoints";
    private static final String EXTENSION = ".ppm";

    private static final Scalar KEYPOINT_COLOR = new Scalar(0, 0, 255); // Blue, Green, Red (proved). WTF?
    private static final Scalar MATCH_COLOR = new Scalar(255, 0, 255);
    private static final String OUTPUT_PATH = "tmp/";

    private final FeatureDetector featureDetector;
    private final DescriptorExtractor descriptorExtractor;
    private final DescriptorMatcher descriptorMatcher;

    private Sift() {
        nu.pattern.OpenCV.loadLibrary();
        this.featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
        this.descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        this.descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
    }

    public static Sift getInstance(){
        if(instance == null){
            instance = new Sift();
        }

        return instance;
    }

    public List<Image> apply(final Image myImage1, final Image myImage2,
                             final int matchingDistance, final double matchingPercentage) {

        String image1path = "tmp/image1.ppm";
        String image2path = "tmp/image2.ppm";

        try {
            ImageManager.saveImage(new File(image1path), myImage1);
            ImageManager.saveImage(new File(image2path), myImage2);
        } catch (IOException | ImageWriteException ex) {
            System.out.println("Could not save images");
            return null;
        }


        final Mat image1 = Highgui.imread(image1path, Highgui.CV_LOAD_IMAGE_COLOR);
        final Mat image2 = Highgui.imread(image2path, Highgui.CV_LOAD_IMAGE_COLOR);

        // For each image, calculate its keyPoints & their descriptors (i.e.: transform them into vectors).
        final MatOfKeyPoint keyPointsImage1 = detectKeyPoints(image1);
        final MatOfKeyPoint descriptorsImage1 = extractDescriptors(image1, keyPointsImage1);

        final MatOfKeyPoint keyPointsImage2 = detectKeyPoints(image2);
        final MatOfKeyPoint descriptorsImage2 = extractDescriptors(image2, keyPointsImage2);

        // Match descriptor of all images, using the second image descriptors as the one to train.
        final MatOfDMatch matches = new MatOfDMatch();
        descriptorMatcher.match(descriptorsImage1, descriptorsImage2, matches);

        // Collect only matches with distance lower to the given matching distance.
        final List<DMatch> goodMatchesList = Arrays.stream(matches.toArray())
                .filter(dMatch -> dMatch.distance < matchingDistance)
                .collect(Collectors.toList());

        // Give the result: "image found" or "image not found".
        final boolean imageFound = hasImageBeenFound(keyPointsImage1, keyPointsImage2, goodMatchesList,
                matchingPercentage);
        if (imageFound) {
            System.out.println("SIFT: Image found.");
        } else {
            System.out.println("SIFT: Image not found.");
        }

        // Save tmp images with the calculated keyPoints drawn over the images.
        final List<File> tmpImageFiles = new LinkedList<>();
        tmpImageFiles.add(saveKeyPointImage(image1, keyPointsImage1, IMAGE_1_FILE_NAME));
        tmpImageFiles.add(saveKeyPointImage(image2, keyPointsImage2, IMAGE_2_FILE_NAME));
        tmpImageFiles.add(saveMatchingKeyPointImage(image1, keyPointsImage1,
                image2, keyPointsImage2,
                goodMatchesList));


        return tmpImageFiles
                .stream()
                .map(file -> {
                    try {
                        return ImageManager.loadImage(file);
                    } catch (IOException | ImageReadException ex) {
                        System.out.println("Could not read image");
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    private MatOfKeyPoint detectKeyPoints(final Mat image) {
        final MatOfKeyPoint imageKeyPoints = new MatOfKeyPoint();
        featureDetector.detect(image, imageKeyPoints);
        return imageKeyPoints;
    }

    private MatOfKeyPoint extractDescriptors(final Mat image,
                                             final MatOfKeyPoint imageKeyPoints) {
        final MatOfKeyPoint keyPointsDescriptors = new MatOfKeyPoint();
        descriptorExtractor.compute(image, imageKeyPoints, keyPointsDescriptors);
        return keyPointsDescriptors;
    }

    /**
     * Give the result: "image found" or "image not found",
     * depending on the percentage of good matches.
     * Percentage is calculated using the minimum available key points set.
     */
    private boolean hasImageBeenFound(final MatOfKeyPoint keyPointsImage1,
                                      final MatOfKeyPoint keyPointsImage2,
                                      final List<DMatch> goodMatches,
                                      final double matchingPercentage) {
        final int numKeyPoints1 = getNumKeyPoints(keyPointsImage1);
        final int numKeyPoints2 = getNumKeyPoints(keyPointsImage2);
        final int minNumKeyPoints = Math.min(numKeyPoints1, numKeyPoints2);
        final int numGoodMatches = goodMatches.size();

        return numGoodMatches > minNumKeyPoints * matchingPercentage;
    }

    private int getNumKeyPoints(final MatOfKeyPoint imageKeyPoints) {
        return imageKeyPoints.rows() * imageKeyPoints.cols();
    }

    private File saveKeyPointImage(final Mat image, final MatOfKeyPoint imageKeyPoints, final String fileName) {
        final Mat outputImage = new Mat(image.rows(), image.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Features2d.drawKeypoints(image, imageKeyPoints, outputImage, KEYPOINT_COLOR, 0);
        final String outputFileName = OUTPUT_PATH + fileName + EXTENSION;
        System.out.println(outputFileName);
        Highgui.imwrite(outputFileName, outputImage);
        System.out.println(new File(outputFileName).getAbsolutePath());
        return new File(outputFileName);
    }

    private File saveMatchingKeyPointImage(final Mat image1, final MatOfKeyPoint keyPointsImage1,
                                           final Mat image2, final MatOfKeyPoint keyPointsImage2,
                                           final List<DMatch> goodMatchesList) {
        final Mat outputImage = new Mat(image1.rows() + image2.rows(),
                image1.cols() + image2.cols(),
                Highgui.CV_LOAD_IMAGE_COLOR);
        final MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(goodMatchesList);
        Features2d.drawMatches(image1, keyPointsImage1, image2, keyPointsImage2, goodMatches,
                outputImage, MATCH_COLOR,
                KEYPOINT_COLOR, new MatOfByte(), 2);
        final String outputFileName = OUTPUT_PATH + IMAGE_MATCHING_FILE_NAME + EXTENSION;
        Highgui.imwrite(outputFileName, outputImage);
        return new File(outputFileName);
    }

}

