package de.ralleytn.simple.image.internal;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class Utils {

	private Utils() {}
	
	public static final int[][] read(Image image) {
		
		BufferedImage imageToRead = Utils.convert(image);
		int width = imageToRead.getWidth();
		int height = imageToRead.getHeight();
		int[][] data = new int[width][height];
		
		for(int x = 0; x < width; x++) {
			
			for(int y = 0; y < height; y++) {
				
				data[x][y] = imageToRead.getRGB(x, y);
			}
		}
		
		return data;
	}
	
	public static final BufferedImage convert(Image image) {

		if(image instanceof BufferedImage) {
			
			BufferedImage convertedImage = (BufferedImage)image;
			
			if(convertedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
				
				return convertedImage;
			}
		}
		
		BufferedImage convertedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = convertedImage.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		
		return convertedImage;
	}
	
	public static final boolean inBounds(int posX, int posY, int boundsX, int boundsY, int boundsWidth, int boundsHeight) {
		
		return posX >= boundsX && posX < boundsX + boundsWidth && posY >= boundsY && posY < boundsY + boundsHeight;
	}
	
	public static final int max(int red, int green, int blue) {
		
		return Math.max(Math.max(red, green), blue);
	}

	public static final int min(int red, int green, int blue) {
		
		return Math.min(Math.min(red, green), blue);
	}
	
	public static final int interpolateColorChannelBilinear(int pixelA, int pixelB, int pixelC, int pixelD, float diffX, float diffY, int channel) {
		
		float diffX2 = 1.0F - diffX;
		float diffY2 = 1.0F - diffY;
		
		int pixelAChannel = (pixelA >> channel) & 0xFF;
		int pixelBChannel = (pixelB >> channel) & 0xFF;
		int pixelCChannel = (pixelC >> channel) & 0xFF;
		int pixelDChannel = (pixelD >> channel) & 0xFF;
		
		float pixelAChannelPart = pixelAChannel * diffX2 * diffY2;
		float pixelBChannelPart = pixelBChannel * diffX * diffY2;
		float pixelCChannelPart = pixelCChannel * diffX2 * diffY;
		float pixelDChannelPart = pixelDChannel * diffX * diffY;
		
		return (int)(pixelAChannelPart + pixelBChannelPart + pixelCChannelPart + pixelDChannelPart);
	}
}
