package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ScenerySpotListBean implements Serializable{

    /**
     * pageNum : 1
     * pageSize : 0
     * size : 4
     * startRow : 1
     * endRow : 4
     * total : 4
     * pages : 0
     * list : [{"scenerySpotId":11,"name":"青城山镇","introduction":"青城山的景点","address":"四川省成都市都江堰市青正街114号","imageUrl":null,"url":null,"lng":103.601324,"lat":30.905453},{"scenerySpotId":10,"name":"圆明园景点01","introduction":"圆明园的景点","address":"北京市海淀区清华西路28号","imageUrl":null,"url":null,"lng":116.309983,"lat":40.012884},{"scenerySpotId":3,"name":"test3","introduction":"景点描述","address":"景点地址","imageUrl":"www.image.com","url":"www.url.com","lng":111.111111,"lat":222.222222},{"scenerySpotId":2,"name":"翡翠谷","introduction":"黄山-翡翠谷","address":"黄山市黄山区汤口镇山岔村","imageUrl":null,"url":"www.url.com","lng":118.228695,"lat":30.117636}]
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : []
     * navigateFirstPage : 0
     * navigateLastPage : 0
     * firstPage : 0
     * lastPage : 0
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


    public static class ListBean implements Serializable{
        /**
         * scenerySpotId : 11
         * name : 青城山镇
         * introduction : 青城山的景点
         * address : 四川省成都市都江堰市青正街114号
         * imageUrl : null
         * url : null
         * lng : 103.601324
         * lat : 30.905453
         */

        private int scenerySpotId;
        private String name;
        private String introduction;
        private String address;
        private Object imageUrl;
        private Object url;
        private double lng;
        private double lat;

        public int getScenerySpotId() {
            return scenerySpotId;
        }

        public void setScenerySpotId(int scenerySpotId) {
            this.scenerySpotId = scenerySpotId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(Object imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "scenerySpotId=" + scenerySpotId +
                    ", name='" + name + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", address='" + address + '\'' +
                    ", imageUrl=" + imageUrl +
                    ", url=" + url +
                    ", lng=" + lng +
                    ", lat=" + lat +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ScenerySpotListBean{" +
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
                '}';
    }
}
