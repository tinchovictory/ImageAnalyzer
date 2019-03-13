package ar.edu.itba.ati;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PgmParser {

    public static void read(File image) {
        try {
            Scanner scanner = new Scanner(image, StandardCharsets.UTF_8);

            // process the top 4 header lines
            String filetype = scanner.nextLine();

            if (!filetype.equalsIgnoreCase("p2") && !filetype.equalsIgnoreCase("p5")) {
                System.out.println("[readPGM]Cannot load the image type of " + filetype);
                return;
            }

//            scanner.nextLine();
            int cols = scanner.nextInt();
            int rows = scanner.nextInt();
            int maxValue = scanner.nextInt();

            System.out.println("cols: " + cols + " rows: " + rows + " max val: " + maxValue);

            int[][] pixels = new int[rows][cols];
            System.out.println("Reading in image from of size " + rows + " by " + cols);
            // process the rest lines that hold the actual pixel values
            System.out.println(scanner.nextLine());
            System.out.println(scanner.next().length());
//            System.out.println(scanner.nextLine().length());

            for (int r=0; r<rows; r++) {
                for (int c=0; c<cols; c++) {
                   // System.out.println("r: " + r + " c: " + c);
//                    System.out.println(scanner.findInLine(".").charAt(0));
//                    pixels[r][c] = (int)(scanner.nextInt()*255.0/maxValue);
                }
            }

            scanner.close();


            BufferedReader br = new BufferedReader(new FileReader(image));
            String result = "", availalbe;
            while((availalbe = br.readLine()) != null) {
                result += availalbe;
            }
            System.out.println(result.length());
        } catch(FileNotFoundException fe) {
            System.out.println("Had a problem opening a file.");
        } catch (Exception e) {
            System.out.println(e.toString() + " caught in readPPM.");
            e.printStackTrace();
        }
    }
}
