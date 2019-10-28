package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.Sales;

@Repository
public class SalesJdbcDAO {

	
	public class salesRowMapper implements RowMapper{

	@Override
	public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Sales salesObj = new Sales();
		salesObj.setDate(rs.getString("EVENT_DTM"));
		salesObj.setContentPartner(rs.getString("COMPANY_NAME"));
		salesObj.setPrice(rs.getFloat("EVENT_COST_MNY"));
		return salesObj;
	}
	}
	
	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;
	
	public List<Sales> salesData(String startDate, String endDate) {
		
		String sql = "SELECT\r\n" + 
				"    to_char(cd.event_dtm, 'dd-mm-yyyy') Event_dtm,\r\n" + 
				"    c.company_name,\r\n" + 
				"    SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + 
				"FROM\r\n" + 
				"    pprabhu.costedevent   cd,\r\n" + 
				"    pprabhu.customer      c,\r\n" + 
				"    pprabhu.account       a\r\n" + 
				"WHERE\r\n" + 
				"    trunc(cd.event_dtm) >= :startDate \r\n" + 
				"    AND trunc(cd.event_dtm) <= :endDate \r\n" + 
				"    AND cd.event_seq = (\r\n" + 
				"        SELECT\r\n" + 
				"            MAX(x.event_seq)\r\n" + 
				"        FROM\r\n" + 
				"            pprabhu.costedevent x\r\n" + 
				"        WHERE\r\n" + 
				"            cd.account_num = x.account_num\r\n" + 
				"    )\r\n" + 
				"    AND cd.account_num = a.account_num\r\n" + 
				"    AND a.customer_ref = c.customer_ref\r\n" + 
				"    AND cd.event_cost_mny < 0\r\n" + 
				"GROUP BY\r\n" + 
				"    to_char(cd.event_dtm, 'dd-mm-yyyy'),\r\n" + 
				"    c.company_name\r\n" + 
				"    union\r\n" + 
				"    SELECT\r\n" + 
				"    to_char(cd.event_dtm, 'dd-mm-yyyy') Event_dtm,\r\n" + 
				"    'GOOGLE CORPORATION'  company_name,\r\n" + 
				"    SUM(cd.event_cost_mny) / 1000 * - 1 event_cost_mny\r\n" + 
				"FROM\r\n" + 
				"    pprabhu.costedevent   cd,\r\n" + 
				"    pprabhu.customer      c,\r\n" + 
				"    pprabhu.account       a\r\n" + 
				"WHERE\r\n" + 
				"    trunc(cd.event_dtm) >= :startDate \r\n" + 
				"    AND trunc(cd.event_dtm) <= :endDate \r\n" + 
				"    AND cd.event_seq = (\r\n" + 
				"        SELECT\r\n" + 
				"            MAX(x.event_seq)\r\n" + 
				"        FROM\r\n" + 
				"            pprabhu.costedevent x\r\n" + 
				"        WHERE\r\n" + 
				"            cd.account_num = x.account_num\r\n" + 
				"    )\r\n" + 
				"    AND cd.account_num = a.account_num\r\n" + 
				"    AND a.customer_ref = c.customer_ref\r\n" + 
				"    AND cd.event_cost_mny < 0\r\n" + 
				"GROUP BY\r\n" + 
				"    to_char(cd.event_dtm, 'dd-mm-yyyy'),\r\n" + 
				"    c.company_name" ;
		
		SqlParameterSource namedParams = new MapSqlParameterSource("startDate",startDate )
				                                                .addValue("endDate", endDate);
		
		return namedjdbcTemplate.query(sql, namedParams, new salesRowMapper());
		
	}
	
			
	}
	


