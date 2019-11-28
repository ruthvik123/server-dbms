package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.serviceRevenue;

@Repository
public class serviceRevenueDAO {

	public class serviceRevenueRowMapper implements RowMapper {

		@Override
		public serviceRevenue mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			serviceRevenue serviceRevenueObj = new serviceRevenue();
			serviceRevenueObj.setServiceType(rs.getString("EVENT_TYPE_NAME"));
			serviceRevenueObj.setRevenue(rs.getFloat("EVENT_COST_MNY"));

			return serviceRevenueObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public JSONArray getServiceRevenueData(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "\r\n" + "    et.event_type_name,\r\n" + "\r\n"
				+ "    SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + "\r\n" + "FROM\r\n" + "\r\n"
				+ "    costedevent   cd,\r\n" + "\r\n" + "    customer      c,\r\n" + "\r\n"
				+ "    account       a,\r\n" + "\r\n" + "    eventtype     et\r\n" + "\r\n" + "WHERE\r\n" + "\r\n"
				+ "    cd.event_seq >= (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            MAX(x.event_seq) - 4\r\n" + "\r\n" + "        FROM\r\n" + "\r\n"
				+ "            costedevent x\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.account_num = x.account_num\r\n" + "\r\n" + "    )\r\n" + "\r\n"
				+ "    AND ( trunc(cd.event_dtm) >= :startDate \r\n" + "\r\n"
				+ "          AND trunc(cd.event_dtm) < :endDate )\r\n" + "\r\n"
				+ "    AND cd.account_num = a.account_num\r\n" + "\r\n" + "    AND a.customer_ref = c.customer_ref\r\n"
				+ "\r\n" + "    AND cd.event_cost_mny < 0\r\n" + "\r\n"
				+ "    AND cd.event_type_id = et.event_type_id\r\n" + "\r\n" + "GROUP BY\r\n" + "\r\n"
				+ "    et.event_type_name";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		List<serviceRevenue> serviceRevenueData = namedjdbcTemplate.query(sql, namedParams,
				new serviceRevenueRowMapper());

		JSONArray responseObj = new JSONArray();

		JSONArray serviceName = new JSONArray();
		JSONArray revenueOfService = new JSONArray();

		for (int i = 0; i < serviceRevenueData.size(); i++) {
			serviceName.add(serviceRevenueData.get(i).getServiceType());
			revenueOfService.add(serviceRevenueData.get(i).getRevenue());
		}

		responseObj.add(serviceName);
		responseObj.add(revenueOfService);

		return responseObj;

	}

}
