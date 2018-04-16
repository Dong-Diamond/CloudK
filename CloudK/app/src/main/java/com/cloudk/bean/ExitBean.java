package com.cloudk.bean;

/**
 * Created by dong on 2018/2/23.
 */

public class ExitBean {
    private int code;
    private ResultBean result;
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
}
