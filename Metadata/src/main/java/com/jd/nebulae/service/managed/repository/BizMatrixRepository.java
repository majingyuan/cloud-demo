package com.jd.nebulae.service.managed.repository;

import com.alibaba.fastjson.JSONObject;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.service.common.repository.NebulaeRepository;

import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface BizMatrixRepository extends NebulaeRepository {

    public List<JSONObject> getBizLineByPid(Long pid) throws RuntimeException;

    public JSONObject getBizLineById(Long id) throws RuntimeException;

    public List<JSONObject> getAll() throws RuntimeException;

    public List<JSONObject> getBizLineByTopId(Long topId) throws RuntimeException;

    public List<JSONObject> getBizLineByTopIdAndLevel(Long topId, Long level) throws RuntimeException;

    public BizLine getBizLineByCodeV2(String code) throws RuntimeException;

    public JSONObject getBizLineByCode(String code) throws RuntimeException;

}
