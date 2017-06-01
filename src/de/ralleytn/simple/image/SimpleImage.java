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

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Represents an image. The image data is saved in a two dimensional integer array.
 * One integer saves a pixel with the color model ARGB. No hardware acceleration is possible with this class.
 * All calculations are done on the CPU. This ensures that all images regardless of their color model are
 * equally fast. There also is no useless over head. Doing image processing is no problem for this class
 * as it already contains a good amount of pre written methods.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.1.1
 * @since 1.0.0
 */
public class SimpleImage {

	/**
	 * Used for {@link #mirror(int)}. Represents the X axis of an image.
	 * @since 1.0.0
	 */
	public static final int AXIS_X = 0;
	
	/**
	 * Used for {@link #mirror(int)}. Represents the Y axis of an image.
	 * @since 1.0.0
	 */
	public static final int AXIS_Y = 1;
	
	/**
	 * Used for {@link #scale(int, int, ScaleAlgorithm)}, {@link #scaleByFactor(float, ScaleAlgorithm)} and {@link #scaleToFit(int, int, ScaleAlgorithm)}.
	 * A simple and really fast scaling algorithm that produces images with low quality. Recommended for pixel arts or thumbnails.
	 * @since 1.0.0
	 */
	public static final ScaleAlgorithm SCALE_NEAREST_NEIGHBOUR = (source, target) -> {
		
		int srcWidth = source.length;
		int srcHeight = source[0].length;
		int targetWidth = target.length;
		int targetHeight = target[0].length;
		
		for(int targetX = 0; targetX < targetWidth; targetX++) {
			
			for(int targetY = 0; targetY < targetHeight; targetY++) {
				
				int srcX = targetX * srcWidth / targetWidth;
				int srcY = targetY * srcHeight / targetHeight;
				
				target[targetX][targetY] = source[srcX][srcY];
			}
		}
	};
	
