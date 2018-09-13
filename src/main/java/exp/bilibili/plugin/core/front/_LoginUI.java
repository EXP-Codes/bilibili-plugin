package exp.bilibili.plugin.core.front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.LoginMgr;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 帐密登陆窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _LoginUI extends PopChildWindow {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1752327112586227761L;

	private final static String TIPS_PATH = Config.getInstn().IMG_DIR().concat("/vcTips.jpg");
	
	private final static String VCIMG_PATH = Config.getInstn().IMG_DIR().concat("/vccode.jpg");
	
	private final static int WIDTH = 400;
	
	private final static int HEIGH = 300;
	
	private JTextField usernameTXT;
	
	private JPasswordField passwordTXT;
	
	private JButton viewBtn;
	
	private JTextField vccodeTXT;
	
	private JLabel imgLabel;
	
	private JButton reflashBtn;
	
	private JButton loginBtn;
	
	/** 与验证码配套的登陆用cookie */
	private String vcCookies;
	
	/** 是否用于登录马甲号 */
	private boolean isMini;
	
	/**
	 * 
	 * @param isMini 是否用于登录马甲号
	 */
	public _LoginUI(boolean isMini) {
		super("B站PC端帐密登陆", WIDTH, HEIGH, false, isMini);
	}
	
	@Override
	protected void initComponents(Object... args) {
		if(args != null && args.length > 0) {
			this.isMini = (Boolean) args[0];
		} else {
			this.isMini = false;
		}
		
		this.usernameTXT = new JTextField();
		this.passwordTXT = new JPasswordField();
		SwingUtils.hide(passwordTXT);
		this.vccodeTXT = new JTextField();
		
		this.viewBtn = new JButton(new ImageIcon(
				_LoginUI.class.getResource("/exp/bilibili/plugin/core/front/eye.png")));
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, viewBtn);
		
		this.reflashBtn = new JButton(new ImageIcon(
				_LoginUI.class.getResource("/exp/bilibili/plugin/core/front/reflash.png")));
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, reflashBtn);
		
		ImageIcon icon = new ImageIcon(TIPS_PATH);
		icon = new ImageIcon(modifySize(icon.getImage()));
		this.imgLabel = new JLabel(icon);
		
		this.loginBtn = new JButton("登陆 哔哩哔哩");
		BeautyEyeUtils.setButtonStyle(NormalColor.green, loginBtn);
		loginBtn.setForeground(Color.BLACK);
		this.vcCookies = "";
	}
	
	private Image modifySize(Image img) {
		return img.getScaledInstance(130, 35, Image.SCALE_FAST);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(toAccountPanel(), BorderLayout.CENTER);
		rootPanel.add(loginBtn, BorderLayout.SOUTH);
	}

	/**
	 * 获取帐密面板
	 * @return
	 */
	private JPanel toAccountPanel() {
		JPanel panel = new JPanel(new GridLayout(7, 1)); {
			panel.add(new JLabel(), 0);
			panel.add(SwingUtils.getWEBorderPanel(
					new JLabel("  [账号] :  "), usernameTXT, 
					new JLabel("   ")), 1);
			panel.add(new JLabel(), 2);
			panel.add(SwingUtils.getWEBorderPanel(
					new JLabel("  [密码] :  "), passwordTXT, 
					SwingUtils.getEBorderPanel(viewBtn, new JLabel("   "))), 3);
			panel.add(new JLabel(), 4);
			panel.add(SwingUtils.getWEBorderPanel(
					new JLabel("  [验证] :  "), 
					SwingUtils.getEBorderPanel(vccodeTXT, imgLabel), 
					SwingUtils.getEBorderPanel(reflashBtn, new JLabel("   "))), 5);
			panel.add(new JLabel(), 6);
		} SwingUtils.addBorder(panel);
		return panel;
	}
	
	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		
		// 设置密码可视按钮监听
		viewBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtils.hide(passwordTXT);	// 鼠标释放时隐藏明文
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				SwingUtils.view(passwordTXT);	// 鼠标按下时显示明文
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
		});
				
		// 设置登陆按钮监听
		loginBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginBtn.setEnabled(false);
				
				String username = usernameTXT.getText();
				String password = new String(passwordTXT.getPassword());
				String vccode = vccodeTXT.getText();
				
				if(StrUtils.isEmpty(username)) {
					SwingUtils.warn("账号不能为空");
					
				} else if(StrUtils.isEmpty(password)) {
					SwingUtils.warn("密码不能为空");
					
				} else if(StrUtils.isEmpty(vccode)) {
					SwingUtils.warn("验证码不能为空");
					
				} else {
					toLogin(username, password, vccode);
				}
				
				loginBtn.setEnabled(true);
			}
		});
		
		// 设置二维码刷新按钮监听
		reflashBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateImg();
				ThreadUtils.tSleep(200);
			}
		});
	}
	
	private void toLogin(String username, String password, String vccode) {
		boolean isOk = false;
		if(isMini == true) {
			isOk = StrUtils.isNotEmpty(LoginMgr.getInstn().toLoginMini(
					username, password, vccode, vcCookies));
			
		} else {
			isOk = LoginMgr.getInstn().toLogin(
					username, password, vccode, vcCookies);
		}
		
		if(isOk == false) {
			SwingUtils.warn("登陆失败: 账号/密码/验证码错误");
			reflashBtn.doClick();
			
		} else {
			_hide();
		}
	}
	
	private void updateImg() {
		this.vcCookies = LoginMgr.getInstn().downloadVccode();
		
		// 注意: 这里不能通过new ImageIcon(ImgPath)的方式更新图片
		// 因为这种方式会因为图片路径没有变化, 而不去更新缓存, 导致显示的二维码一直不变
		Image img = Toolkit.getDefaultToolkit().createImage(VCIMG_PATH);
		imgLabel.setIcon(new ImageIcon(modifySize(img)));
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
