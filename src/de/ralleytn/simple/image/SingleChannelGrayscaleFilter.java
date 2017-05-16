package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Grayscale filter that uses one of the original pixels channels as the new color.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class SingleChannelGrayscaleFilter implements BiConsumer<int[][], int[][]> {
	
	private int shift = 0;
	
	/**
	 * @param channel the color channel of the original pixel that determines the new pixels color
	 * @since 1.0.0
	 */
	public SingleChannelGrayscaleFilter(ColorChannel channel) {
		
		this.shift = channel.getShift();
	}
	
	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int gray = (pixel >> this.shift) & 0xFF;
				
				target[x][y] = ColorUtils.getARGB(gray, gray, gray, alpha);
			}
		}
	}
}
