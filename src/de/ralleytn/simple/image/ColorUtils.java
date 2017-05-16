package de.ralleytn.simple.image;

/**
 * Provides methods for easier color manipulation.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ColorUtils {

	private ColorUtils() {}
	
	/**
	 * Makes sure that the given color channel is a value from 0 to 255.
	 * @param channel the channel to truncate
	 * @return the truncated channel
	 * @since 1.0.0
	 */
	public static final int truncate(int channel) {
		
		if(channel > 255) {
			
			channel = 255;
			
		} else if(channel < 0) {
			
			channel = 0;
		}
		
		return channel;
	}
	
	/**
	 * Extracts the alpha channel from a color.
	 * @param argb the color
	 * @return the alpha channel
	 * @since 1.0.0
	 */
	public static final int getAlpha(int argb) {
		
		return (argb >> 24) & 0xFF;
	}
	
	/**
	 * Extracts the red channel from a color.
	 * @param argb the color
	 * @return the red channel
	 * @since 1.0.0
	 */
	public static final int getRed(int argb) {
		
		return (argb >> 16) & 0xFF;
	}
	
	/**
	 * Extracts the green channel from a color.
	 * @param argb the color
	 * @return the green channel
	 * @since 1.0.0
	 */
	public static final int getGreen(int argb) {
		
		return (argb >> 8) & 0xFF;
	}
	
	/**
	 * Extracts the blue channel from a color.
	 * @param argb the color
	 * @return the blue channel
	 * @since 1.0.0
	 */
	public static final int getBlue(int argb) {
		
		return argb & 0xFF;
	}
	
	/**
	 * Builds a color.
	 * @param red red channel of the color(0 - 255)
	 * @param green green channel of the color(0 - 255)
	 * @param blue blue channel of the color(0 - 255)
	 * @param alpha alpha channel of the color(0 - 255)
	 * @return the resulting color
	 * @since 1.0.0
	 */
	public static final int getARGB(int red, int green, int blue, int alpha) {
		
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
	}
	
	/**
	 * Mixes to colors and returns the result.
	 * @param argb1 color to mix with another
	 * @param argb2 color with which the first color will be fixed
	 * @param ratio the ratio with which the first color will be mixed into the second
	 * @return the mixed color
	 * @since 1.0.0
	 */
	public static final int mix(int argb1, int argb2, float ratio) {
		
		float ratio2 = 1.0F - ratio;
		int alpha = (int)(((ColorUtils.getAlpha(argb1) * ratio) + (ColorUtils.getAlpha(argb2) * ratio2)) / 2);
		int red = (int)(((ColorUtils.getRed(argb1) * ratio) + (ColorUtils.getRed(argb2) * ratio2)) / 2);
		int green = (int)(((ColorUtils.getGreen(argb1) * ratio) + (ColorUtils.getGreen(argb2) * ratio2)) / 2);
		int blue = (int)(((ColorUtils.getBlue(argb1) * ratio) + (ColorUtils.getBlue(argb2) * ratio2)) / 2);
		return ColorUtils.getARGB(red, green, blue, alpha);
	}
	
	static final int __max(int red, int green, int blue) {
		
		return Math.max(Math.max(red, green), blue);
	}

	static final int __min(int red, int green, int blue) {
		
		return Math.min(Math.min(red, green), blue);
	}
	
	static final int __interpolateColorChannelBilinear(int pixelA, int pixelB, int pixelC, int pixelD, float diffX, float diffY, int channel) {
		
		float diffX2 = 1.0F - diffX;
		float diffY2 = 1.0F - diffY;
		
		int pixelAChannel = (pixelA >> channel) & 0xFF;
		int pixelBChannel = (pixelB >> channel) & 0xFF;
		int pixelCChannel = (pixelC >> channel) & 0xFF;
		int pixelDChannel = (pixelD >> channel) & 0xFF;
		
		float pixelAChannelPart = pixelAChannel * diffX2 * diffY2;
		float pixelBChannelPart = pixelBChannel * diffX * diffY2;
		float pixelCChannelPart = pixelCChannel * diffX2 * diffY;
		float pixelDChannelPart = pixelDChannel * diffX * diffY;
		
		return (int)(pixelAChannelPart + pixelBChannelPart + pixelCChannelPart + pixelDChannelPart);
	}
}