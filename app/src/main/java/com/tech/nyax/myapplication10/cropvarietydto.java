package com.tech.nyax.myapplication10;

public class cropvarietydto {
    private long cropvariety_Id;
    private String cropvariety_name;
	private String cropvariety_crop_id;
	private String cropvariety_crop_name;
	private String cropvariety_manufacturer_id;
	private String cropvariety_manufacturer_name;
    private String cropvariety_status;
    private String created_date;
 
    public cropvarietydto() {} // you MUST have an empty constructor

    public long getcropvariety_Id() {
        return cropvariety_Id;
    }
    public void setcropvariety_Id(long _cropvariety_Id) {
        cropvariety_Id = _cropvariety_Id;
    }
    public String getcropvariety_name() {
        return cropvariety_name;
    }
    public void setcropvariety_name(String _cropvariety_name) {
        cropvariety_name = _cropvariety_name;
    }
	
	public String getcropvariety_crop_id() {
        return cropvariety_crop_id;
    }
    public void setcropvariety_crop_id(String _cropvariety_crop_id) {
        cropvariety_crop_id = _cropvariety_crop_id;
    }
	
	public String getcropvariety_crop_name() {
        return cropvariety_crop_name;
    }
    public void setcropvariety_crop_name(String _cropvariety_crop_name) {
        cropvariety_crop_name = _cropvariety_crop_name;
    }
	
	public String getcropvariety_manufacturer_id() {
        return cropvariety_manufacturer_id;
    }
    public void setcropvariety_manufacturer_id(String _cropvariety_manufacturer_id) {
        cropvariety_manufacturer_id = _cropvariety_manufacturer_id;
    }
	
	public String getcropvariety_manufacturer_name() {
        return cropvariety_manufacturer_name;
    }
    public void setcropvariety_manufacturer_name(String _cropvariety_manufacturer_name) {
        cropvariety_manufacturer_name = _cropvariety_manufacturer_name;
    }
	
    public String getcropvariety_status() {
        return cropvariety_status;
    }
    public void setcropvariety_status(String _cropvariety_status) {
        cropvariety_status = _cropvariety_status;
    }
    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}