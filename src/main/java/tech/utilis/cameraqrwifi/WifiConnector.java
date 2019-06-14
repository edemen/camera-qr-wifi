package tech.utilis.cameraqrwifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eugene Dementiev
 */
public class WifiConnector {
	
	public static boolean connect(String ssid, String password) {
		System.out.println("Connecting to network: " + ssid);
		System.out.println("with password: " + password);
		
		String interfaceWifi = null;
		Throwable trace = null;
		
		// Determine interface
		if (System.getProperty("os.name").matches("(?iu)Mac ?Os ?X")){
			try {
				String interfaces = exec(Arrays.asList(new String[]{"networksetup", "-listallhardwareports"}));
				interfaceWifi = macParseWifiInterface(interfaces);
			}
			catch (IOException | InterruptedException ex){
				trace = ex;
			}
		}
		
		if (interfaceWifi == null){
			throw new RuntimeException("No WiFi interface found, please specify the WiFi interface with argument -i", trace);
		}
		
		if (System.getProperty("os.name").matches("(?iu)Mac ?Os ?X")){
			try {
				return macConnect(interfaceWifi, ssid, password);
			}
			catch (IOException | InterruptedException ex){
				throw new RuntimeException("Failed to connect", ex);
			}
		}
		else {
			throw new UnsupportedOperationException("Not implemented for this platform yet");
		}
		
	}
	
	public static String exec(List<String> args) throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);
		Process proc = pb.start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		StringJoiner sj = new StringJoiner("\n");
		br.lines().iterator().forEachRemaining(sj::add);
		proc.waitFor();

		String output = sj.toString();
		
		proc.destroy();
		
		return output;
	}
	
	public static boolean macConnect(String interfaceWifi, String ssid, String password) throws InterruptedException, IOException{
		String connect = exec(Arrays.asList(new String[]{"networksetup", "-setairportnetwork", interfaceWifi, ssid, password}));
		
		boolean error = false;
		error = error || connect.contains("Failed to join network");
		error = error || connect.contains("Could not find network");
		if (error){
			System.out.println(connect);
			return false;
		}
		
		return macIsConnected(interfaceWifi);
	}
	
	public static boolean macIsConnected(String interfaceWifi){
		try {
			String ifconfig = exec(Arrays.asList(new String[]{"ifconfig", interfaceWifi}));
			return ifconfig.contains("status: active");
		}
		catch(IOException | InterruptedException ex){
			System.out.println("Failed to check connection status: " + ex.getMessage());
			return false;
		}
	}
	
	public static String macParseWifiInterface(String execOut){
		String regex = "(?iu)(?<=Hardware Port: Wi-?Fi\\nDevice: )(.*)";
		
		Matcher m = Pattern.compile(regex).matcher(execOut);
		if (m.find()){
			return m.group();
		}
		
		return null;
	}

}
