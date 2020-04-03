package com.jd.nebulae.service.managed.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.service.common.mapper.BizLineMapper;
import com.jd.nebulae.service.common.mapper.JsonMapper;
import com.jd.nebulae.service.common.repository.impl.NebulaeRepositoryImpl;
import com.jd.nebulae.service.managed.repository.BizMatrixRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Repository
@Slf4j
public class BizMatrixRepositoryImpl extends NebulaeRepositoryImpl implements BizMatrixRepository {

    private static String tableName;
    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
//        if ("dev".equalsIgnoreCase(env.getActiveProfiles()[0])) {
//            tableName = "nebulae_biz_matrix";
//        } else {
            tableName = "nebulae_biz_line";
//        }
    }
//    private public final String tableName = "nebulae_biz_matrix";


    @Override
    public List<JSONObject> getBizLineByPid(Long pid) throws RuntimeException {
        if (pid == null) {
            return getJdbcTemplate().query(
                    "SELECT\n" +
                            "  id,\n" +
                            "  name,\n" +
                            "  comment,\n" +
                            "  code,\n" +
                            "  level,\n" +
                            "  level_code,\n" +
                            "  ordinal,\n" +
                            "  top_id,\n" +
                            "  pid\n" +
                            " FROM " + tableName +
                            " WHERE pid is null " +
                            " ORDER BY ordinal ", new JsonMapper());
        } else {
            return getJdbcTemplate().query(
                    "SELECT\n" +
                            "  id,\n" +
                            "  name,\n" +
                            "  comment,\n" +
                            "  code,\n" +
                            "  level,\n" +
                            "  level_code,\n" +
                            "  ordinal,\n" +
                            "  top_id,\n" +
                            "  pid\n" +
                            " FROM " + tableName +
                            " WHERE pid = ?" +
                            " ORDER BY ordinal ", new JsonMapper(), pid);
        }
    }

    @Override
    public JSONObject getBizLineById(Long id) throws RuntimeException {
        return queryForObject("SELECT\n" +
                "  id,\n" +
                "  name,\n" +
                "  comment,\n" +
                "  code,\n" +
                "  level,\n" +
                "  level_code,\n" +
                "  ordinal,\n" +
                "  top_id,\n" +
                "  pid\n" +
                " FROM " + tableName +
                " WHERE id = ?\n" +
                " ORDER BY ordinal", new JsonMapper(), id);
    }

    @Override
    public List<JSONObject> getAll() throws RuntimeException {
        return getJdbcTemplate().query(
                "SELECT\n" +
                        "  id,\n" +
                        "  name,\n" +
                        "  comment,\n" +
                        "  code,\n" +
                        "  level,\n" +
                        "  level_code,\n" +
                        "  ordinal,\n" +
                        "  top_id,\n" +
                        "  pid\n" +
                        " FROM " + tableName +
                        " WHERE top_id = 6", new JsonMapper());
    }

    public List<JSONObject> getBizLineByLevel(ArrayList<Integer> levels) throws RuntimeException {
        return getJdbcTemplate().query(
                "SELECT\n" +
                        "  id,\n" +
                        "  name,\n" +
                        "  comment,\n" +
                        "  code,\n" +
                        "  level,\n" +
                        "  level_code,\n" +
                        "  ordinal,\n" +
                        "  top_id,\n" +
                        "  pid\n" +
                        " FROM " + tableName +
                        " WHERE level in (?)", new JsonMapper(), Joiner.on(",").join(levels));
    }

    @Override
    public List<JSONObject> getBizLineByTopId(Long topId) throws RuntimeException {
        return getJdbcTemplate().query(
                "SELECT\n" +
                        "  id,\n" +
                        "  name,\n" +
                        "  comment,\n" +
                        "  code,\n" +
                        "  level,\n" +
                        "  level_code,\n" +
                        "  ordinal,\n" +
                        "  top_id,\n" +
                        "  pid\n" +
                        " FROM " + tableName +
                        " WHERE top_id = ? " +
                        " ORDER BY ordinal ", new JsonMapper(), topId);
    }

    @Override
    public List<JSONObject> getBizLineByTopIdAndLevel(Long topId, Long level) throws RuntimeException {
        return getJdbcTemplate().query(
                "SELECT\n" +
                        "  id,\n" +
                        "  name,\n" +
                        "  comment,\n" +
                        "  code,\n" +
                        "  level,\n" +
                        "  level_code,\n" +
                        "  ordinal,\n" +
                        "  top_id,\n" +
                        "  pid\n" +
                        " FROM " + tableName +
                        " WHERE top_id = ?" +
                        " AND level = ?" +
                        " ORDER BY ordinal ", new JsonMapper(), topId, level);
    }

    @Override
    public JSONObject getBizLineByCode(String code) throws RuntimeException {
        return queryForObject("SELECT\n" +
                "  id,\n" +
                "  name,\n" +
                "  comment,\n" +
                "  code,\n" +
                "  level,\n" +
                "  level_code,\n" +
                "  ordinal,\n" +
                "  top_id,\n" +
                "  pid\n" +
                " FROM " + tableName +
                " WHERE code = ?\n" +
                " ORDER BY ordinal", new JsonMapper(), code);
    }

    @Override
    public BizLine getBizLineByCodeV2(String code) throws RuntimeException {
        try {
            String sql = "select biz.*,type.name as top_name  from nebulae_biz_line biz inner join tag_type type on biz.top_id = type.id where biz.code = ?";
            return getJdbcTemplate().queryForObject(sql,new BizLineMapper(),code);
        } catch (Exception ex) {
            log.error("BizMatrixRepositoryImpl_getBizLineByCodeV2 error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
    }


}
