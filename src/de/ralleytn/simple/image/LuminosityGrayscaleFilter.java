/*
 * MIT License
 * 
 * Copyright (c) 2017 Ralph Niemitz
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.ralleytn.simple.image;

import java.awt.Rectangle;

/**
 * Grayscale filter that mixes red, green and blue channel in specific ratios.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class LuminosityGrayscaleFilter extends Filter {

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
	public void apply(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		Rectangle bounds = this.getBounds();
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int srcPixel = source[x][y];
				
				if(SimpleImage.__inBounds(x, y, bounds.x, bounds.y, bounds.width, bounds.height)) {
					
					int alpha = ColorUtils.getAlpha(srcPixel);
					int red = (int)(ColorUtils.getRed(srcPixel) * this.luminosityR);
					int green = (int)(ColorUtils.getGreen(srcPixel) * this.luminosityG);
					int blue = (int)(ColorUtils.getBlue(srcPixel) * this.luminosityB);
					int gray = red + green + blue;
					
					target[x][y] = ColorUtils.getARGB(gray, gray, gray, alpha);
					
				} else {
					
					target[x][y] = srcPixel;
				}
			}
		}
	}
}
