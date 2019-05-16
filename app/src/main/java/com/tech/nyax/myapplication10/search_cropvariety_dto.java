package com.tech.nyax.myapplication10;

public class search_cropvariety_dto {
    private long dto_Id;
    private String crop_variety_name;
    private String crop_variety_manufacturer;
 
    public search_cropvariety_dto() {} // you MUST have an empty constructor

    public long getdto_Id() {
        return dto_Id;
    }
    public void setdto_Id(long _dto_Id) {
        dto_Id = _dto_Id;
    }
    public String getcrop_variety_name() {
        return crop_variety_name;
    }
    public void setcrop_variety_name(String _crop_variety_name) {
        crop_variety_name = _crop_variety_name;
    } 
    public String getcrop_variety_manufacturer() {
        return crop_variety_manufacturer;
    }
    public void setcrop_variety_manufacturer(String _crop_variety_manufacturer) {
        crop_variety_manufacturer = _crop_variety_manufacturer;
    }
    
}