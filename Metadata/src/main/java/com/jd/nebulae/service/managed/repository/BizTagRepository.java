package com.jd.nebulae.service.managed.repository;

import com.alibaba.fastjson.JSONObject;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.common.entity.TagType;
import com.jd.nebulae.service.common.repository.NebulaeRepository;

import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface BizTagRepository extends NebulaeRepository {

    public boolean addTag(BizLine bizLine) throws RuntimeException;

    public boolean updateBizTag(BizLine bizLine) throws RuntimeException;

    public boolean deleteTag(String code) throws RuntimeException;

    List<JSONObject> getBizLineAll(Integer pageNum, Integer pageSize, BizLine bizLine);

    Integer countBizLine(BizLine bizLine);

    Integer countBizLineByTopId(Long topId);

    public List<TagType> getTagTypesAll() throws RuntimeException;

    public boolean addTagType(TagType tagType) throws RuntimeException;

    public boolean updateTagType(TagType tagType) throws RuntimeException;

    public boolean deleteTagType(Long topId) throws RuntimeException;

    public boolean addTagV2(BizLine bizLine) throws RuntimeException;

    public boolean updateBizTagV2(BizLine bizLine) throws RuntimeException;

    public boolean deleteTagV2(Long id) throws RuntimeException;

    public List<BizLine> getBizLineByCondition(BizLine bizLine) throws RuntimeException;

    public List<TagType> getTagTypeByCondition(TagType tagType) throws RuntimeException;
}
