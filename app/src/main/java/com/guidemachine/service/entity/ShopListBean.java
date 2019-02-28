package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ShopListBean implements Serializable{

    /**
     * pageNum : 1
     * pageSize : 15
     * size : 1
     * startRow : 1
     * endRow : 1
     * total : 1
     * pages : 1
     * list : [{"shopId":"0da6f3fb859240c087928bd15cbd488e","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","name":"青城山门店","volume":70,"distance":55.85,"shopGoodsList":[{"goodsId":"a14640d2e5ee4f55a136897527ad8626","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","price":50},{"goodsId":"fae223041c904d3da3c59004543334f2","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","price":50}]}]
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
         * shopId : 0da6f3fb859240c087928bd15cbd488e
         * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
         * name : 青城山门店
         * volume : 70
         * distance : 55.85
         * shopGoodsList : [{"goodsId":"a14640d2e5ee4f55a136897527ad8626","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","price":50},{"goodsId":"fae223041c904d3da3c59004543334f2","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","price":50}]
         */

        private String shopId;
        private String imageUrl;
        private String name;
        private int volume;
        private double distance;
        private List<ShopGoodsListBean> shopGoodsList;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public List<ShopGoodsListBean> getShopGoodsList() {
            return shopGoodsList;
        }

        public void setShopGoodsList(List<ShopGoodsListBean> shopGoodsList) {
            this.shopGoodsList = shopGoodsList;
        }

        public static class ShopGoodsListBean {
            /**
             * goodsId : a14640d2e5ee4f55a136897527ad8626
             * goodsImageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
             * price : 50
             */

            private String goodsId;
            private String goodsImageUrl;
            private int price;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsImageUrl() {
                return goodsImageUrl;
            }

            public void setGoodsImageUrl(String goodsImageUrl) {
                this.goodsImageUrl = goodsImageUrl;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            @Override
            public String toString() {
                return "ShopGoodsListBean{" +
                        "goodsId='" + goodsId + '\'' +
                        ", goodsImageUrl='" + goodsImageUrl + '\'' +
                        ", price=" + price +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "shopId='" + shopId + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", name='" + name + '\'' +
                    ", volume=" + volume +
                    ", distance=" + distance +
                    ", shopGoodsList=" + shopGoodsList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopListBean{" +
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
