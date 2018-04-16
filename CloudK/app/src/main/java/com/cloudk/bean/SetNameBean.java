package com.cloudk.bean;

/**
 * Created by dong on 2018/2/4.
 */

public class SetNameBean {

    /**
     * code : 0
     * result : {"id":"02234567","name":"小米手环","createDate":1517293282,"updateDate":1517293282}
     */

    private int code;
    private ResultBean result;
    /**
     * msg : 无效参数
     */

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultBean {
        /**
         * id : 02234567
         * name : 小米手环
         * createDate : 1517293282
         * updateDate : 1517293282
         */

        private String id;
        private String name;
        private int createDate;
        private int updateDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCreateDate() {
            return createDate;
        }

        public void setCreateDate(int createDate) {
            this.createDate = createDate;
        }

        public int getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(int updateDate) {
            this.updateDate = updateDate;
        }
    }
}
