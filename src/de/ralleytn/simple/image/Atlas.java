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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an image atlas. Can be pretty helpful if you do something with animations, spritesheets, etc.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.1.0
 * @since 1.1.0
 */
public final class Atlas {

	private final String name;
	private final SimpleImage baseImage;
	private List<Entry> entries;
	
	Atlas(String name, SimpleImage baseImage) {
		
		this.name = name;
		this.baseImage = baseImage;
		this.entries = new ArrayList<>();
	}
	
	/**
	 * Removes the entry with the given name
	 * @param name name of the entry which should be removed
	 * @since 1.1.0
	 */
	public void remove(String name) {
		
		List<Entry> entries = new ArrayList<>();
		this.entries.forEach(entry -> {
			
			if(!entry.getName().equals(name)) {
				
				entries.add(entry);
			}
		});
		
		this.entries = entries;
	}
	
	/**
	 * Removes the entry with the given index
	 * @param index index of the entry which should be removed
	 * @since 1.1.0
	 */
	public void remove(int index) {
		
		List<Entry> entries = new ArrayList<>();
		
		for(int i = 0; i < this.entries.size(); i++) {
			
			if(i != index) {
				
				entries.add(this.entries.get(i));
			}
		}
		
		this.entries = entries;
	}
	
	/**
	 * Adds a new entry.
	 * @param name the entry's name
	 * @param bounds bounds of the entry
	 * @throws IllegalArgumentException If an entry with the given name already exists
	 * @since 1.1.0
	 */
	public void add(String name, Rectangle bounds) throws IllegalArgumentException {
		
		this.add(name, bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	/**
	 * Adds a new entry.
	 * @param name the entry's name
	 * @param positionn position of the entry
	 * @param size size of the entry
	 * @throws IllegalArgumentException If an entry with the given name already exists
	 * @since 1.1.0
	 */
	public void add(String name, Point position, Dimension size) throws IllegalArgumentException {
		
		this.add(name, position.x, position.y, size.width, size.height);
	}
	
	/**
	 * Adds a new entry.
	 * @param name the entry's name
	 * @param position position of the entry
	 * @param width height of the entry
	 * @param height width of the entry
	 * @throws IllegalArgumentException If an entry with the given name already exists
	 * @since 1.1.0
	 */
	public void add(String name, Point position, int width, int height) throws IllegalArgumentException {
		
		this.add(name, position.x, position.y, width, height);
	}
	
	/**
	 * Adds a new entry.
	 * @param name the entry's name
	 * @param x X position of the entry
	 * @param y Y position of the entry
	 * @param size size of the entry
	 * @throws IllegalArgumentException If an entry with the given name already exists
	 * @since 1.1.0
	 */
	public void add(String name, int x, int y, Dimension size) throws IllegalArgumentException {
		
		this.add(name, x, y, size.width, size.height);
	}
	
	/**
	 * Adds a new entry.
	 * @param name the entry's name
	 * @param x X position of the entry
	 * @param y Y position of the entry
	 * @param width width of the entry
	 * @param height height of the entry
	 * @throws IllegalArgumentException If an entry with the given name already exists
	 * @since 1.1.0
	 */
	public void add(String name, int x, int y, int width, int height) throws IllegalArgumentException {
		
		for(Entry entry : this.entries) {
			
			if(entry.getName().equals(name)) {
				
				throw new IllegalArgumentException("The atlas already has an entry with the name '" + name + "'!");
			}
		}
		
		this.entries.add(new Entry(name, x, y, width, height, this.baseImage.crop(x, y, width, height), this));
	}
	
	/**
	 * Destroys this atlas.
	 * @since 1.1.0
	 */
	public void destroy() {
		
		this.entries.clear();
		this.baseImage.atlases.remove(this.name);
	}
	
	/**
	 * @return the number of entries this atlas has
	 * @since 1.1.0
	 */
	public int getNumberOfEntries() {
		
		return this.entries.size();
	}
	
	/**
	 * @param name name of the entry you want to get
	 * @return the entry with the given name
	 * @since 1.1.0
	 */
	public Entry get(String name) {
		
		for(Entry entry : this.entries) {
			
			if(entry.getName().equals(name)) {
				
				return entry;
			}
		}
		
		return null;
	}
	
	/**
	 * @param index index of the entry you want to get
	 * @return the entry at the given index
	 * @since 1.1.0
	 */
	public Entry get(int index) {
		
		return this.entries.get(index);
	}
	
	/**
	 * @return the parent image of this atlas
	 * @since 1.1.0
	 */
	public SimpleImage getImage() {
		
		return this.baseImage;
	}
	
	/**
	 * @return the name of this atlas
	 * @since 1.1.0
	 */
	public final String getName() {
		
		return this.name;
	}
	
	/**
	 * Represents an atlas entry.
	 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
	 * @version 1.1.0
	 * @since 1.1.0
	 */
	public static final class Entry {
		
		private final int x;
		private final int y;
		private final int width;
		private final int height;
		private final String name;
		private final SimpleImage baseImage;
		private final Atlas atlas;
		
		private Entry(String name, int x, int y, int width, int height, SimpleImage baseImage, Atlas atlas) {
			
			this.name = name;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.baseImage = baseImage;
			this.atlas = atlas;
		}
		
		/**
		 * @return this entry's name
		 * @since 1.1.0
		 */
		public final String getName() {
			
			return this.name;
		}
		
		/**
		 * @return the X position of this entry
		 * @since 1.1.0
		 */
		public final int getX() {
			
			return this.x;
		}
		
		/**
		 * @return the Y position of this entry
		 * @since 1.1.0
		 */
		public final int getY() {
			
			return this.y;
		}
		
		/**
		 * @return the width of this entry
		 * @since 1.1.0
		 */
		public final int getWidth() {
			
			return this.width;
		}
		
		/**
		 * @return the height of this entry
		 * @since 1.1.0
		 */
		public final int getHeight() {
			
			return this.height;
		}
		
		/**
		 * Crops out a sub image of the base image and returns it.
		 * @return the cropped image
		 * @since 1.1.0
		 */
		public final SimpleImage getImage() {
			
			return this.baseImage.crop(this.x, this.y, this.width, this.height);
		}
		
		/**
		 * @return the parent atlas
		 * @since 1.1.0
		 */
		public final Atlas getAtlas() {
			
			return this.atlas;
		}
	}
}
