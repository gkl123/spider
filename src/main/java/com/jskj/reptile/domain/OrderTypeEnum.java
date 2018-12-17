package com.jskj.reptile.domain;

/**
*@className : OrderTypeEnum
*@Description : TODO
*@author : GKL
*@Date : 2018年12月6日
*/
public enum OrderTypeEnum {
	PAID("paid", "已还款", "1"),
	NOTPAID("notpaid", "未还款", "2"),
	OVERDUE("overdue", "逾期", "3"),
	PAIDFAILURE("failed", "还款失败", "4");

	public String code;
	
	public String desc;
	
	public String type;
	
	private OrderTypeEnum(String code, String desc, String type) {
		this.code = code;
		this.desc = desc;
		this.type = type;
	}
	
	public static OrderTypeEnum getByCode(String code) {
		OrderTypeEnum[] dateTypes = OrderTypeEnum.values();
		for (OrderTypeEnum types : dateTypes) {
			if (types.code.equals(code)) {
				return types;
			}
		}
		return null;
	}
	
	public static OrderTypeEnum getByType(String type) {
		OrderTypeEnum[] dateTypes = OrderTypeEnum.values();
		for (OrderTypeEnum types : dateTypes) {
			if (types.type.equals(type)) {
				return types;
			}
		}
		return null;
	}
}
