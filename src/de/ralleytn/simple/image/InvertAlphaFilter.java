package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that inverts the alpha channel of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvertAlphaFilter implements BiConsumer<int[][], int[][]> {

	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				target[x][y] = (pixel & 0x00FFFFFFFF) | (255 - ColorUtils.getAlpha(pixel));
			}
		}
	}
}
