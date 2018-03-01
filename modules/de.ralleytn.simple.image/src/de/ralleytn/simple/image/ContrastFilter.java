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

import de.ralleytn.simple.image.internal.Utils;

/**
 * Filter that allows you to set the contrast of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContrastFilter extends Filter {

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
	public void apply(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		Rectangle bounds = this.getBounds();
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int srcPixel = source[x][y];
				
				if(Utils.inBounds(x, y, bounds.x, bounds.y, bounds.width, bounds.height)) {
					
					int alpha = ColorUtils.getAlpha(srcPixel);
					int red = ContrastFilter.__calculateColor(ColorUtils.getRed(srcPixel), this.factor);
					int green = ContrastFilter.__calculateColor(ColorUtils.getGreen(srcPixel), this.factor);
					int blue = ContrastFilter.__calculateColor(ColorUtils.getBlue(srcPixel), this.factor);

					target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
				
				} else {
					
					target[x][y] = srcPixel;
				}
			}
		}
	}
	
	private static final int __calculateColor(int input, float factor) {

		return ColorUtils.truncate((int)((factor * (input - 128)) + 128));
	}
}
