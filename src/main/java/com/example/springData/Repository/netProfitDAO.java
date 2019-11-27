package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.netProfit;

@Repository
public class netProfitDAO {

	public class netProfitRowMapper implements RowMapper {

		@Override
		public netProfit mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			netProfit netProfitObj = new netProfit();
			netProfitObj.setMonth(rs.getInt("EVENT_DTM"));
			netProfitObj.setProfit(rs.getFloat("EVENT_COST_MNY"));
			return netProfitObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public JSONArray netProfit(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "to_char(cd.event_dtm, 'MM') event_dtm,\r\n"
				+ "    SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + "FROM\r\n"
				+ "    costedevent   cd,\r\n" + "    customer      c,\r\n" + "    account       a\r\n" + "WHERE\r\n"
				+ "    cd.event_seq >= (\r\n" + "        SELECT\r\n" + "            MAX(x.event_seq) - 3\r\n"
				+ "        FROM\r\n" + "            costedevent x\r\n" + "        WHERE\r\n"
				+ "            cd.account_num = x.account_num\r\n" + "    )\r\n"
				+ "    AND ( trunc(cd.event_dtm) >= :startDate \r\n"
				+ "          AND trunc(cd.event_dtm) < :endDate )\r\n" + "    AND cd.account_num = a.account_num\r\n"
				+ "    AND a.customer_ref = c.customer_ref\r\n" + "GROUP BY to_char(cd.event_dtm, 'MM')\r\n"
				+ "ORDER BY\r\n" + "   to_char(cd.event_dtm, 'MM')";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		List<netProfit> netProfitData = namedjdbcTemplate.query(sql, namedParams, new netProfitRowMapper());

		JSONArray responseObj = new JSONArray();

		JSONArray months = new JSONArray();
		JSONArray price = new JSONArray();

		String monthMap[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };

		for (int i = 0; i < netProfitData.size(); i++) {
			price.add(netProfitData.get(i).getProfit());
			months.add(monthMap[netProfitData.get(i).getMonth()-1]);
		}

		responseObj.add(months);
		responseObj.add(price);

		return responseObj;
	}

}
