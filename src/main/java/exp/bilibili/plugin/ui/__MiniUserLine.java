package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.ui.login.LoginBtn;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.SwingUtils;

/**
 * <PRE>
 * 小号账号管理窗口的单行组件
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class __MiniUserLine extends JPanel {

	private final static long serialVersionUID = -8472154443768267316L;
	
	private JTextField roomTF;
	
	private JButton roomBtn;
	
	private JTextField usernameTF;
	
	private LoginBtn loginBtn;
	
	private JRadioButton autoFeed;
	
	private BiliCookie miniCookie;
	
	public __MiniUserLine() {
		this(BiliCookie.NULL);
	}
	
	public __MiniUserLine(BiliCookie cookie) {
		super(new BorderLayout());
		
		this.miniCookie = cookie;
		init();
	}
	
	private void init() {
		this.autoFeed = new JRadioButton("自动投喂");
		setAutoFeedBtnListener();
		
		this.roomTF = new JTextField();
		this.roomBtn = new JButton("修改");
		roomBtn.setForeground(Color.BLACK);
		setRoomBtnListener();
		
		this.usernameTF = new JTextField();
		usernameTF.setEditable(false);
		
		this.loginBtn = new LoginBtn(CookieType.MINI, "登陆", new Callback());
		if(loginBtn.markLogined(miniCookie)) {	
			_afterLogin();	// 自动登陆时触发
		}
		
		// 布局
		add(SwingUtils.getHGridPanel(
				SwingUtils.getEBorderPanel(
						SwingUtils.getPairsPanel("昵称", usernameTF), loginBtn.getButton()),
				SwingUtils.getEBorderPanel(
						SwingUtils.getPairsPanel("投喂房号", roomTF), roomBtn)), 
				BorderLayout.CENTER);
		add(autoFeed, BorderLayout.EAST);
		SwingUtils.addBorder(this);
	}
	
	private class Callback implements __LoginCallback {

		@Override
		public void afterLogin(final BiliCookie cookie) {
			miniCookie = cookie;
			_afterLogin();	// 手动登陆时触发
		}

		@Override
		public void afterLogout(final BiliCookie cookie) {
			miniCookie.setAutoFeed(false);
			miniCookie = BiliCookie.NULL;
			
			autoFeed.setSelected(false);
			roomTF.setText("");
			usernameTF.setText("");
		}
		
	}
	
	private void _afterLogin() {
		autoFeed.setSelected(miniCookie.isAutoFeed());
		roomTF.setText(String.valueOf(miniCookie.getFeedRoomId()));
		usernameTF.setText(miniCookie.NICKNAME());
		usernameTF.setToolTipText(miniCookie.UID());
	}
	
	private void setAutoFeedBtnListener() {
		autoFeed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(miniCookie != BiliCookie.NULL) {
					miniCookie.setAutoFeed(autoFeed.isSelected());
					CookiesMgr.getInstn().update(miniCookie);
				}
			}
		});
	}

	private void setRoomBtnListener() {
		roomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(miniCookie == BiliCookie.NULL) {
					return;
				}
				
				String sRoomId = roomTF.getText().trim();
				int roomId = NumUtils.toInt(sRoomId, 0);
				if(RoomMgr.getInstn().isExist(roomId)) {
					miniCookie.setFeedRoomId(roomId);
					CookiesMgr.getInstn().update(miniCookie);
					
					String msg = StrUtils.concat("[", miniCookie.NICKNAME(), 
							"] 的 [投喂房间号] 变更为: ", roomId);
					SwingUtils.info(msg);
					UIUtils.log(msg);
					
				} else {
					SwingUtils.warn("无效的房间号: ".concat(sRoomId));
					roomTF.setText(String.valueOf(miniCookie.getFeedRoomId()));
				}
			}
		});
	}
	
}
