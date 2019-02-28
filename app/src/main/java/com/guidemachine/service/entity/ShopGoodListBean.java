package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ShopGoodListBean implements Serializable {

    /**
     * pageNum : 1
     * pageSize : 1
     * size : 1
     * startRow : 0
     * endRow : 0
     * total : 1
     * pages : 1
     * list : [{"goodsId":"9782468b7ba747618a4c8c3efe0ab8d3","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","goodsName":"实体商品026","grade":5,"isGroup":0,"groupName":null,"evaluateNum":100,"volume":60,"distanceFromScenery":1102.24,"minPrice":50}]
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
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
    private String distanceFromTerminal;
    private String sceneryId;
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

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public String getDistanceFromTerminal() {
        return distanceFromTerminal;
    }

    public void setDistanceFromTerminal(String distanceFromTerminal) {
        this.distanceFromTerminal = distanceFromTerminal;
    }

    public String getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(String sceneryId) {
        this.sceneryId = sceneryId;
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
         * goodsId : 9782468b7ba747618a4c8c3efe0ab8d3
         * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
         * goodsName : 实体商品026
         * grade : 5
         * isGroup : 0
         * groupName : null
         * evaluateNum : 100
         * volume : 60
         * distanceFromScenery : 1102.24
         * minPrice : 50
         */

        private String goodsId;
        private String imageUrl;
        private String goodsName;
        private String sceneryId;
        private String sceneryName;
        private double distanceFromTerminal;
        private int grade;
        private int isGroup;
        private Object groupName;
        private int evaluateNum;
        private int volume;
        private double distanceFromScenery;
        private int minPrice;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getIsGroup() {
            return isGroup;
        }

        public void setIsGroup(int isGroup) {
            this.isGroup = isGroup;
        }

        public Object getGroupName() {
            return groupName;
        }

        public void setGroupName(Object groupName) {
            this.groupName = groupName;
        }

        public int getEvaluateNum() {
            return evaluateNum;
        }

        public void setEvaluateNum(int evaluateNum) {
            this.evaluateNum = evaluateNum;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public double getDistanceFromScenery() {
            return distanceFromScenery;
        }

        public void setDistanceFromScenery(double distanceFromScenery) {
            this.distanceFromScenery = distanceFromScenery;
        }

        public int getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(int minPrice) {
            this.minPrice = minPrice;
        }

        public String getSceneryId() {
            return sceneryId;
        }

        public void setSceneryId(String sceneryId) {
            this.sceneryId = sceneryId;
        }

        public double getDistanceFromTerminal() {
            return distanceFromTerminal;
        }

        public void setDistanceFromTerminal(double distanceFromTerminal) {
            this.distanceFromTerminal = distanceFromTerminal;
        }

        public String getSceneryName() {
            if (sceneryName==null||sceneryName.equals("")){
                return "青城山";
            }
            return sceneryName;
        }

        public void setSceneryName(String sceneryName) {
            this.sceneryName = sceneryName;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "goodsId='" + goodsId + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", sceneryId='" + sceneryId + '\'' +
                    ", sceneryName='" + sceneryName + '\'' +
                    ", distanceFromTerminal=" + distanceFromTerminal +
                    ", grade=" + grade +
                    ", isGroup=" + isGroup +
                    ", groupName=" + groupName +
                    ", evaluateNum=" + evaluateNum +
                    ", volume=" + volume +
                    ", distanceFromScenery=" + distanceFromScenery +
                    ", minPrice=" + minPrice +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopGoodListBean{" +
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
                ", distanceFromTerminal='" + distanceFromTerminal + '\'' +
                ", sceneryId='" + sceneryId + '\'' +
                ", list=" + list +
                ", navigatepageNums=" + navigatepageNums +
                '}';
    }
}
