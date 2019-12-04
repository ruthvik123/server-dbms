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


@Repository
public class SalesJdbcDAO {

	public class salesRowMapper implements RowMapper {

		@Override
		public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Sales salesObj = new Sales();
			salesObj.setDate(rs.getLong("EVENT_DTM"));
			salesObj.setContentPartner(rs.getString("COMPANY_NAME"));
			salesObj.setPrice(rs.getFloat("EVENT_COST_MNY"));
			return salesObj;
		}
	}



	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public JSONObject salesData(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "\r\n"
				+ "    round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 event_dtm\r\n"
				+ "\r\n" + "    ,\r\n" + "\r\n" + "    c.company_name,\r\n" + "\r\n"
				+ "    SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + "\r\n" + "FROM\r\n" + "\r\n"
				+ "    costedevent   cd,\r\n" + "\r\n" + "    customer      c,\r\n" + "\r\n" + "    account       a\r\n"
				+ "\r\n" + "WHERE\r\n" + "\r\n" + "    cd.event_seq >= (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            MAX(x.event_seq) - 3\r\n" + "\r\n" + "        FROM\r\n" + "\r\n"
				+ "            costedevent x\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.account_num = x.account_num\r\n" + "\r\n" + "    )\r\n" + "\r\n"
				+ "    AND ( trunc(cd.event_dtm) >= :startDate \r\n" + "\r\n"
				+ "          AND trunc(cd.event_dtm) < :endDate )\r\n" + "\r\n"
				+ "    AND cd.account_num = a.account_num\r\n" + "\r\n" + "    AND a.customer_ref = c.customer_ref\r\n"
				+ "\r\n" + "    AND cd.event_cost_mny < 0\r\n" + "\r\n" + "GROUP BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 ),\r\n"
				+ "\r\n" + "    c.company_name\r\n" + "\r\n" + "ORDER BY\r\n" + "\r\n"
				+ "    ( round(((to_date(to_char(cd.event_dtm, 'YYYYMMDD'), 'YYYYMMDD') - TO_DATE('19700101', 'YYYYMMDD'))) /(1 / 86400)) * 1000 ),c.company_name";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		List<Sales> salesData = namedjdbcTemplate.query(sql, namedParams, new salesRowMapper());

//		Map<String, List<Sales>> data = new HashMap();
//		
//		for(int i = 0; i < salesData.size(); i++) {
//			if(! data.containsKey(salesData.get(i).getContentPartner())) {
//				List<Sales> temp = new LinkedList();
//				temp.add(salesData.get(i));
//				data.put(salesData.get(i).getContentPartner(), temp );
//			}
//			
//			else {
//				data.get(salesData.get(i).getContentPartner()).add(salesData.get(i));
//				
//				data.put(salesData.get(i).getContentPartner(), data.get(salesData.get(i).getContentPartner()));
//			}
//		}

		JSONObject sampleObject = new JSONObject();

		for (int i = 0; i < salesData.size(); i++) {
			if (!sampleObject.containsKey(salesData.get(i).getContentPartner())) {
				JSONArray priceDetailsArray = new JSONArray();
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(salesData.get(i).getDate());
				priceDetails.add(salesData.get(i).getPrice());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(salesData.get(i).getContentPartner(), priceDetailsArray);
			} else {
				JSONArray priceDetailsArray = (JSONArray) sampleObject.get(salesData.get(i).getContentPartner());
				JSONArray priceDetails = new JSONArray();
				priceDetails.add(salesData.get(i).getDate());
				priceDetails.add(salesData.get(i).getPrice());
				priceDetailsArray.add(priceDetails);
				sampleObject.put(salesData.get(i).getContentPartner(), priceDetailsArray);

			}

		}

		return sampleObject;

	}



}
