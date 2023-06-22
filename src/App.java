import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;


public class App {
    static String ColorToStringFormat(String inputString, String hexValue, String identifier){
        return inputString.replaceAll(identifier, hexValue);
    }

    public static void main(String[] args) throws IOException {
        int windowSize = 15;
        String stringFormat = "!#[HEX](https://placehold.co/10x10/HEX/HEX.png)";
        BufferedImage pictureInput = null;
        File filePictureOutput = null;
        BufferedWriter writer = null;
        //Try to open/create a textfile.
        try {
            File fileTextOutput = new File("Output.txt");
            if (fileTextOutput.createNewFile()) {
                System.out.println("File created: " + fileTextOutput.getName());
            } else {
                System.out.println("File already exists.");
                PrintWriter eraser = new PrintWriter(fileTextOutput);
                eraser.print("");
                eraser.close();
            }
            writer = new BufferedWriter(new FileWriter(fileTextOutput));
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            //Picture Found!
            System.out.println("Picture Found!");
            pictureInput = ImageIO.read(new File("Sadie.jpg"));
        } catch (IOException e) {
            //No picture found!
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            e.printStackTrace();
            return;
        }
        
        BufferedImage pictureOutput = new BufferedImage(pictureInput.getWidth()/windowSize, pictureInput.getHeight()/windowSize, BufferedImage.TYPE_INT_ARGB);
        //Itterate through each window
        for (int i = 0; i < pictureInput.getWidth()/windowSize; i++){
            for (int j = 0; j < pictureInput.getHeight()/windowSize; j++){
                //Itterate through each pixel in given window
                int rSample = 0, gSample = 0, bSample = 0;
                for (int x = 0; x < windowSize; x++){
                    for (int y = 0; y < windowSize; y++){
                        int pixelSample = pictureInput.getRGB(i*windowSize + x, j*windowSize + y);
                        rSample += (pixelSample >> 16) & 0xff; //Get Red Data
                        gSample += (pixelSample >> 8) & 0xff; //Get Green Data
                        bSample += (pixelSample) & 0xff; //Get Blue Data
                        
                        //System.out.println((hex));
                    }
                }
                int pixelAverage = (255 << 24) | (rSample/(windowSize*windowSize) << 16) | (gSample/(windowSize*windowSize) << 8) | bSample/(windowSize*windowSize);
                String hex = Integer.toHexString(pixelAverage).substring(2);
                pictureOutput.setRGB(i, j, pixelAverage);
                writer.append(ColorToStringFormat(stringFormat,hex,"HEX"));
                //Done itterating through the given window
            }
            writer.append("\n");
        }
        //Done Parsing image
        writer.close();

        //Try to write our file
        System.out.println("Trying to Write File");
        try {
            filePictureOutput = new File(
                "Sadie - Processed.png");
            ImageIO.write(pictureOutput, "png", filePictureOutput);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}
