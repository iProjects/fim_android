package com.tech.nyax.myapplication10;

//create one directory model class
//to store directory title and type in list
public class DirectoryModel {
    String dirName;
    int dirType; // set 1 or 0, where 0 for directory and 1 for file.
    public int getDirType() {
        return dirType;
    }
    public void setDirType(int dirType) {
        this.dirType = dirType;
    }
    public String getDirName() {
        return dirName;
    }
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }
}
