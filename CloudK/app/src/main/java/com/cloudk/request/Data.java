package com.cloudk.request;

/**
 * Created by dong on 2018/1/28.
 */

public class Data {
    private int code;
    private String msg;
    private String result;
    private int id;
    private String userName;
    private int createData;
    private int updateData;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCreateData() {
        return createData;
    }

    public void setCreateData(int createData) {
        this.createData = createData;
    }

    public int getUpdateData() {
        return updateData;
    }

    public void setUpdateData(int updateData) {
        this.updateData = updateData;
    }
}
