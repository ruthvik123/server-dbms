package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.contentPartnerShare;

@Repository
public class contentPartnerShareDAO {

	public class contentPartnerShareRowMapper implements RowMapper {

		@Override
		public contentPartnerShare mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			contentPartnerShare contentPartnerShareObj = new contentPartnerShare();
			contentPartnerShareObj.setName(rs.getString("COMPANY_NAME"));
			contentPartnerShareObj.setY(rs.getFloat("PERCETAGE"));

			return contentPartnerShareObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public List<contentPartnerShare> contentPartnerShareData(String startDate, String endDate) {

		String sql = "\r\n" + "SELECT\r\n" + "\r\n" + "    mas.company_name,\r\n" + "\r\n"
				+ "    round((( mas.event_cost_mny / (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            SUM(cd.event_cost_mny) / 1000 * - 1 total_event_cost\r\n" + "\r\n" + "        FROM\r\n"
				+ "\r\n" + "            costedevent   cd,\r\n" + "\r\n" + "            customer      c,\r\n" + "\r\n"
				+ "            account       a\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.event_seq >= (\r\n" + "\r\n" + "                SELECT\r\n" + "\r\n"
				+ "                    MAX(x.event_seq) - 4\r\n" + "\r\n" + "                FROM\r\n" + "\r\n"
				+ "                    costedevent x\r\n" + "\r\n" + "                WHERE\r\n" + "\r\n"
				+ "                    cd.account_num = x.account_num\r\n" + "\r\n" + "            )\r\n" + "\r\n"
				+ "            AND ( trunc(cd.event_dtm) >= :startDate \r\n" + "\r\n"
				+ "                  AND trunc(cd.event_dtm) < :endDate)\r\n" + "\r\n"
				+ "            AND cd.account_num = a.account_num\r\n" + "\r\n"
				+ "            AND a.customer_ref = c.customer_ref\r\n" + "\r\n"
				+ "            AND cd.event_cost_mny < 0\r\n" + "\r\n" + "    ) ) * 100 ),2) percetage\r\n" + "\r\n"
				+ "FROM\r\n" + "\r\n" + "    (\r\n" + "\r\n" + "        SELECT\r\n" + "\r\n"
				+ "            c.company_name,\r\n" + "\r\n"
				+ "            SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + "\r\n" + "        FROM\r\n"
				+ "\r\n" + "            costedevent   cd,\r\n" + "\r\n" + "            customer      c,\r\n" + "\r\n"
				+ "            account       a\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.event_seq >= (\r\n" + "\r\n" + "                SELECT\r\n" + "\r\n"
				+ "                    MAX(x.event_seq) - 4\r\n" + "\r\n" + "                FROM\r\n" + "\r\n"
				+ "                    costedevent x\r\n" + "\r\n" + "                WHERE\r\n" + "\r\n"
				+ "                    cd.account_num = x.account_num\r\n" + "\r\n" + "            )\r\n" + "\r\n"
				+ "            AND ( trunc(cd.event_dtm) >= :startDate \r\n" + "\r\n"
				+ "                  AND trunc(cd.event_dtm) < :endDate )\r\n" + "\r\n"
				+ "            AND cd.account_num = a.account_num\r\n" + "\r\n"
				+ "            AND a.customer_ref = c.customer_ref\r\n" + "\r\n"
				+ "            AND cd.event_cost_mny < 0\r\n" + "\r\n" + "        GROUP BY\r\n" + "\r\n"
				+ "            c.company_name\r\n" + "\r\n" + "        ORDER BY\r\n" + "\r\n"
				+ "            c.company_name\r\n" + "\r\n" + "    ) mas";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		return namedjdbcTemplate.query(sql, namedParams, new contentPartnerShareRowMapper());

	}

}
