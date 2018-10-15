package exp.bilibili.protocol.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exp.libs.utils.encode.CompressUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.io.flow.FileFlowReader;
import exp.libs.warp.xls.Excel;
import exp.libs.warp.xls.Sheet;


public class TestSttc {

	private final static String DIR = "C:\\Users\\Administrator\\Desktop\\log\\BACKUP";
	
	private static Map<String, Sheet> sheets = new HashMap<String, Sheet>();
	
	private static Map<String, Integer> rows = new HashMap<String, Integer>();
	
	public static void main(String[] args) {
		Excel excel = new Excel("./conf/sttc-fm.xlsx");
		
		File[] files = new File(DIR).listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				File[] fs = file.listFiles();
				for(File f : fs) {
					if(f.getName().contains("sttc") && 
							!f.getName().contains("zip")) {
//						if(f.getName().contains("zip")) {
//							CompressUtils.unZip(f.getPath());
//						}
						
						String path = f.getAbsolutePath();
//						if(f.getName().contains("zip")) {
//							path = path.replace(".zip", "");
//						}
						
						System.out.println(path);
						todo(excel, path);
					}
				}
			}
		}
		
		excel.hideSheet(0);
		excel.saveAs(DIR + "\\sttc.xlsx");
		excel.clear();
	}
	
	private static void todo(Excel excel, String path) {
		FileFlowReader ffr = new FileFlowReader(path, "utf-8");
		while(ffr.hasNextLine()) {
			String line = ffr.readLine();
			List<String> datas = RegexUtils.findBrackets(line, "\\[([^\\]]*)\\]");
			
			String time = "";
			String name = "";
			String type = "";
			String rst = "";
			String reason = "";
			String room = "";
			if(datas.size() < 5) {
				continue;
				
			} else if(datas.size() == 5) {
				time = datas.get(0);
				type = datas.get(1);
				room = "0";
				name = datas.get(2);
				rst = datas.get(3);
				reason = datas.get(4);
				reason = StrUtils.isEmpty(reason) ? "x" : reason;
				
			} else {
				time = datas.get(0);
				type = datas.get(1);
				room = datas.get(2);
				name = datas.get(3);
				rst = datas.get(4);
				reason = datas.get(5);
				reason = StrUtils.isEmpty(reason) ? "x" : reason;
			}
			
			if("ROOM".equals(type)) {
				continue;
			}
			
			Integer row = rows.get(name);
			Sheet sheet = sheets.get(name);
			if(sheet == null) {
				sheet = excel.cloneSheet(0, name);
				sheets.put(name, sheet);
				row = 1;
			}
			
			sheet.setVal(row, 0, time);
			sheet.setVal(row, 1, room);
			sheet.setVal(row, 2, type);
			sheet.setVal(row, 3, rst);
			sheet.setVal(row, 4, reason);
			rows.put(name, ++row);
		}
	}
	
}
