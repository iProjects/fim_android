package com.tech.nyax.myapplication10;

public class manufacturerdto {
    private long manufacturer_Id;
    private String manufacturer_name;
    private String manufacturer_status;
    private String created_date;
 
    public manufacturerdto() {
    } // you MUST have an empty constructor

    public long getmanufacturer_Id() {
        return manufacturer_Id;
    }

    public void setmanufacturer_Id(long _manufacturer_Id) {
        manufacturer_Id = _manufacturer_Id;
    }

    public String getmanufacturer_name() {
        return manufacturer_name;
    }

    public void setmanufacturer_name(String _manufacturer_name) {
        manufacturer_name = _manufacturer_name;
    }

    public String getmanufacturer_status() {
        return manufacturer_status;
    }

    public void setmanufacturer_status(String _manufacturer_status) {
        manufacturer_status = _manufacturer_status;
    }

    public String getcreated_date() {
        return created_date;
    }

    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}
