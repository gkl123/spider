package com.jskj.reptile.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jskj.reptile.domain.QueryDateEnum;
import com.jskj.reptile.domain.QueryType;
import com.jskj.reptile.domain.UserLoanInfo;
import com.jskj.reptile.utils.HttpUtils;

public class DataSpider {

	/**
	 *  获取用户列表信息
	 */
	public List<UserLoanInfo> getUserInfo(String time, String paid_status) {
		// 请求地址 http://prod.admin.timescy.com/api/overdue/list?per_page=15&page=1&admin_id=e6524bf7d829c26cf467076d0729c3f2&time=&paid_status=&current=1
		// 需要用到的参数是admin_id
		HashMap<String, String> tokenMap = getToken();
		String adminId = tokenMap.get("admin_id");
		String token = tokenMap.get("token");

		HttpUtils http = new HttpUtils();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("per_page", getTotalDataNum(time, paid_status)); // 先获取指定条件下的数据总共有多少条；
		params.put("page", 1);
		params.put("admin_id", adminId);
		params.put("current", 1);
		params.put("time", time);
		params.put("paid_status", paid_status);
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		headers.put("Content-Type", "application/json,text/plain,*/*;charset=unicode");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9");
		
		String result = http.doGet("http://prod.admin.timescy.com/api/overdue/list", headers, params);
		System.out.println("result : " + result);
		
		JSONObject jsonResult = JSONObject.parseObject(result);
		if("200".equals(jsonResult.getString("code"))) {
			return parseJsonArray(jsonResult);
		}
		return null;
	}
	
	public List<UserLoanInfo> getAllUserInfo(String time, String paid_status) {
		if (QueryDateEnum.ALL.code.equals(time)) {
			List<UserLoanInfo> returnList = new ArrayList<UserLoanInfo>();
			List<UserLoanInfo> one = getUserInfo(QueryDateEnum.OVERDUEONEDAY.code, paid_status);
			List<UserLoanInfo> tow = getUserInfo(QueryDateEnum.OVERDUETWODAYS.code, paid_status);
			List<UserLoanInfo> three = getUserInfo(QueryDateEnum.OVERDUETHREEDAYS.code, paid_status);
			List<UserLoanInfo> four = getUserInfo(QueryDateEnum.OVERDUEFOURDAY.code, paid_status);
			List<UserLoanInfo> five = getUserInfo(QueryDateEnum.OTHERS.code, paid_status);
			List<UserLoanInfo> six = getUserInfo(QueryDateEnum.TODAY.code, paid_status);
			List<UserLoanInfo> seven = getUserInfo(QueryDateEnum.TOMORROW.code, paid_status);
			if (!CollectionUtils.isEmpty(one)) {
				returnList.addAll(one);
			}
			if (!CollectionUtils.isEmpty(tow)) {
				returnList.addAll(tow);
			}
			if (!CollectionUtils.isEmpty(three)) {
				returnList.addAll(three);
			}
			if (!CollectionUtils.isEmpty(four)) {
				returnList.addAll(four);
			}
			if (!CollectionUtils.isEmpty(five)) {
				returnList.addAll(five);
			}
			if (!CollectionUtils.isEmpty(six)) {
				returnList.addAll(six);
			}
			if (!CollectionUtils.isEmpty(seven)) {
				returnList.addAll(seven);
			}
			return returnList;
		} else {
			return getUserInfo(time, paid_status);
		}
	}
	
	
	public JSONObject getUserDetailInfo(String url, String userId, String type, String idcard) {
		HashMap<String, String> tokenMap = getToken();
		String adminId = tokenMap.get("admin_id");
		String token = tokenMap.get("token");

		HttpUtils http = new HttpUtils();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("admin_id", adminId);
		params.put("id", userId);
		params.put("id_card", idcard);
		params.put("type", type);
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		headers.put("Content-Type", "application/json,text/plain,*/*;charset=unicode");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9");
		
		String result = http.doGet(url, headers, params);
//		System.out.println("result : " + result);
		try {
			JSONObject jsonResult = JSONObject.parseObject(result);
			if("200".equals(jsonResult.getString("code"))) {
				return jsonResult.getJSONObject("data");
			}
		} catch(Exception e) {
			System.out.println("解析" + QueryType.getByCode(type).desc + "数据异常，异常用户身份证信息: " + idcard);
		}
		return null;
	}
	
	/**
	 * 获取请求token
	 * @return
	 */
	public HashMap<String, String> getToken() {
		HashMap<String, String> result = new HashMap<String, String>();
		Connection.Response res;
		try {
			if (StringUtils.isEmpty(RequestParams.adminId) || StringUtils.isEmpty(RequestParams.token)) {
				res = Jsoup.connect("http://prod.admin.timescy.com/api/user/login")
						.data("account", "13800138001", "password", "123456")
						.method(Connection.Method.POST)
						.execute();
				
				System.out.println(res.body());
				
//				需要用到的参数是admin_id
				JSONObject jsonRes = JSONObject.parseObject(res.body());
				JSONObject jsonData = jsonRes.getJSONObject("data");
				JSONObject jsonUserInfo = jsonData.getJSONObject("user_info");
				String adminId = jsonUserInfo.getString("admin_id");
				String token = jsonData.getString("token");
				RequestParams.adminId = adminId; 
				RequestParams.token = token;
				result.put("admin_id", adminId);
				result.put("token", token);
			} else {
				result.put("admin_id", RequestParams.adminId);
				result.put("token", RequestParams.token);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取总数
	 */ 
	private String getTotalDataNum(String time, String paid_status) {
//		需要用到的参数是admin_id
		HashMap<String, String> tokenMap = getToken();
		String adminId = tokenMap.get("admin_id");
		String token = tokenMap.get("token");

		HttpUtils http = new HttpUtils();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("per_page", 0);
		params.put("page", 1);
		params.put("admin_id", adminId);
		params.put("current", 1);
		params.put("time", time);
		params.put("paid_status", paid_status);
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		headers.put("Content-Type", "application/json,text/plain,*/*;charset=unicode");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9");
		
		String result = http.doGet("http://prod.admin.timescy.com/api/overdue/list", headers, params);
		System.out.println("result : " + result);
		
		JSONObject jsonResult = JSONObject.parseObject(result);
		JSONObject jsonData = jsonResult.getJSONObject("data");
		String page_count = jsonData.getString("total");
		return page_count;
	}
	
	/**
	 * 解析返回结果
	 * @param jsonResult
	 * @return
	 */
	public List<UserLoanInfo> parseJsonArray(JSONObject jsonResult) {
		ArrayList<UserLoanInfo> userInfos = new ArrayList<UserLoanInfo>();
		
		JSONObject jsonData = jsonResult.getJSONObject("data");
		JSONArray userInfoArray = jsonData.getJSONArray("rows");
		int num = userInfoArray.size();
		
		for (int i = 0; i < num; i++) {
			UserLoanInfo userInfo = new UserLoanInfo(userInfoArray.getJSONObject(i));
			userInfos.add(userInfo);
		}
		return userInfos;
	}
}
