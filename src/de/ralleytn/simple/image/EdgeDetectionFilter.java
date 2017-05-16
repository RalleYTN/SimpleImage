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
 * Filter that uses the following convolution matrix:
 * <table>
 *   <tr>
 *     <td>-1.0F</td>
 *     <td>-1.0F</td>
 *     <td>-1.0F</td>
 *   </tr>
 *   <tr>
 *     <td>-1.0F</td>
 *     <td>8.0F</td>
 *     <td>-1.0F</td>
 *   </tr>
 *   <tr>
 *     <td>-1.0F</td>
 *     <td>-1.0F</td>
 *     <td>-1.0F</td>
 *   </tr>
 * </table>
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class EdgeDetectionFilter extends ConvolutionFilter {

	/**
	 * @since 1.0.0
	 */
	public EdgeDetectionFilter() {
		
		this(ConvolutionFilter.EDGE_GO_OVER);
	}
	
	/**
	 * @param edgeCondition what should happen when pixel outside the image are needed?
	 * @since 1.0.0
	 */
	public EdgeDetectionFilter(int edgeCondition) {
		
		super(new float[][] {
			new float[] {-1.0F, -1.0F, -1.0F},
			new float[] {-1.0F, 8.0F, -1.0F},
			new float[] {-1.0F, -1.0F, -1.0F}
		}, edgeCondition);
	}
}
