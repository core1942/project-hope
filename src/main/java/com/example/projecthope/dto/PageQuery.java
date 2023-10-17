package com.example.projecthope.dto;

/**
 * @author wp
 * @date 2020/11/23
 * 分页使用
 */

public class PageQuery {


    /**
     * 功能：页大小
     */
    private Integer pageSize;

    /**
     * 功能：页码
     */
    private Integer pageNum;


    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

}
