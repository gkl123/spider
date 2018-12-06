package com.jskj.reptile.htmlparser;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

public class JsoupUtils {
	public static void main(String[] args) {
		Connection.Response res;
		try {
			res = Jsoup.connect("http://prod.admin.timescy.com/api/user/login")
					.data("account", "13800138001", "password", "123456")
					.method(Connection.Method.POST)
					.execute();
			
			System.out.println(res.body());
			
//			需要用到的参数是admin_id
			JSONObject jsonRes = JSONObject.parseObject(res.body());
			JSONObject jsonData = jsonRes.getJSONObject("data");
			JSONObject jsonUserInfo = jsonData.getJSONObject("user_info");
			String adminId = jsonUserInfo.getString("admin_id");
			String token = jsonData.getString("token");
//			String sessionId = res.cookie("JSESSIONID");
//			System.out.println(Jsoup.connect("http://prod.admin.timescy.com/api/user/login")
//			.cookie("JSESSIONID", sessionId).get().body());
			HttpUtils http = new HttpUtils();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("per_page", 15);
			params.put("page", 1);
			params.put("admin_id", adminId);
			params.put("current", 1);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Bearer " + token);
			headers.put("Content-Type", "application/json,text/plain,*/*;charset=GBK");
			headers.put("Accept-Language", "zh-CN,zh;q=0.9");
			String result = http.doGet("http://prod.admin.timescy.com/api/overdue/list", headers, params);
			System.out.println("result : " + result);
			
			// 请求地址 http://prod.admin.timescy.com/api/overdue/list?per_page=15&page=1&admin_id=e6524bf7d829c26cf467076d0729c3f2&time=&paid_status=&current=1
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
