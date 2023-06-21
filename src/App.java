import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class App {

    public static void main(String[] args) throws IOException {
        int windowSize = 15;
        BufferedImage pictureInput = null;
        File fileOutput = null;

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
                        //String hex = "#"+Integer.toHexString(picture.getRGB(i, j)).substring(2);
                        //System.out.println((hex));
                    }
                }
                int pixelAverage = (255 << 24) | (rSample/(windowSize*windowSize) << 16) | (gSample/(windowSize*windowSize) << 8) | bSample/(windowSize*windowSize);
                pictureOutput.setRGB(i, j, pixelAverage);
                //System.out.println("Done itterating through window!");
                //Done itterating through the given window
            }
        }
        //Done Parsing image

        //Try to write our file
        System.out.println("Trying to Write File");
        try {
            fileOutput = new File(
                "Sadie - Processed.png");
            ImageIO.write(pictureOutput, "png", fileOutput);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}
