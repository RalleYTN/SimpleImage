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
