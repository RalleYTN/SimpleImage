package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that only uses black and white.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class MonochromeFilter implements BiConsumer<int[][], int[][]> {

	private int threeshold;
	private boolean invert;
	
	/**
	 * @since 1.0.0
	 */
	public MonochromeFilter() {
		
		this(0.5F, false);
	}
	
	/**
	 * @param invert everything that would be black without this would be white with it
	 * @since 1.0.0
	 */
	public MonochromeFilter(boolean invert) {
		
		this(0.5F, invert);
	}
	
	/**
	 * @param threeshold at which point should the color switch? (0.0F - 1.0F)
	 * @since 1.0.0
	 */
	public MonochromeFilter(float threeshold) {
		
		this(threeshold, false);
	}
	
	/**
	 * @param threeshold at which point should the color switch? (0.0F - 1.0F)
	 * @param invert everything that would be black without this would be white with it
	 * @since 1.0.0
	 */
	public MonochromeFilter(float threeshold, boolean invert) {
		
		this.threeshold = (int)(threeshold * (255 * 3));
	}
	
	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int red = ColorUtils.getRed(pixel);
				int green = ColorUtils.getGreen(pixel);
				int blue = ColorUtils.getBlue(pixel);
				int sum = red + green + blue;
				int color = this.invert ? (sum < this.threeshold ? 0xFFFFFFFF : 0) : (sum > this.threeshold ? 0xFFFFFFFF : 0xFF000000);
				
				target[x][y] = ColorUtils.getARGB(color, color, color, 255);
			}
		}
	}
}