package domain;

/**
*@className : QueryType
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public enum QueryType {
	APPLTDETAIL("applyDetail", "基础信息"),
	MOBILE("mobile", "运营商"),
	ADDINFO("addInfo", "用户补充信息"),
	INFOREPO("infoRepo", "用户信息数据报告");
	
	public String code;
	
	public String desc;
	
	private QueryType(String code, String desc) {
		this.code = code;
		this.desc = desc;
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
