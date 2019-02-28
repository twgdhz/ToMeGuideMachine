package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class SceneryServiceInfoBean implements Serializable{

    /**
     * pageNum : 10
     * pageSize : 1
     * size : 1
     * startRow : 10
     * endRow : 10
     * total : 11
     * pages : 11
     * list : [{"id":3,"name":"武当山","terminalTotalNum":5,"onLineTerminalTotalNum":0,"offLineTerminalTotalNum":5,"chargeName":"gong","chargeTelephone":"15282297723","address":"十堰市丹江口市太和街道永乐路13号"}]
     * prePage : 9
     * nextPage : 11
     * isFirstPage : false
     * isLastPage : false
     * hasPreviousPage : true
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [4,5,6,7,8,9,10,11]
     * navigateFirstPage : 4
     * navigateLastPage : 11
     * lastPage : 11
     * firstPage : 4
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int lastPage;
    private int firstPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * id : 3
         * name : 武当山
         * terminalTotalNum : 5
         * onLineTerminalTotalNum : 0
         * offLineTerminalTotalNum : 5
         * chargeName : gong
         * chargeTelephone : 15282297723
         * address : 十堰市丹江口市太和街道永乐路13号
         */

        private int id;
        private String name;
        private int terminalTotalNum;
        private int onLineTerminalTotalNum;
        private int offLineTerminalTotalNum;
        private String chargeName;
        private String chargeTelephone;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTerminalTotalNum() {
            return terminalTotalNum;
        }

        public void setTerminalTotalNum(int terminalTotalNum) {
            this.terminalTotalNum = terminalTotalNum;
        }

        public int getOnLineTerminalTotalNum() {
            return onLineTerminalTotalNum;
        }

        public void setOnLineTerminalTotalNum(int onLineTerminalTotalNum) {
            this.onLineTerminalTotalNum = onLineTerminalTotalNum;
        }

        public int getOffLineTerminalTotalNum() {
            return offLineTerminalTotalNum;
        }

        public void setOffLineTerminalTotalNum(int offLineTerminalTotalNum) {
            this.offLineTerminalTotalNum = offLineTerminalTotalNum;
        }

        public String getChargeName() {
            return chargeName;
        }

        public void setChargeName(String chargeName) {
            this.chargeName = chargeName;
        }

        public String getChargeTelephone() {
            return chargeTelephone;
        }

        public void setChargeTelephone(String chargeTelephone) {
            this.chargeTelephone = chargeTelephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", terminalTotalNum=" + terminalTotalNum +
                    ", onLineTerminalTotalNum=" + onLineTerminalTotalNum +
                    ", offLineTerminalTotalNum=" + offLineTerminalTotalNum +
                    ", chargeName='" + chargeName + '\'' +
                    ", chargeTelephone='" + chargeTelephone + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SceneryServiceInfoBean{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                ", prePage=" + prePage +
                ", nextPage=" + nextPage +
                ", isFirstPage=" + isFirstPage +
                ", isLastPage=" + isLastPage +
                ", hasPreviousPage=" + hasPreviousPage +
                ", hasNextPage=" + hasNextPage +
                ", navigatePages=" + navigatePages +
                ", navigateFirstPage=" + navigateFirstPage +
                ", navigateLastPage=" + navigateLastPage +
                ", lastPage=" + lastPage +
                ", firstPage=" + firstPage +
                ", list=" + list +
                ", navigatepageNums=" + navigatepageNums +
                '}';
    }
}
