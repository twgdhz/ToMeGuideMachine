package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class AdminDeviceRentNumBean  implements Serializable{

    /**
     * totalRentNum : 0
     * sceneryRentNumPage : {"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":11,"pages":2,"list":[{"sceneryName":"青城山","rentNum":0},{"sceneryName":"武当山","rentNum":0},{"sceneryName":"峨眉山","rentNum":0},{"sceneryName":"故宫","rentNum":0},{"sceneryName":"泰山","rentNum":0},{"sceneryName":"华山","rentNum":0},{"sceneryName":"少林寺","rentNum":0},{"sceneryName":"都江堰","rentNum":0},{"sceneryName":"圆明园","rentNum":0},{"sceneryName":"可可托海矿山公园","rentNum":0}],"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2,"firstPage":1,"lastPage":2}
     */

    private int totalRentNum;
    private SceneryRentNumPageBean sceneryRentNumPage;

    public int getTotalRentNum() {
        return totalRentNum;
    }

    public void setTotalRentNum(int totalRentNum) {
        this.totalRentNum = totalRentNum;
    }

    public SceneryRentNumPageBean getSceneryRentNumPage() {
        return sceneryRentNumPage;
    }

    public void setSceneryRentNumPage(SceneryRentNumPageBean sceneryRentNumPage) {
        this.sceneryRentNumPage = sceneryRentNumPage;
    }

    public static class SceneryRentNumPageBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * total : 11
         * pages : 2
         * list : [{"sceneryName":"青城山","rentNum":0},{"sceneryName":"武当山","rentNum":0},{"sceneryName":"峨眉山","rentNum":0},{"sceneryName":"故宫","rentNum":0},{"sceneryName":"泰山","rentNum":0},{"sceneryName":"华山","rentNum":0},{"sceneryName":"少林寺","rentNum":0},{"sceneryName":"都江堰","rentNum":0},{"sceneryName":"圆明园","rentNum":0},{"sceneryName":"可可托海矿山公园","rentNum":0}]
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2]
         * navigateFirstPage : 1
         * navigateLastPage : 2
         * firstPage : 1
         * lastPage : 2
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
        private int firstPage;
        private int lastPage;
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

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
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
             * sceneryName : 青城山
             * rentNum : 0
             */

            private String sceneryName;
            private int rentNum;

            public String getSceneryName() {
                return sceneryName;
            }

            public void setSceneryName(String sceneryName) {
                this.sceneryName = sceneryName;
            }

            public int getRentNum() {
                return rentNum;
            }

            public void setRentNum(int rentNum) {
                this.rentNum = rentNum;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "sceneryName='" + sceneryName + '\'' +
                        ", rentNum=" + rentNum +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "SceneryRentNumPageBean{" +
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
                    ", firstPage=" + firstPage +
                    ", lastPage=" + lastPage +
                    ", list=" + list +
                    ", navigatepageNums=" + navigatepageNums +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdminDeviceRentNumBean{" +
                "totalRentNum=" + totalRentNum +
                ", sceneryRentNumPage=" + sceneryRentNumPage +
                '}';
    }
}
