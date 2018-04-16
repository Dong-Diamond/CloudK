package com.cloudk.request;

/**
 * Created by dong on 2018/1/29.
 */

public class MapData {
    private String id;
    private String proto;
    private String type;
    private String version;
    private String org;
    private String longitude;

    public String getNothings() {
        return nothings;
    }

    public void setNothings(String nothings) {
        this.nothings = nothings;
    }

    private String nothings;
    public MapData(String nothings)
    {
        this.nothings = nothings;
    }

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

    private String latitude;
}
