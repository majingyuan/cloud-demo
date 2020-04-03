package com.jd.nebulae.service.common.repository;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface NebulaeRepository {
    public Integer executeSql(String sql) throws RuntimeException;

    public <T> Integer insert(T t) throws RuntimeException;

    public <T> Integer bulkInsert(List<T> list) throws RuntimeException;

    public <T> Integer update(T t) throws RuntimeException;

    /**
     * 解决{@link org.springframework.dao.EmptyResultDataAccessException}
     */
    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... args) throws RuntimeException;

    public <T> T queryForObject(String query, Class<T> clazz, Object... args) throws RuntimeException;

    public Long insertAndGetKey(final String sql) throws RuntimeException;
}
