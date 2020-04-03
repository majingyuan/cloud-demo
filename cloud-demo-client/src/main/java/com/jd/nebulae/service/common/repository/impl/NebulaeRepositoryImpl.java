package com.jd.nebulae.service.common.repository.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jd.nebulae.common.tools.BeanTool;
import com.jd.nebulae.service.common.repository.NebulaeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Repository
public class NebulaeRepositoryImpl implements NebulaeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer executeSql(String sql) throws RuntimeException {
        return jdbcTemplate.update(sql);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public <T> Integer update(T t) {
        return jdbcTemplate.update(BeanTool.generateUpdateSql(t));
    }

    @Override
    public <T> Integer insert(T t) {
        return jdbcTemplate.update(BeanTool.generateInsertSql(Lists.newArrayList(t)));
    }

    @Override
    public <T> Integer bulkInsert(List<T> t) {
        return jdbcTemplate.update(BeanTool.generateInsertSql(t));
    }

    @Override
    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... args) throws RuntimeException {
        List<T> temp = jdbcTemplate.query(query, rowMapper, args);
        if (temp.isEmpty()) {
            return null;
        } else {
            return temp.get(0);
        }
    }

    @Override
    public <T> T queryForObject(String query, Class<T> clazz, Object... args) throws RuntimeException {
        List<T> temp = jdbcTemplate.queryForList(query, clazz, args);
        if (temp.isEmpty()) {
            return null;
        } else {
            return temp.get(0);
        }
    }

    @Override
    public Long insertAndGetKey(final String sql) throws RuntimeException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Map<String, Object>> resSet2JsonArr(SqlRowSet rs) throws JSONException {
        List<Map<String, Object>> array = new ArrayList<>();
        SqlRowSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.add(jsonObj);
        }
        return array;
    }

    public Map<String, Object> resSet2Json(SqlRowSet rs) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        SqlRowSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        if (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
        }
        return jsonObj;
    }
}
