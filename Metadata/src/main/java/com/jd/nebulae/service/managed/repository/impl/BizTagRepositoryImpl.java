package com.jd.nebulae.service.managed.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.common.entity.TagType;
import com.jd.nebulae.service.common.mapper.BizLineMapper;
import com.jd.nebulae.service.common.mapper.JsonMapper;
import com.jd.nebulae.service.common.mapper.TagTypeMapper;
import com.jd.nebulae.service.common.repository.impl.NebulaeRepositoryImpl;
import com.jd.nebulae.service.managed.repository.BizTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Repository
@Slf4j
public class BizTagRepositoryImpl extends NebulaeRepositoryImpl implements BizTagRepository {

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

    @Override
    public boolean addTag(BizLine bizLine) throws RuntimeException {
        String sql = "insert into "+tableName+"(name, comment, code, level, level_code, ordinal, top_id, pid)values(?,?,?,?,?,?,?,?)";
        Object args[] = {bizLine.getName(),bizLine.getComment(),bizLine.getCode(),bizLine.getLevel(),bizLine.getLevelCode(),bizLine.getOrder(),bizLine.getTopId(),bizLine.getPid()};
        int temp = getJdbcTemplate().update(sql, args);
        if (temp > 0) {
            return true;
        }
        return false;
    }

	@Override
	public boolean addTagV2(BizLine bizLine) throws RuntimeException {

		String querySql = "";
		int count = 0;
		if(bizLine.getLevel() == 0)
		{
			querySql = "select count(1) from " + tableName + " where level = 0 and name = ?";
			count = getJdbcTemplate().queryForObject(querySql, Integer.class, bizLine.getName());
			if(count > 0)
			{
				throw new RuntimeException("this top name is already exists in the table " + tableName);
			}
			querySql = "select count(1) from " + tableName + " where level = 0 and code = ?";
			count = getJdbcTemplate().queryForObject(querySql, Integer.class, bizLine.getCode());
			if(count > 0)
			{
				throw new RuntimeException("this top code is already exists in the table " + tableName);
			}
		}

        querySql = "select ifnull(max(ordinal),0) from " + tableName + " where level = ?";
        int ordinal = getJdbcTemplate().queryForObject(querySql, Integer.class, bizLine.getLevel()) + 1;
        String levelCode = bizLine.getLevel() + "_" + ordinal;

		String sql = "insert into "+tableName+"(name, comment, code, level, level_code, ordinal, top_id, pid,create_by,create_time)values(?,?,?,?,?,?,?,?,?,?)";
		boolean result = false;
		try {
			int temp = getJdbcTemplate().update(sql, bizLine.getName(),bizLine.getComment(),bizLine.getCode(),bizLine.getLevel(),levelCode,ordinal,bizLine.getTopId(),bizLine.getPid(),
				bizLine.getCreateBy(),System.currentTimeMillis());
			if (temp > 0) {
				result = true;
			} else {
				result = false;
			}
		}catch (Exception ex) {
			log.error("BizTagRepositoryImpl_addTagV2 error: " + ex.getStackTrace());
			throw new RuntimeException(ex);
		}
		return result;
	}

