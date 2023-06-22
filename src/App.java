import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class App {


    public static void main(String[] args) throws IOException {
        int windowSize = 8; //I would not set it to less that 4. (Anything less that 16 will cause a larger image to be generated. Size factor = 16/x)
        BufferedImage minecraftBlockListPictureInput = null;
        BufferedImage sourcePictureInput = null;
        File filePictureOutput = null;
        File fileMinecraftPictureOutput = null;


        // Setting up our minecraft block index //
        KD3DTree kdt = new KD3DTree(351);
        BufferedReader br = new BufferedReader(new FileReader("./MinecraftBlockify/BlockList.txt"));
        String line = null;
        while ((line = br.readLine()) != null) { // read through and get each line of "BlockList.txt"
            int index = 0;
            int a = line.indexOf("{");
            int b = line.indexOf(",");
            int c = line.indexOf(",", b + 1);
            int d = line.indexOf("}");
            int e = line.indexOf("[");
            int f = line.indexOf("]");
            double blockData[] = new double[3];
            blockData[0] = Integer.parseInt(line.substring(a + 1, b)); // extract r channel
            blockData[1] = Integer.parseInt(line.substring(b + 1, c)); // extract g channel
            blockData[2] = Integer.parseInt(line.substring(c + 1, d)); // extract b channel
            index = Integer.parseInt(line.substring(e + 1, f)); // extract the index value of where this is located in
                                                                // "MinecraftBlocksLabeled.png"
            kdt.add(blockData, index); // add colour point to our kdtree point cloud
            // System.out.println("("+red+","+green+","+blue+") ["+ index + "]");
        }
        br.close();
        // Done setting up our colour lookup //

        // Parsing our input picture //

        // Find Source Picture
        try {
            // Picture Found!
            minecraftBlockListPictureInput = ImageIO.read(new File("./MinecraftBlockify/MinecraftBlocksLabeled.png")); //our block texture lookup table
            System.out.println("Tile Set found");
            sourcePictureInput = ImageIO.read(new File("Source.png")); //our source picture to be converted
            System.out.println("Source Picture found");
        } catch (IOException e) {
            // No picture found or BlockList found.
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            e.printStackTrace();
            return; //error
        }
        // Define our picture output size
        BufferedImage pictureOutput = new BufferedImage(sourcePictureInput.getWidth() / windowSize,sourcePictureInput.getHeight() / windowSize, BufferedImage.TYPE_INT_ARGB); //pixelated version
        BufferedImage minecraftPictureOutput = new BufferedImage(((int)(sourcePictureInput.getWidth() / windowSize))*16,((int)(sourcePictureInput.getHeight() / windowSize))*16, BufferedImage.TYPE_INT_ARGB); //minecraftblock version
        // Itterate through each window
        for (int j = 0; j < sourcePictureInput.getHeight() / windowSize; j++) {
            for (int i = 0; i < sourcePictureInput.getWidth() / windowSize; i++) {
                // Itterate through each pixel in given window
                int rSample = 0, gSample = 0, bSample = 0;
                for (int x = 0; x < windowSize; x++) {
                    for (int y = 0; y < windowSize; y++) {
                        int pixelSample = sourcePictureInput.getRGB(i * windowSize + x, j * windowSize + y);
                        rSample += (pixelSample >> 16) & 0xff; // Get Red Data
                        gSample += (pixelSample >> 8) & 0xff; // Get Green Data
                        bSample += (pixelSample) & 0xff; // Get Blue Data
                    }
                }
                //get avg value of each channel sample
                rSample /= (windowSize * windowSize);
                gSample /= (windowSize * windowSize);   
                bSample /= (windowSize * windowSize);
                //concatinate values into pixel value.
                int pixelAverage = (255 << 24) | (rSample << 16) | (gSample << 8) | bSample;
                //System.out.println("Searching: ");
                double RGBValues[] = { rSample, gSample, bSample };
                //System.out.println(kdt.find_nearest(RGBValues).value); // Found the nearest Minecraftblock colour match
                pictureOutput.setRGB(i, j, pixelAverage);
                minecraftPictureOutput.getGraphics().drawImage(minecraftBlockListPictureInput.getSubimage(kdt.find_nearest(RGBValues).value*16,0,16,16), i*16, j*16, null);
                // Done itterating through the given window and matched to its closest minecraft block.
            }
        }
        // Done Parsing image //

        // Writing our file //
        System.out.println("Trying to Write File");
        try {
            filePictureOutput = new File(
                    "SourcePixelated.png");
            ImageIO.write(pictureOutput, "png", filePictureOutput);
            System.out.println("Written: " + filePictureOutput.getName());

            fileMinecraftPictureOutput = new File(
                    "SourceMinecrafted.png");
            ImageIO.write(minecraftPictureOutput, "png", fileMinecraftPictureOutput);

            System.out.println("Written: " + fileMinecraftPictureOutput.getName());
        } catch (IOException e) {
            System.out.println(e);
        }
        // File Write Done! //
    }

}
