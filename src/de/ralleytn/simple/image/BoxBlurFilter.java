package de.ralleytn.simple.image;

/**
 * A box blur, (also known as a box linear filter) is a spatial domain linear filter in which each pixel
 * in the resulting image has a value equal to the average value of its neighboring pixels in the input
 * image. It is a form of low-pass ("blurring") filter.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class BoxBlurFilter extends ConvolutionFilter {
	
	/**
	 * @param radius radius of the blur
	 * @since 1.0.0
	 */
	public BoxBlurFilter(int radius) {
		
		this(radius, ConvolutionFilter.EDGE_GO_OVER);
	}

	/**
	 * @param radius radius of the blue
	 * @param edgeCondition what should happen when pixel outside the image are needed?
	 * @since 1.0.0
	 */
	public BoxBlurFilter(int radius, int edgeCondition) {
		
		super(BoxBlurFilter.__calculateKernel(radius), edgeCondition);
	}

	private static final float[][] __calculateKernel(int radius) {
		
		int size = radius * 2 + 1;
		float[][] kernel = new float[size][size];
		float value = 1.0F / (size * size);
		
		for(int x = 0; x < size; x++) {
			
			for(int y = 0; y < size; y++) {
				
				kernel[x][y] = value;
			}
		}
		
		return kernel;
	}
}
