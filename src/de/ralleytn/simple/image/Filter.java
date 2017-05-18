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
 * Must be extended by all filters.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.1.0
 * @since 1.1.0
 */
public abstract class Filter {

	private final Rectangle bounds = new Rectangle();
	
	/**
	 * Applies the filter on the image.
	 * @param source image on which the filter should be applied to
	 * @param target the output image
	 * @since 1.1.0
	 */
	public abstract void apply(int[][] source, int[][] target);

	/**
	 * Sets the bounds in which the filter should manipulate the pixels.
	 * @param bounds bounds in which the filter should manipulate the pixels
	 * @since 1.1.0
	 */
	public void setBounds(Rectangle bounds) {
		
		this.bounds.x = bounds.x;
		this.bounds.y = bounds.y;
		this.bounds.width = bounds.width;
		this.bounds.height = bounds.height;
	}
	
	/**
	 * @return the bounds in which the filter should manipulate the pixels
	 * @since 1.1.0
	 */
	public Rectangle getBounds() {
		
		return this.bounds;
	}
}
