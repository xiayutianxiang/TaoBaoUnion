package com.example.taobaounion.utils;

public class UrlUtils {

    public static String createHomePagerUrl(int materialId,int page){

        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pic_url) {
        return "https:" + pic_url;
    }

    public static String getCoverPath(String pic_url,int size) {
        return "https:" + pic_url+"_" + size + "x"+size+".jpg";
    }

    public static String getTicketUrl(String url) {
        if(url.startsWith("http") || url.startsWith("https")){
            return url;
        }else {
            return "https:" + url;
        }
    }

    public static String getSelectedPageContentUrl(int categoryId) {
        return "recommend/" + categoryId;
    }

    public static String getOnSellPageContentUrl(int page){
        return "onSell/" + page;
    }
}
