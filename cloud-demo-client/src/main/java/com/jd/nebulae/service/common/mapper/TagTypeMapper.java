package com.jd.nebulae.service.common.mapper;

import com.jd.nebulae.common.entity.TagType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagTypeMapper implements RowMapper<TagType> {

  @Override
  public TagType mapRow(ResultSet rs, int rowNum) throws SQLException {
    TagType tagType = new TagType();
    tagType.setId(rs.getLong("id"));
    tagType.setCode(rs.getString("code"));
    tagType.setName(rs.getString("name"));
    tagType.setDescription(rs.getString("description"));
    tagType.setLevel(rs.getInt("level"));
    tagType.setSort(rs.getInt("sort"));
    tagType.setPCode(rs.getString("p_code"));
    tagType.setPName(rs.getString("p_name"));
    tagType.setCreateBy(rs.getString("create_by"));
    tagType.setUpdateBy(rs.getString("update_by"));
    tagType.setCreateTime(rs.getLong("create_time"));
    tagType.setUpdateTime(rs.getLong("update_time"));
    return tagType;
  }
}
