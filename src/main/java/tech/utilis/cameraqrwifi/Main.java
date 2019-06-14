package tech.utilis.cameraqrwifi;

import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 *
 * @author Eugene Dementiev
 */
public class Main {
	
	public static final long MAX_DURATION = 60*1000;
	public static final long INTERVAL = 25;
	
	public static void main(String[] args) {
		
		final ZonedDateTime start = ZonedDateTime.now();
		
		final CameraCapturer camera = new CameraCapturer();
		final CameraFrame frame = new CameraFrame();
		
		final Thread cameraThread = new Thread(() -> {
			while(true){
				frame.updateImage(camera.captureFromCamera());
				try {
					Thread.sleep(100);
				}
				catch(InterruptedException ex){
					break;
				}
			}
		});
		cameraThread.start();
		
		new Thread(() -> {
			ZonedDateTime current = ZonedDateTime.now();
			
			do {
				CameraFrame.Status status = frame.addStatus();
				try {
					BufferedImage image = camera.captureFromCamera();
					Map<String, String> wifiDetails = QRParser.parseWifi(image);
					
					if(wifiDetails.containsKey("S")){
						status.setState(CameraFrame.Status.State.Connecting);
						cameraThread.interrupt();
						
						boolean success = WifiConnector.connect(wifiDetails.get("S"), wifiDetails.get("P"));
						if (success){

							status.setState(CameraFrame.Status.State.Connected);

							System.out.println("Success");
							System.exit(0);
						}
						else {
							status.setState(CameraFrame.Status.State.Failed);
							System.exit(0);
						}
					}

				}
				catch (Exception ex){
					status.setState(CameraFrame.Status.State.Failed);
					System.out.println("Error: " + ex.getMessage());
					ex.printStackTrace();
				}
				
				System.out.println("Retry");
				
				try {
					Thread.sleep(INTERVAL);
				}
				catch (InterruptedException ex){
					status.setState(CameraFrame.Status.State.Failed);
					ex.printStackTrace();
					break;
				}
				
				current = ZonedDateTime.now();
			}
			while(start.until(current, ChronoUnit.MILLIS) < MAX_DURATION);
			
			System.exit(0);
			
		}).start();
	}
}
