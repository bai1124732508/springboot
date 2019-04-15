
package com.fhzm.common;
import java.io.InputStream;
import java.security.MessageDigest;


public class FileUtils {
	public static String getHash(InputStream fis, String hashType)  
	        throws Exception  
	    {  
	        byte buffer[] = new byte[1024];  
	        MessageDigest md5 = MessageDigest.getInstance(hashType);  
	        for(int numRead = 0; (numRead = fis.read(buffer)) > 0;)  
	        {  
	            md5.update(buffer, 0, numRead);  
	        }  
	        fis.close();  
	        return HexUtil.toHexString(md5.digest());
	        
	    } 

}
