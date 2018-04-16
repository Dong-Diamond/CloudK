package com.cloudk.bean;

/**
 * Created by dong on 2018/2/5.
 */

public class LocationBean {

    /**
     * id : 01234567
     * proto : 10
     * type : 10
     * version : 11
     * org : 1002
     * longitude : 113.340182
     * latitude : 23.160860
     */

    private String id;
    private String proto;
    private String type;
    private String version;
    private String org;
    private String longitude;
    private String latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
