package com.jskj.reptile.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
*@className : UserOutputVO
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public class UserOutputVO {
	private String name; // 姓名 *
	
	private String phone; // 手机号 *
	
	private String contactListStatus; // 积极联系人是否填写正确
	
	private String isPhoneStopService; // 是否停机 *
	
	private String isCutDown; // 是否不接电话
	
	private String isZeroCostOfCalls; // 话费为0 *
	
	private String creditStatus; // 信用状态 *
	
	private String callingFilter; // 手机是否存在屏蔽电话相关的App

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContactListStatus() {
		return contactListStatus;
	}

	public void setContactListStatus(String contactListStatus) {
		this.contactListStatus = contactListStatus;
	}

	public String getIsPhoneStopService() {
		return isPhoneStopService;
	}

	public void setIsPhoneStopService(String isPhoneStopService) {
		this.isPhoneStopService = isPhoneStopService;
	}

	public String getIsCutDown() {
		return isCutDown;
	}

	public void setIsCutDown(String isCutDown) {
		this.isCutDown = isCutDown;
	}

	public String getIsZeroCostOfCalls() {
		return isZeroCostOfCalls;
	}

	public void setIsZeroCostOfCalls(String isZeroCostOfCalls) {
		this.isZeroCostOfCalls = isZeroCostOfCalls;
	}

	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getCallingFilter() {
		return callingFilter;
	}

	public void setCallingFilter(String callingFilter) {
		this.callingFilter = callingFilter;
	}
	
	
	public static void main(String[] args) {
		String msg = "{\"name\" : \"123\",\"app_list\": [{\"uid\": 30830405,\"name\": \"潘康来\",\"createtime\": null,\"create_time\": \"2018-07-09 17:53:20\",\"phone_dirty\": \"18682322120\",\"phone\": \"18682322120\"}, {\"uid\": 30830405,\"name\": \"丽\",\"createtime\": null,\"create_time\": \"2018-07-09 17:53:20\",\"phone_dirty\": \"13457833815\",\"phone\": \"13457833815\"}]}";
		
		JSONObject json = JSONObject.parseObject(msg);
		
		
		String arrayJson = json.getString("app_list");
		
		System.out.println(arrayJson.indexOf("{"));
		
	}
}
