package com.tech.nyax.myapplication10;

public class pestinsecticidedto {
    private long pestinsecticide_Id;
	private String pestinsecticide_crop_disease_id;
	private String pestinsecticide_crop_disease_name;
    private String pestinsecticide_name;
    private String pestinsecticide_category;
	private String pestinsecticide_manufacturer_id;
	private String pestinsecticide_manufacturer_name;
    private String pestinsecticide_status;
    private String created_date;
 
    public pestinsecticidedto() {
    } // you MUST have an empty constructor

    public long getpestinsecticide_Id() {
        return pestinsecticide_Id;
    }

    public void setpestinsecticide_Id(long _pestinsecticide_Id) {
        pestinsecticide_Id = _pestinsecticide_Id;
    }

    public String getpestinsecticide_name() {
        return pestinsecticide_name;
    }

    public void setpestinsecticide_name(String _pestinsecticide_name) {
        pestinsecticide_name = _pestinsecticide_name;
    }

    public String getpestinsecticide_category() {
        return pestinsecticide_category;
    }

    public void setpestinsecticide_category(String _pestinsecticide_category) {
        pestinsecticide_category = _pestinsecticide_category;
    }

    public String getpestinsecticide_crop_disease_id() {
        return pestinsecticide_crop_disease_id;
    }

    public void setpestinsecticide_crop_disease_id(String _pestinsecticide_crop_disease_id) {
        pestinsecticide_crop_disease_id = _pestinsecticide_crop_disease_id;
    }

    public String getpestinsecticide_crop_disease_name() {
        return pestinsecticide_crop_disease_name;
    }

    public void setpestinsecticide_crop_disease_name(String _pestinsecticide_crop_disease_name) {
        pestinsecticide_crop_disease_name = _pestinsecticide_crop_disease_name;
    }

    public String getpestinsecticide_manufacturer_id() {
        return pestinsecticide_manufacturer_id;
    }

    public void setpestinsecticide_manufacturer_id(String _pestinsecticide_manufacturer_id) {
        pestinsecticide_manufacturer_id = _pestinsecticide_manufacturer_id;
    }

    public String getpestinsecticide_manufacturer_name() {
        return pestinsecticide_manufacturer_name;
    }

    public void setpestinsecticide_manufacturer_name(String _pestinsecticide_manufacturer_name) {
        pestinsecticide_manufacturer_name = _pestinsecticide_manufacturer_name;
    }

    public String getpestinsecticide_status() {
        return pestinsecticide_status;
    }

    public void setpestinsecticide_status(String _pestinsecticide_status) {
        pestinsecticide_status = _pestinsecticide_status;
    }

    public String getcreated_date() {
        return created_date;
    }

    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}
