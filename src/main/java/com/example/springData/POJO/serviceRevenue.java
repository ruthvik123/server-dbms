package com.example.springData.POJO;

import org.springframework.stereotype.Component;

@Component
public class serviceRevenue {

	private String serviceType;

	private float revenue;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public float getRevenue() {
		return revenue;
	}

	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

}
