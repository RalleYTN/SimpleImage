package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that changes the brightness of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class BrightnessFilter implements BiConsumer<int[][], int[][]> {

	private int brightness;
	
	/**
	 * @param brightness 0.0F = dark, 1.0F = bright
	 * @since 1.0.0
	 */
	public BrightnessFilter(float brightness) {
		
		if(brightness > 1.0F) {
			
			brightness = 1.0F;
			
		} else if(brightness < -1.0F) {
			
			brightness = -1.0F;
		}
		
		this.brightness = (int)(brightness * 255);
	}
	
	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int red = ColorUtils.truncate(ColorUtils.getRed(pixel) + this.brightness);
				int green = ColorUtils.truncate(ColorUtils.getGreen(pixel) + this.brightness);
				int blue = ColorUtils.truncate(ColorUtils.getBlue(pixel) + this.brightness);
				
				target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
			}
		}
	}
}
