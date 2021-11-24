package xyz.nyroma;

import xyz.nyroma.matrices.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Isoler {
    private BufferedImage img;
    private LinkedList<BufferedImage> subImgs;
    private String name;

    public Isoler(File file) {
        try {
            BufferedImage original = ImageIO.read(file);
            this.name = file.getName();
            this.img = resizeImage(original, 720, 1080);

            for(int i = 100; i < 1080 ; i += 100){

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector getAsVector(){
        LinkedList<Float> pixels = new LinkedList<>();
        for (int y = 0; y < this.img.getHeight(); y++) { // balaie les pixels ligne par ligne
            for (int x = 0; x < this.img.getWidth(); x++) {
                pixels.add((float) new Color(this.img.getRGB(x, y)).getRed() / 255);
            }
        }
        return(Vector.vectorialize(pixels));
    }

    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        return src.getSubimage(0, 0, rect.width, rect.height);
    }


    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public void saveImage(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "png", new File(name + "_resized.png"));
    }
}
