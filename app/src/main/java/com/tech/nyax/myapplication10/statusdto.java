package com.tech.nyax.myapplication10;

public class statusdto {

    private Long status_id;
    private String status_key;
    private String status_value;

    public statusdto(Long statusid, String statuskey, String statusvalue) {
        status_id = statusid;
        status_key = statuskey;
        status_value = statusvalue;
    }

    public statusdto() {
    } // you MUST have an empty constructor

    public Long getstatus_id() {
        return status_id;
    }

    public void setstatus_id(Long statusid) {
        status_id = statusid;
    }

    public String getstatus_key() {
        return status_key;
    }

    public void setstatus_key(String statuskey) {
        status_key = statuskey;
    }

    public String getstatus_value() {
        return status_value;
    }

    public void setstatus_value(String statusvalue) {
        status_value = statusvalue;
    }

}