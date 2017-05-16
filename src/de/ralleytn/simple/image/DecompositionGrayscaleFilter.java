package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Grayscale filter that uses the color channel with either highest or the lowest value as
 * the new pixel color.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class DecompositionGrayscaleFilter implements BiConsumer<int[][], int[][]> {

	/**
	 * Use the color channel with the lowest value as new pixel color.
	 * @since 1.0.0
	 */
	public static final int DECOMPOSITION_MINIMUM = 0;
	
	/**
	 * Use the color channel with the highest value as new pixel color.
	 * @since 1.0.0
	 */
	public static final int DECOMPOSITION_MAXIMUM = 1;
	
	private int decomposition;
	
	/**
	 * @param decomposition should the color channel with the highest or the one with the lowest value
	 *        be used as the new pixel color?
	 * @since 1.0.0
	 */
	public DecompositionGrayscaleFilter(int decomposition) {
		
		this.decomposition = decomposition;
	}
	
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
				int gray = 0x00000000;
				
				if(this.decomposition == DecompositionGrayscaleFilter.DECOMPOSITION_MAXIMUM) {
					
					gray = ColorUtils.__max(red, green, blue);
					
				} else if(this.decomposition == DecompositionGrayscaleFilter.DECOMPOSITION_MINIMUM) {
					
					gray = ColorUtils.__min(red, green, blue);
				}
				
				target[x][y] = ColorUtils.getARGB(gray, gray, gray, alpha);
			}
		}
	}
}
