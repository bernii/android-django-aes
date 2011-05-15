package crypt.test;

import static org.junit.Assert.*;
import org.junit.Test;

import crypt.Cipher;

public class AesTest {

	protected Cipher cipher;
	String knownValues[][] = {
				{"123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ",
				 "MSGi2oLpyuzaLQSA+bxQtsmW6q7OFwYeOBKTBBwoM9QKCHfry9Y3Sw7i9yDxrm5RYe3fPUaW5j59Yj2clkLgUALllj1V2LuTV3L0FctLtDigkmHwlKjRvAKw/ISr5++R"
				},
				{"abcdd","e8SYh4uEoLMUJPawEh18urx7djjWG+ou+yNG5mr56MM="}
			};

	@Test                                         
	public final void testDecryptKnownValues() {       
		for (String[] arr : knownValues) {
			String encrypted_text = arr[1]; String decrypted_text = arr[0];
			String result = null;
			try {
				result = Cipher.decryptString(encrypted_text,true);
			} catch (Exception e) {
				e.printStackTrace();
			}          
			assertEquals(decrypted_text, result);   			
		}
	}
	
	@Test                                         
	public final void testEncryptKnownValues() {       
		for (String[] arr : knownValues) {
			String clear_text = arr[0]; String encrypted_text = arr[1];
			String result = null;
			try {
				result = Cipher.encryptString(clear_text,true);
			} catch (Exception e) {
				e.printStackTrace();
			}   
			assertEquals(encrypted_text, result);   			
		}
	}
	
	
	@Test
	public final void testSanity(){
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(12048);
        for(int i=0; i<12048;i++){
            sb.append((char)i);
        }
        String out = null ;
		try {
			Cipher.init();
			out = Cipher.decrypt(Cipher.encrypt(sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        assertEquals(sb.toString(), out);
	}
	
	@Test
	public final void testHexStringSanity(){
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(12048);
        for(int i=0; i<12048;i++){
            sb.append((char)i);
        }
        String out = null ;
		try {
			Cipher.init();
			out = Cipher.decryptString((Cipher.encryptString(sb.toString(),true)), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
        assertEquals(sb.toString(), out);
	}
	
	@Test
	public final void testInnerSanity(){
        // decrypt(encrypt(str))==str for all unicode chars
        StringBuffer sb = new StringBuffer(12048);
        for(int i=0; i<12048;i++){
            sb.append((char)i);
        }
        byte[] enc = null ;
        byte[] result = null ;
        byte[] str = null ;
		try {
			Cipher.init();
			str = sb.toString().getBytes("UTF8") ; 
			enc = Cipher.encrypt(str);
			result = Cipher.decrypt(enc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<str.length;i++) 
			assertEquals(str[i], result[i]);
	}
}