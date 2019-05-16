package com.tech.nyax.myapplication10;

public class cropdto {
    private long crop_Id;
    private String crop_name; 
    private String crop_status;
    private String created_date;

    public cropdto(long id, String cropname, String cropstatus, String createddate) {
        crop_Id = id;
        crop_name = cropname; 
        crop_status = cropstatus;
        created_date = createddate;
    }

    public cropdto() {} // you MUST have an empty constructor

    public long getcrop_Id() {
        return crop_Id;
    }
    public void setcrop_Id(long _crop_Id) {
        crop_Id = _crop_Id;
    }
    public String getcrop_name() {
        return crop_name;
    }
    public void setcrop_name(String _crop_name) {
        crop_name = _crop_name;
    } 
    public String getcrop_status() {
        return crop_status;
    }
    public void setcrop_status(String _crop_status) {
        crop_status = _crop_status;
    }
    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}