package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class refund {

	private long date;
	private String contentPartner;
	private float refundAmt ;
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getContentPartner() {
		return contentPartner;
	}
	public void setContentPartner(String contentPartner) {
		this.contentPartner = contentPartner;
	}
	public float getRefundAmt() {
		return refundAmt;
	}
	public void setRefundAmt(float refundAmt) {
		this.refundAmt = refundAmt;
	}
	
}
