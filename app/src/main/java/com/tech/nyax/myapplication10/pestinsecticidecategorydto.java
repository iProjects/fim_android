package com.tech.nyax.myapplication10;

public class pestinsecticidecategorydto {

    private Long dto_id;
    private String dto_key;
    private String dto_value;

    public pestinsecticidecategorydto(Long dtoid, String dtokey, String dtovalue) {
        dto_id = dtoid;
        dto_key = dtokey;
        dto_value = dtovalue;
    }

    public pestinsecticidecategorydto() {
    } // you MUST have an empty constructor

    public Long getdto_id() {
        return dto_id;
    }

    public void setdto_id(Long dtoid) {
        dto_id = dtoid;
    }

    public String getdto_key() {
        return dto_key;
    }

    public void setdto_key(String dtokey) {
        dto_key = dtokey;
    }

    public String getdto_value() {
        return dto_value;
    }

    public void setdto_value(String dtovalue) {
        dto_value = dtovalue;
    }

}