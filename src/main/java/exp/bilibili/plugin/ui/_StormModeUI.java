package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.plugin.envm.Identity;
import exp.bilibili.plugin.utils.SwingUtils;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;
import exp.libs.warp.ui.layout.VFlowLayout;

/**
 * <PRE>
 * 节奏风暴/舰队亲密奖励 扫描策略选择窗口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _StormModeUI extends PopChildWindow {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8873664562824572800L;

	private final static HotLiveRange RANGE_1_TO_2 = new HotLiveRange(1, 2);
	
	private final static HotLiveRange RANGE_2_TO_3 = new HotLiveRange(2, 3);
	
	private final static HotLiveRange RANGE_1_TO_3 = new HotLiveRange(1, 3);
	
	private final static HotLiveRange RANGE_3_TO_5 = new HotLiveRange(3, 5);
	
	private final static int WIDTH = 450;
	
	private final static int HEIGHT = 380;
	
	private JButton okBtn;
	
	/**
	 * 仅被冻结账号参与节奏风暴.
	 * B站允许被关小黑屋的账号可以正常参与节奏风暴和部分舰长抽奖, 
	 * 打开此开关既可保证被关账号也有一定收益， 也可降低其他账号被抓到的几率。
	 */
	private JCheckBox onlyFreezeBtn;
	
	private JRadioButton autoBtn;
	
	private JRadioButton top50Btn;
	
	private JRadioButton top100Btn;
	
	private JRadioButton sec100Btn;
	
	private JRadioButton customBtn;
	
	private JTextField bgnTF;
	
	private JTextField endTF;
	
	protected _StormModeUI() {
		super("节奏风暴/舰队 扫描范围", WIDTH, HEIGHT, false);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.okBtn = new JButton("确 认");
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, okBtn);
		okBtn.setForeground(Color.BLACK);
		
		this.onlyFreezeBtn = new JCheckBox("仅被关小黑屋的账号参与节奏风暴");
		onlyFreezeBtn.setToolTipText("打开此开关既可保证被关小黑屋账号也有一定收益， 也可降低其他账号被抓到的几率");
		onlyFreezeBtn.setForeground(Color.BLACK);
		onlyFreezeBtn.setSelected(true);
		
		this.autoBtn = new JRadioButton("自动  ( 根据早晚时间段智能筛选人气直播间 )");
		this.top50Btn = new JRadioButton("TOP-50  ( 固定扫描排名1-50的人气直播间 )");
		this.top100Btn = new JRadioButton("TOP-100  ( 固定扫描排名1-100的人气直播间 )");
		this.sec100Btn = new JRadioButton("SEC-100  ( 固定扫描排名100-200的人气直播间 )");
		this.customBtn = new JRadioButton("自定义  ( 仅对主播版开放, 每页固定30个直播间 )");
		autoBtn.setForeground(Color.BLACK);
		top50Btn.setForeground(Color.BLACK);
		top100Btn.setForeground(Color.BLACK);
		sec100Btn.setForeground(Color.BLACK);
		customBtn.setForeground(Color.BLACK);
		ButtonGroup rangeGroup = new ButtonGroup();
		rangeGroup.add(autoBtn);
		rangeGroup.add(top50Btn);
		rangeGroup.add(top100Btn);
		rangeGroup.add(sec100Btn);
		rangeGroup.add(customBtn);
		autoBtn.setSelected(true);
		
		this.bgnTF = new JTextField("4");
		this.endTF = new JTextField("6");
		
		if(Identity.less(Identity.UPLIVE)) {
			customBtn.setEnabled(false);
			bgnTF.setEditable(false);
			endTF.setEditable(false);
		}
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		JPanel panel = new JPanel(new VFlowLayout(VFlowLayout.LEFT));
		panel.add(onlyFreezeBtn);
		panel.add(autoBtn);
		panel.add(top50Btn);
		panel.add(top100Btn);
		panel.add(sec100Btn);
		panel.add(customBtn);
		panel.add(SwingUtils.getHGridPanel(
				SwingUtils.getPairsPanel("始页码", bgnTF), 
				SwingUtils.getPairsPanel("止页码", endTF)
		));
		SwingUtils.addBorder(panel);
		
		rootPanel.add(panel, BorderLayout.CENTER);
		rootPanel.add(okBtn, BorderLayout.SOUTH);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		setOkBtnListener();
		setCustomBtnListener();
		setNumTextFieldListener(bgnTF);
		setNumTextFieldListener(endTF);
	}
	
	private void setOkBtnListener() {
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_hide();
			}
		});
	}
	
	private void setCustomBtnListener() {
		customBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(customBtn.isSelected()) {
					int bgn = NumUtils.toInt(bgnTF.getText(), 1);
					int end = NumUtils.toInt(endTF.getText(), 0);
					bgn = (bgn > 99 ? 99 : bgn);	// 限制范围 (超过100页意义不大)
					end = (end < bgn ? (bgn + 1) : end);
					
					bgnTF.setText(String.valueOf(bgn));
					endTF.setText(String.valueOf(end));
				}
			}
		});
	}
	
	private void setNumTextFieldListener(final JTextField textField) {
		textField.addKeyListener(new KeyListener() {

		    @Override
		    public void keyTyped(KeyEvent e) {
		        String text = textField.getText();  // 当前输入框内容
		        char ch = e.getKeyChar();   // 准备附加到输入框的字符

		        // 限制不能输入非数字
		        if(!(ch >= '0' && ch <= '9')) {
		            e.consume();    // 销毁当前输入字符

		        // 限制不能是0开头
		        } else if(text.startsWith("0")) {   
		            e.consume();
		        }
		    }

		    @Override
		    public void keyReleased(KeyEvent e) {
		        // TODO Auto-generated method stub
		    }

		    @Override
		    public void keyPressed(KeyEvent e) {
		        // TODO Auto-generated method stub
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
	
	/**
	 * 仅被冻结账号参与节奏风暴.
	 * B站允许被关小黑屋的账号可以正常参与节奏风暴和部分舰长抽奖, 
	 * 打开此开关既可保证被关账号也有一定收益， 也可降低其他账号被抓到的几率。
	 * @return
	 */
	protected boolean isOnlyFreeze() {
		return onlyFreezeBtn.isSelected();
	}
	
	/**
	 * 根据节奏风暴策略获取人气直播间的扫描范围
	 * @return
	 */
	protected HotLiveRange getHotLiveRange() {
		HotLiveRange range = null;
		if(autoBtn.isSelected()) {
			range = TimeUtils.isNight() ? RANGE_2_TO_3 : RANGE_1_TO_2;
			
		} else if(top50Btn.isSelected()) {
			range = RANGE_1_TO_2;
			
		} else if(top100Btn.isSelected()) {
			range = RANGE_1_TO_3;
			
		} else if(sec100Btn.isSelected()) {
			range = RANGE_3_TO_5;
			
		} else {
			customBtn.doClick();	// 修正范围
			int bgn = NumUtils.toInt(bgnTF.getText(), 1);
			int end = NumUtils.toInt(endTF.getText(), 2);
			range = new HotLiveRange(bgn, end);
		}
		return range;
	}

}
