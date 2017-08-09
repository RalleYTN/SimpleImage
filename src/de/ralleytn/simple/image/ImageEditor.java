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

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

/**
 * 
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public class ImageEditor {

	private SimpleImage image;
	private int color;
	
	/**
	 * 
	 * @param image
	 * @since 1.2.0
	 */
	public ImageEditor(SimpleImage image) {
		
		this.image = image;
	}
	
	/**
	 * 
	 * @param color
	 * @since 1.2.0
	 */
	public void setColor(int color) {
		
		this.color = color;
	}
	
	/**
	 * 
	 * @param color
	 * @since 1.2.0
	 */
	public void setColor(Color color) {
		
		this.color = color.getRGB();
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since 1.2.0
	 */
	public void drawRect(int x, int y, int width, int height) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since 1.2.0
	 */
	public void fillRect(int x, int y, int width, int height) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void drawSquare(int x, int y, int size) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void fillSquare(int x, int y, int size) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since 1.2.0
	 */
	public void drawOval(int x, int y, int width, int height) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since 1.2.0
	 */
	public void fillOval(int x, int y, int width, int height) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void drawCircle(int x, int y, int size) {
		
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void fillCircle(int x, int y, int size) {
		
		
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @since 1.2.0
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		
		
	}
	
	/**
	 * 
	 * @param points
	 * @since 1.2.0
	 */
	public void drawLine(Point[] points) {
		
		
	}
	
	/**
	 * 
	 * @param points
	 * @since 1.2.0
	 */
	public void drawPolygon(Point[] points) {
		
		
	}
	
	/**
	 * 
	 * @param points
	 * @since 1.2.0
	 */
	public void fillPolygon(Point[] points) {
		
		
	}
	
	/**
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @since 1.2.0
	 */
	public void drawString(String string, int x, int y) {
		
		
	}
	
	/**
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @since 1.2.0
	 */
	public void drawImage(Image image, int x, int y) {
		
		
	}
	
	/**
	 * 
	 * @return
	 * @since 1.2.0
	 */
	public int getColor() {
		
		return this.color;
	}
	
	/**
	 * 
	 * @return
	 * @since 1.2.0
	 */
	public SimpleImage getImage() {
		
		return this.image;
	}
}
