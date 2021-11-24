package xyz.nyroma;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class ImageUtils {
    public static LinkedList<LinkedList<Color>> getPixels(@NotNull BufferedImage image){
        LinkedList<LinkedList<Color>> pixels = new LinkedList<>();
        for(int y = 0 ; y < image.getHeight() ; y++){
            boolean blank = true;
            LinkedList<Color> line = new LinkedList<>();
            for(int x = 0 ; x < image.getWidth() ; x++){
                line.add(new Color(image.getRGB(x, y)));
            }
            pixels.add(line);
        }
        return pixels;
    }

    public static BufferedImage whitened(@NotNull BufferedImage image, int limit){
        LinkedList<LinkedList<Color>> pixels = getPixels(image);
        BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics g = image2.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();


        for(int x = 0 ; x < pixels.get(0).size() ; x++){
            for(int y = 0 ; y < pixels.size() ; y++){
                Color c = pixels.get(y).get(x);
                Color rgbC;
                float value = (c.getRed() + c.getBlue() + c.getGreen())/3f;
                if(value > limit){
                    rgbC = new Color(255, 255, 255);
                } else {
                    rgbC = new Color(0, 0, 0);
                }
                image2.setRGB(x, y, rgbC.getRGB());
            }
        }
        return image2;
    }

    public static LinkedList<LinkedList<Color>> transposePixels(LinkedList<LinkedList<Color>> list1){
        LinkedList<LinkedList<Color>> list2 = new LinkedList<>();
        for(int c = 0 ; c < list1.get(0).size() ; c++) {
            LinkedList<Color> column = new LinkedList<>();
            for (LinkedList<Color> objects : list1) {
                column.add(objects.get(c));
            }
            list2.add(column);
        }
        return list2;
    }

    public static LinkedHashMap<Integer, LinkedList<Integer>> getMinMax(BufferedImage img){
        LinkedHashMap<Integer, LinkedList<Integer>> minmax = new LinkedHashMap<>();
        LinkedList<LinkedList<Color>> pxls = transposePixels(getPixels(img));
        for(int c = 0 ; c < pxls.size() ; c++){
            int min = 0;
            int max = 0;
            for(int l = 0 ; l < pxls.get(c).size() ; l++){
                if(pxls.get(c).get(l).getRed() == 0){
                    min = l;
                    break;
                }
            }

            for(int l = pxls.get(c).size() - 1 ; l >= 0 ; l--){
                if(pxls.get(c).get(l).getRed() == 0){
                    max = l;
                    break;
                }
            }

            if(min != max) minmax.put(c, new LinkedList<>(Arrays.asList(min, max)));
        }
        return minmax;
    }

    public static LinkedList<Integer> getBlankLinesIndex(LinkedList<LinkedList<Color>> pxls, int maxi){
        LinkedList<Integer> blankLines = new LinkedList<>();

        for(int l = 0 ; l < pxls.size() ; l++){
            int b = 0;
            for(int c = 0 ; c < pxls.get(l).size() ; c++){
                if(pxls.get(l).get(c).getRed() == 0){
                    b++;
                }
            }
            if(b <= maxi){
                blankLines.add(l);
            }
        }
        return blankLines;
    }

    public static BufferedImage transposedImage(BufferedImage img){
        BufferedImage imgT = new BufferedImage(
                img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
        LinkedList<LinkedList<Color>> tPxls = transposePixels(getPixels(img));
        for(int l = 0 ; l < tPxls.size() ; l++){
            for(int c = 0 ; c < tPxls.get(0).size() ; c++){
                imgT.setRGB(c, l, tPxls.get(l).get(c).getRGB());
            }
        }
        return imgT;
    }

    public static LinkedList<BufferedImage> getParts(BufferedImage img, int maxi){
        LinkedList<BufferedImage> parts = new LinkedList<>();
        LinkedList<Integer> blankLines = ImageUtils.getBlankLinesIndex(ImageUtils.getPixels(img), maxi);

        for(int i = 0 ; i < blankLines.size() ; i++){
            if(i + 1 < blankLines.size()) {
                int height = blankLines.get(i + 1) - blankLines.get(i);
                if (height != 1) parts.add(img.getSubimage(0, blankLines.get(i), img.getWidth(), height));
            }
        }
        return parts;
    }
}
