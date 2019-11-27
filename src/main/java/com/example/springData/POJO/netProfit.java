package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class netProfit {

	private int month;
	private float profit;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}
	
	
	
}
