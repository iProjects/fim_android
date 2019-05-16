package com.tech.nyax.myapplication10;

import java.util.List;

public class search_crop_dto {
	
    private long dto_Id;
    private String crop_name; 
	private String crop_variety_name;
	private String manufacturer_name;
    private List<search_cropvariety_dto> crop_varieties; 
 
    public search_crop_dto() {} // you MUST have an empty constructor

    public long getdto_Id() {
        return dto_Id;
    }
    public void setdto_Id(long _dto_Id) {
        dto_Id = _dto_Id;
    }
    public String getcrop_name() {
        return crop_name;
    }
    public void setcrop_name(String _crop_name) {
        crop_name = _crop_name;
    }  
	public String getcrop_variety_name() {
        return crop_variety_name;
    }
    public void setcrop_variety_name(String _crop_variety_name) {
        crop_variety_name = _crop_variety_name;
    }  
	public String getmanufacturer_name() {
        return manufacturer_name;
    }
    public void setmanufacturer_name(String _manufacturer_name) {
        manufacturer_name = _manufacturer_name;
    }  
    public List<search_cropvariety_dto> getcrop_varieties() {
        return crop_varieties;
    }
    public void setcrop_varieties(List<search_cropvariety_dto> _crop_varieties) {
        crop_varieties = _crop_varieties;
    }
    
}