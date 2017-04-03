package com.company;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Vinicius on 02/04/2017
 */
public class Atividade3 {
    public static final String PATH = "C:\\Users\\Pichau\\Desktop\\img\\img\\gray";

    int[] CalcHist(BufferedImage img){
        int[] hist = new int[256];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color cor = new Color(img.getRGB(x, y));
                int red = cor.getRed();
                hist[red] += 1;
            }
        }
        return hist;
    }

    public int[] CalcularHistSomado(int[] hist){

        int[] somado = new int[256];
        somado[0] = hist[0];

        for (int i = 1;i < hist.length; i++){
            somado[i] = hist[i] + somado[i-1];
        }
        return somado;
    }

    private int menorValor(int[] hist){
        for(int i = 0; i < hist.length; i++){
            if (hist[i] !=0){
                return i;
            }
        }
        return 0;
    }

    int[] CalcularColorMap(int[] hist, int pixels){
        int[] ColorMap = new int[256];
        int[] somado = CalcularHistSomado(hist);
        float menorV = menorValor(hist);

        for(int i = 0;i<hist.length; i++){
            ColorMap[i] = Math.round(((somado[i] - menorV) / (pixels - menorV)) * 255);
        }

        return ColorMap;
    }

    BufferedImage Equalize(BufferedImage img){
        int[] histograma = CalcHist(img);
        int[] ColorMap = CalcularColorMap(histograma, img.getWidth() * img.getHeight());
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color cor = new Color(img.getRGB(x, y));
                int tom = cor.getRed();

                int newTom = ColorMap[cor.getRed()];

                Color newCor = new Color(newTom, newTom, newTom);

                out.setRGB(x, y, newCor.getRGB());
            }
        }
        return out;
    }

    void run() throws IOException
    {
        BufferedImage img = ImageIO.read(new File(PATH, "university.png"));
        BufferedImage img2 = Equalize(img);
        ImageIO.write(img2, "png", new File(PATH, "equniversity.png"));
    }
    public static void main(String[] args) throws IOException {
        new Atividade3().run();
    }
}