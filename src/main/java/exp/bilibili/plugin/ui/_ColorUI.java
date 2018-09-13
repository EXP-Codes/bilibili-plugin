package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import exp.bilibili.plugin.envm.ChatColor;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 弹幕颜色选择窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _ColorUI extends PopChildWindow {

	/** serialVersionUID */
	private static final long serialVersionUID = -5691969159309932864L;

	private final static int WIDTH = 440;
	
	private final static int HEIGHT = 120;
	
	private JButton whiteBtn;
	
	private JButton redBtn;
	
	private JButton blueBtn;
	
	private JButton purpleBtn;
	
	private JButton cyanBtn;
	
	private JButton greenBtn;
	
	private JButton yellowBtn;
	
	private JButton orangeBtn;
	
	private JButton pinkBtn;
	
	private JButton goldBtn;
	
	protected _ColorUI() {
		super("弹幕颜色", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.whiteBtn = new JButton("●");
		whiteBtn.setForeground(ChatColor.WHITE.COLOR());
		
		this.redBtn = new JButton("●");
		redBtn.setForeground(ChatColor.RED.COLOR());
		
		this.blueBtn = new JButton("●");
		blueBtn.setForeground(ChatColor.BLUE.COLOR());
		
		this.purpleBtn = new JButton("●");
		purpleBtn.setForeground(ChatColor.PURPLE.COLOR());
		
		this.cyanBtn = new JButton("●");
		cyanBtn.setForeground(ChatColor.CYAN.COLOR());
		
		this.greenBtn = new JButton("●");
		greenBtn.setForeground(ChatColor.GREEN.COLOR());
		
		this.yellowBtn = new JButton("●");
		yellowBtn.setForeground(ChatColor.YELLOW.COLOR());
		
		this.orangeBtn = new JButton("●");
		orangeBtn.setForeground(ChatColor.ORANGE.COLOR());
		
		this.pinkBtn = new JButton("●");
		pinkBtn.setForeground(ChatColor.PINK.COLOR());
		
		this.goldBtn = new JButton("●");
		goldBtn.setForeground(ChatColor.GOLD.COLOR());
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(SwingUtils.getHGridPanel(
				whiteBtn, redBtn, blueBtn, purpleBtn, cyanBtn, 
				greenBtn, yellowBtn, orangeBtn, pinkBtn, goldBtn
		), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		whiteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.WHITE);
			}
		});
		
		redBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.RED);
			}
		});

		blueBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.BLUE);
			}
		});

		purpleBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.PURPLE);
			}
		});

		cyanBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.CYAN);
			}
		});

		greenBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.GREEN);
			}
		});

		yellowBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.YELLOW);
			}
		});

		orangeBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.ORANGE);
			}
		});

		pinkBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.PINK);
			}
		});

		goldBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(ChatColor.GOLD);
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
