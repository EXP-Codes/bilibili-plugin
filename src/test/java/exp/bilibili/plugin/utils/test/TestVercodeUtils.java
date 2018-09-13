package exp.bilibili.plugin.utils.test;

import java.io.File;

import exp.bilibili.plugin.utils.OCRUtils;
import exp.bilibili.plugin.utils.VercodeUtils;

public class TestVercodeUtils {

	public static void main(String[] args) {
		File dir = new File("./src/test/java/exp/bilibili/plugin/utils/test/img");
		File[] imgs = dir.listFiles();
		for(File img : imgs) {
			if(img.getName().endsWith(".jpg")) {
				String expression = OCRUtils.jpgToTxt(img.getPath());
				int rst = VercodeUtils.calculate(expression);
				System.out.println(img.getName() + " : " + expression + "=" + rst);
			}
		}
	}
	
}
