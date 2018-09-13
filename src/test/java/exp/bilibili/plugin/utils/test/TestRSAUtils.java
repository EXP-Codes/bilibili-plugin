package exp.bilibili.plugin.utils.test;

import exp.bilibili.plugin.utils.RSAUtils;

public class TestRSAUtils {

	public static void main(String[] args) throws Exception {
		String pk = "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdScM09sZJqFPX7bvmB2y6i08J\nbHsa0v4THafPbJN9NoaZ9Djz1LmeLkVlmWx1DwgHVW+K7LVWT5FV3johacVRuV98\n37+RNntEK6SE82MPcl7fA++dmW2cLlAjsIIkrX+aIvvSGCuUfcWpWFy3YVDqhuHr\nNDjdNcaefJIQHMW+sQIDAQAB\n-----END PUBLIC KEY-----\n";
		String hash = "2c3e8e055acb5a65";
		String pwd = "liao5422";
		
		System.out.println(RSAUtils.encrypt(hash + pwd, pk));
	}
	
}
