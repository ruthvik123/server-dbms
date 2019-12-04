package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.Sales;
import com.example.springData.POJO.customerCount;
import com.example.springData.Repository.SalesJdbcDAO.salesRowMapper;

@Repository
public class customerCountDAO {

	public class customerCountRowMapper implements RowMapper {

		@Override
		public customerCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			customerCount customerCountObj = new customerCount();
			customerCountObj.setDate(rs.getLong("EVENT_DTM"));
			customerCountObj.setContentPartner(rs.getString("COMPANY_NAME"));
			customerCountObj.setCustomerCount(rs.getInt("CUSTOMER_COUNT"));
			return customerCountObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public JSONObject customerCountData(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "\r\n"
				+ "    round(((to_date(to_char(cd.event_dtm, 'YYYYMM'), 'YYYYMM') - TO_DATE('197001', 'YYYYMM'))) /(1 / 86400)) * 1000 event_dtm,\r\n"
				+ "\r\n" + "    decode(c.company_name,\r\n" + "\r\n"
				+ "    'Q-MOBILE MIDDLE  EAST CO. (AMERICAN TRADE)', 'AMERICAN TRADE',\r\n" + "\r\n"
				+ "    'APPLE DISTRIBTION INTERNATIONAL', 'APPLE CORP',\r\n" + "\r\n"
				+ "           'AIWA GULF FOR GENERAL TRADING and', 'AIWA GULF',\r\n" + "\r\n"
				+ "           'KITMAKER ENTERTAINMENT SA', 'KITMAKER',\r\n" + "\r\n"
				+ "           c.company_name) company_name,\r\n" + "\r\n"
				+ "    COUNT(DISTINCT cd.event_attr_16) customer_count\r\n" + "\r\n" + "FROM\r\n" + "\r\n"
				+ "    costedevent          cd,\r\n" + "\r\n" + "    customer             c,\r\n" + "\r\n"
				+ "    account              a,\r\n" + "\r\n" + "    eventtypeattribute   e\r\n" + "\r\n" + "WHERE\r\n"
				+ "\r\n" + "    cd.event_seq >= (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            MAX(x.event_seq) - 4\r\n" + "\r\n" + "        FROM\r\n" + "\r\n"
				+ "            costedevent x\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.account_num = x.account_num\r\n" + "\r\n" + "    )\r\n" + "\r\n"
				+ "    AND ( trunc(cd.event_dtm) >= :startDate \r\n" + "\r\n"
				+ "          AND trunc(cd.event_dtm) < :endDate )\r\n" + "\r\n"
				+ "    AND cd.account_num = a.account_num\r\n" + "\r\n"
				+ "    AND cd.event_type_id = e.event_type_id\r\n" + "\r\n" + "    AND e.event_attr_num = 16\r\n"
				+ "\r\n" + "    AND e.attr_name IN (\r\n" + "\r\n" + "        'Source Event Source',\r\n" + "\r\n"
				+ "        'MSISDN'\r\n" + "\r\n" + "    )\r\n" + "\r\n" + "    AND a.customer_ref = c.customer_ref\r\n"
				+ "\r\n" + "GROUP BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMM'), 'YYYYMM') - TO_DATE('197001', 'YYYYMM'))) /(1 / 86400)) * 1000 ),\r\n"
				+ "\r\n" + "    c.company_name\r\n" + "\r\n" + "ORDER BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMM'), 'YYYYMM') - TO_DATE('197001', 'YYYYMM'))) /(1 / 86400)) * 1000 ),\r\n"
				+ "\r\n" + "    c.company_name\r\n" + "\r\n" + " ";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		List<customerCount> customerData = namedjdbcTemplate.query(sql, namedParams, new customerCountRowMapper());

		JSONObject sampleObject = new JSONObject();

		for (int i = 0; i < customerData.size(); i++) {
			if (!sampleObject.containsKey(customerData.get(i).getContentPartner())) {
				JSONArray priceDetailsArray = new JSONArray();
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(customerData.get(i).getDate());
				priceDetails.add(customerData.get(i).getCustomerCount());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(customerData.get(i).getContentPartner(), priceDetailsArray);
			} else {
				JSONArray priceDetailsArray = (JSONArray) sampleObject.get(customerData.get(i).getContentPartner());
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(customerData.get(i).getDate());
				priceDetails.add(customerData.get(i).getCustomerCount());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(customerData.get(i).getContentPartner(), priceDetailsArray);

			}

		}

		return sampleObject;

	}

}
