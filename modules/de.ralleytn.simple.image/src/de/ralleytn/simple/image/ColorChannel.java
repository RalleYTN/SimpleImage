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

/**
 * Represents a color channel in the ARGB color model.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ColorChannel {

	/**
	 * @since 1.0.0
	 */
	ALPHA(24),
	
	/**
	 * @since 1.0.0
	 */
	RED(16),
	
	/**
	 * @since 1.0.0
	 */
	GREEN(8),
	
	/**
	 * @since 1.0.0
	 */
	BLUE(0);
	
	private int shift;
	
	private ColorChannel(int shift) {
		
		this.shift = shift;
	}
	
	/**
	 * @return the right shift to extract the channel from a pixel
	 * @since 1.0.0
	 */
	public int getShift() {
		
		return this.shift;
	}
}
