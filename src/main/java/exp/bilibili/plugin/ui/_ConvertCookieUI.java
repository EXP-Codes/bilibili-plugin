package exp.bilibili.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.bilibili.plugin.cache.CookiesMgr;
import exp.libs.envm.Charset;
import exp.libs.utils.encode.CryptoUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.time.TimeUtils;
import exp.libs.warp.tpl.Template;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
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
class _ConvertCookieUI extends PopChildWindow {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8873664562824572800L;

	private final static int WIDTH = 500;
	
	private final static int HEIGHT = 400;
	
	private final static long _30_DAY = 3600000L * 24 * 30;
	
	private final static String COOKIE_TPL_PATH = "/exp/bilibili/plugin/ui/login/cookie.tpl";
	
	private JRadioButton mainBtn;
	
	private JRadioButton vestBtn;
	
	private JRadioButton miniBtn;
	
	/** cookie字段：DedeUserID */
	private JTextField uidTF;
	
	/** cookie字段：DedeUserID__ckMd5 */
	private JTextField md5TF;
	
	/** cookie字段：SESSDATA */
	private JTextField sedTF;
	
	/** cookie字段：bili_jct */
	private JTextField jctTF;
	
	private JButton helpBtn;
	
	private JButton okBtn;
	
	protected _ConvertCookieUI() {
		super("Cookie 登录凭证转换器", WIDTH, HEIGHT, false);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.mainBtn = new JRadioButton("主号");
		this.vestBtn = new JRadioButton("小号");
		this.miniBtn = new JRadioButton("马甲");
		mainBtn.setForeground(Color.BLACK);
		vestBtn.setForeground(Color.BLACK);
		miniBtn.setForeground(Color.BLACK);
		
		ButtonGroup group = new ButtonGroup();
		group.add(mainBtn);
		group.add(vestBtn);
		group.add(miniBtn);
		mainBtn.setSelected(true);
		
		this.uidTF = new JTextField();
		this.md5TF = new JTextField();
		this.sedTF = new JTextField();
		this.jctTF = new JTextField();
		
		this.helpBtn = new JButton("如何获取登录凭证参数 ?");
		BeautyEyeUtils.setButtonStyle(NormalColor.green, helpBtn);
		helpBtn.setForeground(Color.BLACK);
		
		this.okBtn = new JButton("生成登录凭证 (Cookie)");
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, okBtn);
		okBtn.setForeground(Color.BLACK);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(SwingUtils.addBorder(SwingUtils.getHGridPanel(
				mainBtn, miniBtn, vestBtn), "凭证类型"), BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new VFlowLayout(VFlowLayout.LEFT));
		panel.add(SwingUtils.getPairsPanel("DedeUserID", uidTF));
		panel.add(SwingUtils.getPairsPanel("DedeUserID__ckMd5", md5TF));
		panel.add(SwingUtils.getPairsPanel("SESSDATA", sedTF));
		panel.add(SwingUtils.getPairsPanel("bili_jct", jctTF));
		SwingUtils.addBorder(panel, "凭证参数");
		rootPanel.add(panel, BorderLayout.CENTER);
		
		rootPanel.add(SwingUtils.getVGridPanel(helpBtn, okBtn), BorderLayout.SOUTH);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		helpBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_hide();
			}
		});

		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = uidTF.getText().trim();
				String md5 = md5TF.getText().trim();
				String sed = sedTF.getText().trim();
				String jct = jctTF.getText().trim();
				if(StrUtils.isNotEmpty(uid, md5, sed, jct)) {
					Template tpl = new Template(COOKIE_TPL_PATH, Charset.ISO);
					tpl.set("DedeUserID", uid);
					tpl.set("DedeUserID__ckMd5", md5);
					tpl.set("SESSDATA", sed);
					tpl.set("bili_jct", jct);
					tpl.set("Expires", TimeUtils.toExpires(new Date(TimeUtils.getSysMillis() + _30_DAY)));
					String cookie = CryptoUtils.toDES(tpl.getContent());
					
					String filePath = mainBtn.isSelected() ? CookiesMgr.COOKIE_MAIN_PATH : 
							vestBtn.isSelected() ? CookiesMgr.COOKIE_VEST_PATH : 
							CookiesMgr.COOKIE_MINI_PATH(uid);
					
					boolean isDone = true;
					if(FileUtils.exists(filePath)) {
						isDone = SwingUtils.confirm("已存在登录凭证, 覆盖 ?");
					}
					
					if(isDone == true) {
						if(FileUtils.write(filePath, cookie, Charset.ISO, false)) {
							SwingUtils.info("生成登录凭证成功");
						} else {
							SwingUtils.info("生成登录凭证失败");
						}
					}
					
				} else {
					SwingUtils.warn("登录凭证参数全部必填");
				}
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
