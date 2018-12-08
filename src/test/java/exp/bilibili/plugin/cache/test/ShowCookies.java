package exp.bilibili.plugin.cache.test;

import java.util.Set;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;

public class ShowCookies {


	public static void main(String[] args) {
		CookiesMgr.getInstn().load(CookieType.MAIN);
		CookiesMgr.getInstn().load(CookieType.VEST);
		CookiesMgr.getInstn().load(CookieType.MINI);
		
		System.out.println("================= MAIN-COOKIE =================");
		System.out.println(CookiesMgr.MAIN().toNVCookie());
		System.out.println(CookiesMgr.MAIN().toHeaderCookie());
		
		System.out.println("================= VEST-COOKIE =================");
		System.out.println(CookiesMgr.VEST().toNVCookie());
		System.out.println(CookiesMgr.VEST().toHeaderCookie());
		
		System.out.println("================= MINI-COOKIE =================");
		Set<BiliCookie> minis = CookiesMgr.MINIs();
		for(BiliCookie mini : minis) {
			System.out.println(mini.toNVCookie());
			System.out.println(mini.toHeaderCookie());
			System.out.println("--------");
		}
		
	}
	
}
