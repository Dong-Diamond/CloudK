package com.cloudk.bean;

import java.util.List;

/**
 * Created by dong on 2018/2/2.
 */

public class EquiListBean {

    private int code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {


        private String id;
        private int createDate;
        private int updateDate;
        private AgreementBean agreement;
        private List<AttributesBean> attributes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public AgreementBean getAgreement() {
            return agreement;
        }

        public void setAgreement(AgreementBean agreement) {
            this.agreement = agreement;
        }

        public List<AttributesBean> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<AttributesBean> attributes) {
            this.attributes = attributes;
        }

        public static class AgreementBean {

            private String id;
            private String agreementNum;
            private String versionNum;
            private String packageType;
            private int createDate;
            private int updateDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAgreementNum() {
                return agreementNum;
            }

            public void setAgreementNum(String agreementNum) {
                this.agreementNum = agreementNum;
            }

            public String getVersionNum() {
                return versionNum;
            }

            public void setVersionNum(String versionNum) {
                this.versionNum = versionNum;
            }

            public String getPackageType() {
                return packageType;
            }

            public void setPackageType(String packageType) {
                this.packageType = packageType;
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

        public static class AttributesBean {

            private String id;
            private String agreementId;
            private String attributeName;
            private String attributeKey;
            private String attributeInterval;
            private int createDate;
            private int updateDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAgreementId() {
                return agreementId;
            }

            public void setAgreementId(String agreementId) {
                this.agreementId = agreementId;
            }

            public String getAttributeName() {
                return attributeName;
            }

            public void setAttributeName(String attributeName) {
                this.attributeName = attributeName;
            }

            public String getAttributeKey() {
                return attributeKey;
            }

            public void setAttributeKey(String attributeKey) {
                this.attributeKey = attributeKey;
            }

            public String getAttributeInterval() {
                return attributeInterval;
            }

            public void setAttributeInterval(String attributeInterval) {
                this.attributeInterval = attributeInterval;
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
