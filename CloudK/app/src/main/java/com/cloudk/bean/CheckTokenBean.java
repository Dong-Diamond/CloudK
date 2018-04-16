package com.cloudk.bean;

/**
 * Created by dong on 2018/1/31.
 */

public class CheckTokenBean {

    private int code = -1;
    private String msg = "";
    private ResultBean result = null;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        private String id;
        private String userName;
        private CompanyBean company;
        private int createDate;
        private int updateDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
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

        public static class CompanyBean {


            private String id = null;
            private String name = null;
            private int createDate = -1;
            private int updateDate = -1;

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
}
