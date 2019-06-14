package tech.utilis.cameraqrwifi;

import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Eugene Dementiev
 */
public class Main {
	
	public static final long KEEP_TRYING = 5*1000;
	public static final long INTERVAL = 25;
	
	public static void main(String[] args) {
		new Thread(() -> {
			long waited = 0;
			
			JFrame jframe = new JFrame("Image scan");
			jframe.setVisible(true);
			
			do {
				try {
					BufferedImage image = CameraCapturer.captureFromCamera();
					jframe.getContentPane().removeAll();
					jframe.getContentPane().add(new JLabel(new ImageIcon(image)));
					jframe.pack();
					Map<String, String> wifiDetails = QRParser.parseWifi(image);
					boolean success = WifiConnector.connect(wifiDetails.get("S"), wifiDetails.get("P"));
					if (success){
						System.out.println("Success");
						System.exit(0);
					}
				}
				catch (Exception ex){
					System.out.println("Error: " + ex.getMessage());
					ex.printStackTrace();
				}
				
				System.out.println("Retry");
				
				try {
					Thread.sleep(INTERVAL);
				}
				catch (InterruptedException ex){
					ex.printStackTrace();
					break;
				}
				waited += INTERVAL;
			}
			while(waited < KEEP_TRYING);
		}).start();
	}
}