	/**
	 * Used for {@link #scale(int, int, ScaleAlgorithm)}, {@link #scaleByFactor(float, ScaleAlgorithm)} and {@link #scaleToFit(int, int, ScaleAlgorithm)}.
	 * A bit more complex than {@link #SCALE_NEAREST_NEIGHBOUR} causing it to be slower, but the quality of resulting images is better.
	 * @since 1.0.0
	 */
	public static final ScaleAlgorithm SCALE_BILINEAR_INTERPOLATION = (source, target) -> {
		
		int srcWidth = source.length;
		int srcHeight = source[0].length;
		int targetWidth = target.length;
		int targetHeight = target[0].length;
		
		float ratioX = (float)srcWidth / (float)targetWidth;
		float ratioY = (float)srcHeight / (float)targetHeight;
		
		for(int targetX = 0; targetX < targetWidth; targetX++) {
			
			for(int targetY = 0; targetY < targetHeight; targetY++) {
				
				int srcX1 = (int)(ratioX * targetX);
				int srcX2 = srcX1 + 1;
				int srcY1 = (int)(ratioY * targetY);
				int srcY2 = srcY1 + 1;
				
				float diffX = (ratioX * targetX) - srcX1;
	            float diffY = (ratioY * targetY) - srcY1;

				int pixelA = source[srcX1][srcY1];
				int pixelB = SimpleImage.__inBounds(srcX2, srcY1, 0, 0, srcWidth, srcHeight) ? source[srcX2][srcY1] : pixelA;
				int pixelC = SimpleImage.__inBounds(srcX1, srcY2, 0, 0, srcWidth, srcHeight) ? source[srcX1][srcY2] : pixelA;
				int pixelD = SimpleImage.__inBounds(srcX2, srcY2, 0, 0, srcWidth, srcHeight) ? source[srcX2][srcY2] : pixelA;

				int alpha = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 24);
				int red = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 16);
				int green = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 8);
				int blue = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 0);
				
				target[targetX][targetY] = ColorUtils.getARGB(red, green, blue, alpha);
			}
		}
	};
	
	/**
	 * Used for {@link #rotate(double, RotationAlgorithm)}, {@link #rotate(double, Dimension, RotationAlgorithm)}, {@link #rotate(double, int, int, int, int, RotationAlgorithm)} and {@link #rotate(double, Dimension, Point, RotationAlgorithm)}.
	 * Not as fast as {@link #ROTATE_NEAREST_NEIGHBOUR} but it produces better results.
	 * @since 1.1.0
	 */
	public static final RotationAlgorithm ROTATE_BILINEAR_INTERPOLATION = (source, target, degrees, rotationCenterX, rotationCenterY) -> {
		
		int srcWidth = source.length;
		int srcHeight = source[0].length;
		double radians = Math.toRadians(degrees);
		int targetWidth = target.length;
		int targetHeight = target[0].length;
		
		for(int targetX = 0; targetX < targetWidth; targetX++) {
			
			for(int targetY = 0; targetY < targetHeight; targetY++) {
				
				double srcY = rotationCenterX - ((rotationCenterY - targetY) * Math.cos(radians)) + ((rotationCenterY - targetX) * Math.sin(radians));
				double srcX = rotationCenterY - ((rotationCenterY - targetY) * Math.sin(radians)) - ((rotationCenterX - targetX) * Math.cos(radians));

				int x1 = (int)srcX;
				int y1 = (int)srcY;
				
				if(SimpleImage.__inBounds(x1, y1, 0, 0, srcWidth, srcHeight)) {
					
					int x2 = (int)srcX + 1;
					int y2 = (int)srcY + 1;
					
					float diffX = (float)(srcX - (int)srcX);
					float diffY = (float)(srcY - (int)srcY);
					
					int pixelA = source[x1][y1];
					int pixelB = SimpleImage.__inBounds(x2, y1, 0, 0, srcWidth, srcHeight) ? source[x2][y1] : pixelA;
					int pixelC = SimpleImage.__inBounds(x1, y2, 0, 0, srcWidth, srcHeight) ? source[x1][y2] : pixelA;
					int pixelD = SimpleImage.__inBounds(x2, y2, 0, 0, srcWidth, srcHeight) ? source[x2][y2] : pixelA;
					
					int alpha = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 24);
					int red = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 16);
					int green = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 8);
					int blue = ColorUtils.__interpolateColorChannelBilinear(pixelA, pixelB, pixelC, pixelD, diffX, diffY, 0);
					
					target[targetX][targetY] = ColorUtils.getARGB(red, green, blue, alpha);
				}
			}
		}
	};
	
	/**
	 * Used for {@link #rotate(double, RotationAlgorithm)}, {@link #rotate(double, Dimension, RotationAlgorithm)}, {@link #rotate(double, int, int, int, int, RotationAlgorithm)} and {@link #rotate(double, Dimension, Point, RotationAlgorithm)}.
	 * Fast rotation algorithm.
	 * @since 1.1.0
	 */
	public static final RotationAlgorithm ROTATE_NEAREST_NEIGHBOUR = (source, target, degrees, rotationCenterX, rotationCenterY) -> {
		
		int srcWidth = source.length;
		int srcHeight = source[0].length;
		double radians = Math.toRadians(degrees);
		int targetWidth = target.length;
		int targetHeight = target[0].length;
		
		for(int targetX = 0; targetX < targetWidth; targetX++) {
			
			for(int targetY = 0; targetY < targetHeight; targetY++) {
				
				int srcY = (int)(rotationCenterX - ((rotationCenterY - targetY) * Math.cos(radians)) + ((rotationCenterY - targetX) * Math.sin(radians)));
				int srcX = (int)(rotationCenterY - ((rotationCenterY - targetY) * Math.sin(radians)) - ((rotationCenterX - targetX) * Math.cos(radians)));

				if(SimpleImage.__inBounds(srcX, srcY, 0, 0, srcWidth, srcHeight)) {

					target[targetX][targetY] = source[srcX][srcY];
				}
			}
		}
	};

	private int[][] data;
	final Map<String, Atlas> atlases = new HashMap<>();
	
	/**
	 * Takes a screenshot and initializes the instance with it's data.
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when {@link java.awt.GraphicsEnvironment#isHeadless()} returns {@code true}
	 * @since 1.0.0
	 */
	public SimpleImage() throws AWTException {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SimpleImage.__read(new Robot().createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height)));
	}
	
	/**
	 * Wraps already existing data into an instance of {@linkplain SimpleImage}.
	 * if one of the arrays on the 2nd dimension is too short or too long, errors may occur.
	 * @param data the already existing image data
	 * @since 1.0.0
	 */
	public SimpleImage(int[][] data) {
		
		if(data.length < 1 || data[0].length < 1) {

			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
		
		this.data = data;
	}
	
	/**
	 * Creates a new image with the given size.
	 * @param width width of the image
	 * @param height height of the image
	 * @since 1.0.0
	 */
	public SimpleImage(int width, int height) {
		
		if(width < 1 || height < 1) {
			
			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
		
		this.data = new int[width][height];
	}
	
	/**
	 * Wraps an instance of {@linkplain Image} into a new instance of {@linkplain SimpleImage}.
	 * @param image {@linkplain Image} to wrap
	 * @since 1.0.0
	 */
	public SimpleImage(Image image) {
		
		this.data = SimpleImage.__read(image);
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(String file) throws IOException {
		
		try(InputStream inputStream = new FileInputStream(new File(file))) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(Path file) throws IOException {
		
		try(InputStream inputStream = Files.newInputStream(file)) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(File file) throws IOException {
		
		try(InputStream inputStream = new FileInputStream(file)) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from an URL.
	 * @param url object representing the URL
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(URL url) throws IOException {
		
		try(InputStream inputStream = url.openStream()) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from an URI.
	 * @param uri object representing the URI
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(URI uri) throws IOException {
		
		try(InputStream inputStream = uri.toURL().openStream()) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(String zipFile, String entry) throws IOException {
		
		try(ZipFile zip = new ZipFile(zipFile);
			InputStream inputStream = zip.getInputStream(zip.getEntry(entry))) {
			
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(File zipFile, String entry) throws IOException {
		
		try(ZipFile zip = new ZipFile(zipFile);
			InputStream inputStream = zip.getInputStream(zip.getEntry(entry))) {
				
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(Path zipFile, String entry) throws IOException {
		
		try(ZipFile zip = new ZipFile(zipFile.toFile());
			InputStream inputStream = zip.getInputStream(zip.getEntry(entry))) {
				
			this.data = SimpleImage.__read(ImageIO.read(inputStream));
		}
	}
	
	/**
	 * Loads an image from an input stream.
	 * The input stream will not be closed automatically. The developer has to do it himself.
	 * @param inputStream the input stream to read the data from
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImage(InputStream inputStream) throws IOException {
		
		this.data = SimpleImage.__read(ImageIO.read(inputStream));
	}
	
	/**
	 * Allows you to use the {@linkplain Graphics2D} class to paint directly to the image.
	 * It is slower than editing the data array though.
	 * @param callback the callback function to use
	 * @since 1.0.0
	 */
	public void paint(Consumer<Graphics2D> callback) {
		
		BufferedImage image = this.toBufferedImage();
		Graphics2D graphics = image.createGraphics();
		callback.accept(graphics);
		graphics.dispose();
		SimpleImage.__read(image);
	}

	/**
	 * Writes the binary data of the image on an output stream.
	 * @param outputStream output stream to write the data on
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public void write(OutputStream outputStream) throws IOException {
		
		ImageIO.write(this.toBufferedImage(), "PNG", outputStream);
	}

	/**
	 * Writes the binary data of the image on an output stream.
	 * @param outputStream output stream to write the data on
	 * @param format image format to use
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public void write(OutputStream outputStream, String format) throws IOException {
		
		ImageIO.write(this.toBufferedImage(), format, outputStream);
	}
	
	/**
	 * Writes the binary data of the image on an output stream.
	 * @param outputStream output stream to write the data on
	 * @param type the color model
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public void write(OutputStream outputStream, int type) throws IOException {
		
		ImageIO.write(this.toBufferedImage(type), "PNG", outputStream);
	}

	/**
	 * Writes the binary data of the image on an output stream.
	 * @param outputStream output stream to write the data on
	 * @param format image format to use
	 * @param type the color model
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public void write(OutputStream outputStream, String format, int type) throws IOException {
		
		ImageIO.write(this.toBufferedImage(type), format, outputStream);
	}
	
	/**
	 * Sets the pixel on the given position.
	 * @param position target position
	 * @param pixel new color of the pixel
	 * @since 1.0.0
	 */
	public void setPixel(Point position, int pixel) {
		
		this.setPixel(position.x, position.y, pixel);
	}
	
	/**
	 * Sets the pixel on the given position.
	 * @param x target X position
	 * @param y target Y position
	 * @param pixel new color of the pixel
	 * @since 1.0.0
	 */
	public void setPixel(int x, int y, int pixel) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		
		if(SimpleImage.__inBounds(x, y, 0, 0, imgWidth, imgHeight)) {
			
			this.data[x][y] = pixel;
		}
	}
	
	/**
	 * Mirrors the image on the given axis.
	 * @param axis axis to mirror the image on
	 * @return the mirrored image
	 * @since 1.0.0
	 */
	public SimpleImage mirror(int axis) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		int[][] target = new int[imgWidth][imgHeight];
		int arrayX = imgWidth - 1;
		int arrayY = imgHeight - 1;
		
		if(axis == SimpleImage.AXIS_Y) {
			
			for(int x = 0; x < imgWidth; x++) {
				
				for(int y = 0; y < imgHeight; y++) {
					
					target[arrayX][arrayY] = this.data[x][arrayY];
					arrayY--;
				}
				
				arrayY = imgHeight -1;
				arrayX--;
			}
			
		} else if(axis == SimpleImage.AXIS_X) {
			
			for(int x = 0; x < imgWidth; x++) {
				
				for(int y = 0; y < imgHeight; y++) {
					
					target[arrayX][arrayY] = this.data[arrayX][y];
					arrayY--;
				}
				
				arrayY = imgHeight -1;
				arrayX--;
			}
		}
		
		return new SimpleImage(target);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @return the rotated image
	 * @since 1.0.0
	 */
	public SimpleImage rotate(double degrees) {
		
		int rotationCenterX = this.data.length / 2;
		int rotationCenterY = this.data[0].length / 2;
		
		return this.rotate(degrees, this.data.length, this.data[0].length, rotationCenterX, rotationCenterY, SimpleImage.ROTATE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param algorithm algorithm with which the image should rotate
	 * @return the rotated image
	 * @since 1.1.0
	 */
	public SimpleImage rotate(double degrees, RotationAlgorithm algorithm) {
		
		int rotationCenterX = this.data.length / 2;
		int rotationCenterY = this.data[0].length / 2;
		
		return this.rotate(degrees, this.data.length, this.data[0].length, rotationCenterX, rotationCenterY, algorithm);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param size size of the resulting image
	 * @return the rotated image
	 * @throws IllegalArgumentException If the size is smaller than 1x1
	 * @since 1.0.0
	 */
	public SimpleImage rotate(double degrees, Dimension size) throws IllegalArgumentException {
		
		int rotationCenterX = this.data.length / 2;
		int rotationCenterY = this.data[0].length / 2;
		
		return this.rotate(degrees, size.width, size.height, rotationCenterX, rotationCenterY, SimpleImage.ROTATE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param size size of the resulting image
	 * @param algorithm algorithm with which the image should rotate
	 * @return the rotated image
	 * @throws IllegalArgumentException If the size is smaller than 1x1
	 * @since 1.1.0
	 */
	public SimpleImage rotate(double degrees, Dimension size, RotationAlgorithm algorithm) throws IllegalArgumentException {
		
		int rotationCenterX = this.data.length / 2;
		int rotationCenterY = this.data[0].length / 2;
		
		return this.rotate(degrees, size.width, size.height, rotationCenterX, rotationCenterY, algorithm);
	}
	
	
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param rotationCenter center of rotation
	 * @return the rotated image
	 * @since 1.0.0
	 */
	public SimpleImage rotate(double degrees, Point rotationCenter) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		
		return this.rotate(degrees, imgWidth, imgHeight, rotationCenter.x, rotationCenter.y, SimpleImage.ROTATE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param rotationCenter center of rotation
	 * @param algorithm algorithm with which the image should rotate
	 * @return the rotated image
	 * @since 1.1.0
	 */
	public SimpleImage rotate(double degrees, Point rotationCenter, RotationAlgorithm algorithm) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		
		return this.rotate(degrees, imgWidth, imgHeight, rotationCenter.x, rotationCenter.y, algorithm);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param size size of the resulting image
	 * @param rotationCenter center of rotation
	 * @return the rotated image
	 * @throws IllegalArgumentException If the size is smaller than 1x1
	 * @since 1.0.0
	 */
	public SimpleImage rotate(double degrees, Dimension size, Point rotationCenter) throws IllegalArgumentException {
		
		return this.rotate(degrees, size.width, size.height, rotationCenter.x, rotationCenter.y, SimpleImage.ROTATE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param size size of the resulting image
	 * @param rotationCenter center of rotation
	 * @param algorithm algorithm with which the image should rotate
	 * @throws IllegalArgumentException If the size is 1x1
	 * @return the rotated image
	 * @since 1.1.0
	 */
	public SimpleImage rotate(double degrees, Dimension size, Point rotationCenter, RotationAlgorithm algorithm) throws IllegalArgumentException {
		
		return this.rotate(degrees, size.width, size.height, rotationCenter.x, rotationCenter.y, algorithm);
	}
	
	/**
	 * Rotates the image.
	 * @param degrees degrees by which the image should rotate
	 * @param width width of the resulting image
	 * @param height height of the resulting image
	 * @param rotationCenterX X position of the rotation center
	 * @param rotationCenterY Y position of the rotation center
	 * @throws IllegalArgumentException If the size is smaller than 1x1
	 * @return the rotated image
	 * @since 1.0.0
	 */
    public SimpleImage rotate(double degrees, int width, int height, int rotationCenterX, int rotationCenterY) throws IllegalArgumentException {
        
		return this.rotate(degrees, width, height, rotationCenterX, rotationCenterY, SimpleImage.ROTATE_NEAREST_NEIGHBOUR);
    }
    
    /**
     * Rotates the image.
     * @param degrees degrees by which the image should rotate
     * @param width width of the resulting image
     * @param height height of the resulting image
     * @param rotationCenterX X position of the rotation center
     * @param rotationCenterY Y position of the rotation center
     * @param algorithm algorithm with which the image should rotate
     * @throws IllegalArgumentException If the size is smaller than 1x1
     * @return the rotated image
     * @since 1.1.0
     */
    public SimpleImage rotate(double degrees, int width, int height, int rotationCenterX, int rotationCenterY, RotationAlgorithm algorithm) throws IllegalArgumentException {
    	
		if(width < 1 || height < 1) {
			
			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
    	
    	int[][] target = new int[width][height];
    	algorithm.calc(this.data, target, degrees, rotationCenterX, rotationCenterY);
    	
    	return new SimpleImage(target);
    }
    
    /**
     * Applies a filter to the image.
     * @param filter filter to use
     * @param x X position on which the filter starts
     * @param y Y position on which the filter starts
     * @param width width of the area the filter should work in
     * @param height height of the area the filter should work in
     * @return the filtered image
     * @since 1.1.0
     */
    public SimpleImage filter(Filter filter, int x, int y, int width, int height) {
    	
    	int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		int[][] target = new int[imgWidth][imgHeight];
		filter.setBounds(new Rectangle(x, y, width, height));
		filter.apply(this.data, target);
		
		return new SimpleImage(target);
    }
	
	/**
	 * Applies a filter to the image.
	 * @param filter filter to use
	 * @return the filtered image
	 * @since 1.0.0
	 */
	public SimpleImage filter(Filter filter) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		
		return this.filter(filter, 0, 0, imgWidth, imgHeight);
	}
	
	/**
	 * Scales the image by the given factor.
	 * @param factor factor by which the image should be scaled
	 * @return the scaled image
	 * @since 1.0.0
	 */
	public SimpleImage scaleByFactor(float factor) {
		
		return this.scaleByFactor(factor, SimpleImage.SCALE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Scales the image by the given factor.
	 * @param factor factor by which the image should be scaled
	 * @param algorithm algorithm to scale the image with
	 * @return the scaled image
	 * @since 1.0.0
	 */
	public SimpleImage scaleByFactor(float factor, ScaleAlgorithm algorithm) {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		int scaledWidth = (int)(imgWidth * factor);
		int scaledHeight = (int)(imgHeight * factor);
		
		if(scaledWidth < 1) {
			
			scaledWidth = 1;
		}
		
		if(scaledHeight < 1) {
			
			scaledHeight = 1;
		}
		
		return this.scale(scaledWidth, scaledHeight, algorithm);
	}
	
	/**
	 * Scales the image proportional to fit the given size.
	 * @param width maximum width of the resulting image
	 * @param height maximum height of the resulting image
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scaleToFit(int width, int height) throws IllegalArgumentException {
		
		return this.scaleToFit(width, height, SimpleImage.SCALE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Scales the image proportional to fit the given size.
	 * @param size maximum size of the resulting image
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scaleToFit(Dimension size) throws IllegalArgumentException {
		
		return this.scaleToFit(size.width, size.height, SimpleImage.SCALE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Scales the image proportional to fit the given size.
	 * @param size maximum size of the resulting image
	 * @param algorithm algorithm to scale the image with
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scaleToFit(Dimension size, ScaleAlgorithm algorithm) throws IllegalArgumentException {
		
		return this.scaleToFit(size.width, size.height, algorithm);
	}
	
	/**
	 * Scales the image proportional to fit the given size.
	 * @param width maximum width of the resulting image
	 * @param height maximum height of the resulting image
	 * @param algorithm algorithm to scale the image with
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scaleToFit(int width, int height, ScaleAlgorithm algorithm) throws IllegalArgumentException {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		
		float scaledWidth = (float)imgWidth / width;
        float scaledHeight = (float)imgHeight / height;
        
        if(scaledWidth > scaledHeight) {
            
            scaledHeight = imgHeight / scaledWidth;
            scaledWidth = width;
            
        } else if(scaledHeight > scaledWidth) {
            
            scaledWidth = imgWidth / scaledHeight;
            scaledHeight = height;
        }
        
        return this.scale((int)scaledWidth, (int)scaledHeight, algorithm);
	}

	/**
	 * Scales the image to the given size.
	 * @param width width of the resulting image
	 * @param height height of the resulting image
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scale(int width, int height) throws IllegalArgumentException {
		
		return this.scale(width, height, SimpleImage.SCALE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Scales the image to the given size.
	 * @param size size of the resulting image
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scale(Dimension size) throws IllegalArgumentException {
		
		return this.scale(size.width, size.height, SimpleImage.SCALE_NEAREST_NEIGHBOUR);
	}
	
	/**
	 * Scales the image to the given size.
	 * @param size size of the resulting image
	 * @param algorithm algorithm to scale the image with
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scale(Dimension size, ScaleAlgorithm algorithm) throws IllegalArgumentException {
		
		return this.scale(size.width, size.height, algorithm);
	}

	/**
	 * Scales the image to the given size.
	 * @param width width of the resulting image
	 * @param height height of the resulting image
	 * @param algorithm algorithm to scale the image with
	 * @return the scaled image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage scale(int width, int height, ScaleAlgorithm algorithm) throws IllegalArgumentException {
		
		if(width < 1 || height < 1) {
			
			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
		
		int[][] target = new int[width][height];
		algorithm.calc(this.data, target);
		return new SimpleImage(target);
	}
	
	/**
	 * Cuts out a part of the image and deletes it in the source image.
	 * @param position position of the part which should be cut out
	 * @param width width of the part which should be cut out
	 * @param height height of the part which should be cut out
	 * @return the cut out image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage cut(Point position, int width, int height) throws IllegalArgumentException {
		
		return this.cut(position.x, position.y, width, height);
	}
	
	/**
	 * Cuts out a part of the image and deletes it in the source image.
	 * @param x X position of the part which should be cut out
	 * @param y Y position of the part which should be cut out
	 * @param size size of the part which should be cut out
	 * @return the cut out image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage cut(int x, int y, Dimension size) throws IllegalArgumentException {
		
		return this.cut(x, y, size.width, size.height);
	}
	
	/**
	 * Cuts out a part of the image and deletes it in the source image.
	 * @param position position of the part which should be cut out
	 * @param size size of the part which should be cut out
	 * @return the cut out image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage cut(Point position, Dimension size) throws IllegalArgumentException {
		
		return this.cut(position.x, position.y, size.width, size.height);
	}
	
	/**
	 * Cuts out a part of the image and deletes it in the source image.
	 * @param bounds bounds of the part which should be cut out
	 * @return the cut out image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage cut(Rectangle bounds) throws IllegalArgumentException {
		
		return this.cut(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	/**
	 * Cuts out a part of the image and deletes it in the source image.
	 * @param x X position of the part which should be cut out
	 * @param y Y position of the part which should be cut out
	 * @param width width of the part which should be cut out
	 * @param height height of the part which should be cut out
	 * @return the cut out image
	 * @throws IllegalArgumentException If width or height is smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage cut(int x, int y, int width, int height) throws IllegalArgumentException {
		
		if(width < 1 || height < 1) {
			
			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
		
		int[][] target = new int[width][height];
		int targetX = 0;
		int targetY = 0;
		int endX = x + width;
		int endY = y + height;
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;

		for(int currentX = x; currentX < endX; currentX++) {
			
			for(int currentY = y; currentY < endY; currentY++) {
			
				target[targetX][targetY] = SimpleImage.__inBounds(currentX, currentY, 0, 0, imgWidth, imgHeight) ? this.data[currentX][currentY] : 0x00000000;
				this.data[currentX][currentY] = 0x00000000;
				targetY++;
			}
			
			targetX++;
			targetY = 0;
		}
		
		return new SimpleImage(target);
	}
	
	/**
	 * Removes the pixels inside the given box.
	 * @param bounds bounds in which all pixels should be deleted
	 * @return the resulting image
	 * @since 1.1.0
	 */
	public SimpleImage delete(Rectangle bounds) {
		
		return this.delete(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	/**
	 * Removes the pixels inside the given box.
	 * @param position position of the part which should be deleted
	 * @param size size of the part which should be deleted
	 * @return the resulting image
	 * @since 1.1.0
	 */
	public SimpleImage delete(Point position, Dimension size) {
		
		return this.delete(position.x, position.y, size.width, size.height);
	}
	
	/**
	 * Removes the pixels inside the given box.
	 * @param position position of the part which should be deleted
	 * @param width width of the part which should be deleted
	 * @param height height of the part which should be deleted
	 * @return the resulting image
	 * @since 1.1.0
	 */
	public SimpleImage delete(Point position, int width, int height) {
		
		return this.delete(position.x, position.y, width, height);
	}
	
	/**
	 * Removes the pixels inside the given box.
	 * @param x X position of the part which should be deleted
	 * @param y Y position of the part which should be deleted
	 * @param size size of the part which should be deleted
	 * @return the resulting image
	 * @since 1.1.0
	 */
	public SimpleImage delete(int x, int y, Dimension size) {
		
		return this.delete(x, y, size.width, size.height);
	}
	
	/**
	 * Removes the pixels inside the given box.
	 * @param x X position of the part which should be deleted
	 * @param y Y position of the part which should be deleted
	 * @param width width of the part which should be deleted
	 * @param height height of the part which should be deleted
	 * @return the resulting image
	 * @since 1.0.0
	 */
	public SimpleImage delete(int x, int y, int width, int height) {
		
		int endX = x + width;
		int endY = y + height;
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		int[][] target = new int[imgWidth][imgHeight];

		for(int currentX = 0; currentX < imgWidth; currentX++) {
			
			for(int currentY = 0; currentY < imgHeight; currentY++) {
				
				if(!(currentX >= x && currentX < endX && currentY >= y && currentY < endY)) {
					
					target[currentX][currentY] = this.data[currentX][currentY];
				}
			}
		}
		
		return new SimpleImage(target);
	}
	
	/**
	 * Crops out the given box from the image.
	 * @param bounds bounds of the part which should be cropped
	 * @return the cropped image
	 * @throws IllegalArgumentException If width or height of the new image are smaller than 1
	 * @since 1.1.0
	 */
	public SimpleImage crop(Rectangle bounds) throws IllegalArgumentException {
		
		return this.crop(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	/**
	 * Crops out the given box from the image.
	 * @param position position of the part which should be cropped
	 * @param size size of the part which should be cropped
	 * @return the cropped image
	 * @throws IllegalArgumentException If width or height of the new image are smaller than 1
	 * @since 1.1.0
	 */
	public SimpleImage crop(Point position, Dimension size) throws IllegalArgumentException {
		
		return this.crop(position.x, position.y, size.width, size.height);
	}
	
	/**
	 * Crops out the given box from the image.
	 * @param x X position of the part which should be cropped
	 * @param y Y position of the part which should be cropped
	 * @param size size of the part which should be cropped
	 * @return the cropped image
	 * @throws IllegalArgumentException If width or height of the new image are smaller than 1
	 * @since 1.1.0
	 */
	public SimpleImage crop(int x, int y, Dimension size) throws IllegalArgumentException {
		
		return this.crop(x, y, size.width, size.height);
	}
	
	/**
	 * Crops out the given box from the image.
	 * @param position position of the part which should be cropped
	 * @param width width of the part which should be cropped
	 * @param height height of the part which should be cropped
	 * @return the cropped image
	 * @throws IllegalArgumentException If width or height of the new image are smaller than 1
	 * @since 1.1.0
	 */
	public SimpleImage crop(Point position, int width, int height) throws IllegalArgumentException {
		
		return this.crop(position.x, position.y, width, height);
	}

	/**
	 * Crops out the given box from the image.
	 * @param x X position of the part which should be cropped
	 * @param y Y position of the part which should be cropped
	 * @param width width of the part which should be cropped
	 * @param height height of the part which should be cropped
	 * @return the cropped image
	 * @throws IllegalArgumentException If width or height of the new image are smaller than 1
	 * @since 1.0.0
	 */
	public SimpleImage crop(int x, int y, int width, int height) throws IllegalArgumentException {
		
		if(width < 1 || height < 1) {
			
			throw new IllegalArgumentException("An image must be at least 1x1px of size!");
		}
		
		int[][] target = new int[width][height];
		int targetX = 0;
		int targetY = 0;
		int endX = x + width;
		int endY = y + height;
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;

		for(int currentX = x; currentX < endX; currentX++) {
			
			for(int currentY = y; currentY < endY; currentY++) {
				
				target[targetX][targetY] = currentX >= 0 && currentX < imgWidth && currentY >= 0 && currentY < imgHeight ? this.data[currentX][currentY] : 0x00000000;
				targetY++;
			}
			
			targetX++;
			targetY = 0;
		}
		
		return new SimpleImage(target);
	}

	/**
	 * Copies the image data into a new image. Atlases and other meta data will not be copied over.
	 * @return a copy of the image
	 * @since 1.0.0
	 */
	public SimpleImage copy() {
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		int[][] target = new int[imgWidth][imgHeight];
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {
				
				target[x][y] = this.data[x][y];
			}
		}
		
		return new SimpleImage(target);
	}
	
	/**
	 * @return the width of the image
	 * @since 1.0.0
	 */
	public int getWidth() {
		
		return this.data.length;
	}
	
	/**
	 * @return the height of the image
	 * @since 1.0.0
	 */
	public int getHeight() {
		
		return this.data[0].length;
	}
	
	/**
	 * @param x X position
	 * @param y Y position
	 * @return the pixel at the given position
	 * @since 1.0.0
	 */
	public int getPixel(int x, int y) {

		return this.data[x][y];
	}
	
	/**
	 * Wraps the image into a {@linkplain TransferableImage}.
	 * @return the {@linkplain TransferableImage} instance
	 * @since 1.1.0
	 */
	public TransferableImage toTransferableImage() {
		
		return this.toTransferableImage(BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Wraps the image into a {@linkplain TransferableImage}.
	 * @param type color model of the resulting image
	 * @return the {@linkplain TransferableImage} instance
	 * @since 1.1.0
	 */
	public TransferableImage toTransferableImage(int type) {
		
		return new TransferableImage(this.toBufferedImage(type));
	}
	
	/**
	 * Converts the image into an instance of {@linkplain BufferedImage}.
	 * @return an instance of {@linkplain BufferedImage}
	 * @since 1.0.0
	 */
	public BufferedImage toBufferedImage() {
		
		return this.toBufferedImage(BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Converts the image into an instance of {@linkplain BufferedImage}.
	 * @param type Type of the resulting {@linkplain BufferedImage}
	 * @return an instance of {@linkplain BufferedImage}
	 * @since 1.0.0
	 */
	public BufferedImage toBufferedImage(int type) {
		
		// BufferedImage#setRGB(x, y, rgb) destroys the hardware acceleration of the resulting image.
		// In order to preserve the hardware acceleration the image will be drawn onto the resulting image with the Graphics class.
		// Another reason is that I don't have to worry about color models.
		
		int imgWidth = this.data.length;
		int imgHeight = this.data[0].length;
		BufferedImage image = new BufferedImage(imgWidth, imgHeight, type);
		Graphics graphics = image.createGraphics();
		
		for(int x = 0; x < imgWidth; x++) {
			
			for(int y = 0; y < imgHeight; y++) {

				graphics.setColor(new Color(this.data[x][y], true));
				graphics.fillRect(x, y, 1, 1);
			}
		}
		
		graphics.dispose();
		return image;
	}
	
	/**
	 * Encodes the binary data of the image into Base64.
	 * @return the encoded data
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBase64() throws IOException {
		
		return this.toBase64("PNG", BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Encodes the binary data of the image into Base64.
	 * @param type the color model of the image
	 * @return the encoded data
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBase64(int type) throws IOException {
		
		return this.toBase64("PNG", type);
	}
	
	/**
	 * Encodes the binary data of the image into Base64.
	 * @param format image format
	 * @return the encoded data
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBase64(String format) throws IOException {
		
		return this.toBase64(format, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Encodes the binary data of the image into Base64.
	 * @param format image format
	 * @param type the color model of the image
	 * @return the encoded data
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBase64(String format, int type) throws IOException {
		
		return Base64.getEncoder().encode(this.toBinary(format, type));
	}
	
	/**
	 * @return the binary data of the image
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBinary() throws IOException {
		
		return this.toBinary("PNG", BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * @param type color model of the image
	 * @return the binary data of the image
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBinary(int type) throws IOException {
		
		return this.toBinary("PNG", type);
	}
	
	/**
	 * @param format image format
	 * @return the binary data of the image
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBinary(String format) throws IOException {
		
		return this.toBinary(format, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * @param format image format
	 * @param type the color model of the image
	 * @return the binary data of the image
	 * @throws IOException if an error occurs during writing.
	 * @since 1.0.0
	 */
	public byte[] toBinary(String format, int type) throws IOException {
		
		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			
			this.write(outputStream, format, type);
			return outputStream.toByteArray();
		}
	}
	
	/**
	 * @return the data array
	 * @since 1.0.0
	 */
	public int[][] getData() {
		
		return this.data;
	}
	
	/**
	 * Puts this image onto the clipboard.
	 * @since 1.1.0
	 */
	public void putOnClipboard() {
		
		this.putOnClipboard(BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Puts this image onto the clipboard.
	 * @param type color model of the resulting image
	 * @since 1.1.0
	 */
	public void putOnClipboard(int type) {
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(this.toTransferableImage(type), (clipboard, data) -> {});
	}
	
	/**
	 * Creates a new atlas.
	 * @param name name of the atlas
	 * @return the created atlas
	 * @throws IllegalArgumentException If an atlas with the given name already exists
	 * @since 1.1.0
	 */
	public Atlas createAtlas(String name) throws IllegalArgumentException {
		
		if(this.atlases.containsKey(name)) {
			
			throw new IllegalArgumentException("There already is an atlas with the name '" + name + "'!");
		}
		
		Atlas atlas = new Atlas(name, this);
		this.atlases.put(name, atlas);
		return atlas;
	}
	
	/**
	 * @param name name of the atlas
	 * @return the atlas with the given name
	 * @since 1.1.0
	 */
	public Atlas getAtlas(String name) {
		
		return this.atlases.get(name);
	}
	
	/**
	 * @return a list of all atlases for this image
	 * @since 1.1.0
	 */
	public List<Atlas> getAtlases() {
		
		List<Atlas> atlases = new ArrayList<>();
		this.atlases.forEach((name, atlas) -> atlases.add(atlas));
		return atlases;
	}
	
	/**
	 * @return the number of atlases this image has
	 * @since 1.1.0
	 */
	public int getNumberOfAtlases() {
		
		return this.atlases.size();
	}
	
	/**
	 * @return the image from the clipboard or {@code null} if there is no readable image on the clipboard.
	 * @throws UnsupportedFlavorException if the requested data flavor is not supported
	 * @throws IOException if the data is no longer available in the requested flavor
	 * @since 1.1.0
	 */
	public static final SimpleImage getClipboardImage() throws UnsupportedFlavorException, IOException {
		
		Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		SimpleImage image = null;
		
		if(contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			
			image = new SimpleImage((Image)contents.getTransferData(DataFlavor.imageFlavor));
		}
		
		return image;
	}
	
	/**
	 * Creates a {@linkplain JFrame} showing this image.
	 * This method exists for debugging purposes.
	 * @since 1.1.0
	 */
	public void show() {
		
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(this.toBufferedImage())));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private static final int[][] __read(Image image) {
		
		BufferedImage imageToRead = SimpleImage.__convert(image);
		int width = imageToRead.getWidth();
		int height = imageToRead.getHeight();
		int[][] data = new int[width][height];
		
		for(int x = 0; x < width; x++) {
			
			for(int y = 0; y < height; y++) {
				
				data[x][y] = imageToRead.getRGB(x, y);
			}
		}
		
		return data;
	}
	
	private static final BufferedImage __convert(Image image) {

		if(image instanceof BufferedImage) {
			
			BufferedImage convertedImage = (BufferedImage)image;
			
			if(convertedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
				
				return convertedImage;
			}
		}
		
		BufferedImage convertedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = convertedImage.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		
		return convertedImage;
	}
	
	static final boolean __inBounds(int posX, int posY, int boundsX, int boundsY, int boundsWidth, int boundsHeight) {
		
		return posX >= boundsX && posX < boundsX + boundsWidth && posY >= boundsY && posY < boundsY + boundsHeight;
	}
}
