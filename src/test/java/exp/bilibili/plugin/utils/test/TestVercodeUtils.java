package exp.bilibili.plugin.utils.test;

import java.io.File;

import org.junit.Test;

import exp.bilibili.plugin.utils.VercodeUtils;
import exp.libs.envm.FileType;
import exp.libs.utils.io.FileUtils;

public class TestVercodeUtils {

	@Test
	public void testCalculateExpressionImage() {
		File dir = new File("./src/test/resources/exp/bilibili/plugin/utils/test/math");
		File[] imgs = dir.listFiles();
		for(File img : imgs) {
			int rst = VercodeUtils.calculateExpressionImage(img.getPath());
			System.out.println(img.getName() + " : " + rst);
		}
	}
	
	@Test
	public void testRecognizeStormImage() {
		File dir = new File("./src/test/resources/exp/bilibili/plugin/utils/test/storm/train");
		File[] imgs = dir.listFiles();
		for(File img : imgs) {
			FileType type = FileUtils.getFileType(img);
			if(type == FileType.PNG) {
				String code = VercodeUtils.recognizeStormImage(img.getPath());
				System.out.println(img.getName() + " : " + code);
			}
		}
	}
	
}
