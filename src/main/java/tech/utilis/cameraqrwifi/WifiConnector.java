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
				interfaceWifi = parseMacWifiInterface(interfaces);
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
				String connect = exec(Arrays.asList(new String[]{"networksetup", "-setairportnetwork", interfaceWifi, ssid, password}));
				System.out.println(connect);
			}
			catch (IOException | InterruptedException ex){
				throw new RuntimeException("Failed to connect", ex);
			}

			return false;
		}
		else {
			throw new UnsupportedOperationException("Not implemented yet");
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
	
	public static String parseMacWifiInterface(String execOut){
		String regex = "(?iu)(?<=Hardware Port: Wi-?Fi\\nDevice: )(.*)";
		
		Matcher m = Pattern.compile(regex).matcher(execOut);
		if (m.find()){
			return m.group();
		}
		
		return null;
	}
	
}
