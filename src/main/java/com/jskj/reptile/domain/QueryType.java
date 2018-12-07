package com.jskj.reptile.domain;

/**
*@className : QueryType
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public enum QueryType {
	APPLTDETAIL("applyDetail", "基础信息", "http://prod.admin.timescy.com/api/order/extra-info"),
	MOBILE("mobile", "运营商", "http://prod.admin.timescy.com/api/order/extra-info"),
	ADDINFO("addInfo", "用户补充信息", "http://prod.admin.timescy.com/api/order/add-info"),
	INFOREPO("infoRepo", "用户信息数据报告", "http://prod.admin.timescy.com/api/report/index");
	
	public String code;
	
	public String desc;
	
	public String url;
	
	private QueryType(String code, String desc, String url) {
		this.code = code;
		this.desc = desc;
		this.url = url;
	}
	
	public static QueryType getByCode(String code) {
		QueryType[] types = QueryType.values();
		for (QueryType type : types) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		return null;
	}

}
