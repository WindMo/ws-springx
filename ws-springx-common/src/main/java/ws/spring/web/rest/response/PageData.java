/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.rest.response;

import java.util.List;

/**
 * @author WindShadow
 * @version 2021-12-20.
 */

public class PageData<T> {

    private int pageNum;
    private int pageSize;
    private long total;
    private List<T> elements;

    public PageData() {
    }

    public PageData(int pageNum, int pageSize, long total, List<T> elements) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.elements = elements;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getElements() {
        return elements;
    }

    public PageData<T> setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageData<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageData<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public PageData<T> setElements(List<T> elements) {
        this.elements = elements;
        return this;
    }

    @Override
    public String toString() {
        return "PageData(pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ", total=" + this.getTotal() + ", elements=" + this.getElements() + ")";
    }

    public static <E> PageData<E> of(List<E> elements) {

        int size = elements.size();
        return of(1, size, size, elements);
    }

    public static <E> PageData<E> of(int pageNumber, int pageSize, long total, List<E> elements) {

        return new PageData<>(pageNumber, pageSize, total, elements);
    }
}
