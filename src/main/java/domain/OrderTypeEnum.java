package domain;

/**
*@className : OrderTypeEnum
*@Description : TODO
*@author : GKL
*@Date : 2018年12月6日
*/
public enum OrderTypeEnum {
	PAID("paid", "已还款"),
	NOTPAID("notpaid", "未还款"),
	OVERDUE("overdue", "逾期");

	public String code;
	
	public String desc;
	
	private OrderTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static OrderTypeEnum getByCode(String code) {
		OrderTypeEnum[] dateTypes = OrderTypeEnum.values();
		for (OrderTypeEnum type : dateTypes) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		
		return null;
	}
}
