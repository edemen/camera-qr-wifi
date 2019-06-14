
package tech.utilis.cameraqrwifi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Eugene Dementiev
 */
public final class CameraFrame extends JFrame {
	
	private final JPanel imagePanel = new JPanel(new BorderLayout());
	private final JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	public CameraFrame(){
		super("Image scan");
		
		setLayout(new BorderLayout());
		add(imagePanel, BorderLayout.CENTER);
		imagePanel.setPreferredSize(new Dimension(176, 144));
		
		add(statusPanel, BorderLayout.EAST);
		statusPanel.setPreferredSize(new Dimension(400, 144));
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				
		pack();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - getWidth();
        int y = (int) rect.getMaxY() - getHeight();
        setLocation(x, y);
		
		setVisible(true);

	}

	public void updateImage(BufferedImage image){
		imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		
//		imagePanel.getGraphics().drawImage(image, 0, 0, this);
		imagePanel.getGraphics().drawImage(image, 0 + image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);

	}
	
	public Status addStatus(){
		Status status = new Status();
		this.statusPanel.add(status);
		this.statusPanel.revalidate();
		return status;
	}
	
	public static final class Status extends JPanel {
		
		public enum State {
			Pending,
			Failed,
			Connecting,
			Connected
		}
		
		private Color color = Color.BLACK;
		
		private State state = State.Pending;
		
		public Status(){
			setPreferredSize(new Dimension(10, 10));
			setBackground(color);
		}

		public void setState(State state) {
			this.state = state;
			switch(this.state){
				case Pending: color = Color.ORANGE; break;
				case Failed: color = Color.RED; break;
				case Connecting: color = Color.YELLOW; break;
				case Connected: color = Color.GREEN; break;
				default: color = Color.BLACK;
			}
			setBackground(color);
		}
		
	}
	
}
