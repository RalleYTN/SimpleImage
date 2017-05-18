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
 * Filter that changes the brightness of an image.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class BrightnessFilter extends Filter {

	private int brightness;
	
	/**
	 * @param brightness 0.0F = dark, 1.0F = bright
	 * @since 1.0.0
	 */
	public BrightnessFilter(float brightness) {
		
		if(brightness > 1.0F) {
			
			brightness = 1.0F;
			
		} else if(brightness < -1.0F) {
			
			brightness = -1.0F;
		}
		
		this.brightness = (int)(brightness * 255);
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
					int red = ColorUtils.truncate(ColorUtils.getRed(srcPixel) + this.brightness);
					int green = ColorUtils.truncate(ColorUtils.getGreen(srcPixel) + this.brightness);
					int blue = ColorUtils.truncate(ColorUtils.getBlue(srcPixel) + this.brightness);
					
					target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
					
				} else {
					
					target[x][y] = srcPixel;
				}
			}
		}
	}
}
