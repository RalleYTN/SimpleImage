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
 * Filter that blurs the image with gaussian blur.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class GaussianBlurFilter extends ConvolutionFilter {

	/**
	 * @param radius radius of the blur
	 * @since 1.0.0
	 */
	public GaussianBlurFilter(int radius) {
		
		this(radius, ConvolutionFilter.EDGE_GO_OVER);
	}
	
	/**
	 * @param radius radius of the blur
	 * @param edgeCondition what should happen when pixel outside the image are needed?
	 * @since 1.0.0
	 */
	public GaussianBlurFilter(int radius, int edgeCondition) {
		
		super(GaussianBlurFilter.__calculateKernel(radius), edgeCondition);
	}

	private static final float[][] __calculateKernel(int radius) {
		
		double sigma = 1.0D;
		int size = radius * 2 + 1;
		float[][] kernel = new float[size][size];
		double mean = size / 2.0D;
		double sum = 0.0D;
		
		for(int x = 0; x < size; x++) {
			
			for(int y = 0; y < size; y++) {
				
				kernel[x][y] = (float)(Math.exp(-0.5D * (Math.pow((x - mean) / sigma, 2.0D) + Math.pow((y - mean) / sigma, 2.0D))) / (2 * Math.PI * sigma * sigma));
				sum += kernel[x][y];
			}
		}
		
		for(int x = 0; x < size; x++) {
			
			for(int y = 0; y < size; y++) {
				
				kernel[x][y] /= sum;
			}
		}
		
		return kernel;
	}
}
