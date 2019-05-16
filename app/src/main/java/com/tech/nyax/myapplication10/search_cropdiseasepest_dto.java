package com.tech.nyax.myapplication10;

public class search_cropdiseasepest_dto {
    private long dto_Id; 
	private String crop_diseasepest_name; 
    private String pestinsecticide_name;
    private String pestinsecticide_manufacturer;

    public search_cropdiseasepest_dto(long id,  String cropdiseasepestname, String pestinsecticidename, String pestinsecticidemanufacturer) {
        dto_Id = id; 
        crop_diseasepest_name = cropdiseasepestname; 
        pestinsecticide_name = pestinsecticidename;
        pestinsecticide_manufacturer = pestinsecticidemanufacturer;
    }

    public search_cropdiseasepest_dto() {} // you MUST have an empty constructor

    public long getdto_Id() {
        return dto_Id;
    }
    public void setdto_Id(long _dto_Id) {
        dto_Id = _dto_Id;
    }
    public String getcrop_diseasepest_name() {
        return crop_diseasepest_name;
    }
   public void setcrop_diseasepest_name(String _crop_diseasepest_name) {
        crop_diseasepest_name = _crop_diseasepest_name;
    } 
    public String getpestinsecticide_name() {
        return pestinsecticide_name;
    }
    public void setpestinsecticide_name(String _pestinsecticide_name) {
        pestinsecticide_name = _pestinsecticide_name;
    }
    public String getpestinsecticide_manufacturer() {
        return pestinsecticide_manufacturer;
    }
    public void setpestinsecticide_manufacturer(String _pestinsecticide_manufacturer) {
        pestinsecticide_manufacturer = _pestinsecticide_manufacturer;
    }

}