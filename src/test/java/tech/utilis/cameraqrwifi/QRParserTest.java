package tech.utilis.cameraqrwifi;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Eugene Dementiev
 */
public class QRParserTest {
	
	public QRParserTest() {
	}

	/**
	 * Test of parseWifi method, of class QRParser.
	 */
	@Test
	public void testParseWifi() throws Exception {
		System.out.println("parseWifi");
		
		String[][] tests = new String[][]{
			{ "wifi_hidden.png",		"WIFI:S:WiFiTest;T:WPA;P:dsa;H:true;"},
			{ "escaped_semicolon.png",	"WIFI:S:WiFiTest;T:WPA;P:123\\;;;" },
			{ "qrcode_white_border.png","WIFI:S:WiFiTest;T:WPA;P:777888999111222333;;" },
			{ "qrcode_no_border.png",	"WIFI:S:WiFiTest;T:WPA;P:777888999111222333;;" },
		};
		
		for( String[] testCase: tests){
			String fileName = testCase[0];
			String plainExpected = testCase[1];
			System.out.println("Test: " + fileName);
			BufferedImage image = ImageIO.read(QRParserTest.class.getResourceAsStream(fileName));
			Map<String, String> result = QRParser.parseWifi(image);
			assertEquals(plainExpected, result.get("plaintext"));
		}
		
	}
	
	/**
	 * Test of parseFields method, of class QRParser.
	 */
	@Test
	public void testParseFields() throws Exception {
		System.out.println("parseFields");
		
		String[][][] tests = new String[][][]{
			{
				{"T", "WPA"},
				{"S", "mynetwork"},
				{"P", "mypass"},
				{}
			},
			{
				{"T", "WPA"},
				{"S", "\\\"foo\\;bar\\\\baz\\\""},
				{"P", "mypass"},
				{}
			},
			{
				{"S", "Test"},
			}
		};
		
		for(String[][] testCase: tests){
			String plainText1 = "WIFI:";
			Map<String, String> map1 = new HashMap<>();
			
			for(String[] pair: testCase){
				if(pair.length > 0){
					plainText1 += pair[0];
					map1.put(pair[0], "False");
				}
				
				if (pair.length > 1){
					plainText1 += ":" + pair[1];
					map1.put(pair[0], pair[1]);
				}
				
				plainText1 += ";";
			}
			map1.put("plaintext", plainText1);
			
			System.out.println("Test: " + plainText1);
			
			Map map2 = QRParser.parseFields(plainText1);
			assertEquals(map1, map2);
		}
		
	}
}
