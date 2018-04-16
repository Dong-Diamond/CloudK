package com.cloudk.bean;

import java.util.List;

/**
 * Created by dong on 2018/2/4.
 */

public class RealTimeBean {
    /**
     * code : 0
     * result : [{"equipmentId":"01234567","attributeId":"3","attributeName":"x轴加速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"4","attributeName":"y轴加速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"5","attributeName":"z轴加速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"6","attributeName":"x轴角速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"7","attributeName":"y轴角速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"8","attributeName":"z轴角速度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"9","attributeName":"心率","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"1","attributeName":"经度","attributeValue":"0","timestamp":1517709135571},{"equipmentId":"01234567","attributeId":"2","attributeName":"纬度","attributeValue":"0","timestamp":1517709135571}]
     */

    private int code;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * equipmentId : 01234567
         * attributeId : 3
         * attributeName : x轴加速度
         * attributeValue : 0
         * timestamp : 1517709135571
         */

        private String equipmentId;
        private String attributeId;
        private String attributeName;
        private String attributeValue;
        private long timestamp;

        public String getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
