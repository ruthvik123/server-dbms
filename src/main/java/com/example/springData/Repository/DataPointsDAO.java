package com.example.springData.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.springData.POJO.dataPoints;
import com.example.springData.Repository.SalesJdbcDAO.salesRowMapper;

@Repository
public class DataPointsDAO {

	public class DatapointsRowMapper implements RowMapper {

		@Override
		public dataPoints mapRow(ResultSet rs2, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			dataPoints dataPointsObj = new dataPoints();
			dataPointsObj.setNumOftuples(rs2.getInt("DATA"));
			return dataPointsObj;
		}
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public dataPoints DataPoints(String startDate, String endDate) {

		String sql = "SELECT\r\n" + "\r\n" + "    SUM(v_count) As DATA\r\n" + "\r\n" + "FROM\r\n" + "\r\n" + "    (\r\n"
				+ "\r\n" + "        SELECT\r\n" + "\r\n" + "            COUNT(1) v_count\r\n" + "\r\n"
				+ "        FROM\r\n" + "\r\n" + "            costedevent cd\r\n" + "\r\n" + "        WHERE\r\n" + "\r\n"
				+ "            cd.event_dtm BETWEEN :startDate AND :endDate \r\n" + "\r\n" + "        UNION\r\n"
				+ "\r\n" + "        SELECT\r\n" + "\r\n" + "            COUNT(1) v_count\r\n" + "\r\n"
				+ "        FROM\r\n" + "\r\n" + "            eventtype\r\n" + "\r\n" + "        UNION\r\n" + "\r\n"
				+ "        SELECT\r\n" + "\r\n" + "            COUNT(1) v_count\r\n" + "\r\n" + "        FROM\r\n"
				+ "\r\n" + "            eventtypeattribute\r\n" + "\r\n" + "        UNION\r\n" + "\r\n"
				+ "        SELECT\r\n" + "\r\n" + "            COUNT(1) v_count\r\n" + "\r\n" + "        FROM\r\n"
				+ "\r\n" + "            customer\r\n" + "\r\n" + "        UNION\r\n" + "\r\n" + "        SELECT\r\n"
				+ "\r\n" + "            COUNT(1) v_count\r\n" + "\r\n" + "        FROM\r\n" + "\r\n"
				+ "            account\r\n" + "\r\n" + "    )";

		SqlParameterSource namedParams = new MapSqlParameterSource("startDate", startDate).addValue("endDate", endDate);

		return (dataPoints) namedjdbcTemplate.queryForObject(sql, namedParams, new DatapointsRowMapper());

	}
}