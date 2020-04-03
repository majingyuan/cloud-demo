package com.jd.nebulae.service.common.mapper;

import com.jd.nebulae.common.entity.BizLine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BizLineMapper implements RowMapper<BizLine> {
  @Override
  public BizLine mapRow(ResultSet rs, int rowNum) throws SQLException {
    BizLine bizLine = new BizLine();
    bizLine.setId(rs.getLong("id"));
    bizLine.setCode(rs.getString("code"));
    bizLine.setName(rs.getString("name"));
    bizLine.setComment(rs.getString("comment"));
    bizLine.setLevel(rs.getInt("level"));
    bizLine.setLevelCode(rs.getString("level_code"));
    bizLine.setOrder(rs.getInt("ordinal"));
    bizLine.setTopId(rs.getInt("top_id"));
    bizLine.setTopName(rs.getString("top_name"));
    bizLine.setPid(rs.getLong("pid"));
    bizLine.setCreateBy(rs.getString("create_by"));
    bizLine.setUpdateBy(rs.getString("update_by"));
    bizLine.setCreateTime(rs.getLong("create_time"));
    bizLine.setUpdateTime(rs.getLong("update_time"));
    return bizLine;
  }
}
