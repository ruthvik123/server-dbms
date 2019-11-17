package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class Sales {
	
	private long date;
	private String contentPartner;
	private float price;
	
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
		

}
