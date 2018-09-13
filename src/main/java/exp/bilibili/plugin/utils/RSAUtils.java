package exp.bilibili.plugin.utils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.security.rsa.Base64;

import exp.bilibili.plugin.Config;
import exp.libs.utils.encode.CharsetUtils;

/**
 * <PRE>
 * RSA密钥编解码工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RSAUtils {

	private final static Logger log = LoggerFactory.getLogger(RSAUtils.class);
	
	private static final String ALGORITHM_RSA = "RSA";
	
	private static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----\n";
	
	private static final String PUBLIC_KEY_END = "\n-----END PUBLIC KEY-----\n";
	
	protected RSAUtils() {}
	
	/**
	 * 
	 * @param plainText
	 * @param publicKey
	 * @return
	 */
	public static String encrypt(String plainText, String publicKey) {
		return encrypt(plainText, publicKey, Config.DEFAULT_CHARSET);
	}

	/**
	 * 
	 * @param plainText
	 * @param publicKey -----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdScM09sZJqFPX7bvmB2y6i08J\nbHsa0v4THafPbJN9NoaZ9Djz1LmeLkVlmWx1DwgHVW+K7LVWT5FV3johacVRuV98\n37+RNntEK6SE82MPcl7fA++dmW2cLlAjsIIkrX+aIvvSGCuUfcWpWFy3YVDqhuHr\nNDjdNcaefJIQHMW+sQIDAQAB\n-----END PUBLIC KEY-----\n
	 * @param charset
	 * @return
	 */
	public static String encrypt(String plainText, String publicKey, String charset) {
		publicKey = publicKey.replace(PUBLIC_KEY_BEGIN, "").replace(PUBLIC_KEY_END, "");
		byte[] bytes = encrypt(CharsetUtils.toBytes(plainText, charset), toPublicKey(publicKey));
		String cipherText = Base64.encode(bytes);
		return cipherText;
	}
	
	/**
	 * 公钥加密过程
	 * @param publicKey 公钥
	 * @param plainText 明文数据
	 * @return
	 */
	public static byte[] encrypt(byte[] plainText, RSAPublicKey publicKey) {
		byte[] bytes = new byte[0];
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			bytes = cipher.doFinal(plainText);
			
		} catch(Exception e) {
			log.error("RSA公钥加密失败", e);
		} 
		return bytes;
	}
	
	/**
	 * 从字符串中加载公钥
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	private static RSAPublicKey toPublicKey(String publicKey)  {
		RSAPublicKey rsaPublicKey = null;
		try {
			byte[] buffer = Base64.decode(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			
		} catch(Exception e) {
			log.error("生成公钥失败", e);
		} 
		return rsaPublicKey;
	}
	
}
