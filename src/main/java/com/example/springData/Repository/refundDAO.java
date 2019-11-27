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
import com.example.springData.POJO.netProfit;
import com.example.springData.POJO.refund;
import com.example.springData.Repository.SalesJdbcDAO.salesRowMapper;

@Repository
public class refundDAO {

	public class refundRowMapper implements RowMapper {

		@Override
		public refund mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			refund refundObj = new refund();
			refundObj.setDate(rs.getLong("EVENT_DTM"));
			refundObj.setContentPartner(rs.getString("COMPANY_NAME"));
			refundObj.setRefundAmt(rs.getFloat("EVENT_COST_MNY"));
			return refundObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public JSONObject refundData(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "\r\n"
				+ "    round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 event_dtm\r\n"
				+ "\r\n" + "    ,\r\n" + "\r\n" + "    c.company_name,\r\n" + "\r\n"
				+ "    SUM(cd.event_cost_mny) / 1000  event_cost_mny\r\n" + "\r\n" + "FROM\r\n" + "\r\n"
				+ "    costedevent   cd,\r\n" + "\r\n" + "    customer      c,\r\n" + "\r\n" + "    account       a\r\n"
				+ "\r\n" + "WHERE\r\n" + "\r\n" + "    cd.event_seq >= (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            MAX(x.event_seq) - 3\r\n" + "\r\n" + "        FROM\r\n" + "\r\n"
				+ "            costedevent x\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.account_num = x.account_num\r\n" + "\r\n" + "    )\r\n" + "\r\n"
				+ "    AND ( trunc(cd.event_dtm) >= :startDate\r\n" + "\r\n"
				+ "          AND trunc(cd.event_dtm) < :endDate )\r\n" + "\r\n"
				+ "    AND cd.account_num = a.account_num\r\n" + "\r\n" + "    AND a.customer_ref = c.customer_ref\r\n"
				+ "\r\n" + "    AND cd.event_cost_mny > 0\r\n" + "\r\n" + "GROUP BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 ),\r\n"
				+ "\r\n" + "    c.company_name\r\n" + "\r\n" + "ORDER BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 ),c.company_name";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);	
		
		List<refund> refundData = namedjdbcTemplate.query(sql, namedParams, new refundRowMapper());
		
		JSONObject sampleObject = new JSONObject();
		
		for (int i = 0; i < refundData.size(); i++) {
			if (!sampleObject.containsKey(refundData.get(i).getContentPartner())) {
				JSONArray priceDetailsArray = new JSONArray();
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(refundData.get(i).getDate());
				priceDetails.add(refundData.get(i).getRefundAmt());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(refundData.get(i).getContentPartner(), priceDetailsArray);
			} else {
				JSONArray priceDetailsArray = (JSONArray) sampleObject.get(refundData.get(i).getContentPartner());
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(refundData.get(i).getDate());
				priceDetails.add(refundData.get(i).getRefundAmt());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(refundData.get(i).getContentPartner(), priceDetailsArray);

			}

		}
		
		return sampleObject;
		
	}

}
