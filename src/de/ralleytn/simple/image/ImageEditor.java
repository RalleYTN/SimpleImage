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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public class ImageEditor {

	private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, 0, 18);
	
	private SimpleImage image;
	private Font font;
	private int color;
	
	/**
	 * 
	 * @param image
	 * @since 1.2.0
	 */
	public ImageEditor(SimpleImage image) {
		
		this.image = image;
		this.font = ImageEditor.DEFAULT_FONT;
	}
	
	public void setImage(SimpleImage image) {
		
		this.image = image;
	}
	
	public void setImage(Image image) {
		
		this.image = new SimpleImage(image);
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
		
		this.color = color != null ? color.getRGB() : 0;
	}
	
	/**
	 * 
	 * @param font
	 * @since 1.2.0
	 */
	public void setFont(Font font) {
		
		this.font = font != null ? font : ImageEditor.DEFAULT_FONT;
	}
	
	/**
	 * 
	 * @param position
	 * @param width
	 * @param height
	 * @since 1.2.0
	 */
	public void drawRect(Point position, int width, int height) {
		
		this.drawRect(position.x, position.y, width, height);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void drawRect(int x, int y, Dimension size) {
		
		this.drawRect(x, y, size.width, size.height);
	}
	
	/**
	 * 
	 * @param position
	 * @param size
	 * @since 1.2.0
	 */
	public void drawRect(Point position, Dimension size) {
		
		this.drawRect(position.x, position.y, size.width, size.height);
	}
	
	/**
	 * 
	 * @param rect
	 * @since 1.2.0
	 */
	public void drawRect(Rectangle rect) {
		
		this.drawRect(rect.x, rect.y, rect.width, rect.height);
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
		
		int rightEnd = width - 1;
		int bottomEnd = height - 1;
		
		for(int _x = 0; _x < width; _x++) {
			
			for(int _y = 0; _y < height; _y++) {

				if(_x == 0 || _x == rightEnd || _y == 0 || _y == bottomEnd) {
					
					this.image.setPixel(x + _x, y + _y, this.color);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param position
	 * @param width
	 * @param height
	 * @param thickness
	 * @since 1.2.0
	 */
	public void drawRect(Point position, int width, int height, int thickness) {
		
		this.drawRect(position.x, position.y, width, height, thickness);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param thickness
	 * @since 1.2.0
	 */
	public void drawRect(int x, int y, Dimension size, int thickness) {
		
		this.drawRect(x, y, size.width, size.height, thickness);
	}
	
	/**
	 * 
	 * @param position
	 * @param size
	 * @param thickness
	 * @since 1.2.0
	 */
	public void drawRect(Point position, Dimension size, int thickness) {
		
		this.drawRect(position.x, position.y, size.width, size.height, thickness);
	}
	
	/**
	 * 
	 * @param rect
	 * @param thickness
	 * @since 1.2.0
	 */
	public void drawRect(Rectangle rect, int thickness) {
		
		this.drawRect(rect.x, rect.y, rect.width, rect.height, thickness);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param thickness
	 * @since 1.2.0
	 */
	public void drawRect(int x, int y, int width, int height, int thickness) {
		
		int rightEnd = width - 1;
		int bottomEnd = height - 1;
		
		for(int _x = 0; _x < width; _x++) {
			
			for(int _y = 0; _y < height; _y++) {

				if((_x >= 0 && _x <= thickness) || (_x >= rightEnd - thickness && _x <= rightEnd) || (_y >= 0 && _y <= thickness) || (_y >= bottomEnd - thickness && _y <= bottomEnd)) {
					
					this.image.setPixel(x + _x, y + _y, this.color);
				}
			}
		}
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
		
		for(int _x = 0; _x < width; _x++) {
			
			for(int _y = 0; _y < height; _y++) {

				this.image.setPixel(x + _x, y + _y, this.color);
			}
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void drawSquare(int x, int y, int size) {
		
		this.drawRect(x, y, size, size);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void fillSquare(int x, int y, int size) {
		
		this.fillRect(x, y, size, size);
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
		
		this.drawOval(x, y, size, size);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @since 1.2.0
	 */
	public void fillCircle(int x, int y, int size) {
		
		this.fillOval(x, y, size, size);
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
		
		if(x1 == x2) {
			
			int yStart = y2;
			int yEnd = y2;
			
			if(y1 < yStart) {
				
				yStart = y1;
				
			} else {
				
				yEnd = y1;
			}
			
			for(int y = yStart; y < yEnd; y++) {
				
				this.image.setPixel(x1, y, this.color);
			}
			
		} else if(y1 == y2) {
			
			int xStart = x2;
			int xEnd = x2;
			
			if(x1 < xStart) {
				
				xStart = x1;
				
			} else {
				
				xEnd = x1;
			}
			
			for(int x = xStart; x < xEnd; x++) {
				
				this.image.setPixel(x, y1, this.color);
			}
			
		} else {
			
			
		}
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
		
		BufferedImage bimg = SimpleImage.__convert(image);
		
		for(int _x = 0; _x < bimg.getWidth(); _x++) {
			
			for(int _y = 0; _y < bimg.getHeight(); _y++) {

				this.image.setPixel(x + _x, y + _y, bimg.getRGB(_x, _y));
			}
		}
	}
	
	/**
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @since 1.2.0
	 */
	public void drawImage(SimpleImage image, int x, int y) {
		
		this.drawImage(image, x, y, false);
	}
	
	public void drawImage(Image image, int x, int y, boolean alphaBlending) {
		
		BufferedImage bimg = SimpleImage.__convert(image);
		
		for(int _x = 0; _x < bimg.getWidth(); _x++) {
			
			for(int _y = 0; _y < bimg.getHeight(); _y++) {

				int pX = x + _x;
				int pY = y + _y;
				int fgPixel = bimg.getRGB(_x, _y);
				this.image.setPixel(pX, pY, alphaBlending ? ColorUtils.blend(this.image.getPixel(pX, pY), fgPixel) : fgPixel);
			}
		}
	}
	
	public void drawImage(SimpleImage image, int x, int y, boolean alphaBlending) {
		
		for(int _x = 0; _x < image.getWidth(); _x++) {
			
			for(int _y = 0; _y < image.getHeight(); _y++) {

				int pX = x + _x;
				int pY = y + _y;
				int fgPixel = image.getPixel(_x, _y);
				this.image.setPixel(pX, pY, alphaBlending ? ColorUtils.blend(this.image.getPixel(pX, pY), fgPixel) : fgPixel);
			}
		}
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
	public Font getFont() {
		
		return this.font;
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
