package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.MsgKwMgr;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 公告/打call机编辑窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _EditorUI extends PopChildWindow {

	private static final long serialVersionUID = -5197173910874606869L;

	private final static int WIDTH = 500;
	
	private final static int HEIGHT = 400;
	
	private JTextArea textTA;
	
	private JButton saveBtn;
	
	private String filePath;
	
	protected _EditorUI(String name, String filePath) {
		super(StrUtils.concat("[", name, "] 编辑器"), WIDTH, HEIGHT, false, filePath);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.saveBtn = new JButton("保存");
		saveBtn.setForeground(Color.BLACK);
		
		this.textTA = new JTextArea();
		if(args != null && args.length > 0) {
			this.filePath = args[0].toString();
			textTA.setText(FileUtils.read(filePath, Config.DEFAULT_CHARSET));
		}
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(SwingUtils.addAutoScroll(textTA), BorderLayout.CENTER);
		rootPanel.add(saveBtn, BorderLayout.SOUTH);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		saveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textTA.getText();
				if(FileUtils.write(filePath, text, Config.DEFAULT_CHARSET, false)) {
					MsgKwMgr.getInstn().reload();
					SwingUtils.info("保存成功");
					
				} else {
					SwingUtils.warn("保存失败");
				}
			}
		});
	}
	
	@Override
	protected void AfterView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beforeHide() {
		// TODO Auto-generated method stub
		
	}
	
}
