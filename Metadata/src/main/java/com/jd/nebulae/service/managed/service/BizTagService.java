package com.jd.nebulae.service.managed.service;

import com.alibaba.fastjson.JSONObject;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.common.entity.Page;
import com.jd.nebulae.common.entity.TagType;
import com.jd.nebulae.dto.BizLineDto;

import java.util.List;
import java.util.Set;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface BizTagService {

    public JSONObject getBizLineByCode(String code) throws RuntimeException;

    public Boolean addTag(BizLine bizLine) throws RuntimeException;

    public Boolean updateBizTag(BizLine bizLine) throws RuntimeException;

    public Boolean deleteTag(String code) throws RuntimeException;

    Page<JSONObject> getBizLineAll(Integer pageNum, Integer pageSize, BizLine bizLine);

    public List<TagType> getTagTypesAll() throws RuntimeException;

    public Boolean addTagType(TagType tagType) throws RuntimeException;

    public Boolean updateTagType(TagType tagType) throws RuntimeException;

    public Boolean deleteTagType(Long id) throws RuntimeException;

    public BizLine getBizLineByCodeV2(String code) throws RuntimeException;

    public Boolean addTagV2(BizLine bizLine) throws RuntimeException;

    public Boolean updateBizTagV2(BizLine bizLine) throws RuntimeException;

    public Boolean deleteTagV2(Long id) throws RuntimeException;

    public List<BizLine> getBizLineByCondition(BizLine bizLine) throws RuntimeException;

    public Set<BizLineDto> getBizLineLinkByCondition(BizLine bizLine) throws RuntimeException;
}
