package exp.bilibili.plugin.ui.login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.ui.__LoginCallback;
import exp.bilibili.plugin.utils.SwingUtils;

/**
 * <PRE>
 * 登陆按钮：
 * 	封装了 二维码扫码登陆 和 帐密登陆 两种方式
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LoginBtn {

	private final static String LOGOUT_TIPS = "注销";
	
	private String loginTips;
	
	private CookieType type;
	
	private JButton btn;
	
	private __LoginCallback callback;
	
	private BiliCookie cookie;
	
	private QRLoginUI qrLoginUI;
	
	private VCLoginUI vcLoginUI;
	
	public LoginBtn(CookieType type) {
		this(type, "", null);
	}
	
	public LoginBtn(CookieType type, String btnName) {
		this(type, btnName, null);
	}
	
	public LoginBtn(CookieType type, String btnName, __LoginCallback callback) {
		this.type = type;
		this.callback = callback;
		this.cookie = BiliCookie.NULL;
		
		this.loginTips = btnName;
		this.btn = new JButton(btnName);
		btn.setForeground(Color.BLACK);
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(LOGOUT_TIPS.equals(btn.getText())) {
					logout();
					
				} else {
					login();
				}
			}
		});
		
		initVCLoginUI();
	}
	
	private void initQRLoginUI() {
		this.qrLoginUI = new QRLoginUI(type);
		qrLoginUI.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				cookie = qrLoginUI.getCookie();
				if(cookie.isVaild()) {
					btn.setText(LOGOUT_TIPS);
					if(callback != null) {
						callback.afterLogin(cookie);
					}
				}
			}
		});
	}
	
	private void initVCLoginUI() {
		this.vcLoginUI = new VCLoginUI(type);
		vcLoginUI.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				cookie = vcLoginUI.getCookie();
				if(cookie.isVaild()) {
					btn.setText(LOGOUT_TIPS);
					if(callback != null) {
						callback.afterLogin(cookie);
					}
				}
			}
		});
	}
	
	private void login() {
		if(SwingUtils.confirm("请选择登陆方式 : ", "扫码登陆 (1天)", "帐密登陆 (30天)")) {
			initQRLoginUI();	// QR登陆时的检测线程不能重复启动，只能每次新建对象
			qrLoginUI._view();
			
		} else {
			vcLoginUI._view();
		}
	}
	
	private void logout() {
		if(SwingUtils.confirm("注销登陆 ?") && CookiesMgr.getInstn().del(cookie)) {
			btn.setText(loginTips);
			if(callback != null) {
				callback.afterLogout(cookie);
			}
		}
	}
	
	public JButton getButton() {
		return btn;
	}
	
	public void doClick() {
		btn.doClick();
	}
	
	public boolean markLogined(BiliCookie cookie) {
		boolean isOk = false;
		if(cookie.isVaild()) {
			isOk = true;
			this.cookie = cookie;
			btn.setText(LOGOUT_TIPS);
		}
		return isOk;
	}
	
	public BiliCookie getCookie() {
		return cookie;
	}
	
}
