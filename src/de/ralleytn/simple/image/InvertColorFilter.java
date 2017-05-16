package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that inverts the red, green and blue channels of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvertColorFilter implements BiConsumer<int[][], int[][]>{

	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int red = 255 - ColorUtils.getRed(pixel);
				int green = 255 - ColorUtils.getGreen(pixel);
				int blue = 255 - ColorUtils.getBlue(pixel);
				
				target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
			}
		}
	}
}
