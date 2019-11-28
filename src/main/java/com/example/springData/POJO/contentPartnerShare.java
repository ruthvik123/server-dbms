package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class contentPartnerShare {

	String name;  // name of content partner
	float y;     // percentage contributed to profit [since UI has a dataFeild 'y' in highcharts]
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
}
