
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;



public class Thresholding {
    
    // Get binary treshold using Otsu's method
    // http://zerocool.is-a-geek.net/java-image-binarization/
    public int otsuTreshold(BufferedImage original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        } 
        return threshold;
    }
    
    public BufferedImage binarize(BufferedImage original) {

        int red;
        int newPixel;
 
        int threshold = otsuTreshold(original); // where treshold is calculated
 
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel); 
 
            }
        }
 
        return binarized;
 
    }
    
        // Return histogram of grayscale image
    public int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
    
    // Convert R, G, B, Alpha to standard 8 bit
    public static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
    }
    
    /*
        outsu ends
        Dumb thres begins
    */
    
    public BufferedImage dumbThres(BufferedImage original){

        int red;
        int newPixel;

        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        int threshold = 128;
        Color color;
        for(int i = 0; i < original.getWidth(); i++ ){
            for(int j = 0; j < original.getHeight(); j++){
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                image.setRGB(i, j, newPixel);
            }    
        }
        return image;
    }

    public BufferedImage colorMinMaxAverage(BufferedImage original){

        int max = new Color(original.getRGB(0, 0)).getRed();
        int min = new Color(original.getRGB(0, 0)).getRed();
        int red;
        int newPixel;

        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i = 0; i < original.getWidth(); i++ ) {
            for (int j = 0; j < original.getHeight(); j++) {
                int currentPixel = new Color(original.getRGB(i, j)).getRed();
                if(currentPixel < min)
                    min = currentPixel;
                if(currentPixel > max)
                    max = currentPixel;
            }
        }

        // average of the max and min pixel
        int threshold = (max + min) / 2;
        for(int i = 0; i < original.getWidth(); i++ ){
            for(int j = 0; j < original.getHeight(); j++){
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                image.setRGB(i, j, newPixel);
            }
        }
        return image;
    }

    //http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Calculationofthemeanvalueofanimage.htm
    public BufferedImage mean(BufferedImage original){

        int red;
        int newPixel;

        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        Raster raster = original.getRaster();
        double sum = 0.0;

        for (int y = 0; y < image.getHeight(); ++y){
            for (int x = 0; x < image.getWidth(); ++x){
                sum += raster.getSample(x, y, 0);
            }
        }
        
        int threshold = (int)(sum / (image.getWidth() * image.getHeight()));

        Color color;
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                image.setRGB(i, j, newPixel);
            }    
        }
        return image;
    }
    
    //Finds the threshold using maximum
    public BufferedImage maxEntropy(BufferedImage original){
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        int red;
        int newPixel;

        int pixel[] = new int [256];
        int threshold = 0;		//holds the threshold value

        double bHistogramSum;	 //holds the sum of the histogram of all background pixels
        double fHistogramSum;	 //holds the sum of the histogram of all foreground pixels

        double maxEntropy = 0;	 //holds the value of the maximum entropy calculated so far
        double bEntropy;         //background entropy
        double fEntropy;		 //foreground entropy

        //frequency of pixels
        for(int row=0; row<original.getWidth(); row++){
            for(int col=0; col<original.getHeight(); col++){
                int pValue = new Color(original.getRGB(row,col)).getRed();
                pixel[pValue]++;

            }//for
        }//for

        //looks for t that produces the maximum entropy
        for(int t=0; t<256; t++){

            if(pixel[t]!=0){

                //BACKGROUND ENTROPY
                bHistogramSum = 0;
                bEntropy = 0;
                for(int bSum=0; bSum<=t; bSum++){//background sum
                    bHistogramSum = bHistogramSum + pixel[bSum];
                }//for

                for(int b=0; b<=t; b++){//calculates background entropy
                    if(pixel[b]!=0)
                        bEntropy = bEntropy + (pixel[b]/bHistogramSum) * (Math.log(pixel[b]/bHistogramSum)/Math.log(2));

                }//for
                bEntropy = bEntropy *-1;


                //FOREGROUND ENTROPY

                fHistogramSum = 0;
                fEntropy = 0;
                for(int fSum=t+1; fSum<256; fSum++){//foreground sum
                    fHistogramSum = fHistogramSum + pixel[fSum];
                }//for

                for(int f=t+1; f<256; f++){//calculates foreground entropy
                    if(pixel[f]!=0)
                        fEntropy = fEntropy + (pixel[f]/fHistogramSum) * (Math.log(pixel[f]/fHistogramSum)/Math.log(2));

                }//for
                fEntropy = fEntropy *-1;



                //checks if current entropy calculated is larger than the previous one
                if(maxEntropy < (bEntropy+fEntropy)){
                    maxEntropy = (bEntropy+fEntropy);
                    threshold = t;
                }//if


            }//if

        }//for

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                image.setRGB(i, j, newPixel);
            }
        }
        return image;
    }

}
