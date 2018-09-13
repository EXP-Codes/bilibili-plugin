package exp.bilibili.plugin.cache.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exp.bilibili.plugin.Config;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 二维码登陆窗口.
 *  暂时仅用于登陆主号
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class QRLoginUI extends PopChildWindow {

	public static void main(String[] args) {
		BeautyEyeUtils.init();
		new QRLoginUI()._view();
	}
	
	/** serialVersionUID */
	private final static long serialVersionUID = 3032128610929327304L;

	private final static String IMG_DIR = Config.getInstn().IMG_DIR();
	
	private final static String TIPS_PATH = IMG_DIR.concat("/qrTips.png");
	
	private final static String TIPS = "正在更新二维码...";
	
	private final static int WIDTH = 300;
	
	private final static int HEIGHT = 320;
	
	private JLabel imgLabel;
	
	private JLabel tipLabel;
	
	private QRLogin qrLogin;
	
	protected QRLoginUI() {
		super("哔哩哔哩-APP扫码登陆", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.imgLabel = new JLabel(new ImageIcon(TIPS_PATH));
		this.tipLabel = new JLabel(TIPS);
		tipLabel.setForeground(Color.RED);
		
		this.qrLogin = new QRLogin(this);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(imgLabel, BorderLayout.CENTER);
		JPanel btnPanel = SwingUtils.getHFlowPanel(
				new JLabel(" "), tipLabel, new JLabel(" "));
		rootPanel.add(btnPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		// Undo
	}
	
	@Override
	protected void AfterView() {
		qrLogin._start();
	}

	@Override
	protected void beforeHide() {
		qrLogin._stop();
	}

	protected void updateQrcodeImg(String imgDir, String qrImgName) {
		File dir = new File(imgDir);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().contains(qrImgName)) {
				SwingUtils.setImage(imgLabel, file.getPath());
				break;
			}
		}
	}
	
	protected void updateQrcodeTips(int time) {
		if(time < 0) {
			tipLabel.setText(TIPS);
			
		} else {
			String sTime = StrUtils.leftPad(String.valueOf(time), '0', 3);
			tipLabel.setText(StrUtils.concat("有效时间 : ", sTime, " 秒"));
		}
	}

}
