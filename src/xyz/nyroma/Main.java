package xyz.nyroma;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        try {
            File file = new File("bts.png");
            BufferedImage img = ImageUtils.whitened(ImageIO.read(file), 130);
            ImageIO.write(img, "png", new File("parts/" + file.getName() + " (copy).png"));
            LinkedList<BufferedImage> parts = ImageUtils.getParts(img, 20);
            ImageIO.write(ImageUtils.transposedImage(img), "png", new File("parts/" + file.getName() + "_transposed.png"));
            for (int i = 0; i < parts.size(); i++) {
                //ImageIO.write(parts.get(i), "png", new File("parts/part_" + i + ".png"));
                String path = "parts/part" + i + "/";
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                LinkedList<BufferedImage> subParts = ImageUtils.getParts(ImageUtils.transposedImage(parts.get(i)), 0);
                for (int j = 0; j < subParts.size(); j++) {
                    ImageIO.write(ImageUtils.transposedImage(subParts.get(j)), "png", new File(path + "_letter_" + j + ".png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Temps total : " + (System.currentTimeMillis() - t1));
    }
}
