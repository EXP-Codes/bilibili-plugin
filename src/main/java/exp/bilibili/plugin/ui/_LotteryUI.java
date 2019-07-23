package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.cache.OnlineUserMgr;
import exp.bilibili.plugin.utils.SwingUtils;
import exp.libs.envm.Charset;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.RandomUtils;
import exp.libs.utils.time.TimeUtils;
import exp.libs.warp.cmd.CmdUtils;
import exp.libs.warp.thread.LoopThread;
import exp.libs.warp.tpl.Template;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 直播间在线用户抽奖器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _LotteryUI extends PopChildWindow {

	private final static long serialVersionUID = -4322589966897649896L;

	/** 3D抽奖姬-页面模板路径 */
	private final static String _3D_TPL_PATH = "./conf/web/template.html";
	
	/** 3D抽奖姬-页面路径 */
	private final static String _3D_PAGE_PATH = "./conf/web/lucky-princess.html";
	
	private final static int WIDTH = 800;
	
	private final static int HEIGHT = 400;
	
	private final static int ROW = 10;
	
	private final static int COL = 4;
	
	private List<String> users;
	
	private JPanel userPanel;
	
	private JButton luckyBtn;
	
	private JTextField luckyTF;
	
	private JTextArea luckyTA;
	
	private boolean isLottery;
	
	private NameViewer viewer;
	
	/** 切换到3D标签云抽奖模式 */
	private JButton to3DBtn;
	
	protected _LotteryUI() {
		super("直播间活跃用户抽奖", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.users = new LinkedList<String>();
		this.userPanel = new JPanel(new GridLayout(ROW, COL));
		SwingUtils.addBorder(userPanel, "在线活跃用户列表  (仅随机显示" +  (ROW * COL) + "名)");
		
		this.luckyBtn = new JButton("抽奖人数:" + users.size());
		luckyBtn.setForeground(Color.RED);
		
		this.luckyTF = new JTextField();
		luckyTF.setForeground(Color.RED);
		luckyTF.setEditable(false);
		
		this.luckyTA = new JTextArea(1, 20);
		luckyTA.setEditable(false);
		
		this.isLottery = false;
		
		this.to3DBtn = new JButton("3D模式");
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, to3DBtn);
		to3DBtn.setForeground(Color.WHITE);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(userPanel, BorderLayout.CENTER);
		rootPanel.add(getLotteryPanel(), BorderLayout.SOUTH);
		rootPanel.add(getLuckyPanel(), BorderLayout.EAST);
	}
	
	private JPanel getLotteryPanel() {
		JPanel panel = new JPanel(new BorderLayout()); {
			panel.add(SwingUtils.getHGridPanel(
						new JLabel(" "),
						luckyBtn,
						new JLabel("[小幸运]: ", JLabel.RIGHT),
						luckyTF,
						SwingUtils.getEBorderPanel(new JLabel(" "), to3DBtn)
				), 
				BorderLayout.CENTER
			);
		}
		SwingUtils.addBorder(panel, "下一个小幸运是你吗？");
		return panel;
	}
	
	private JPanel getLuckyPanel() {
		JPanel panel = new JPanel(new BorderLayout()); {
			panel.add(SwingUtils.addAutoScroll(luckyTA), BorderLayout.CENTER);
		}
		SwingUtils.addBorder(panel, "小幸运们");
		return panel;
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		setLuckyBtnListener();
		set3DBtnListener();
	}
	
	private void setLuckyBtnListener() {
		luckyBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// 触发开始抽奖事件
				if(isLottery == false) {
					if(users.size() > 1) {
						isLottery = true;
						startLotteryEvent();
					} else {
						SwingUtils.warn("人数不足, 无法抽奖");
					}
					
				// 触发停止抽奖事件
				} else {
					stopLotteryEvent();
					isLottery = false;
				}
				
				ThreadUtils.tSleep(200); // 避免连续点击
			}
		});
	}
	
	private void startLotteryEvent() {
		luckyBtn.setText("祈祷吧！");
		
		viewer = new NameViewer();
		viewer._start();
	}

	private void stopLotteryEvent() {
		if(viewer == null) {
			return;
		}
		viewer._stop();
		viewer._join();
		String username = luckyTF.getText();
		users.remove(username);
		
		luckyBtn.setText("抽奖人数:" + users.size());
		toLuckys(username);
		SwingUtils.info("恭喜 [" + username + "] 中奖!!!");
	}
	
	private void toLuckys(String username) {
		luckyTA.append(username);
		luckyTA.append("\r\n");
		SwingUtils.toEnd(luckyTA);
	}
	
	private void set3DBtnListener() {
		to3DBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder tags = new StringBuilder();
				for(String user : users) {
					tags.append("       <a href=\"#\" title=\"").append(user);
					tags.append("\">").append(user).append("</a>\r\n");
				}
				
				// 根据在线用户更新3D抽奖页面
				Template tpl = new Template(_3D_TPL_PATH, Charset.UTF8);
				tpl.set("date", TimeUtils.getSysDate());
				tpl.set("tags", tags.toString());
				
				if(FileUtils.write(_3D_PAGE_PATH, tpl.getContent(), Charset.UTF8, false)) {
					CmdUtils.execute("cmd /c start ".concat(_3D_PAGE_PATH));	// 打开3D抽奖界面
					_hide();	// 隐藏2D抽奖界面
					
				} else {
					SwingUtils.warn("召唤3D抽奖姬失败 o(>_<)o");
				}
			}
		});
	}
	
	@Override
	protected void AfterView() {
		if(isLottery == true) {
			return;	// 若在抽奖中, 则不更新用户表
		}
		
		refreshUsers();
		refreshUserPanel();
		
		luckyBtn.setText("抽奖人数:" + users.size());
		luckyTF.setText("");
	}

	private void refreshUsers() {
		users.clear();
		users = OnlineUserMgr.getInstn().getAllOnlineUsers();
	}
	
	private void refreshUserPanel() {
		userPanel.removeAll();
		
		int size = ROW * COL;
		size = (size > users.size() ? users.size() : size);
		for(int i = 0; i < size; i++) {
			userPanel.add(new JLabel(users.get(i)), i);
		}
	}
	
	@Override
	protected void beforeHide() {
		// TODO Auto-generated method stub
		
	}
	
	protected void clear() {
		users.clear();
		if(viewer != null) {
			viewer._stop();
			OnlineUserMgr.getInstn().clear();
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	/**
	 * <PRE>
	 * 名字闪现器
	 * </PRE>
	 * <br/><B>PROJECT : </B> bilibili-plugin
	 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
	 * @version   2017-12-17
	 * @author    EXP: 272629724@qq.com
	 * @since     jdk版本：jdk1.6
	 */
	private class NameViewer extends LoopThread {

		protected NameViewer() {
			super("名字随机闪现器");
		}

		@Override
		protected void _before() {}

		@Override
		protected void _loopRun() {
			int idx = RandomUtils.genInt(users.size());
			String username = users.get(idx);
			luckyTF.setText(username);
		}

		@Override
		protected void _after() {}
		
	}
	
}
