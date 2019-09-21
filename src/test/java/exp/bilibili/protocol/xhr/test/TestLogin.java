package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.plugin.Config;
import exp.bilibili.protocol.xhr.Login;

public class TestLogin extends _Init {

	@Test
	public void testGetQrcodeInfo() {
		String rst = Login.getQrcodeInfo();
		System.out.println(rst);
	}

	@Test
	public void testDownloadVccode() {
		String imgPath = Config.getInstn().IMG_DIR().concat("/vccode.jpg");
		String cookie = Login.downloadVccode(imgPath);
		System.out.println(cookie);
	}
	
	@Test
	public void testToLoginString() {
		// Undo
	}

	@Test
	public void testToLoginStringStringStringString() {
		// Undo
	}

}
