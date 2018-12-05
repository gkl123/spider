package com.jskj.reptile.htmlparser;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JsoupUtils {
	public static void main(String[] args) {
		Connection.Response res;
		try {
			res = Jsoup.connect("http://prod.admin.timescy.com/api/user/login")
					.data("account", "13800138001", "password", "123456")
					.method(Connection.Method.POST)
					.execute();
			
			System.out.println(res.body());
//			String sessionId = res.cookie("JSESSIONID");
//			System.out.println(Jsoup.connect("http://prod.admin.timescy.com/api/user/login")
//			.cookie("JSESSIONID", sessionId).get().body());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
	}

}
