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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Wrapper for {@linkplain Image}s in a {@linkplain Transferable}.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.1.0
 * @since 1.1.0
 */
public class TransferableImage implements Transferable {

	private final BufferedImage image;
	
	/**
	 * @param image the {@linkplain Image} instance to wrap
	 * @since 1.1.0
	 */
	public TransferableImage(Image image) {
		
		if(image instanceof BufferedImage) {
			
			this.image = (BufferedImage)image;
			
		} else {
			
			this.image = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = this.image.createGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		
		 if(flavor.equals(DataFlavor.imageFlavor) && this.image != null) {
	         
			 return this.image;
			 
         } else {
                
        	 throw new UnsupportedFlavorException(flavor);
         }
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		
		return new DataFlavor[] {
				
			DataFlavor.imageFlavor
		};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
	
		DataFlavor[] flavors = getTransferDataFlavors();
		
        for(int index = 0; index < flavors.length; index++) {
        	
            if(flavor.equals(flavors[index])) {
            	
                return true;
            }
        }

        return false;
	}
	
	/**
	 * @return the wrapped image
	 * @since 1.1.0
	 */
	public BufferedImage toBufferedImage() {
		
		return this.image;
	}
}
