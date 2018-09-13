package exp.bilibili.plugin.monitor;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.NoticeWindow;

/**
 * <PRE>
 * 授权无效导致程序自动退出的通知面板
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-11
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _ExitNoticeUI extends NoticeWindow {

	/** serialVersionUID */
	private static final long serialVersionUID = -7835582158711626274L;
	
	private final static int WIDTH = 300;
	
	private final static int HEIGHT = 150;
	
	private JEditorPane editor;
	
	public _ExitNoticeUI(String cause) {
		super("授权校验失败", WIDTH, HEIGHT, false, cause);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.editor = new JEditorPane();
		editor.setEditable(false);
		editor.setContentType("text/html");	// 将编辑框设置为支持html的编辑格式
		
		if(args != null && args.length > 0) {
			String cause = (String) args[0];
			editor.setText(getNoticeText(cause));
		}
	}

	@Override
	protected int LOCATION() {
		return LOCATION_CENTER;	// 出现坐标屏幕中心
	}
	
	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(SwingUtils.addBorder(editor), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		// TODO Auto-generated method stub
		
	}
	
	private String getNoticeText(String cause) {
		String text = StrUtils.concat(
				"<html>", 
					"<body>", 
						"<div style='text-align:center'>", 
							"哔哩哔哩插件姬 即将停止 <br/>", 
							"[<font color='red'>", cause, "</font>]",
						"</div>", 
					"</body>", 
				"</html>"
		);
		return text;
	}

}
