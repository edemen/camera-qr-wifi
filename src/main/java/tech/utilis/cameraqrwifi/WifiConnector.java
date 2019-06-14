package tech.utilis.cameraqrwifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringJoiner;

/**
 *
 * @author Eugene Dementiev
 */
public class WifiConnector {
	
	public static boolean connect(String ssid, String password){
		System.out.println("Connecting to network: " + ssid);
		System.out.println("with password: " + password);
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public static String exec(List<String> args) throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder(args);
		Process proc = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
		br.lines().iterator().forEachRemaining(sj::add);
		proc.waitFor();

		String output = sj.toString();
		
		proc.destroy();
		
		return output;
	}
}
