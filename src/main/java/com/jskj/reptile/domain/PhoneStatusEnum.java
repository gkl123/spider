package com.jskj.reptile.domain;

public enum PhoneStatusEnum {
// 0未知；1正常；2停机；3单向停机（单向停机指的是可以收短信电话）；4预销户；5销户；6过户；7改号；8此号码不存在；9挂失/冻结
	UNKNOWN("0", "未知"),
	NORMAL("1", "正常"),
	STOPSERVICE("2", "停机"),
	STOPONESIDED("3", "单向停机（单向停机指的是可以收短信电话）"),
	PRELOGOUT("4", "预销户"),
	LOGOUT("5", "销户"),
	TRANSFER("6", "过户"),
	CHANGENUMBER("7", "改号"),
	UNKNOWNNUMBER("8", "此号码不存在"),
	FORZEN("9", "挂失/冻结");
	

	public String code;
	
	public String desc;
	
	private PhoneStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static PhoneStatusEnum getByCode(String code) {
		PhoneStatusEnum[] dateTypes = PhoneStatusEnum.values();
		for (PhoneStatusEnum types : dateTypes) {
			if (types.code.equals(code)) {
				return types;
			}
		}
		return null;
	}
}
