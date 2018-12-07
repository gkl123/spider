package domain;

/**
*@className : QueryDateEnum
*@Description : TODO
*@author : GKL
*@Date : 2018年12月6日
*/
public enum QueryDateEnum {
	TODAY("today", "今天", "1"),
	TOMORROW("tomorrow", "明天", "2"),
	OVERDUEONEDAY("overdue_one_day", "逾期一天", "3"),
	OVERDUETOWDAYS("overdue_tow_days", "逾期二天", "4"),
	OVERDUETHREEDAYS("overdue_three_days", "逾期3-4天", "5"),
	OVERDUEFOURDAY("overdue_four_days", "逾期4-7天", "6"),
	OHTERS("others", "逾期超过7天", "7");
	
	public String code;
	
	public String desc;
	
	public String type;
	
	private QueryDateEnum(String code, String desc, String type) {
		this.code = code;
		this.desc = desc;
		this.type = type;
	}
	
	public static QueryDateEnum getByCode(String code) {
		QueryDateEnum[] dateTypes = QueryDateEnum.values();
		for (QueryDateEnum type : dateTypes) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		return null;
	}
	
	public static QueryDateEnum getByType(String type) {
		QueryDateEnum[] dateTypes = QueryDateEnum.values();
		for (QueryDateEnum types : dateTypes) {
			if (types.type.equals(type)) {
				return types;
			}
		}
		return null;
	}
}
