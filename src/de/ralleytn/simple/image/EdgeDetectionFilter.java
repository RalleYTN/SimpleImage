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
