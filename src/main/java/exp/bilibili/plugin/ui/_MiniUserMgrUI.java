package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.pnl.ADPanel;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 小号账号管理窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _MiniUserMgrUI extends PopChildWindow {
	
	private static final long serialVersionUID = 4379374798564622516L;

	private final static int WIDTH = 900;
	
	private final static int HEIGHT = 600;
	
	/** 上限挂机人数 */
	private final static int MAX_USER = CookiesMgr.MAX_MINI_NUM;
	
	private JTextField mainRoomTF;
	
	private JButton mainRoomBtn;
	
	private JRadioButton mainAutoFeed;
	
	private JTextField vestRoomTF;
	
	private JButton vestRoomBtn;
	
	private JRadioButton vestAutoFeed;
	
	private ADPanel<__MiniUserLine> adPanel;
	
	private JLabel userLabel;
	
	private JButton feedBtn;
	
	private boolean autoFeed;
	
	private boolean init;
	
	public _MiniUserMgrUI() {
		super("哔哩哔哩-账号管理列表", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.mainRoomTF = new JTextField();
		this.mainRoomBtn = new JButton("修改");
		this.mainAutoFeed = new JRadioButton("自动投喂 [房号]: ");
		this.vestRoomTF = new JTextField();
		this.vestRoomBtn = new JButton("修改");
		this.vestAutoFeed = new JRadioButton("自动投喂 [房号]: ");
		
		
		this.userLabel = new JLabel("0/".concat(String.valueOf(MAX_USER)));
		userLabel.setForeground(Color.RED);
		
		this.feedBtn = new JButton("自动投喂");
		feedBtn.setForeground(Color.BLACK);
		
		this.adPanel = new ADPanel<__MiniUserLine>(__MiniUserLine.class, MAX_USER);
		this.autoFeed = false;
		this.init = false;
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(getNorthPanel(), BorderLayout.NORTH);
		rootPanel.add(getCenterPanel(), BorderLayout.CENTER);
		rootPanel.add(getSouthPanel(), BorderLayout.SOUTH);
	}

	private JPanel getNorthPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(SwingUtils.addBorder(SwingUtils.getWEBorderPanel(
				mainAutoFeed, mainRoomTF, mainRoomBtn), "主号"), 0);
		panel.add(SwingUtils.addBorder(SwingUtils.getWEBorderPanel(
				vestAutoFeed, vestRoomTF, vestRoomBtn), "马甲号"), 1);
		return panel;
	}
	
	private JScrollPane getCenterPanel() {
		return SwingUtils.addBorder(adPanel.getJScrollPanel(), "小号列表");
	}
	
	private JPanel getSouthPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel);
		panel.add(SwingUtils.getPairsPanel("挂机数", userLabel), BorderLayout.WEST);
		panel.add(SwingUtils.getWBorderPanel(feedBtn, new JLabel("   ")), BorderLayout.CENTER);
		return panel;
	}
	
	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		setAutoFeedListener(true);
		setAutoFeedListener(false);
		
		feedBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				autoFeed = !autoFeed;
				if(autoFeed == true) {
					BeautyEyeUtils.setButtonStyle(NormalColor.green, feedBtn);
					UIUtils.log("[自动投喂姬] 被召唤成功O(∩_∩)O");
					
				} else {
					BeautyEyeUtils.setButtonStyle(NormalColor.normal, feedBtn);
					UIUtils.log("[自动投喂姬] 被封印啦/(ㄒoㄒ)/");
				}
			}
		});
	}
	
	private void setAutoFeedListener(final boolean flag) {
		final JTextField roomTF = flag ? mainRoomTF : vestRoomTF;
		final JButton roomBtn = flag ? mainRoomBtn : vestRoomBtn;
		final JRadioButton autoFeed = flag ? mainAutoFeed : vestAutoFeed;
		
		autoFeed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BiliCookie cookie = flag ? CookiesMgr.MAIN() : CookiesMgr.VEST();
				if(cookie == BiliCookie.NULL) {
					return;
				}
				
				cookie.setAutoFeed(autoFeed.isSelected());
				CookiesMgr.getInstn().update(cookie);
			}
		});
		
		roomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BiliCookie cookie = flag ? CookiesMgr.MAIN() : CookiesMgr.VEST();
				if(cookie == BiliCookie.NULL) {
					return;
				}
				
				String sRoomId = roomTF.getText().trim();
				int roomId = NumUtils.toInt(sRoomId, 0);
				if(RoomMgr.getInstn().isExist(roomId)) {
					cookie.setFeedRoomId(roomId);
					CookiesMgr.getInstn().update(cookie);
					
					String msg = StrUtils.concat("[", cookie.NICKNAME(), 
							"] 的 [投喂房间号] 变更为: ", roomId);
					SwingUtils.info(msg);
					UIUtils.log(msg);
					
				} else {
					SwingUtils.warn("无效的房间号: ".concat(sRoomId));
					roomTF.setText(String.valueOf(cookie.getFeedRoomId()));
				}
			}
		});
	}

	protected void init() {
		if(init == false) {
			init = true;
			int idx = 0;
			CookiesMgr.getInstn().load(CookieType.MINI);
			Set<BiliCookie> cookies = CookiesMgr.MINIs();
			for(BiliCookie cookie : cookies) {
				__MiniUserLine line = new __MiniUserLine(cookie);
				adPanel.set(line, idx++);
			}
			updateUserCount();
		}
	}
	
	@Override
	protected void AfterView() {
		updateUserCount();
	}
	
	private void updateUserCount() {
		String text = StrUtils.concat(CookiesMgr.MINI_SIZE(), "/", MAX_USER);
		userLabel.setText(text);
	}
	
	@Override
	protected void beforeHide() {
		// Undo
		
	}

	protected boolean isAutoFeed() {
		return autoFeed;
	}
	
}
