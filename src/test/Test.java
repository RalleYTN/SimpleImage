package test;

import java.io.File;
import java.io.IOException;

import de.ralleytn.simple.image.ImageEditor;
import de.ralleytn.simple.image.SimpleImage;

public class Test {

	public static void main(String[] args) {
		
		try {
			
			SimpleImage image = new SimpleImage(new File("image.jpg"));
			SimpleImage watermark = new SimpleImage(new File("watermark.png"));
			
			ImageEditor editor = new ImageEditor(image);
			editor.drawImage(watermark, 300, 400, true);
			
			image.show();
			
		} catch(IOException exception) {
			
			exception.printStackTrace();
		}
	}
}
