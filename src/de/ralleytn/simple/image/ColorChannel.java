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
