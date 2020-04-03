package com.jd.nebulae.common.entity;

import lombok.Data;

import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Data
public class Page<T> {

    private int pageNum;
    private int pageSize;
    private int totalRecord;

    private int totalPage;
    private int startIndex;

    private List<T> data;

    public Page(int pageNum, int pageSize, int totalRecord) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        if (totalRecord % pageSize == 0) {
            this.totalPage = totalRecord / pageSize;
        } else {
            this.totalPage = totalRecord / pageSize + 1;
        }
        this.startIndex = (pageNum - 1) * pageSize;
    }
}
