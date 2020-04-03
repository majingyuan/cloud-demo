package com.jd.nebulae.service.common.mapper;

import com.alibaba.fastjson.JSONObject;
import com.jd.nebulae.service.common.tool.FieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Slf4j
public class JsonMapper implements RowMapper<JSONObject> {

    @Override
    public JSONObject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        JSONObject jsonObj = new JSONObject();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = FieldUtil.underline2Camel(metaData.getColumnLabel(i));
            log.debug("columnName:{}", columnName);
            String value = resultSet.getString(metaData.getColumnLabel(i));
            log.debug("columnValue:{}", value);
            jsonObj.put(columnName, value);
        }
        return jsonObj;
    }
}
