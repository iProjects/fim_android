package com.tech.nyax.myapplication10;

public class cropdiseasedto {
    private long cropdisease_Id;
    private String cropdisease_name;
    private String cropdisease_category;
    private String cropdisease_status;
    private String created_date;
 
    public cropdiseasedto() {} // you MUST have an empty constructor

    public long getcropdisease_Id() {
        return cropdisease_Id;
    }
    public void setcropdisease_Id(long _cropdisease_Id) {
        cropdisease_Id = _cropdisease_Id;
    }
    public String getcropdisease_name() {
        return cropdisease_name;
    }
    public void setcropdisease_name(String _cropdisease_name) {
        cropdisease_name = _cropdisease_name;
    }
    public String getcropdisease_category() {
        return cropdisease_category;
    }
    public void setcropdisease_category(String _cropdisease_category) {
        cropdisease_category = _cropdisease_category;
    }
    public String getcropdisease_status() {
        return cropdisease_status;
    }
    public void setcropdisease_status(String _cropdisease_status) {
        cropdisease_status = _cropdisease_status;
    }
    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String _created_date) {
        created_date = _created_date;
    }

}