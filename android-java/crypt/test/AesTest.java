package crypt.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import crypt.Cipher;

/**
 * Test Cipher coding capabilities.
 * @author berni
 *
 */
public class AesTest {

	private Cipher cipher;
	private String[][] knownValues = {
				{"123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ",
				 "MSGi2oLpyuzaLQSA+bxQtsmW6q7OFwYeOBKTBBwoM9QKCHfry9Y3Sw7i9yDxrm5RYe3fPUaW5j59Yj2clkLgUALllj1V2LuTV3L0FctLtDigkmHwlKjRvAKw/ISr5++R"
				},
				{"abcdd", "e8SYh4uEoLMUJPawEh18urx7djjWG+ou+yNG5mr56MM="}
			};

	@Test                                         
	public final void testDecryptKnownValues() {       
		for (String[] arr : knownValues) {
			String encryptedText = arr[1]; String decryptedText = arr[0];
			String result = null;
			try {
				result = Cipher.decryptString(encryptedText, true);
			} catch (Exception e) {
				e.printStackTrace();
			}          
			assertEquals(decryptedText, result);   			
		}
	}
	
	@Test                                         
	public final void testEncryptKnownValues() {       
		for (String[] arr : knownValues) {
			String clearText = arr[0]; String encryptedText = arr[1];
			String result = null;
			try {
				result = Cipher.encryptString(clearText,true);
			} catch (Exception e) {
				e.printStackTrace();
			}   
			assertEquals(encryptedText, result);   			
		}
	}
	
	
	@Test
	public final void testSanity() {
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(numOfUnicodeChars);
        for (int i = 0; i < numOfUnicodeChars; i++) {
            sb.append((char) i);
        }
        String out = null;
		try {
			Cipher.init();
			out = Cipher.decrypt(Cipher.encrypt(sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        assertEquals(sb.toString(), out);
	}
	
	@Test
	public final void testHexStringSanity() {
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(numOfUnicodeChars);
        for (int i = 0; i < numOfUnicodeChars; i++) {
            sb.append((char) i);
        }
        String out = null;
		try {
			Cipher.init();
			out = Cipher.decryptString((
					Cipher.encryptString(sb.toString(), true)), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
        assertEquals(sb.toString(), out);
	}
	
	int numOfUnicodeChars = 12048 ;
	@Test
	public final void testInnerSanity() {
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(numOfUnicodeChars);
        for (int i = 0; i < numOfUnicodeChars; i++) {
            sb.append((char) i);
        }
        byte[] enc = null;
        byte[] result = null;
        byte[] str = null;
		try {
			Cipher.init();
			str = sb.toString().getBytes("UTF8"); 
			enc = Cipher.encrypt(str);
			result = Cipher.decrypt(enc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < str.length; i++) {
			assertEquals(str[i], result[i]);
		}
	}

	/**
	 * Cipher getter.
	 * @return cipher instance
	 */
	public final Cipher getCipher() {
		return cipher;
	}
}