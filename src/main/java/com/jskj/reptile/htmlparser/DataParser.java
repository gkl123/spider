package com.jskj.reptile.htmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jskj.reptile.domain.CreditStatusEnum;
import com.jskj.reptile.domain.PhoneStatusEnum;
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
	
	String urgeCallingNumberBegin = "010"; // 催收电话号码开头
	private DataSpider spider = new DataSpider();
	
	
	/**
	 * 用户信息列表对象
	 * @param userInfos
	 * @return
	 */
	public List<UserOutputVO> getUserOutputInfo(List<UserLoanInfo> userInfos) {
		List<UserOutputVO> userOutputInfo = new ArrayList<UserOutputVO>();
		
		for (UserLoanInfo userInfo : userInfos) {
			UserOutputVO userOutputVo = this.getCreditStatus(userInfo); // 1、获取用户姓名，信用，手机号信息
			
			HashMap<String, String> phoneInfo = this.getMobileMerchantInfo(userInfo); // 2、获取用户花费情况，是否停机
			userOutputVo.setIsPhoneStopService(phoneInfo.get("phoneStatus"));
			userOutputVo.setIsZeroCostOfCalls(phoneInfo.get("money"));
			userOutputVo.setCallingbomb(phoneInfo.get("callingBoomNum"));
			userOutputVo.setIsCallingUrgeCost(phoneInfo.get("urgeCallingNum"));
			
			userOutputVo.setContactListStatus(this.getEmergencyContactInfo(userInfo)); // 3、联系人是否填写正确
			userOutputInfo.add(userOutputVo);
		}
		
		return userOutputInfo;
	}
	
	/**
	 * 省份人数统计
	 * @param userInfos
	 */
	public HashMap<String, Integer> parseProvince(List<UserLoanInfo> userInfos) {
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
		return provinceCount;
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
		CreditStatusEnum creditStatus = CreditStatusEnum.getByCode(creditCode);
		outputVo.setCreditStatus(creditStatus == null ? "未知状态" : creditStatus.desc);
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
		
		JSONObject content = result.getJSONObject("content");
		JSONObject userdata = content.getJSONObject("userdata");
		JSONArray tel = content.getJSONArray("tel");
		resultMap = this.telephoneCallingInfo(tel);
		String phoneStatus = userdata.getString("phone_status");
		String money = userdata.getString("phone_remain");
		PhoneStatusEnum phoneStatusEnum = PhoneStatusEnum.getByCode(phoneStatus);
		resultMap.put("phoneStatus", phoneStatusEnum == null ? "未知状态" : phoneStatusEnum.desc);
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
			
			int size = arrayJson.size();
			for (int i = 0; i < size; i++) {
				String appName = arrayJson.getString(i);
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
	
	
	/**
	 * 分析用户有几个催收电话和是否又被电话轰炸过；
	 * @param array
	 * @return
	 */
	public HashMap<String, String> telephoneCallingInfo(JSONArray array) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		int urgeCallingNum = 0;
		int callingBoomNum = 0;
		if (array != null) {
			int size = array.size();
			for (int i = 0; i < size; i++) {
				JSONObject subObject = array.getJSONObject(i);
				JSONArray subTel = subObject.getJSONArray("teldata");
				int subSize = subTel.size();
				for (int j = 0; j < subSize; j++) {
					JSONObject tel = subTel.getJSONObject(j);
					if (tel.getString("receive_phone").indexOf(urgeCallingNumberBegin) == 0) {
						// 号码开头是010的认为是催收电话
						urgeCallingNum ++;
					}
					
					if (tel.getInteger("trade_time") <= 3) {
						callingBoomNum ++;
					}
				}
			}
		}
		
		if (urgeCallingNum > 0) {
			resultMap.put("urgeCallingNum", "用户接过催收电话，次数为：" + urgeCallingNum);
		} else {
			resultMap.put("urgeCallingNum", "用户未接过催收电话");
		}
		
		resultMap.put("callingBoomNum", "用户少于三秒的通话次数是 : " + callingBoomNum + "次");
		return resultMap;
	}
}
