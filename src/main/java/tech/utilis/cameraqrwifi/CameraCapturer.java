package tech.utilis.cameraqrwifi;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;

/**
 *
 * @author Eugene Dementiev
 */
public class CameraCapturer {
	
	private final Webcam webcam;
	
	public CameraCapturer(){
		webcam = Webcam.getDefault();
		webcam.open();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			webcam.close();
		}));
	}
	
	public BufferedImage captureFromCamera(){
		synchronized(this){
			return webcam.getImage();
		}
	}

}
