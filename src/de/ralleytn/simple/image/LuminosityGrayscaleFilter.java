package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Grayscale filter that mixes red, green and blue channel in specific ratios.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class LuminosityGrayscaleFilter implements BiConsumer<int[][], int[][]>{

	private float luminosityR;
	private float luminosityG;
	private float luminosityB;
	
	/**
	 * red = 0.21F, green = 0.72F, blue = 0.07F
	 * @since 1.0.0
	 */
	public LuminosityGrayscaleFilter() {
		
		this(0.21F, 0.72F, 0.07F);
	}
	
	/**
	 * The sum must be 1.0F
	 * @param luminosityR how much red should the new color have?
	 * @param luminosityG how much green should the new color have?
	 * @param luminosityB how much blue should the new color have?
	 * @since 1.0.0
	 */
	public LuminosityGrayscaleFilter(float luminosityR, float luminosityG, float luminosityB) {
		
		if(luminosityR + luminosityG + luminosityB != 1) {
			
			throw new IllegalArgumentException("The luminosity has to be equal to 1!");
		}
		
		this.luminosityR = luminosityR;
		this.luminosityG = luminosityG;
		this.luminosityB = luminosityB;
	}
	
	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int pixel = source[x][y];
				int alpha = ColorUtils.getAlpha(pixel);
				int red = (int)(ColorUtils.getRed(pixel) * this.luminosityR);
				int green = (int)(ColorUtils.getGreen(pixel) * this.luminosityG);
				int blue = (int)(ColorUtils.getBlue(pixel) * this.luminosityB);
				int gray = red + green + blue;
				
				target[x][y] = ColorUtils.getARGB(gray, gray, gray, alpha);
			}
		}
	}
}
