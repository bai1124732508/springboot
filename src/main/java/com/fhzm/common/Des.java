package com.fhzm.common;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 
 * 字符串工具集合
 * 
 * @author LiuDanian
 * 
 */

public class Des {

	private static final String PASSWORD_CRYPT_KEY = "!!!12356dae___daeibnbx__0982@__";
	private final static String DES = "DES";
	private static byte[] passwordBytes;

	/**
	 * 获得单一密码byte数组
	 * 
	 * @return
	 */
	private static byte[] getSingletonPassword() {
		if (passwordBytes == null) {
			passwordBytes = PASSWORD_CRYPT_KEY.getBytes();
		}
		return passwordBytes;
	}

	/**
	 * 
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * 
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * 
	 * @return 返回加密后的数据
	 * 
	 * @throws Exception
	 * 
	 */

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		// 现在，获取数据并加密

		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * 
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * 
	 * @return 返回解密后的原始数据
	 * 
	 * @throws Exception
	 * 
	 */

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);

	}

	/**
	 * 解密byte数组
	 * 
	 * @param data
	 * @return
	 */
	public final static byte[] decryptBytes(byte[] data) {
		try {
			return decrypt(data, getSingletonPassword());
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 
	 * 解密字符串
	 * 
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	public final static String decryptString(String data) {
		try {
			return new String(decryptBytes(ObjectConvert.hexStringToByte(data)));
		} catch (Exception e) {
		}
		return null;
	}


	/**
	 * 
	 * 加密字符串
	 * 
	 * @param password
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	public final static String encryptString(String content) {

		try {
			return ObjectConvert.bytesToHexString(encrypt(content.getBytes(),
					getSingletonPassword()));
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 加密byte数组
	 * 
	 * @param bytes
	 * @return
	 */
	public final static byte[] encryptBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try {
			return encrypt(bytes, getSingletonPassword());
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密byte数组为字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public final static String encryptBytes2Str(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		String content = null;
		try {
			content = ObjectConvert.bytesToHexString(encryptBytes(bytes));
		} catch (Exception e) {
		}
		return content;
	}

	/**
	 * 
	 * 加密对象
	 * 
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	public final static String encryptObject(Object data) {

		try {
			return ObjectConvert.bytesToHexString(encryptBytes(ObjectConvert
					.objectToByte(data)));
		} catch (Exception e) {

		}
		return null;
	}

	public final static String toBytesString(byte[] bytes) {
		try {
			return ObjectConvert.bytesToHexString(bytes);
		} catch (Exception e) {

		}
		return null;
	}
	
	
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
         Pattern pattern = Pattern.compile("[0-9]*");
         Matcher isNum = pattern.matcher(str);
         if( !isNum.matches() ){
             return false;
         }
         return true;
	}
	
	
	public static void main(String[] args) {
		String pass ="{\"recordId\":\"1529657118823n6e\",\"questionId\":\"1529563600086a9t\",\"logId\":\"1529657118823k64\",\"type\":\"1\"}";
		String passAfterEncrypt = encryptString(pass);
		String passAfterDecrypt = decryptString(passAfterEncrypt);
		System.out.println(passAfterEncrypt.length());
		System.out.println(passAfterDecrypt.length());
		System.out.println("原始字段:" + pass);
		System.out.println("字段加密后:" + passAfterEncrypt);
		System.out.println("字段解密后:" + passAfterDecrypt);
	}
	//转义符替换
	public static String replaceJava(String str){
		String strnew=str.replace("\\","\\\\").replace("\'","\\‘").replace("\"","\\“");
		return strnew;
	}
}
