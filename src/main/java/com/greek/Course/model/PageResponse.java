package com.greek.Course.model;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {
    private Integer totalPage;
    private Integer pageNum;
    private Integer pageSize;
    private List<T> data;

    public PageResponse() {
    }

    public PageResponse(Integer totalPage,
                        Integer currentPage,
                        Integer pageSize,
                        List<T> data) {
        this.totalPage = totalPage;
        this.pageNum = currentPage;
        this.pageSize = pageSize;
        this.data = data;
    }

    public static PageResponse of(Page page,
                                  Integer currentPage,
                                  Integer pageSize) {
        return new PageResponse(page.getTotalPages(), currentPage, pageSize, page.toList());
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public List<T> getData() {
        return data;
    }
}