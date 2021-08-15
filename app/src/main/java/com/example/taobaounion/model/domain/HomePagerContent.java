package com.example.taobaounion.model.domain;

import java.util.List;

public class HomePagerContent {

    /**
     * "success": true,
     * "code": 10000,
     * "message": "获取成功.",
     */
    private Boolean success;
    private Long code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "HomePagerContent{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements ILinearItemInfo{
        private Long category_id;
        private Object category_name;
        private String click_url;
        private String commission_rate;
        private Long coupon_amount;
        private String coupon_click_url;
        private String coupon_end_time;
        private Object coupon_info;
        private Long coupon_remain_count;
        private String coupon_share_url;
        private String coupon_start_fee;
        private String coupon_start_time;
        private Long coupon_total_count;
        private String item_description;
        private Long item_id;
        private Long level_one_category_id;
        private String level_one_category_name;
        private String nick;
        private String pict_url;
        private Long seller_id;
        private String shop_title;
        private SmallImagesDTO small_images;
        private String title;
        private Long user_type;
        private Long volume;
        private String zk_final_price;

        @Override
        public String toString() {
            return "DataBean{" +
                    "category_id=" + category_id +
                    ", category_name=" + category_name +
                    ", click_url='" + click_url + '\'' +
                    ", commission_rate='" + commission_rate + '\'' +
                    ", coupon_amount=" + coupon_amount +
                    ", coupon_click_url='" + coupon_click_url + '\'' +
                    ", coupon_end_time='" + coupon_end_time + '\'' +
                    ", coupon_info=" + coupon_info +
                    ", coupon_remain_count=" + coupon_remain_count +
                    ", coupon_share_url='" + coupon_share_url + '\'' +
                    ", coupon_start_fee='" + coupon_start_fee + '\'' +
                    ", coupon_start_time='" + coupon_start_time + '\'' +
                    ", coupon_total_count=" + coupon_total_count +
                    ", item_description='" + item_description + '\'' +
                    ", item_id=" + item_id +
                    ", level_one_category_id=" + level_one_category_id +
                    ", level_one_category_name='" + level_one_category_name + '\'' +
                    ", nick='" + nick + '\'' +
                    ", pict_url='" + pict_url + '\'' +
                    ", seller_id=" + seller_id +
                    ", shop_title='" + shop_title + '\'' +
                    ", small_images=" + small_images +
                    ", title='" + title + '\'' +
                    ", user_type=" + user_type +
                    ", volume=" + volume +
                    ", zk_final_price='" + zk_final_price + '\'' +
                    '}';
        }

        public Long getCategory_id() {
            return category_id;
        }

        public void setCategory_id(Long category_id) {
            this.category_id = category_id;
        }

        public Object getCategory_name() {
            return category_name;
        }

        public void setCategory_name(Object category_name) {
            this.category_name = category_name;
        }

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public Long getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(Long coupon_amount) {
            this.coupon_amount = coupon_amount;
        }

        public String getCoupon_click_url() {
            return coupon_click_url;
        }

        public void setCoupon_click_url(String coupon_click_url) {
            this.coupon_click_url = coupon_click_url;
        }

        public String getCoupon_end_time() {
            return coupon_end_time;
        }

        public void setCoupon_end_time(String coupon_end_time) {
            this.coupon_end_time = coupon_end_time;
        }

        public Object getCoupon_info() {
            return coupon_info;
        }

        public void setCoupon_info(Object coupon_info) {
            this.coupon_info = coupon_info;
        }

        public Long getCoupon_remain_count() {
            return coupon_remain_count;
        }

        public void setCoupon_remain_count(Long coupon_remain_count) {
            this.coupon_remain_count = coupon_remain_count;
        }

        public String getCoupon_share_url() {
            return coupon_share_url;
        }

        public void setCoupon_share_url(String coupon_share_url) {
            this.coupon_share_url = coupon_share_url;
        }

        public String getCoupon_start_fee() {
            return coupon_start_fee;
        }

        public void setCoupon_start_fee(String coupon_start_fee) {
            this.coupon_start_fee = coupon_start_fee;
        }

        public String getCoupon_start_time() {
            return coupon_start_time;
        }

        public void setCoupon_start_time(String coupon_start_time) {
            this.coupon_start_time = coupon_start_time;
        }

        public Long getCoupon_total_count() {
            return coupon_total_count;
        }

        public void setCoupon_total_count(Long coupon_total_count) {
            this.coupon_total_count = coupon_total_count;
        }

        public String getItem_description() {
            return item_description;
        }

        public void setItem_description(String item_description) {
            this.item_description = item_description;
        }

        public Long getItem_id() {
            return item_id;
        }

        public void setItem_id(Long item_id) {
            this.item_id = item_id;
        }

        public Long getLevel_one_category_id() {
            return level_one_category_id;
        }

        public void setLevel_one_category_id(Long level_one_category_id) {
            this.level_one_category_id = level_one_category_id;
        }

        public String getLevel_one_category_name() {
            return level_one_category_name;
        }

        public void setLevel_one_category_name(String level_one_category_name) {
            this.level_one_category_name = level_one_category_name;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPict_url() {
            return pict_url;
        }

        public void setPict_url(String pict_url) {
            this.pict_url = pict_url;
        }

        public Long getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(Long seller_id) {
            this.seller_id = seller_id;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public SmallImagesDTO getSmall_images() {
            return small_images;
        }

        public void setSmall_images(SmallImagesDTO small_images) {
            this.small_images = small_images;
        }

        @Override
        public String getCover() {
            return pict_url;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String getUrl() {
            return coupon_click_url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getUser_type() {
            return user_type;
        }

        public void setUser_type(Long user_type) {
            this.user_type = user_type;
        }


        @Override
        public String getFinalPrise() {
            return zk_final_price;
        }

        @Override
        public long getCouponAmount() {
            return coupon_amount;
        }

        public long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }

        public String getZk_final_price() {
            return zk_final_price;
        }

        public void setZk_final_price(String zk_final_price) {
            this.zk_final_price = zk_final_price;
        }

        public static class SmallImagesDTO {
            private List<String> string;

            public List<String> getString() {
                return string;
            }

            public void setString(List<String> string) {
                this.string = string;
            }
        }

    }
}
