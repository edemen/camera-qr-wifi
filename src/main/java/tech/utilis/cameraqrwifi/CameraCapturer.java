package tech.utilis.cameraqrwifi;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Eugene Dementiev
 */
public class CameraCapturer {
	
	public static BufferedImage captureFromCamera() throws IOException {
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		BufferedImage image = webcam.getImage();
		
		// Small delay to fix weird hanging behaviour
		try{
			Thread.sleep(10);
		}
		catch(InterruptedException ex){}
		
		webcam.close();
		return image;
	}

}
