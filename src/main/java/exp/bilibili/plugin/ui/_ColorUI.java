package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import exp.bilibili.plugin.utils.SwingUtils;
import exp.libs.envm.Colors;
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

	private final static int WIDTH = 480;
	
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
	
	private JButton peachPinkBtn;
	
	private JButton goldBtn;
	
	protected _ColorUI() {
		super("弹幕颜色", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.whiteBtn = new JButton("●");
		whiteBtn.setForeground(Colors.WHITE.COLOR());
		whiteBtn.setToolTipText(Colors.WHITE.ZH());
		
		this.redBtn = new JButton("●");
		redBtn.setForeground(Colors.RED.COLOR());
		redBtn.setToolTipText(Colors.RED.ZH());
		
		this.blueBtn = new JButton("●");
		blueBtn.setForeground(Colors.BLUE.COLOR());
		blueBtn.setToolTipText(Colors.BLUE.ZH());
		
		this.purpleBtn = new JButton("●");
		purpleBtn.setForeground(Colors.PURPLE.COLOR());
		purpleBtn.setToolTipText(Colors.PURPLE.ZH());
		
		this.cyanBtn = new JButton("●");
		cyanBtn.setForeground(Colors.CYAN.COLOR());
		cyanBtn.setToolTipText(Colors.CYAN.ZH());
		
		this.greenBtn = new JButton("●");
		greenBtn.setForeground(Colors.GREEN.COLOR());
		greenBtn.setToolTipText(Colors.GREEN.ZH());
		
		this.yellowBtn = new JButton("●");
		yellowBtn.setForeground(Colors.YELLOW.COLOR());
		yellowBtn.setToolTipText(Colors.YELLOW.ZH());
		
		this.orangeBtn = new JButton("●");
		orangeBtn.setForeground(Colors.ORANGE.COLOR());
		orangeBtn.setToolTipText(Colors.ORANGE.ZH());
		
		this.pinkBtn = new JButton("●");
		pinkBtn.setForeground(Colors.PINK.COLOR());
		pinkBtn.setToolTipText(Colors.PINK.ZH());
		
		this.peachPinkBtn = new JButton("●");
		peachPinkBtn.setForeground(Colors.PEACH_PINK.COLOR());
		peachPinkBtn.setToolTipText(Colors.PEACH_PINK.ZH());
		
		this.goldBtn = new JButton("●");
		goldBtn.setForeground(Colors.GOLD.COLOR());
		goldBtn.setToolTipText(Colors.GOLD.ZH());
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(SwingUtils.getHGridPanel(
				whiteBtn, redBtn, blueBtn, purpleBtn, cyanBtn, greenBtn, 
				yellowBtn, orangeBtn, pinkBtn, peachPinkBtn, goldBtn
		), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		whiteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.WHITE);
			}
		});
		
		redBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.RED);
			}
		});

		blueBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.BLUE);
			}
		});

		purpleBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.PURPLE);
			}
		});

		cyanBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.CYAN);
			}
		});

		greenBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.GREEN);
			}
		});

		yellowBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.YELLOW);
			}
		});

		orangeBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.ORANGE);
			}
		});

		pinkBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.PINK);
			}
		});
		
		peachPinkBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.PEACH_PINK);
			}
		});

		goldBtn.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				AppUI.getInstn().updateChatColor(Colors.GOLD);
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
