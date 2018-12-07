package domain;

/**
*@className : CreditStatusEnum
*@Description : TODO
*@author : GKL
*@Date : 2018年12月7日
*/
public enum CreditStatusEnum {
//1：无信用卡或贷款，2：信用良好，3：1年内有逾期记录，但90天以上无逾期，4：1年内有90天以上逾期
	NOCREDIT("1", "无信用卡或贷款"),
	CREDITGOOD("2", "信用良好"),
	CREDITBECOMEGOOD("3", "1年内有逾期记录，但90天以上无逾期"),
	CREDITBAD("4", "1年内有90天以上逾期");
	
	public String code;
	
	public String desc;
	
	
	private CreditStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static CreditStatusEnum getByCode(String code) {
		CreditStatusEnum[] dateTypes = CreditStatusEnum.values();
		for (CreditStatusEnum types : dateTypes) {
			if (types.code.equals(code)) {
				return types;
			}
		}
		return null;
	}
}
