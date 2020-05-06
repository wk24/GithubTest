package com.pep.core.update.bean;


import java.io.Serializable;

/**
 * Created on 2017/4/26.
 */

public class UpdateBaseData implements Serializable {
    /**
     * statusCode : 500110
     * msg : request success
     * dataMap : {"inner_version":"1.7.0.2","count_across":1,"product_id":"11120101","product_name":"人教智慧教学平台","package_url":"file:///storage/emulated/0/rjsz/Test.apk","limit_count":-110,"version":"1.7.0.2","id":71,"file_list_json":"","client_type":"03","description":"1.教材添加模拟翻页功能。\n2.主页面优化。\n3.教材页面优化。\n4.教材下载界面优化。\n5.修改教材下架跟新。","is_coerce":"1","md5":"1","long_version":"100700000200000000","is_current":"0","is_full":"1"}
     */

    private String statusCode;
    private String msg;
    private DataMapBean dataMap;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataMapBean getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMapBean dataMap) {
        this.dataMap = dataMap;
    }

    public static class DataMapBean {
        /**
         * inner_version : 1.7.0.2
         * count_across : 1
         * product_id : 11120101
         * product_name : 人教智慧教学平台
         * package_url : file:///storage/emulated/0/rjsz/Test.apk
         * limit_count : -110
         * version : 1.7.0.2
         * id : 71
         * file_list_json :
         * client_type : 03
         * description : 1.教材添加模拟翻页功能。
         2.主页面优化。
         3.教材页面优化。
         4.教材下载界面优化。
         5.修改教材下架跟新。
         * is_coerce : 1
         * md5 : 1
         * long_version : 100700000200000000
         * is_current : 0
         * is_full : 1
         */

        private String inner_version;
        private int count_across;
        private String product_id;
        private String product_name;
        private String package_url;
        private int limit_count;
        private String version;
        private int id;
        private String client_type;
        private String description;
        private String is_coerce;
        private String long_version;
        private String is_current;
        private String is_full;

        public String getInner_version() {
            return inner_version;
        }

        public void setInner_version(String inner_version) {
            this.inner_version = inner_version;
        }

        public int getCount_across() {
            return count_across;
        }

        public void setCount_across(int count_across) {
            this.count_across = count_across;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getPackage_url() {
            return package_url;
        }

        public void setPackage_url(String package_url) {
            this.package_url = package_url;
        }

        public int getLimit_count() {
            return limit_count;
        }

        public void setLimit_count(int limit_count) {
            this.limit_count = limit_count;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getClient_type() {
            return client_type;
        }

        public void setClient_type(String client_type) {
            this.client_type = client_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIs_coerce() {
            return is_coerce;
        }

        public void setIs_coerce(String is_coerce) {
            this.is_coerce = is_coerce;
        }

        public String getLong_version() {
            return long_version;
        }

        public void setLong_version(String long_version) {
            this.long_version = long_version;
        }

        public String getIs_current() {
            return is_current;
        }

        public void setIs_current(String is_current) {
            this.is_current = is_current;
        }

        public String getIs_full() {
            return is_full;
        }

        public void setIs_full(String is_full) {
            this.is_full = is_full;
        }
    }

}
