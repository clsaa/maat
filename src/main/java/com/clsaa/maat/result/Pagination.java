package com.clsaa.maat.result;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 *
 * @author 任贵杰
 */
public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 实体对象列表
     */
    private List<T> pageList;
    /**
     * 每页记录数，如果pageSize<=0，则默认为20.
     */
    private int pageSize;
    /**
     * 当前页号，如果pageNo<=0，则默认为1
     */
    private int pageNo;
    /**
     * 总记录数
     */
    private int totalCount = 0;

    public Pagination() {
        super();
    }

    public Pagination(int pageNo, int pageSize) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    /**
     * 每页记录数，如果pageSize<=0，则默认为20.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 每页记录数，如果pageSize<=0，则默认为20.
     *
     * @param pageSize 页大小
     */
    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = 20;
        }
        this.pageSize = pageSize;
    }

    /**
     * 当前页号，如果pageNo<=0，则默认为1
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 当前页号，如果pageNo<=0，则默认为1
     *
     * @param pageNo 页号
     */
    public void setPageNo(int pageNo) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
    }

    /**
     * 总记录数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 总记录数
     *
     * @param totalCount 总记录数
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 返回总页数
     */
    public int getTotalPage() {
        if (getTotalCount() == 0) {
            return 1;
        } else {
            if (getTotalCount() % getPageSize() == 0) {
                return getTotalCount() / getPageSize();
            } else {
                return getTotalCount() / getPageSize() + 1;
            }
        }
    }

    /**
     * 分页起始行
     */
    public int getRowOffset() {
        // pageNo 小于0已判断，此处不用判断了
        return (this.pageNo - 1) * this.pageSize;
    }

    /**
     * 返回下一页页码
     *
     * @return 下一页页码
     */
    public int getNextPage() {
        if (isLastPage()) {
            return getTotalPage();
        } else {
            return getPageNo() + 1;
        }
    }

    /**
     * 返回上一页页码
     *
     * @return 下一页页码
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return 1;
        } else {
            return getPageNo() - 1;
        }
    }

    /**
     * 是否是最后一页
     */
    public boolean isLastPage() {
        if (getTotalPage() <= 0) {
            return true;
        } else {
            return getPageNo() >= getTotalPage();
        }
    }

    /**
     * 是否是第一页
     */
    public boolean isFirstPage() {
        return getPageNo() <= 1;
    }
}
