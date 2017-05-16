package de.ralleytn.simple.image;

import java.util.function.BiConsumer;

/**
 * Filter that applies a convolution matrix on an image.
 * @author Ralph Niemitz/RalleYTN
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConvolutionFilter implements BiConsumer<int[][], int[][]> {

	/**
	 * Interprets pixels outside the image as pixels with the color {@code 0x00000000}.
	 * @since 1.0.0
	 */
	public static final int EDGE_GO_OVER = 0;
	
	/**
	 * The edge will simply be filled with empty pixels.
	 * @since 1.0.0
	 */
	public static final int EDGE_ZERO_FILL = 1;
	
	/**
	 * Fills the edge with the source pixel.
	 * @since 1.0.0
	 */
	public static final int EDGE_FROM_SOURCE = 2;
	
	/**
	 * Repeats the image when going outside.
	 * @since 1.0.0
	 */
	public static final int EDGE_REPEAT = 3;
	
	/**
	 * Repeats the pixels at the outer most edge of the image.
	 */
	public static final int EDGE_CONTINUE = 4;
	
	private float[][] kernel;
	private int edgeCondition;
	
	/**
	 * @param kernel the convolution matrix to use
	 * @since 1.0.0
	 */
	public ConvolutionFilter(float[][] kernel) {
		
		this.kernel = kernel;
	}
	
	/**
	 * @param kernel the convolution matrix to use
	 * @param edgeCondition what should happen when pixel outside the image are needed?
	 * @since 1.0.0
	 */
	public ConvolutionFilter(float[][] kernel, int edgeCondition) {
		
		this.kernel = kernel;
		this.edgeCondition = edgeCondition;
	}
	
	@Override
	public void accept(int[][] source, int[][] target) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		int kernelWidth = this.kernel.length;
		int kernelHeight = this.kernel[0].length;
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				int srcPixel = source[x][y];
				int[][] matrix = ConvolutionFilter.__getMatrix(x, y, kernelWidth, kernelHeight, source, this.edgeCondition);
				
				if(matrix != null) {
					
					float redFloat = 0.0F;
					float greenFloat = 0.0F;
					float blueFloat = 0.0F;
					
					for(int matrixX = 0; matrixX < matrix.length; matrixX++) {
						
						for(int matrixY = 0; matrixY < matrix.length; matrixY++) {

							float factor = this.kernel[matrixX][matrixY];
							int pixel = matrix[matrixX][matrixY];
							
							redFloat += ColorUtils.getRed(pixel) * factor;
							greenFloat += ColorUtils.getGreen(pixel) * factor;
							blueFloat += ColorUtils.getBlue(pixel) * factor;
						}
					}
					
					int red = ColorUtils.truncate((int)redFloat);
					int green = ColorUtils.truncate((int)greenFloat);
					int blue = ColorUtils.truncate((int)blueFloat);
					int alpha = ColorUtils.getAlpha(srcPixel);

					target[x][y] = ColorUtils.getARGB(red, green, blue, alpha);
					
				} else {
					
					target[x][y] = srcPixel;
				}
			}
		}
	}
	
	private static final int[][] __getMatrix(int centerX, int centerY, int width, int height, int[][] source, int edgeCondition) {
		
		int imgWidth = source.length;
		int imgHeight = source[0].length;
		int[][] matrix = new int[width][height];
		int matrixX = 0;
		int matrixY = 0;
		int _centerX = ((width - 1) / 2);
		int _centerY = ((height - 1) / 2);
		int centerWidth = centerX + _centerX;
		int centerHeight = centerY + _centerY;
		
		for(int x = centerX - _centerX; x <= centerWidth; x++) {
			
			for(int y = centerY - _centerY; y <= centerHeight; y++) {
				
				if(SimpleImage.__inBounds(x, y, 0, 0, imgWidth, imgHeight)) {
					
					matrix[matrixX][matrixY] = source[x][y];
					
				} else if(edgeCondition == ConvolutionFilter.EDGE_ZERO_FILL) {
					
					return new int[width][height];
					
				} else if(edgeCondition == ConvolutionFilter.EDGE_FROM_SOURCE) {
					
					return null;
					
				} else if(edgeCondition == ConvolutionFilter.EDGE_REPEAT) {
					
					int srcX = x;
					int srcY = y;
					
					if(srcX >= imgWidth) {
						
						srcX -= imgWidth;
						
					} else if(srcX < 0) {
						
						srcX = imgWidth + srcX;
					}
					
					if(srcY >= imgHeight) {
						
						srcY -= imgHeight;
						
					} else if(srcY < 0) {
						
						srcY = imgHeight + srcY;
					}
					
					matrix[matrixX][matrixY] = source[srcX][srcY];
					
				} else if(edgeCondition == ConvolutionFilter.EDGE_CONTINUE) {
					
					int srcX = x;
					int srcY = y;
					
					if(srcX >= imgWidth) {
						
						srcX = imgWidth - 1;
						
					} else if(srcX < 0) {
						
						srcX = 0;
					}
					
					if(srcY >= imgHeight) {
						
						srcY = imgHeight - 1;
					
					} else if(srcY < 0) {
						
						srcY = 0;
					}
					
					matrix[matrixX][matrixY] = source[srcX][srcY];
				}
				
				matrixY++;
			}
			
			matrixY = 0;
			matrixX++;
		}
		
		return matrix;
	}
}
