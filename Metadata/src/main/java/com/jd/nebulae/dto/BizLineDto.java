package com.jd.nebulae.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BizLineDto implements Serializable {

    private Long id;

    private Long pid;
    private String name;

    private String comment;

    private String code;

    private Integer level;

    private String levelCode;


    private Integer order;


    private Integer topId;

    private String topName;

    private List<BizLineDto> childList  = new ArrayList<>();


    private String createBy;


    private String updateBy;


    private Long createTime;

    private Long updateTime;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getTopId() {
        return topId;
    }

    public void setTopId(Integer topId) {
        this.topId = topId;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }


    public List<BizLineDto> getChildList() {
        return childList;
    }

    public void setChildList(List<BizLineDto> childList) {
        this.childList = childList;
    }

    public void addChildBizLineDto(BizLineDto childDizLine){
        if(!this.getChildList().contains(childDizLine)){
            this.getChildList().add(childDizLine);
        }

    }
}
