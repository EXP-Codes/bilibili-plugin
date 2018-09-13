package exp.bilibili.plugin.core.front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.LoginMgr;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 登陆二维码展示界面
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _QrcodeUI extends PopChildWindow {

	/** serialVersionUID */
	private final static long serialVersionUID = 3032128610929327304L;

	private final static String COOKIE_DIR = Config.getInstn().COOKIE_DIR();
	
	private final static String TIPS_PATH = Config.getInstn().IMG_DIR().concat("/qrTips.png");
	
	private final static int WIDTH = 300;
	
	private final static int HEIGHT = 320;
	
	private JLabel imgLabel;
	
	private JLabel timeLabel;
	
	protected _QrcodeUI() {
		super("B站手机APP扫码登陆", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.imgLabel = new JLabel(new ImageIcon(TIPS_PATH));
		this.timeLabel = new JLabel(FileUtils.isEmpty(COOKIE_DIR) ? 
				"正在更新二维码..." : "正在尝试自动登录...");
		timeLabel.setForeground(Color.RED);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(imgLabel, BorderLayout.CENTER);
		JPanel btnPanel = SwingUtils.getHFlowPanel(
				new JLabel(" "), timeLabel, new JLabel(" "));
		rootPanel.add(btnPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void AfterView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beforeHide() {
		// TODO Auto-generated method stub
		
	}
	
	protected void updateImg() {
		File dir = new File(LoginMgr.IMG_DIR);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().contains(LoginMgr.QRIMG_NAME)) {
				
				// 注意: 这里不能通过new ImageIcon(ImgPath)的方式更新图片
				// 因为这种方式会因为图片路径没有变化, 而不去更新缓存, 导致显示的二维码一直不变
				Image img = Toolkit.getDefaultToolkit().createImage(file.getPath());
				imgLabel.setIcon(new ImageIcon(img));
				break;
			}
		}
	}
	
	protected void updateTime(int time) {
		if(time < 0) {
			timeLabel.setText("正在更新二维码...");
			
		} else {
			String sTime = StrUtils.leftPad(String.valueOf(time), '0', 3);
			timeLabel.setText(StrUtils.concat("有效时间 : ", sTime, " 秒"));
		}
	}
	
}
