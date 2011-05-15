package crypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cipher Java class that works with created Python class
 * DON'T FORGET TO CHANGE SECRET KEY !!!
 * @author Bernard Kobos
 */
public class Cipher {
	static String secret = "1234567890abcdef" ;

	public static void setSkeySpec(SecretKeySpec skeySpec) {
		Cipher.skeySpec = skeySpec;
	}

	private static SecretKeySpec skeySpec;

	private static int BLOCK_SIZE = 32;
	private static char PADDING = '|' ;

	static String nullPadString(String original) {
		String output = original;
		int remain = 0;
		try {
			remain = output.getBytes("UTF8").length % BLOCK_SIZE;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (remain != 0) {
			remain = BLOCK_SIZE - remain;
			for (int i = 0; i < remain; i++) {
				output += PADDING;
			}
		}
		return output;
	}

	public static String encryptString(final String RAWDATA, boolean ENCODE)
	throws Exception { // This was a custom

		String encrypted = null;
		byte[] encryptedBytes = null;
		byte[] key;
		key = secret.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		// Init the cypher
		javax.crypto.Cipher cipher = null;
		try {
			String input = Integer.toString(RAWDATA.length()) + PADDING
			+ RAWDATA;
			cipher = javax.crypto.Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec);
			encryptedBytes =
				cipher.doFinal(nullPadString(input).getBytes("UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ENCODE) {
			// NO_WRAP to eliminate new line character at the end of string
			encrypted = new String(Base64.encode(encryptedBytes,Base64.NO_WRAP));
		} else {
			encrypted = new String(encryptedBytes);
		}
		return encrypted;
	}

	// Decrypt with base 64 decode
	public static String decryptString(final String ENCRYPTEDDATA, final boolean DECODE)
	throws Exception {
		String raw = null;
		byte[] rawBytes = null;
		byte[] encryptedBytes;
		if (DECODE) {
			encryptedBytes =
				Base64.decode(ENCRYPTEDDATA.getBytes(),Base64.DEFAULT);
		} else {
			encryptedBytes = ENCRYPTEDDATA.getBytes();
		}
		byte[] key;
		key = secret.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		// Cipher init
		javax.crypto.Cipher cipher = null;
		try {
			cipher = javax.crypto.Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec);
			rawBytes = cipher.doFinal(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		raw = new String(rawBytes,"UTF8");
		int delimiter = raw.indexOf(PADDING);
		int length = Integer.valueOf(raw.substring(0, delimiter));
		raw = raw.substring(delimiter + 1, length + delimiter + 1);
		return raw;
	}

	public static String encrypt(String cleartext)
	throws Exception {
		if(skeySpec==null)
			init();
		byte[] result = encrypt(cleartext.getBytes("UTF8"));
		return toHex(result);
	}

	public static String decrypt(String encrypted)
	throws Exception {
		if(skeySpec==null)
			init();
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(enc);
		return new String(result,"UTF8");
	}

	public static void init(){
		try {
			skeySpec = new SecretKeySpec(getRawKey("mylocalkey".getBytes()), "AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}


	public static byte[] encrypt(byte[] clear) throws Exception {
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	public static byte[] decrypt( byte[] encrypted)
	throws Exception {
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	public static final String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
			.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Fast and dirty command line - just for testing purposes
	 * @param args
	 */
	public static void main(String[] args) {
		String output = "";
		int action = 0;
		if(args[0].equalsIgnoreCase("-e")){
			action = 1 ;
		}else if(args[0].equalsIgnoreCase("-d")){
			action = 2 ;
		}else
			System.exit(0);

		StringBuffer fileData = new StringBuffer(1000);
		InputStreamReader reader;
		String s = null ;
		try {
			reader = new InputStreamReader(new FileInputStream(args[1]), "UTF-8");
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			switch (action) {
			case 1:
				output = Cipher.encryptString(fileData.toString(), true);
				break;

			default:
				output = Cipher.decryptString(fileData.toString(), true);
				break;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(output);
	}
}
