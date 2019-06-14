package tech.utilis.cameraqrwifi;

import java.util.Map;

import com.google.zxing.NotFoundException;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Reader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.*;

import com.google.zxing.qrcode.QRCodeReader;

/**
 *
 * @author Eugene Dementiev
 */
public class QRParser {

	/**
	 * 
	 * @param image QR code image
	 * 
	 * @return Map of WiFi details or null if no details found
	 * 
	 * @throws NotFoundException if decoding failed
	 * @throws ChecksumException when checksum error 
	 * @throws FormatException when wrong format
	 */
	public static Map<String, String> parseWifi(BufferedImage image) throws NotFoundException, ChecksumException, FormatException {
		
		Reader xReader = new QRCodeReader();
		LuminanceSource source = new BufferedImageLuminanceSource(image);

		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		List<BarcodeFormat> barcodeFormats = new ArrayList<>();
		barcodeFormats.add(BarcodeFormat.QR_CODE);

		HashMap<DecodeHintType, Object> decodeHints = new HashMap<>(3);
		decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, barcodeFormats);
		decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		try {
			Result result = xReader.decode(bitmap, decodeHints);
			String plaintext = result.getText();
			
			return parseFields(plaintext);
		}
		catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, String> parseFields(String plainText){
		HashMap<String, String> map = new HashMap<>();
		map.put("plaintext", plainText);
		
		String plainTextStripped = plainText.replaceFirst("^WIFI:", "");
		String[] pairs = plainTextStripped.split("(?<!\\\\);");
		for(String pairString: pairs){
			String[] pair = pairString.split("(?<!\\\\):");
			map.put(pair[0].toUpperCase(), "False");
			if (pair.length > 1){
				map.put(pair[0], pair[1]);
			}
		}
		
		return map;
	}
}
