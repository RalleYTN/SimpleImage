package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Grayscale filter that sets the RGB channels of a pixel to their average.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class AveragingGrayscaleFilter implements BiConsumer<int[][], int[][]> {

	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int red = ColorUtils.getRed(pixel);
				int green = ColorUtils.getGreen(pixel);
				int blue = ColorUtils.getBlue(pixel);
				int gray = (red + green + blue) / 3;
				
				target[x][y] = ColorUtils.getARGB(gray, gray, gray, alpha);
			}
		}
	}
}