    @Override
    public boolean updateBizTag(BizLine bizLine) throws RuntimeException {
        String sql = "update "+ tableName+" set name= ?, comment= ?, code= ?, level= ?, level_code= ?, ordinal= ?, top_id= ?, pid = ? where id = ?";
        Object args[] = {bizLine.getName(),bizLine.getComment(),bizLine.getCode(),bizLine.getLevel(),bizLine.getLevelCode(),bizLine.getOrder(),bizLine.getTopId(),bizLine.getPid(),bizLine.getId()};
        int temp = getJdbcTemplate().update(sql,args);
        if (temp > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBizTagV2(BizLine bizLine) throws RuntimeException {
        String sql = "update "+ tableName+" set name= ?, comment= ?, code= ?, level= ?, level_code= ?, ordinal= ?, top_id= ?, pid = ?,update_by = ?,update_time = ? where id = ?";
        boolean result = false;
        try {
            int temp = getJdbcTemplate().update(sql, bizLine.getName(),bizLine.getComment(),bizLine.getCode(),bizLine.getLevel(),bizLine.getLevelCode(),bizLine.getOrder(),bizLine.getTopId(),bizLine.getPid(),
                bizLine.getUpdateBy(),System.currentTimeMillis(),bizLine.getId());
            if (temp > 0) {
                result = true;
            } else {
                result = false;
            }
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_updateBizTagV2 error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public boolean deleteTag(String id) throws RuntimeException {
        String sql = "delete from "+tableName+" where id = ?";
        Object args[] = new Object[]{Integer.parseInt(id)};
        int temp = getJdbcTemplate().update(sql,args);
        if (temp > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTagV2(Long id) throws RuntimeException {
        String sql = "delete from "+tableName+" where id = ?";
        boolean result = false;
        try {
            int temp = getJdbcTemplate().update(sql,id);
            if (temp > 0) {
                result = true;
            } else {
                result = false;
            }
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_deleteTagV2 error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public List<JSONObject> getBizLineAll(Integer pageNum, Integer pageSize, BizLine bizLine) {
        String sql = "SELECT id,name,comment,code,level,level_code,ordinal,top_id,pid FROM " +tableName+" where 1=1 ";
        StringBuilder sb =BuildSqlStr(sql,bizLine);
        sb.append(" ORDER BY ordinal limit "+(pageNum-1)*pageSize +","+pageSize);
        return getJdbcTemplate().query(sb.toString(), new JsonMapper());
    }

    @Override
    public Integer countBizLine(BizLine bizLine) throws RuntimeException {
        String sql = "SELECT count(1) FROM "+tableName+" where 1=1 ";
        StringBuilder sb =BuildSqlStr(sql,bizLine);
        return getJdbcTemplate().queryForObject(sb.toString(), Integer.class);
    }

    @Override
    public Integer countBizLineByTopId(Long topId) throws RuntimeException {
        String sql = "SELECT count(*) FROM "+tableName+" where top_id = ?";
        int result = 0;
        try {
            result = getJdbcTemplate().queryForObject(sql, Integer.class, new Object[]{topId});
        } catch (Exception ex) {
            log.error("BizTagRepositoryImpl_countBizLineByTopId error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public List<TagType> getTagTypesAll() throws RuntimeException{
        List<TagType> results = Lists.newArrayList();
        try {
            String sql = "select * from tag_type order by sort";
            //results = getJdbcTemplate().query(sql,new JsonMapper());
            results = getJdbcTemplate().query(sql,new TagTypeMapper());
        } catch (Exception ex) {
            log.error("BizTagRepositoryImpl_getTagTypesAll error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return results;
    }

    @Override
    public boolean addTagType(TagType tagType) throws RuntimeException {
        String sql = "insert into tag_type (code,name,description,level,sort,p_code,p_name,create_by,create_time,category) values (?,?,?,?,?,?,?,?,?,?)";
        boolean result = false;
        try {
            int temp = getJdbcTemplate().update(sql, tagType.getCode(), tagType.getName(),tagType.getDescription(),tagType.getLevel(),tagType.getSort(),tagType.getPCode(),tagType.getPName(),
                tagType.getCreateBy(),System.currentTimeMillis(),tagType.getCategory());
            if (temp > 0) {
                result = true;
            } else {
                result = false;
            }
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_addTagType error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public boolean updateTagType(TagType tagType) throws RuntimeException {
        //String updateToBizLineSql = String.format("update s% set name = %s, comment = s%, code = %s, level = s%, level_code = s%, ordinal = s%, top_id = s%, pid = s% where top",
        //    tableName,bizLine.getName(),bizLine.getComment(),bizLine.getCode(),bizLine.getLevel(),bizLine.getLevelCode(),bizLine.getOrder(),bizLine.getTopId(),bizLine.getPid(),bizLine.getId());
        //String updateToTagTypeSql = String.format("update tag_type set id = %,name = %",tagType.getId(),tagType.getName());
        //return excuteSql(updateToTagTypeSql);
        String sql = "update tag_type set code = ?,name = ?,description = ?,level = ?,sort = ?,p_code = ?,p_name = ?,update_by = ?,update_time = ?,category = ? where id = ?";
        boolean result = false;
        try {
            int temp = getJdbcTemplate().update(sql, tagType.getCode(), tagType.getName(),tagType.getDescription(),tagType.getLevel(),tagType.getSort(),tagType.getPCode(),tagType.getPName(),
                tagType.getUpdateBy(),System.currentTimeMillis(),tagType.getCategory(),tagType.getId());
            if (temp > 0) {
                result = true;
            } else {
                result = false;
            }
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_updateTagType error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public boolean deleteTagType(Long topId) throws RuntimeException {
        String sql = "delete from tag_type where id = ?";
        boolean result = false;
        try {
            int temp = getJdbcTemplate().update(sql, topId);
            if (temp > 0) {
                result = true;
            } else {
                result = false;
            }
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_deleteTagType error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public List<BizLine> getBizLineByCondition(BizLine bizLine) throws RuntimeException {
        try {
            String sql = "select biz.*,type.name as top_name  from nebulae_biz_line biz inner join tag_type type on biz.top_id = type.id where 1 =1 ";
            sql = constructSql(sql,bizLine) + " order by biz.ordinal";
            return getJdbcTemplate().query(sql,new BizLineMapper());
        } catch (Exception ex) {
            log.error("BizTagRepositoryImpl_getBizLineByCondition error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
    }


    private boolean excuteSql(String ...sqls) {
        Connection con = null;
        boolean ac = true;
        Statement stmt = null;
        try {
            con = getJdbcTemplate().getDataSource().getConnection();
            ac = con.getAutoCommit();
            con.setAutoCommit(false);
            stmt = con.createStatement();
        } catch (SQLException ex) {
            log.error("BizTagRepositoryImpl_addTagType get jdbc connection or statement error: " + ex.getStackTrace());
            return false;
        }
        try {
            for(String sql:sqls) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            con.commit();
            return true;
        }catch (Exception ex) {
            log.error("BizTagRepositoryImpl_addTagType error: " + ex.getStackTrace());
            try {
                con.rollback();
            } catch (SQLException sqlEx) {
                log.error("BizTagRepositoryImpl_addTagType roll back error: " + sqlEx.getStackTrace());
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            try {
                con.setAutoCommit(ac);
                if(stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("BizTagRepositoryImpl_addTagType close connection error: " + ex.getStackTrace());
                throw new RuntimeException(ex);
            }
        }
    }
    @Override
    public List<TagType> getTagTypeByCondition(TagType tagType) throws RuntimeException {
        try {
            String sql = "select tagType.* from tag_type tagType  where 1 =1 ";
            sql = constructTagTypeSql(sql,tagType) ;
            return getJdbcTemplate().query(sql,new TagTypeMapper());
        } catch (Exception ex) {
            log.error("BizTagRepositoryImpl_getBizLineByCondition error: " + ex.getStackTrace());
            throw new RuntimeException(ex);
        }
    }

    /**
     * @desc: 扩展查询条件
     * @author zhoujiayang2
     * @date 2019/3/28
      * @param sqlStr
     * @param bizLine
     * @return java.lang.StringBuilder
     */
    private StringBuilder BuildSqlStr(String sqlStr,BizLine bizLine){
        StringBuilder sb = new StringBuilder();
        String sql = sqlStr;
        sb.append(sql);
        if(bizLine!=null){
            if(!StringUtils.isEmpty(bizLine.getLevel())){
                sb.append(" and level="+bizLine.getLevel());
            }
            if(!StringUtils.isEmpty(bizLine.getCode())){
                sb.append(" and code='"+bizLine.getCode()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getComment())){
                sb.append(" and COMMENT='"+bizLine.getComment()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getName())){
                sb.append(" and name like '%"+bizLine.getName()+"%'");
            }
            if(!StringUtils.isEmpty(bizLine.getLevelCode())){
                sb.append(" and level_code='"+bizLine.getLevelCode()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getTopId())){
                sb.append(" and top_id="+bizLine.getTopId());
            }
            if(!StringUtils.isEmpty(bizLine.getPid())){
                sb.append(" and pid="+bizLine.getPid());
            }
            if(!StringUtils.isEmpty(bizLine.getId())){
                sb.append(" and id="+bizLine.getId());
            }
        }
        return sb;
    }

    /**
     * @desc: 扩展更多查询条件
     * @author zhudan33
     * @date 2020/2/29
     * @param sqlStr
     * @param bizLine
     * @return java.lang.StringBuilder
     */
    private String constructSql(String sqlStr,BizLine bizLine){
        StringBuilder sb = new StringBuilder(sqlStr);
        if(bizLine!=null){
            if(!StringUtils.isEmpty(bizLine.getLevel())){
                sb.append(" and biz.level="+bizLine.getLevel());
            }
            if(!StringUtils.isEmpty(bizLine.getCode())){
                sb.append(" and biz.code='"+bizLine.getCode()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getComment())){
                sb.append(" and biz.comment='"+bizLine.getComment()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getName())){
                sb.append(" and biz.name like '%"+bizLine.getName()+"%'");
            }
            if(!StringUtils.isEmpty(bizLine.getLevelCode())){
                sb.append(" and biz.level_code='"+bizLine.getLevelCode()+"'");
            }
            if(!StringUtils.isEmpty(bizLine.getTopId())){
                sb.append(" and biz.top_id="+bizLine.getTopId());
            }
            if(!StringUtils.isEmpty(bizLine.getPid())){
                sb.append(" and biz.pid="+bizLine.getPid());
            }
            if(!StringUtils.isEmpty(bizLine.getId())){
                sb.append(" and biz.id="+bizLine.getId());
            }
            if(!StringUtils.isEmpty(bizLine.getCreateBy())){
                sb.append(" and biz.create_by="+bizLine.getCreateBy());
            }
            if(!StringUtils.isEmpty(bizLine.getUpdateBy())){
                sb.append(" and biz.update_by="+bizLine.getUpdateBy());
            }
            if(!StringUtils.isEmpty(bizLine.getCreateTime())){
                sb.append(" and biz.create_time="+bizLine.getCreateTime());
            }
            if(!StringUtils.isEmpty(bizLine.getUpdateTime())){
                sb.append(" and biz.update_time="+bizLine.getUpdateTime());
            }
        }
        return sb.toString();
    }

    private String constructTagTypeSql(String sqlStr,TagType tagType) {
        StringBuilder sb = new StringBuilder(sqlStr);
        if (tagType != null) {
            if (!StringUtils.isEmpty(tagType.getName())) {
                sb.append(" and tagType.name=" + tagType.getName());
            }
            if (!StringUtils.isEmpty(tagType.getId())) {
                sb.append(" and tagType.id=" + tagType.getId());
            }
        }
        return sb.toString();
    }
}
