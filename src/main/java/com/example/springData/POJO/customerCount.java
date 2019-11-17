package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class customerCount {

	private long date;
	private String contentPartner;
	private int customerCount;

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

	public int getCustomerCount() {
		return customerCount;
	}

	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}

}
