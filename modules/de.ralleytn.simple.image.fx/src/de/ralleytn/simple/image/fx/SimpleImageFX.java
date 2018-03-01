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

package de.ralleytn.simple.image.fx;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import de.ralleytn.simple.image.SimpleImage;
import javafx.embed.swing.SwingFXUtils;

/**
 * Extends {@linkplain SimpleImageFX} to support JavaFX.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleImageFX extends SimpleImage {

	/**
	 * Takes a screenshot and initializes the instance with it's data.
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when {@link java.awt.GraphicsEnvironment#isHeadless()} returns {@code true}
	 * @since 1.0.0
	 */
	public SimpleImageFX() throws AWTException {
		
		super();
	}
	
	/**
	 * Wraps a JavaFX image into an instance of {@linkplain SimpleImage}.
	 * @param image the JavaFX image
	 * @since 1.0.0
	 */
	public SimpleImageFX(javafx.scene.image.Image image) {
		
		super(SwingFXUtils.fromFXImage(image, null));
	}
	
	/**
	 * Wraps already existing data into an instance of {@linkplain SimpleImage}.
	 * if one of the arrays on the 2nd dimension is too short or too long, errors may occur.
	 * @param data the already existing image data
	 * @since 1.0.0
	 */
	public SimpleImageFX(int[][] data) {
		
		super(data);
	}
	
	/**
	 * Creates a new image with the given size.
	 * @param width width of the image
	 * @param height height of the image
	 * @since 1.0.0
	 */
	public SimpleImageFX(int width, int height) {
		
		super(width, height);
	}
	
	/**
	 * Wraps an instance of {@linkplain Image} into a new instance of {@linkplain SimpleImage}.
	 * @param image {@linkplain Image} to wrap
	 * @since 1.0.0
	 */
	public SimpleImageFX(Image image) {
		
		super(image);
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(String file) throws IOException {
		
		super(file);
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(Path file) throws IOException {
		
		super(file);
	}
	
	/**
	 * Loads an image from a file.
	 * @param file object representing the file
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(File file) throws IOException {
		
		super(file);
	}
	
	/**
	 * Loads an image from an URL.
	 * @param url object representing the URL
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(URL url) throws IOException {
		
		super(url);
	}
	
	/**
	 * Loads an image from an URI.
	 * @param uri object representing the URI
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(URI uri) throws IOException {
		
		super(uri);
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(String zipFile, String entry) throws IOException {
		
		super(zipFile, entry);
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(Path zipFile, String entry) throws IOException {
		
		super(zipFile, entry);
	}
	
	/**
	 * Loads an image from a zip file.
	 * @param zipFile object representig the zip file
	 * @param entry name of the zip entry
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(File zipFile, String entry) throws IOException {
		
		super(zipFile, entry);
	}
	
	/**
	 * Loads an image from an input stream.
	 * The input stream will not be closed automatically. The developer has to do it himself.
	 * @param inputStream the input stream to read the data from
	 * @throws IOException if an error occurs during reading.
	 * @since 1.0.0
	 */
	public SimpleImageFX(InputStream inputStream) throws IOException {
		
		super(inputStream);
	}
	
	/**
	 * Converts the image to an instance of {@linkplain javafx.scene.image.Image}.
	 * @return the JavaFX image
	 * @since 1.0.0
	 */
	public javafx.scene.image.Image toImageFX() {
		
		return SwingFXUtils.toFXImage(this.toBufferedImage(BufferedImage.TYPE_INT_ARGB), null);
	}
	
	/**
	 * Converts the image to an instance of {@linkplain javafx.scene.image.Image}.
	 * @param type the color model of the image
	 * @return the JavaFX image
	 * @since 1.0.0
	 */
	public javafx.scene.image.Image toImageFX(int type) {
		
		return SwingFXUtils.toFXImage(this.toBufferedImage(type), null);
	}
}
