import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 
/**
 * Image to grayscale algorithm(s)
 *
 * Author: Bostjan Cigan (http://zerocool.is-a-geek.net)
 *
 */
 
public class Grayscale {
 
    
 
    // The average grayscale method
    public static BufferedImage avg(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage avg_gray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] avgLUT = new int[766];
        for(int i=0; i<avgLUT.length; i++) avgLUT[i] = (int) (i / 3);
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
                
                newPixel = (red + green + blue);
                newPixel = avgLUT[newPixel];
                // Return back to original format
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
 
                // Write pixels into image
                avg_gray.setRGB(i, j, newPixel);
 
            }
        }
 
        return avg_gray;
 
    }
 
    // The luminance method
    public static BufferedImage luminosity(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
 
                // Write pixels into image
                lum.setRGB(i, j, newPixel);
 
            }
        }
 
        return lum;
 
    }    
 
    // The desaturation method
    public static BufferedImage desaturation(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        int[] pixel = new int[3];
 
        BufferedImage des = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] desLUT = new int[511];
        for(int i=0; i<desLUT.length; i++) desLUT[i] = (int) (i / 2);
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;
 
                int newval = (int) (findMax(pixel) + findMin(pixel));
                newval = desLUT[newval];
 
                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);
 
                // Write pixels into image
                des.setRGB(i, j, newPixel);
 
            }
        }
 
        return des;
 
    }    
 
    // The minimal decomposition method
    public static BufferedImage decompMin(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        int[] pixel = new int[3];
 
        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;
 
                int newval = findMin(pixel);
 
                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);
 
                // Write pixels into image
                decomp.setRGB(i, j, newPixel);
 
            }
        }
 
        return decomp;
 
    }    
 
    // The maximum decomposition method
    public static BufferedImage decompMax(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        int[] pixel = new int[3];
 
        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;
 
                int newval = findMax(pixel);
 
                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);
 
                // Write pixels into image
                decomp.setRGB(i, j, newPixel);
 
            }
 
        }
 
        return decomp;
 
    }    
 
    // The "pick the color" method
    public static BufferedImage rgb(BufferedImage original, int color) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        int[] pixel = new int[3];
 
        BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;
 
                int newval = pixel[color];
 
                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);
 
                // Write pixels into image
                rgb.setRGB(i, j, newPixel);
 
            }
 
        }
 
        return rgb;        
 
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
 
    public static int findMin(int[] pixel) {
 
        int min = pixel[0];
 
        for(int i=0; i<pixel.length; i++) {
            if(pixel[i] < min)
                    min = pixel[i];
        }
 
        return min;
 
    }
 
    public static int findMax(int[] pixel) {
 
        int max = pixel[0];
 
        for(int i=0; i<pixel.length; i++) {
            if(pixel[i] > max)
                    max = pixel[i];
        }
 
        return max;
 
    }
    
    /*
     *  Added algorithms needed for the rest of the project
     */
    
    // Custom # of gray shades  
    // algorithm taken from "http://www.tannerhelland.com/3643/"
    public static BufferedImage customGray(BufferedImage original, int numberOfShades){
    	
    	int alpha, red, green, blue;
        int averageValue;
        int conversionFactor;
        int gray; 	
 
        BufferedImage customGray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        
        conversionFactor = 255 / (numberOfShades -1);
        
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                averageValue = (red + green + blue)/3;
                gray = (int)((averageValue / conversionFactor) + .05) * conversionFactor;
                
                // Return back to original format
                gray = colorToRGB(alpha, gray, gray, gray);
 
                // Write pixels into image
                customGray.setRGB(i, j, gray);
 
            }
        }
 
        return customGray;
    	
    }
 
    // Custom # of gray shades with dithering, algorithm is used 
    // is "http://stackoverflow.com/questions/5940188/how-to-convert-a-24-bit-png-to-3-bit-png-using-floyd-steinberg-dithering"
    public static BufferedImage dithering(BufferedImage original) {
    	
    	
        BufferedImage dithering = original;
 
        C3[] palette = new C3[] {
            new C3(  0,   0,   0),
            new C3(  0,   0, 255),
            new C3(  0, 255,   0),
            new C3(  0, 255, 255),
            new C3(255,   0,   0),
            new C3(255,   0, 255),
            new C3(255, 255,   0),
            new C3(255, 255, 255)
        };

        int w = dithering.getWidth();
        int h = dithering.getHeight();

        C3[][] d = new C3[h][w];

        for (int y = 0; y < h; y++) 
          for (int x = 0; x < w; x++) 
            d[y][x] = new C3(dithering.getRGB(x, y));

        for (int y = 0; y < dithering.getHeight(); y++) {
          for (int x = 0; x < dithering.getWidth(); x++) {

            C3 oldColor = d[y][x];
            C3 newColor = findClosestPaletteColor(oldColor, palette);
            dithering.setRGB(x, y, newColor.toColor().getRGB());

            C3 err = oldColor.sub(newColor);

            if (x+1 < w)         d[y  ][x+1] = d[y  ][x+1].add(err.mul(7./16));
            if (x-1>=0 && y+1<h) d[y+1][x-1] = d[y+1][x-1].add(err.mul(3./16));
            if (y+1 < h)         d[y+1][x  ] = d[y+1][x  ].add(err.mul(5./16));
            if (x+1<w && y+1<h)  d[y+1][x+1] = d[y+1][x+1].add(err.mul(1./16));
          }
        }
      
        return original;
        
      }

      private static C3 findClosestPaletteColor(C3 c, C3[] palette) {
        C3 closest = palette[0];

        for (C3 n : palette) 
          if (n.diff(c) < closest.diff(c))
            closest = n;

        return closest;
      }
      
      // this class is used by the dithering algorithm
      static class C3 {
    	    int r, g, b;

    	    public C3(int c) {
    	      Color color = new Color(c);
    	      this.r = color.getRed();
    	      this.g = color.getGreen();
    	      this.b = color.getBlue();
    	    }
    	    public C3(int r, int g, int b) {
    	      this.r = r;
    	      this.g = g;
    	      this.b = b;
    	    }

    	    public C3 add(C3 o) {
    	      return new C3(r + o.r, g + o.g, b + o.b);
    	    }
    	    public C3 sub(C3 o) {
    	      return new C3(r - o.r, g - o.g, b - o.b);
    	    }
    	    public C3 mul(double d) {
    	      return new C3((int) (d * r), (int) (d * g), (int) (d * b));
    	    }
    	    public int diff(C3 o) {
    	      return Math.abs(r - o.r) +  Math.abs(g - o.g) +  Math.abs(b - o.b);
    	    }

    	    public int toRGB() {
    	      return toColor().getRGB();
    	    }
    	    public Color toColor() {
    	      return new Color(clamp(r), clamp(g), clamp(b));
    	    }
    	    public int clamp(int c) {
    	      return Math.max(0, Math.min(255, c));
    	    }
      }
 
}