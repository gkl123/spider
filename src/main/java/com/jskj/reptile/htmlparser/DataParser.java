package com.jskj.reptile.htmlparser;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jskj.reptile.domain.CreditStatusEnum;
import com.jskj.reptile.domain.QueryType;
import com.jskj.reptile.domain.UserLoanInfo;
import com.jskj.reptile.domain.UserOutputVO;
import com.jskj.reptile.utils.CommonUtil;
import com.jskj.reptile.utils.UnicodeUtil;

/**
*@className : DataParser
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public class DataParser {
	
	private DataSpider spider = new DataSpider();
	
	/**
	 * 省份人数统计
	 * @param userInfos
	 */
	public void parseProvince(List<UserLoanInfo> userInfos) {
		HashMap<String, Integer> provinceCount = new HashMap<String, Integer>();
		for (UserLoanInfo userInfo : userInfos) {
			String province = userInfo.getId_card_addr().substring(0, 2);
			if (provinceCount.containsKey(province)) {
				int count = provinceCount.get(province);
				count += 1;
				provinceCount.put(province, count);
			} else {
				provinceCount.put(province, 1);
			}
		}
	}
	
	/**
	 * 年龄段统计
	 */
	public HashMap<String, Integer> parseAge(List<UserLoanInfo> userInfos) {
		HashMap<String, Integer> ageCount = new HashMap<String, Integer>();
		Integer youngerThan20 = 0;
		Integer between20And30 = 0;
		Integer between30And40 = 0;
		Integer between40And50 = 0;
		Integer elderThan50 = 0;
		for (UserLoanInfo userInfo : userInfos) {
			Integer age = CommonUtil.getAgeByIdCard(userInfo.getBorrower_id_card());
			if (age < 20) {
				youngerThan20 += 1;
			} else if (age >= 20 && age < 30) {
				between20And30 += 1;
			} else if (age >= 30 && age < 40) {
				between30And40 += 1;
			} else if (age >= 40 && age < 50) {
				between40And50 += 1;
			} else {
				elderThan50 += 1;
			}
		}
		ageCount.put("20岁以下", youngerThan20);
		ageCount.put("20-30岁", between20And30);
		ageCount.put("30-40岁", between30And40);
		ageCount.put("40-50岁", between40And50);
		ageCount.put("50岁以上", elderThan50);
		
		return ageCount;
	}
	
	/**
	 * 获取用户信用状态
	 * @param userInfo
	 * @return
	 */
	public UserOutputVO getCreditStatus(UserLoanInfo userInfo) {
		UserOutputVO outputVo = new UserOutputVO();
		
		JSONObject result = this.spider.getUserDetailInfo(QueryType.APPLTDETAIL.url, userInfo.getId(), 
				QueryType.APPLTDETAIL.code, userInfo.getBorrower_id_card());
		
		JSONObject content = result.getJSONObject("content");
		String creditCode = content.getString("credit_status");
		String name = content.getString("bureau_user_name");
		String phone = content.getString("phone_number_house");
		outputVo.setCreditStatus(CreditStatusEnum.getByCode(creditCode).desc);
		outputVo.setName(name);
		outputVo.setPhone(phone);
		return outputVo;
	}
	

	/**
	 * 用户是否停机&用户话费查询
	 * @param userInfo
	 * @return
	 */
	public HashMap<String, String> getMobileMerchantInfo(UserLoanInfo userInfo) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		JSONObject result = this.spider.getUserDetailInfo(QueryType.MOBILE.url, userInfo.getId(), 
				QueryType.MOBILE.code, userInfo.getBorrower_id_card());
		
		JSONObject deviceInfo = result.getJSONObject("device_info");
		String phoneStatus = deviceInfo.getString("phone_status");
		String money = deviceInfo.getString("phone_remain");
		resultMap.put("phoneStatus", phoneStatus);
		resultMap.put("money", money + "元");
		
		return resultMap;
	}
	
	
	/**
	 * 对用使用的不同app进行统计
	 */
	public HashMap<String, Integer> appCount(List<UserLoanInfo> userInfos) {
		HashMap<String, Integer> appCountMap = new HashMap<String, Integer>();
		for (UserLoanInfo userInfo : userInfos) {
			JSONObject result = this.spider.getUserDetailInfo(QueryType.INFOREPO.url, userInfo.getId(), 
					QueryType.INFOREPO.code, userInfo.getBorrower_id_card());
			JSONArray arrayJson = result.getJSONArray("app_list");
			
			int size = arrayJson.size();;
			for (int i = 0; i < size; i++) {
				String appName = UnicodeUtil.decode(arrayJson.getString(i));
				if (appCountMap.containsKey(appName)) {
					int j = appCountMap.get(appName);
					j += 1;
					appCountMap.put(appName, j);
				} else {
					appCountMap.put(appName, 1);
				}
			}
		}
		return appCountMap;
	}
	
	
	/**
	 * 用户紧急联系人是否填写正确
	 * @param userInfo
	 * @return
	 */
	public String getEmergencyContactInfo(UserLoanInfo userInfo) {
		String resultStr = "填写错误"; 
		JSONObject result = this.spider.getUserDetailInfo(QueryType.ADDINFO.url, userInfo.getId(), 
				QueryType.ADDINFO.code, userInfo.getBorrower_id_card());
		
		String emergencyContactA = result.getString("emergency_contact_personA_phone");
		String emergencyContactB = result.getString("emergency_contact_personB_phone");
		JSONObject contacts = result.getJSONObject("contacts");
		String phone_list = contacts.getString("phone_list");

		if (phone_list.indexOf(emergencyContactA) >= 0 || phone_list.indexOf(emergencyContactB) >= 0) {
			resultStr = "填写正确";
		}
		
		return resultStr;
	}
	
}
