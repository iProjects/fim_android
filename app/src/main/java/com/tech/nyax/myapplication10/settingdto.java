package com.tech.nyax.myapplication10;

public class settingdto {
    private long setting_Id;
    private String setting_name;
    private String setting_value;
    private String setting_status;
    private String created_date;

    public settingdto(long settingid, String settingname, String settingvalue, String settingstatus, String createddate) {
        setting_Id = settingid;
        setting_name = settingname;
        setting_value = settingvalue;
        setting_status = settingstatus;
        created_date = createddate;
    }

    public settingdto() {
    } // you MUST have an empty constructor

    public long getsetting_Id() {
        return setting_Id;
    }

    public void setsetting_Id(long _setting_Id) {
        setting_Id = _setting_Id;
    }

    public String getsetting_name() {
        return setting_name;
    }

    public void setsetting_name(String _setting_name) {
        setting_name = _setting_name;
    }

    public String getsetting_value() {
        return setting_value;
    }

    public void setsetting_value(String _setting_value) {
        setting_value = _setting_value;
    }

    public String getsetting_status() {
        return setting_status;
    }

    public void setsetting_status(String _setting_status) {
        setting_status = _setting_status;
    }

    public String getcreated_date() {
        return created_date;
    }

    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}
