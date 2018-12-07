package com.jskj.reptile.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jskj.reptile.utils.UnicodeUtil;

public class UserLoanInfo {
	private String borrower_id_card;
	
	private String borrower_mobile;
	
	private String id;
	
	private String borrower_name;

	private String id_card_addr;
	
	private String overdue_remark;
	
	private String order_no;
	
	private String paid_at;
	
	private String paid_remark;
	
	private String paid_status;
	
	private String repayment_time;
	
	private String sex;
	
	public UserLoanInfo(JSONObject json) {
//		JSONObject.toJavaObject(json, UserLoanInfo.class);
		this.borrower_id_card = json.getString("borrower_id_card");
		this.borrower_mobile = json.getString("borrower_mobile");
		this.id = json.getString("id");
		this.borrower_name = UnicodeUtil.decode(json.getString("borrower_name"));
		this.id_card_addr = UnicodeUtil.decode(json.getString("id_card_addr"));
		this.overdue_remark = UnicodeUtil.decode(json.getString("overdue_remark"));
		this.order_no = json.getString("order_no");
		this.paid_at = json.getString("paid_at");
		this.paid_remark = UnicodeUtil.decode(json.getString("paid_remark"));
		this.paid_status = json.getString("paid_status");
		
		Date repayMentTime = new Date(json.getLong("repayment_time"));
		this.repayment_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(repayMentTime);
		
		String sexStr = json.getString("sex");
		if (sexStr.isEmpty()) {
			this.sex = "无数据";
		}
		this.sex = "1".equals(sexStr) ? "男" : "女";
	}

	public String getBorrower_id_card() {
		return borrower_id_card;
	}

	public void setBorrower_id_card(String borrower_id_card) {
		this.borrower_id_card = borrower_id_card;
	}

	public String getBorrower_mobile() {
		return borrower_mobile;
	}

	public void setBorrower_mobile(String borrower_mobile) {
		this.borrower_mobile = borrower_mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBorrower_name() {
		return borrower_name;
	}

	public void setBorrower_name(String borrower_name) {
		this.borrower_name = borrower_name;
	}

	public String getId_card_addr() {
		return id_card_addr;
	}

	public void setId_card_addr(String id_card_addr) {
		this.id_card_addr = id_card_addr;
	}

	public String getOverdue_remark() {
		return overdue_remark;
	}

	public void setOverdue_remark(String overdue_remark) {
		this.overdue_remark = overdue_remark;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getPaid_at() {
		return paid_at;
	}

	public void setPaid_at(String paid_at) {
		this.paid_at = paid_at;
	}

	public String getPaid_remark() {
		return paid_remark;
	}

	public void setPaid_remark(String paid_remark) {
		this.paid_remark = paid_remark;
	}

	public String getPaid_status() {
		return paid_status;
	}

	public void setPaid_status(String paid_status) {
		this.paid_status = paid_status;
	}

	public String getRepayment_time() {
		return repayment_time;
	}

	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
