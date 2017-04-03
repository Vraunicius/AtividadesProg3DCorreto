package com.company;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Vinicius on 02/04/2017.
 */
public class Atividade2 {

    static float contraste[][] = {
            { 0, -1,  0},
            {-1,  5, -1},
            { 0, -1,  0}
    };

    public static int saturação(int value){
        if (value > 255) {
            return 255;
        }
        else if (value < 0){
            return 0;
        }
        return value;
    }

    static Color applyKernel(Color[][] colors, float[][] kernel) {
        Color newColors[][] = new Color[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int r = saturação((int) (colors[x][y].getRed() * kernel[x][y]));
                int g = saturação((int) (colors[x][y].getGreen() * kernel[x][y]));
                int b = saturação((int) (colors[x][y].getBlue() * kernel[x][y]));
                newColors[x][y] = new Color(r, g, b);
            }
        }
        int sr = 0;
        int sg = 0;
        int sb = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                sr +=  newColors[x][y].getRed();
                sg +=  newColors[x][y].getGreen();
                sb +=  newColors[x][y].getBlue();
            }
        }
        return new Color(sr, sg, sb);
    }

    static Color getColor (BufferedImage img, int x, int y) {
        if(x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight())
            return new Color(0,0,0);

        return new Color (img.getRGB(x, y));

    }

    static Color[][] getPixelColor(BufferedImage img, int x, int y) {
        return new Color[][]{
                {getColor(img,x-1, y-1), getColor(img, x, y-1), getColor(img, x+1, y-1)},
                {getColor(img,x-1, y+0), getColor(img, x, y+0), getColor(img, x+1, y+0)},
                {getColor(img,x-1, y+1), getColor(img, x, y+1), getColor(img, x+1, y+1)}
        };

    }

    static BufferedImage convoluir(BufferedImage img, float[][] kernel, int pixelSize) {
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y+=pixelSize) {
            for (int x = 0; x < img.getWidth() ; x+=pixelSize) {
                Color colors = new Color(applyKernel(getPixelColor(img, x, y), kernel).getRGB());
                setProxCorContraste(img,out,x,y,pixelSize,colors);
                out.setRGB(x, y, colors.getRGB());
            }

        }
        return out;
    }

    public static BufferedImage pixelate(BufferedImage img, int pixelSize) {

        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),img.getType());

        for (int y = 0; y < img.getHeight();  y += pixelSize) {
            for (int x = 0; x < img.getWidth(); x += pixelSize) {
                setProxCor(img, out, x, y, pixelSize);
            }
        }

        return out;
    }

    public static void setProxCorContraste(BufferedImage img, BufferedImage out, int x, int y, int pixelSize, Color cor) {
        for (int height = y; height < y + pixelSize ; height++) {
            for (int width = x; width < x + pixelSize ; width++) {
                if(height >= img.getHeight() || width >= img.getWidth()) {
                    continue;
                }
                out.setRGB(width, height, cor.getRGB());
            }
        }
    }

    public static void setProxCor(BufferedImage img,BufferedImage out, int x, int y, int pixelSize) {
        Color newColor = new Color(img.getRGB(x,y));

        for (int height = y; height < y + pixelSize; height++) {
            for (int width = x; width < x + pixelSize; width++) {

                if(height >= img.getHeight() || width >= img.getWidth()) {
                    continue;
                }
                out.setRGB(width, height, newColor.getRGB());
            }
        }
    }

    public static void run() throws IOException {
        String PATH = "C:\\Users\\Pichau\\Desktop\\img\\img\\cor";
        BufferedImage img = ImageIO.read(new File(PATH,"metroid1.jpg"));
        BufferedImage pixelateImg = pixelate(img, 20);
        BufferedImage pixelContraste = convoluir(pixelateImg, contraste, 15);

        ImageIO.write(pixelateImg, "png", new File("pixelMetroid.png"));
        ImageIO.write(pixelContraste, "png", new File("pixelConvolveMetroid.png"));
    }

    public static void main(String[] args) throws IOException {
        new Atividade2().run();
    }
}