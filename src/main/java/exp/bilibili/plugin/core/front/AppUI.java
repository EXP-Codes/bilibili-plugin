package exp.bilibili.plugin.core.front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.ActivityMgr;
import exp.bilibili.plugin.cache.Browser;
import exp.bilibili.plugin.cache.ChatMgr;
import exp.bilibili.plugin.cache.LoginMgr;
import exp.bilibili.plugin.cache.MsgKwMgr;
import exp.bilibili.plugin.cache.OnlineUserMgr;
import exp.bilibili.plugin.cache.RedbagMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.cache.StormScanner;
import exp.bilibili.plugin.core.back.MsgSender;
import exp.bilibili.plugin.core.back.WebSockClient;
import exp.bilibili.plugin.envm.ChatColor;
import exp.bilibili.plugin.envm.Level;
import exp.bilibili.plugin.monitor.SafetyMonitor;
import exp.bilibili.plugin.utils.SafetyUtils;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.envm.Charset;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.MainWindow;


/**
 * <PRE>
 * 主应用程序窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class AppUI extends MainWindow {

	/** serialVersionUID */
	private final static long serialVersionUID = 2097374309672044616L;

	/** 避免连续点击按钮的锁定时间 */
	private final static long LOCK_TIME = 100;
	
	/** 界面文本框最大缓存行数 */
	private final static int MAX_LINE = 200;
	
	private final static String LINE_END = "\r\n";
	
	private final static int WIDTH = 1024;
	
	private final static int HEIGHT = 600;
	
	private final static int CHAT_LIMIT = 20;
	
	private JButton defaultBtn;
	
	private JButton linkBtn;
	
	private JButton lotteryBtn;
	
	private JButton activeListBtn;
	
	private JButton loginBtn;
	
	private JButton stormBtn;
	
	private JButton addUserBtn;
	
	private JButton clrBtn;
	
	private JButton sendBtn;
	
	private JButton colorBtn;
	
	private JButton thxBtn;
	
	private JButton noticeBtn;
	
	private JButton eNoticeBtn;
	
	private JButton callBtn;
	
	private JButton eCallBtn;
	
	private JButton nightBtn;
	
	private JButton redbagBtn;
	
	private JTextField httpTF;
	
	private JTextField ridTF;
	
	private JTextArea chatTA;
	
	private JTextField chatTF;
	
	private JTextArea consoleTA;
	
	private JTextArea notifyTA;
	
	private JTextArea sttcTA;
	
	private int lotteryCnt;
	
	private JLabel lotteryLabel;
	
	private WebSockClient wsClient;
	
	private _LotteryUI lotteryUI;
	
	private _LoginUI loginUI;
	
	private _QrcodeUI qrcodeUI;
	
	private _RedbagUI redbagUI;
	
	private _ColorUI colorUI;
	
	private ChatColor curChatColor;
	
	private String loginUser;
	
	private boolean isLogined;
	
	private static volatile AppUI instance;
	
	private AppUI() {
		super("哔哩哔哩插件姬 - By 亚絲娜", WIDTH, HEIGHT);
	}
	
	/**
	 * 创建实例
	 * @param args main入参
	 */
	public static void createInstn(String[] args) {
		checkIdentity(args);
		getInstn();
	}
	
	/**
	 * 身份校验
	 * @param args main入参
	 */
	public static void checkIdentity(String[] args) {
		
		// 管理员: 无条件开启所有功能
		if(args == null || args.length <= 0) {	
			if(FileUtils.exists("./doc/icon.ico")) {	// 发布的项目是不存在doc文件夹的, 避免管理员权限泄露
				Config.LEVEL = Level.ADMIN;
				
			} else {
				SwingUtils.warn("很明显你是假的管理员");
				System.exit(0);
			}
			
		// 用户
		} else {
			String code = SwingUtils.input("请输入注册码");
			String errMsg = SafetyUtils.checkAC(code);
			if(StrUtils.isNotEmpty(errMsg)) {
				SwingUtils.warn(errMsg);
				System.exit(0);
				
			} else if(args.length > 1) {
				Config.LEVEL = Level.UPLIVE;
			}
		}
	}
	
	/**
	 * 获取单例
	 * @return
	 */
	public static AppUI getInstn() {
		if(instance == null) {
			synchronized (AppUI.class) {
				if(instance == null) {
					instance = new AppUI();
				}
			}
		}
		return instance;
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.chatTF = new JTextField();
		this.httpTF = new JTextField("http://live.bilibili.com/");
		this.ridTF = new JTextField(String.valueOf(Config.getInstn().SIGN_ROOM_ID()), 15);
		chatTF.setToolTipText("内容长度限制: 20");
		httpTF.setEditable(false);
		
		this.defaultBtn = new JButton("★");
		this.linkBtn = new JButton("偷窥直播间 (无需登陆)");
		this.lotteryBtn = new JButton("抽奖姬 (发起直播间抽奖)");
		this.activeListBtn = new JButton("☷");
		this.loginBtn = new JButton("扫码/帐密登陆 (自动抽奖)");
		this.stormBtn = new JButton("节奏风暴扫描");
		this.addUserBtn = new JButton("╋");
		this.clrBtn = new JButton("清");
		this.sendBtn = new JButton("发言");
		this.colorBtn = new JButton("●");
		this.thxBtn = new JButton("答谢姬");
		this.noticeBtn = new JButton("公告姬");
		this.eNoticeBtn = new JButton(">");
		this.callBtn = new JButton("小call姬");
		this.eCallBtn = new JButton(">");
		this.nightBtn = new JButton("晚安姬");
		this.redbagBtn = new JButton("红包兑奖姬");
		defaultBtn.setToolTipText("设为默认");
		defaultBtn.setForeground(Color.MAGENTA);
		linkBtn.setForeground(Color.BLACK);
		lotteryBtn.setForeground(Color.BLACK);
		activeListBtn.setForeground(Color.BLUE);
		loginBtn.setForeground(Color.BLACK);
		stormBtn.setForeground(Color.BLACK);
		addUserBtn.setForeground(Color.BLACK);
		clrBtn.setForeground(Color.BLACK);
		sendBtn.setForeground(Color.BLACK);
		colorBtn.setForeground(ChatColor.BLUE.COLOR());
		thxBtn.setForeground(Color.BLACK);
		noticeBtn.setForeground(Color.BLACK);
		eNoticeBtn.setForeground(Color.BLACK);
		callBtn.setForeground(Color.BLACK);
		eCallBtn.setForeground(Color.BLACK);
		nightBtn.setForeground(Color.BLACK);
		redbagBtn.setForeground(Color.BLACK);
		
		this.chatTA = new JTextArea();
		this.consoleTA = new JTextArea(8, 10);
		this.notifyTA = new JTextArea(1, 40);
		this.sttcTA = new JTextArea(10, 40);
		chatTA.setEditable(false);
		consoleTA.setEditable(false);
		notifyTA.setEditable(false);
		sttcTA.setEditable(false);
		
		this.lotteryCnt = 0;
		this.lotteryLabel = new JLabel(" 00000 ");
		lotteryLabel.setForeground(Color.RED);
		
		this.wsClient = new WebSockClient();
		this.lotteryUI = new _LotteryUI();
		this.qrcodeUI = new _QrcodeUI();
		this.redbagUI = new _RedbagUI();
		this.colorUI = new _ColorUI();
		this.curChatColor = ChatColor.WHITE;
		
		this.loginUser = "";
		this.isLogined = false;
		printVersionInfo();
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(getLeftPanel(), BorderLayout.CENTER);
		rootPanel.add(getRightPanel(), BorderLayout.EAST);
	}
	
	private JPanel getLeftPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(_getLinkPanel(), BorderLayout.NORTH);
		panel.add(_getLivePanel(), BorderLayout.CENTER);
		panel.add(_getConsolePanel(), BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel _getLinkPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		SwingUtils.addBorder(panel);
		panel.add(SwingUtils.getHGridPanel(linkBtn, 
				SwingUtils.getEBorderPanel(lotteryBtn, activeListBtn)), 0);
		
		JPanel livePanel = new JPanel(new BorderLayout()); {
			livePanel.add(SwingUtils.getPairsPanel("直播间地址", httpTF), BorderLayout.CENTER);
			livePanel.add(SwingUtils.getEBorderPanel(
					SwingUtils.getPairsPanel("房间号", ridTF), defaultBtn), 
					BorderLayout.EAST);
		}
		panel.add(livePanel, 1);
		return panel;
	}
	
	private JPanel _getLivePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel, "直播间信息");
		panel.add(SwingUtils.addAutoScroll(chatTA), BorderLayout.CENTER);
		panel.add(_getSendPanel(), BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel _getSendPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(chatTF, BorderLayout.CENTER);
		panel.add(SwingUtils.getEBorderPanel(sendBtn, colorBtn), BorderLayout.EAST);
		return panel;
	}
	
	private JPanel _getConsolePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel, "运行日志");
		panel.add(SwingUtils.addAutoScroll(consoleTA), BorderLayout.CENTER);
		panel.add(_getCtrlPanel(), BorderLayout.EAST);
		return panel;
	}
	
	private JPanel _getCtrlPanel() {
		return SwingUtils.getVGridPanel(
				thxBtn, 
				SwingUtils.getEBorderPanel(noticeBtn, eNoticeBtn), 
				SwingUtils.getEBorderPanel(callBtn, eCallBtn), 
				nightBtn, redbagBtn);
	}
	
	private JPanel getRightPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(_getLoginPanel(), BorderLayout.NORTH);
		panel.add(_getNotifyPanel(), BorderLayout.CENTER);
		panel.add(_getStatisticsPanel(), BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel _getLoginPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel);
		panel.add(SwingUtils.getEBorderPanel(loginBtn, stormBtn), BorderLayout.CENTER);
		panel.add(SwingUtils.getHGridPanel(addUserBtn, clrBtn), BorderLayout.EAST);
		return panel;
	}
	
	private JPanel _getNotifyPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel, " 系统公告 ");
		panel.add(SwingUtils.addAutoScroll(notifyTA), BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel _getStatisticsPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SwingUtils.addBorder(panel, " 抽奖统计 ");
		panel.add(SwingUtils.addAutoScroll(sttcTA), BorderLayout.CENTER);
		
		JPanel sumPanel = new JPanel(new BorderLayout()); {
			sumPanel.add(SwingUtils.getPairsPanel("自动参与抽奖次数累计", lotteryLabel), 
					BorderLayout.EAST);
		}
		panel.add(sumPanel, BorderLayout.SOUTH);
		return panel;
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		setDefaultBtnListener();
		setLinkBtnListener();
		setLotteryBtnListener();
		setActiveListBtnListener();
		setLoginBtnListener();
		setStormBtnListener();
		setAddUserBtnListener();
		setClrBtnListener();
		setSendBtnListener();
		setColorBtnListener();
		setThxBtnListener();
		setNoticeBtnListener();
		setCallBtnListener();
		setNightBtnListener();
		setRedbagBtnListener();
		setChatTFListener();
		setEditBtnListener();
	}
	
	private void setDefaultBtnListener() {
		defaultBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Config.LEVEL < Level.UPLIVE) {
					SwingUtils.warn("非主播用户没有这个技能哦::>_<::");
					return;
				}
				
				int roomId = getCurRoomId();
				int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
				if(realRoomId <= 0) {
					SwingUtils.warn("直播间房号无效/未收录");
					return;
				}
				
				if(Config.getInstn().setSignRoomId(roomId)) {
					linkBtn.doClick();
					SwingUtils.info("默认房间号变更为: ".concat(String.valueOf(roomId)));
				}
			}
		});
	}

	private void setLinkBtnListener() {
		linkBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int roomId = RoomMgr.getInstn().getRealRoomId(getCurRoomId());
				if(roomId <= 0) {
					SwingUtils.warn("直播间房号无效/未收录");
					return;
				}
				
				if(!wsClient.isRun()) {
					wsClient.reset(roomId);
					wsClient._start();
					
				} else {
					wsClient.relink(roomId);
				}
				
				chatTA.setText("");		// 清空版聊区
				OnlineUserMgr.getInstn().clear(); // 重连直播间时清空在线用户列表
				lockBtn();
			}
		});
	}
	
	private void setLotteryBtnListener() {
		lotteryBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lotteryUI.refreshUsers();
				lotteryUI._view();
				lockBtn();
			}
		});
	}
	
	private void setActiveListBtnListener() {
		activeListBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Config.LEVEL < Level.ADMIN) {
					SwingUtils.warn("您未被授权管理 [活跃值排行榜] 哦~");
					return;
				}
				
				ActivityMgr.getInstn().init();
				new _ActiveListUI()._view();
			}
		});
	}
	
	private void setLoginBtnListener() {
		loginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isLogined()) {
					loginBtn.setEnabled(false);
					return;
				}
				
				if(SwingUtils.confirm("请选择登陆方式 : ", "扫码登陆 (1天)", "帐密登陆 (30天)")) {
					_loginByQrcode();
					
				} else {
					_loginByVccode();
				}
			}
		});
	}
	
	/**
	 * 二维码扫码登陆
	 */
	private void _loginByQrcode() {
		qrcodeUI._view();
		
		if(LoginMgr.getInstn().isRun() == false) {
			UIUtils.log("正在连接登陆服务器, 请稍后...");
			LoginMgr.getInstn()._start();
		}
	}
	
	/**
	 * 验证码帐密登陆
	 */
	private void _loginByVccode() {
		if(loginUI != null) {
			loginUI._view();
			
		} else {
			UIUtils.log("正在连接登陆服务器, 请稍后...");
			new Thread() {
				public void run() {
					loginBtn.setEnabled(false);
					if(LoginMgr.getInstn().autoLogin()) {
						LoginMgr.getInstn().saveLoginInfo();
						
					} else {
						loginBtn.setEnabled(true);
						loginUI = new _LoginUI(false);
						loginUI._view();
					}
				};
			}.start();
		}
	}
	
	private void setStormBtnListener() {
		stormBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("登陆后才能使用此功能");
					return;
				}
				
				// 扫描器线程未启动，则触发登录马甲流程
				if(!StormScanner.getInstn().isRun()) {
					_loginMini();
					
				// 扫描器线程已启动，则纯粹切换扫描状态
				} else {
					StormScanner.getInstn().setScan();
					if(StormScanner.getInstn().isScan()) {
						BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, stormBtn);
						UIUtils.log("[全平台节奏风暴扫描] 已启动");
						
					} else {
						BeautyEyeUtils.setButtonStyle(NormalColor.normal, stormBtn);
						UIUtils.log("[全平台节奏风暴扫描] 已停止");
					}
				}
				lockBtn();
			}
		});
	}
	
	/**
	 * 登录马甲(用于扫描全平台节奏风暴)
	 */
	private void _loginMini() {
		
		// 使用马甲号登录
		if(SwingUtils.confirm("存在风险, 是否使用 [马甲号] 扫描 ? (收益归主号所有)")) {
			
			// 未登录过马甲号, 则登录一个马甲号
			if(StrUtils.isEmpty(FileUtils.read(LoginMgr.MINI_COOKIE_PATH, Charset.ISO))) {
				_LoginUI miniLogin = new _LoginUI(true);
				
				miniLogin.addWindowListener(new WindowAdapter() {
					
					@Override
					public void windowClosed(WindowEvent e) {
						if(StrUtils.isEmpty(FileUtils.read(LoginMgr.MINI_COOKIE_PATH, Charset.ISO))) {
							SwingUtils.warn("登录马甲失败, 终止扫描节奏风暴");
							
						// 使用新登录的马甲号(节奏风暴扫描器启动后会在内部识别)
						} else {
							_startStormScanner();
						}
					}
				});
				miniLogin._view();
				
			// 使用上次的马甲号(节奏风暴扫描器启动后会在内部识别)
			} else {
				_startStormScanner();
			}
			
		// 使用主号登录(节奏风暴扫描器启动后会在内部识别)
		} else {
			LoginMgr.getInstn().clearMiniCookie();
			_startStormScanner();
		}
	}
	
	private void _startStormScanner() {
		StormScanner.getInstn()._start();
		stormBtn.doClick();
		lockBtn();
	}
	
	private void setAddUserBtnListener() {
		addUserBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
	}
	
	private void setClrBtnListener() {
		clrBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SwingUtils.confirm("[清除登陆信息] 后下次需重新登陆, 继续吗 ?")) {
					if(LoginMgr.getInstn().clearAllCookies()) {
						SwingUtils.info("清除登陆信息成功");
						
					} else {
						SwingUtils.info("清除登陆信息失败");
					}
				}
			}
		});
	}
	
	private void setSendBtnListener() {
		sendBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能发言哦~");
					return;
				}
				
				String msg = chatTF.getText();
				int roomId = getCurRoomId();
				if(StrUtils.isNotEmpty(msg) && roomId > 0) {
					MsgSender.sendChat(msg, curChatColor, roomId);
					chatTF.setText("");
				}
			}
		});
	}
	
	private void setColorBtnListener() {
		colorBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorUI._view();
			}
		});
	}
	
	private void setThxBtnListener() {
		thxBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能召唤 [答谢姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.UPLIVE) {
					SwingUtils.warn("为了守护直播间秩序, 非主播用户无法召唤 [答谢姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.ADMIN && 
						Config.getInstn().isTabuAutoChat(getCurRoomId())) {
					SwingUtils.warn("您未被授权在此直播间使用 [答谢姬] 哦~");
					return;
				}
				
				ChatMgr.getInstn()._start();
				ChatMgr.getInstn().setAutoThankYou();
				if(ChatMgr.getInstn().isAutoThankYou()) {
					BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, thxBtn);
					UIUtils.log("[答谢姬] 被召唤成功O(∩_∩)O");
					
				} else {
					BeautyEyeUtils.setButtonStyle(NormalColor.normal, thxBtn);
					UIUtils.log("[答谢姬] 被封印啦/(ㄒoㄒ)/");
				}
				lockBtn();
			}
		});
	}
	
	private void setNoticeBtnListener() {
		noticeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能召唤 [公告姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.UPLIVE) {
					SwingUtils.warn("为了守护直播间秩序, 非主播用户无法召唤 [公告姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.ADMIN && 
						Config.getInstn().isTabuAutoChat(getCurRoomId())) {
					SwingUtils.warn("您未被授权在此直播间使用 [公告姬] 哦~");
					return;
				}
				
				ChatMgr.getInstn()._start();
				ChatMgr.getInstn().setAutoNotice();
				if(ChatMgr.getInstn().isAutoNotice()) {
					BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, noticeBtn);
					UIUtils.log("[公告姬] 被召唤成功O(∩_∩)O");
					
				} else {
					BeautyEyeUtils.setButtonStyle(NormalColor.normal, noticeBtn);
					UIUtils.log("[公告姬] 被封印啦/(ㄒoㄒ)/");
				}
				lockBtn();
			}
		});
	}
	
	private void setCallBtnListener() {
		callBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能召唤 [小call姬] 哦~");
					return;
				}
				
				ChatMgr.getInstn()._start();
				ChatMgr.getInstn().setAutoCall();
				if(ChatMgr.getInstn().isAutoCall()) {
					BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, callBtn);
					UIUtils.log("[小call姬] 被召唤成功O(∩_∩)O");
					
				} else {
					BeautyEyeUtils.setButtonStyle(NormalColor.normal, callBtn);
					UIUtils.log("[小call姬] 被封印啦/(ㄒoㄒ)/");
				}
				lockBtn();
			}
		});
	}
	
	private void setNightBtnListener() {
		nightBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能召唤 [晚安姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.UPLIVE) {
					SwingUtils.warn("为了守护直播间秩序, 非主播用户无法召唤 [晚安姬] 哦~");
					return;
					
				} else if(Config.LEVEL < Level.ADMIN && 
						Config.getInstn().isTabuAutoChat(getCurRoomId())) {
					SwingUtils.warn("您未被授权在此直播间使用 [晚安姬] 哦~");
					return;
				}
				
				ChatMgr.getInstn()._start();
				ChatMgr.getInstn().setAutoGoodNight();
				if(ChatMgr.getInstn().isAutoGoodNight()) {
					BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, nightBtn);
					UIUtils.log("[晚安姬] 被召唤成功O(∩_∩)O");
					
				} else {
					BeautyEyeUtils.setButtonStyle(NormalColor.normal, nightBtn);
					UIUtils.log("[晚安姬] 被封印啦/(ㄒoㄒ)/");
				}
				lockBtn();
			}
		});
	}
	
	private void setRedbagBtnListener() {
		redbagBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLogined()) {
					SwingUtils.warn("您是个有身份的人~ 先登录才能召唤 [红包兑奖姬] 哦~");
					return;
				}
				
				redbagUI._view();
			}
		});
	}
	
	private void setChatTFListener() {
		chatTF.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(chatTF.getText().length() > CHAT_LIMIT) {
					e.consume(); // 销毁新输入的字符，限制长度
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// Undo
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					sendBtn.doClick();// 监听到回车键则触发发送按钮
				}
			}
		});
	}
	
	private void setEditBtnListener() {
		eNoticeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Config.LEVEL < Level.UPLIVE) {
					return;
				}
				
				new _EditorUI("公告姬", Config.getInstn().NOTICE_PATH())._view();
				lockBtn();
			}
			
		});
		
		eCallBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new _EditorUI("打call姬", Config.getInstn().CALL_PATH())._view();
				lockBtn();
			}
			
		});
	}
	
	@Override
	protected void AfterView() {}

	@Override
	protected void beforeHide() {}
	
	@Override
	protected void beforeExit() {
		wsClient._stop();
		lotteryUI.clear();
		
		RedbagMgr.getInstn()._stop();
		StormScanner.getInstn()._stop();
		ChatMgr.getInstn()._stop();
		LoginMgr.getInstn()._stop();	
		WebBot.getInstn()._stop();
		MsgKwMgr.getInstn().clear();
		SafetyMonitor.getInstn()._stop();
		ActivityMgr.getInstn().save();
		
		Browser.quit();
	}
	
	
	public void toChat(String msg) {
		if(StrUtils.count(chatTA.getText(), '\n') >= MAX_LINE) {
			chatTA.setText("");
		}
		
		chatTA.append(msg.concat(LINE_END));
		SwingUtils.toEnd(chatTA);
	}
	
	public void toConsole(String msg) {
		if(StrUtils.count(consoleTA.getText(), '\n') >= MAX_LINE) {
			consoleTA.setText("");
		}
		
		consoleTA.append(msg.concat(LINE_END));
		SwingUtils.toEnd(consoleTA);
	}
	
	public void toNotify(String msg) {
		if(StrUtils.count(notifyTA.getText(), '\n') >= MAX_LINE) {
			notifyTA.setText("");
		}
		
		notifyTA.append(msg.concat(LINE_END));
		SwingUtils.toEnd(notifyTA);
	}
	
	public void toStatistics(String msg) {
		if(isLogined() == true) {
			if(StrUtils.count(sttcTA.getText(), '\n') >= MAX_LINE) {
				sttcTA.setText("");
			}
			
			sttcTA.append(msg.concat(LINE_END));
			SwingUtils.toEnd(sttcTA);
		}
	}
	
	/**
	 * 更新抽奖成功次数
	 */
	public void updateLotteryCnt(int num) {
		if(isLogined() && num > 0) {
			lotteryCnt += num;
			String cnt = StrUtils.leftPad(String.valueOf(lotteryCnt), '0', 5);
			lotteryLabel.setText(StrUtils.concat(" ", cnt, " "));
		}
	}
	
	/**
	 * 更新二维码图片
	 */
	public void updateQrcode() {
		qrcodeUI.updateImg();
	}
	
	/**
	 * 更新二维码有效时间
	 * @param time
	 */
	public void updateQrcodeTime(int time) {
		qrcodeUI.updateTime(time);
	}
	
	/**
	 * 标记已登陆成功
	 */
	public void markLogin(String username) {
		loginUser = username;
		isLogined = true;
		loginBtn.setEnabled(false);	
		
		if(loginUI != null) { loginUI._hide(); }
		qrcodeUI._hide();
		linkBtn.doClick();	// 登陆后自动连接到当前直播间
		WebBot.getInstn()._start();	// 启动仿真机器人
		
		updateTitle("0000-00-00");
		UIUtils.log("欢迎肥来: ".concat(loginUser));
		UIUtils.log("已激活全平台自动抽奖机能（包括小电视、高能抽奖等）");
		SwingUtils.info("登陆成功 (自动抽奖已激活)");
		
		// 开始监控软件授权
		SafetyMonitor.getInstn()._start();
	}
	
	/**
	 * 更新软件标题(用户名+授权时间)
	 */
	public void updateTitle(String certificateTime) {
		String title = StrUtils.concat(getTitle().replaceFirst("    \\[.*", ""), 
				"    [登陆用户 : ", loginUser, 
				"] [授权到期: ", certificateTime, "]");
		setTitle(title);
	}
	
	/**
	 * 检查是否已登录
	 * @return
	 */
	public boolean isLogined() {
		return isLogined;
	}	
	
	/**
	 * 获取当前监听的直播间地址
	 * @return
	 */
	public String getLiveUrl() {
		return StrUtils.concat(httpTF.getText(), ridTF.getText());
	}
	
	/**
	 * 获取当前监听的直播房间号
	 * @return
	 */
	public int getCurRoomId() {
		return NumUtils.toInt(ridTF.getText().trim());
	}
	
	/**
	 * 更新弹幕颜色
	 * @param color
	 */
	protected void updateChatColor(ChatColor color) {
		curChatColor = color;
		colorBtn.setForeground(color.COLOR());
		colorUI._hide();
		UIUtils.log("当前弹幕颜色 [", curChatColor.ZH(), "]");
	}
	
	public ChatColor getCurChatColor() {
		return curChatColor;
	}
	
	/**
	 * 瞬时锁定按钮，避免连续点击
	 */
	private void lockBtn() {
		ThreadUtils.tSleep(LOCK_TIME);
	}
	
	/**
	 * 打印授权版本信息
	 */
	public void printVersionInfo() {
		toConsole("**********************************************************");
		toConsole(" [亚絲娜] 享有本软件的完全著作权");
		toConsole(" 未经许可严禁擅自用于商业用途, 违者保留追究其法律责任的权利");
		toConsole("**********************************************************");
	}
	
}
