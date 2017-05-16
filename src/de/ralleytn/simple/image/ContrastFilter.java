package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that allows you to set the contrast of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContrastFilter implements BiConsumer<int[][], int[][]> {

	private float factor;
	
	/**
	 * @param contrast -1.0F = minimum contrast, 1.0F = maximum contrast
	 * @since 1.0.0
	 */
	public ContrastFilter(float contrast) {
		
		if(contrast > 1.0F) {
			
			contrast = 1.0F;
			
		} else if(contrast < -1.0F) {
			
			contrast = -1.0F;
		}
		
		int contrastInt = (int)(contrast * 255.0F);
		this.factor = (259.0F * (contrastInt + 255.0F)) / (255.0F * (259.0F - contrastInt));
	}

	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int red = ContrastFilter.__calculateColor(ColorUtils.getRed(pixel), this.factor);
				int green = ContrastFilter.__calculateColor(ColorUtils.getGreen(pixel), this.factor);
				int blue = ContrastFilter.__calculateColor(ColorUtils.getBlue(pixel), this.factor);

				target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
			}
		}
	}
	
	private static final int __calculateColor(int input, float factor) {

		return ColorUtils.truncate((int)((factor * (input - 128)) + 128));
	}
}
