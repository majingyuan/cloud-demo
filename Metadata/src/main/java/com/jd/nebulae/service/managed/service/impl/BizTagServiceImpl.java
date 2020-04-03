package com.jd.nebulae.service.managed.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.common.entity.Page;
import com.jd.nebulae.common.entity.TagType;
import com.jd.nebulae.dto.BizLineDto;
import com.jd.nebulae.service.managed.repository.BizMatrixRepository;
import com.jd.nebulae.service.managed.repository.BizTagRepository;
import com.jd.nebulae.service.managed.service.BizTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Service
@Slf4j
public class BizTagServiceImpl implements BizTagService {

    private static final Long OFFSET = 1000000000L;

    @Autowired
    private BizMatrixRepository bizMatrixRepository;

    @Autowired
    private BizTagRepository bizTagRepository;

    @Override
    public JSONObject getBizLineByCode(String code) throws RuntimeException {
        return bizMatrixRepository.getBizLineByCode(code);
    }

    @Override
    public Boolean addTag(BizLine bizLine) throws RuntimeException {
        return  bizTagRepository.addTag(bizLine);
    }

    @Override
    public Boolean updateBizTag(BizLine bizLine) throws RuntimeException {
        return bizTagRepository.updateBizTag(bizLine);
    }

    @Override
    public Boolean deleteTag(String id) throws RuntimeException {
        return bizTagRepository.deleteTag(id);
    }

    @Override
    public Page<JSONObject> getBizLineAll(Integer pageNum, Integer pageSize, BizLine bizLine) {
        Integer total = bizTagRepository.countBizLine(bizLine);
        Page<JSONObject> result = new Page<>(pageNum, pageSize, total);
        List<JSONObject> list = bizTagRepository.getBizLineAll( pageNum, pageSize, bizLine);
        result.setData(list);
        return result;
    }

    @Override
    public List<TagType> getTagTypesAll() throws RuntimeException{
        return bizTagRepository.getTagTypesAll();
    }

    @Override
    public Boolean  addTagType(TagType tagType) throws RuntimeException {
        if(this.tagTypeNameExist(tagType)){
            throw new RuntimeException("类型名称已存在");
        }
        return bizTagRepository.addTagType(tagType);
    }



    @Override
    public Boolean updateTagType(TagType tagType) throws RuntimeException {
        return bizTagRepository.updateTagType(tagType);
    }

    @Override
    public Boolean deleteTagType(Long id) throws RuntimeException {
        if (bizTagRepository.countBizLineByTopId(id) > 0) {
            throw new RuntimeException("你不能删除已经被使用的类型");
        } else {
            return bizTagRepository.deleteTagType(id);
        }
    }

    @Override
    public BizLine getBizLineByCodeV2(String code) throws RuntimeException {
        return bizMatrixRepository.getBizLineByCodeV2(code);
    }

    @Override
    public List<BizLine> getBizLineByCondition(BizLine bizLine) throws RuntimeException {
        return bizTagRepository.getBizLineByCondition(bizLine);
    }

    @Override
    public Boolean addTagV2(BizLine bizLine) throws RuntimeException {

        BizLine pbizLine = this.getParentBizLine(bizLine.getPid());
        if(bizLine.getPid() != 0 &&(pbizLine==null || this.checkTagLevel(pbizLine))){
            return false;
        }

        bizLine.setLevel(this.generateLevelCode(pbizLine,bizLine));

        return  bizTagRepository.addTagV2(bizLine);
    }


    private boolean tagTypeNameExist(TagType tagType) {
        TagType tagTypeBean = new TagType();
        tagTypeBean.setName(tagType.getName());
        List<TagType> result = bizTagRepository.getTagTypeByCondition(tagTypeBean);
        if(result!=null && !result.isEmpty()) return true;

        return false;
    }

    @Override
    public Boolean updateBizTagV2(BizLine bizLine) throws RuntimeException {
        return bizTagRepository.updateBizTagV2(bizLine);
    }

    @Override
    public Boolean deleteTagV2(Long id) throws RuntimeException {
        return bizTagRepository.deleteTagV2(id);
    }
    private BizLine getParentBizLine(Long pid) {
        BizLine bl =  new BizLine();
        bl.setId(pid);

        List<JSONObject> list = bizTagRepository .getBizLineAll(1,1,bl);
        if(list == null||list.isEmpty()){
            log.error("未能查询到对应的父标签");
            return null;
        }

        return JSONObject.parseObject(list.get(0).toJSONString(), BizLine.class);
    }

    @Override
    public Set<BizLineDto> getBizLineLinkByCondition(BizLine bizLine) throws RuntimeException{
        Set<BizLineDto> result =new HashSet();

        List<BizLine> fristResultList = this.getBizLineByCondition(bizLine);

        Map<Long,BizLineDto> bizLineMap =  new HashMap<>();
        Map<Long,BizLineDto> deleteMap = new HashMap<>();
        List<BizLineDto> bizLineDtoList = new ArrayList<>();

        for(BizLine bLine:fristResultList){
            BizLineDto bizLineDto = JSON.parseObject(JSONObject.toJSONString(bLine),BizLineDto.class);
            bizLineMap.put(bLine.getId(),bizLineDto);
            bizLineDtoList.add(bizLineDto);
        }
        for(BizLineDto bizLineDto:bizLineDtoList){
            if(deleteMap.get(bizLineDto.getId()) !=null ){
                continue;
            }
            if(deleteMap.get(bizLineDto.getPid()) !=null ){
                BizLineDto pBizLineDto = deleteMap.get(bizLineDto.getPid());
                pBizLineDto.addChildBizLineDto(bizLineDto);

                deleteMap.put(bizLineDto.getId(),bizLineDto);
                continue;
            }
            BizLineDto topLineDto = recursiveQueryParents(bizLineMap,deleteMap,bizLineDto,0);
            result.add(topLineDto);
            deleteMap.put(topLineDto.getId(),topLineDto);
        }
        return result;
    }
    private BizLineDto recursiveQueryParents(Map<Long,BizLineDto> bizLineMap,Map<Long,BizLineDto> deleteMap , BizLineDto bizLineDto,int level){
        if(level >= 4 || ( bizLineDto.getPid() == 0 || bizLineDto.getPid() == null )) {
            return bizLineDto;
        }
        level += 1;

        BizLineDto pBizLineDto = null;

        if(bizLineMap.get(bizLineDto.getPid()) != null){
            pBizLineDto = bizLineMap.get(bizLineDto.getPid());
            deleteMap.put(pBizLineDto.getId(),pBizLineDto);
        }else{
            BizLine bizLine =  new BizLine();
            bizLine.setPid(bizLineDto.getPid());
            List<BizLine> resultList = this.getBizLineByCondition(bizLine);

            if(resultList != null && !resultList.isEmpty()){
                pBizLineDto =JSON.parseObject(JSONObject.toJSONString(resultList.get(0)),BizLineDto.class);
            }

        }

        if(pBizLineDto == null){
            return bizLineDto;
        }else{
            pBizLineDto.addChildBizLineDto(bizLineDto);
            return recursiveQueryParents(bizLineMap,  deleteMap,pBizLineDto, level);
        }

    }

    private Integer generateLevelCode(BizLine pbizLine, BizLine bizLine) {
        if(bizLine.getPid() == 0){
            return 0;
        }

        return pbizLine.getLevel()+1;
    }

    private boolean checkTagLevel(BizLine pbizLine) {

        if(pbizLine.getLevel()>=4){
            log.error("子标签层级超过了限制");
            return true;
        }else{
            return false;
        }

    }
}
