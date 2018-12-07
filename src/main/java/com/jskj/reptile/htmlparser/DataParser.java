package com.jskj.reptile.htmlparser;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jskj.reptile.utils.CommonUtil;

import domain.CreditStatusEnum;
import domain.QueryType;
import domain.UserLoanInfo;
import domain.UserOutputVO;

/**
*@className : DataParser
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public class DataParser {
	
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
			} else if (age >= 50) {
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
		
		DataSpider spider = new DataSpider();
		JSONObject result = spider.getUserDetailInfo("http://prod.admin.timescy.com/api/order/extra-info", userInfo.getId(), 
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

}
