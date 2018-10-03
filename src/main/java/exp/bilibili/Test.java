package exp.bilibili;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.io.flow.FileFlowReader;

public class Test {

	public static void main(String[] args) {
		Map<String, StringBuilder> sttc = new HashMap<String, StringBuilder>();
		
		String dir = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\sttc";
		String out = dir + "\\out\\";
		
		File[] files = new File(dir).listFiles();
		for(File file : files) {
			if(!file.getName().endsWith(".log")) {
				continue;
			}
			FileFlowReader ffr = new FileFlowReader(file, "utf-8");
			while(ffr.hasNextLine()) {
				String line = ffr.readLine();
				List<String> datas = RegexUtils.findBrackets(line, "\\[([^\\]]*)\\]");
				
				String data = "";
				String name = "";
				String type = "";
				if(datas.size() < 5) {
					continue;
					
				} else if(datas.size() == 5) {
					String time = datas.get(0);
					type = datas.get(1);
					String room = "0";
					name = datas.get(2);
					String rst = datas.get(3);
					String reason = datas.get(4);
					reason = StrUtils.isEmpty(reason) ? "x" : reason;
					data = StrUtils.concat(Arrays.asList(new String[] { time, type, room, rst, reason }), ", ");
					
				} else {
					String time = datas.get(0);
					type = datas.get(1);
					String room = datas.get(2);
					name = datas.get(3);
					String rst = datas.get(4);
					String reason = datas.get(5);
					reason = StrUtils.isEmpty(reason) ? "x" : reason;
					data = StrUtils.concat(Arrays.asList(new String[] { time, type, room, rst, reason }), ", ");
				}
				
				if("ROOM".equals(type)) {
					continue;
				}
				
				StringBuilder sb = sttc.get(name);
				if(sb == null) {
					sb = new StringBuilder();
					sttc.put(name, sb);
				}
				sb.append(data).append("\r\n");
			}
			ffr.close();
		}
		
		Iterator<String> its = sttc.keySet().iterator();
		while(its.hasNext()) {
			String name = its.next();
			String data = sttc.get(name).toString();
			FileUtils.write(out + name, data, "utf-8", false);
		}
		
	}
	
	
}
