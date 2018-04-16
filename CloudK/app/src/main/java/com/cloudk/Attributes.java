package com.cloudk;

/**
 * Created by dong on 2018/2/2.
 *
 * todo 这个类暂时没用到
 */

public class Attributes {
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
