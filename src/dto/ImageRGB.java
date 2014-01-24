package dto;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class ImageRGB implements Serializable{
    
    int width; int height; int[] pixels;

    public ImageRGB(BufferedImage bi) { 
         width = bi.getWidth(); 
         height = bi.getHeight(); 
         pixels = new int[width * height]; 
         int[] tmp=bi.getRGB(0,0,width,height,pixels,0,width); 
    }

    public BufferedImage getImage() { 
         BufferedImage bi = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
         bi.setRGB(0,0,width,height,pixels,0,width);
    return bi; 
    } 
    
}