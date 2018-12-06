package domain;

/**
*@className : QueryDateEnum
*@Description : TODO
*@author : GKL
*@Date : 2018年12月6日
*/
public enum QueryDateEnum {
	TODAY("today", "今天"),
	TOMORROW("tomorrow", "明天"),
	OVERDUEONEDAY("overdue_one_day", "逾期一天"),
	OVERDUETOWDAYS("overdue_tow_days", "逾期二天"),
	OVERDUETHREEDAYS("overdue_three_days", "逾期3-4天"),
	OVERDUEFOURDAY("overdue_four_days", "逾期4-7天"),
	OHTERS("others", "逾期超过7天");
	
	public String code;
	
	public String desc;
	
	private QueryDateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
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
}
