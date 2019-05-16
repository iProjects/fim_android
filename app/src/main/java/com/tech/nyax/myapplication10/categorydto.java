package com.tech.nyax.myapplication10;

public class categorydto {
    private long category_Id;
    private String category_name; 
    private String category_status;
    private String created_date;
 
    public categorydto() {} // you MUST have an empty constructor

    public long getcategory_Id() {
        return category_Id;
    }
    public void setcategory_Id(long _category_Id) {
        category_Id = _category_Id;
    }
    public String getcategory_name() {
        return category_name;
    }
    public void setcategory_name(String _category_name) {
        category_name = _category_name;
    } 
    public String getcategory_status() {
        return category_status;
    }
    public void setcategory_status(String _category_status) {
        category_status = _category_status;
    }
    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}