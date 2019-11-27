package com.example.springData.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springData.POJO.Sales;

import com.example.springData.Repository.SalesJdbcDAO;
import com.example.springData.Repository.customerCountDAO;
import com.example.springData.Repository.netProfitDAO;
import com.example.springData.Repository.refundDAO;

@RestController
public class controller1 {

	@Autowired
	SalesJdbcDAO repo;

	@Autowired
	customerCountDAO customerRepo;
	
	@Autowired
	netProfitDAO netProfitRepo;
	
	@Autowired
	refundDAO refundRepo;

//	@Autowired
//	Sales obj;

	@GetMapping("/")
	public String startApp() {
		return "App. started...enter correct URL";
	}

	@GetMapping(value = "/sales/{startDate}/{endDate}")
	@CrossOrigin(origins = "http://localhost:4200")
	public JSONObject entry(@PathVariable String startDate, @PathVariable String endDate) {

		return repo.salesData(startDate, endDate);

	}

	@GetMapping(value = "/customerCount/{startDate}/{endDate}")
	@CrossOrigin(origins = "http://localhost:4200")
	public JSONObject customerCount(@PathVariable String startDate, @PathVariable String endDate) {

		return customerRepo.customerCountData(startDate, endDate);

	}
	
	@GetMapping(value = "/netProfit/{startDate}/{endDate}")
	@CrossOrigin(origins = "http://localhost:4200")
	public JSONArray netProfit(@PathVariable String startDate, @PathVariable String endDate) {

		return netProfitRepo.netProfit(startDate, endDate);

	}
	
	
	@GetMapping(value = "/refund/{startDate}/{endDate}")
	@CrossOrigin(origins = "http://localhost:4200")
	public JSONObject refund(@PathVariable String startDate, @PathVariable String endDate) {

		return refundRepo.refundData(startDate, endDate);

	}
	
	

}
