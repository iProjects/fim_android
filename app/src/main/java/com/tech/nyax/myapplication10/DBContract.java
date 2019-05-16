package com.tech.nyax.myapplication10;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

//Define the tables and columns of your local database
public final class DBContract {

    // To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.
    public DBContract() {
    }

    public static final String DATABASE_NAME = "ntharenedb.sqlite3";

	//crops
    public static final class cropsentitytable {
        public static final String CROPS_TABLE_NAME = "tblcrops";
        //Columns of the table
        public static final String CROP_ID = "crop_id";
        public static final String CROP_NAME = "crop_name";
        public static final String CROP_STATUS = "crop_status";
        public static final String CREATED_DATE = "created_date";

    }

	//crops varieties
    public static final class cropsvarietiesentitytable {
        public static final String CROPS_VARIETIES_TABLE_NAME = "tblcropsvarieties";
        //Columns of the table
        public static final String CROP_VARIETY_ID = "crop_variety_id";
        public static final String CROP_VARIETY_NAME = "crop_variety_name";
        public static final String CROP_VARIETY_STATUS = "crop_variety_status";
        public static final String CREATED_DATE = "created_date";
        public static final String CROP_VARIETY_CROP_ID = "crop_variety_crop_id";
        public static final String CROP_VARIETY_MANUFACTURER_ID = "crop_variety_manufacturer_id";

    }

	//diseases/pests
    public static final class cropsdiseasesentitytable {
        public static final String CROPS_DISEASES_TABLE_NAME = "tblcropsdiseases";
        //Columns of the table
        public static final String CROP_DISEASE_ID = "crop_disease_id";
        public static final String CROP_DISEASE_NAME = "crop_disease_name";
        public static final String CROP_DISEASE_CATEGORY = "crop_disease_category";
        public static final String CROP_DISEASE_STATUS = "crop_disease_status";
        public static final String CREATED_DATE = "created_date";

    }

	//manufacturers
    public static final class manufacturersentitytable {
        public static final String MANUFACTURERS_TABLE_NAME = "tblmanufacturers";
        //Columns of the table
        public static final String MANUFACTURER_ID = "manufacturer_id";
        public static final String MANUFACTURER_NAME = "manufacturer_name";
        public static final String MANUFACTURER_STATUS = "manufacturer_status";
        public static final String CREATED_DATE = "created_date";

    }

	//pesticides/insecticides
    public static final class pestsinsecticidesentitytable {
        public static final String PESTSINSECTICIDES_TABLE_NAME = "tblpestsinsecticides";
        //Columns of the table
        public static final String PESTINSECTICIDE_ID = "pestinsecticide_id";
        public static final String PESTINSECTICIDE_NAME = "pestinsecticide_name";
        public static final String PESTINSECTICIDE_CATEGORY = "pestinsecticide_category";
        public static final String PESTINSECTICIDE_STATUS = "pestinsecticide_status";
        public static final String CREATED_DATE = "created_date";
        public static final String PESTINSECTICIDE_CROP_DISEASE_ID = "pestinsecticide_crop_disease_id";
        public static final String PESTINSECTICIDE_MANUFACTURER_ID = "pestinsecticide_manufacturer_id";

    }

	//settings
    public static final class settingsentitytable {
        public static final String SETTINGS_TABLE_NAME = "tblsettings";
        //Columns of the table
        public static final String SETTING_ID = "setting_id";
        public static final String SETTING_NAME = "setting_name";
        public static final String SETTING_VALUE = "setting_value";
        public static final String SETTING_STATUS = "setting_status";
        public static final String CREATED_DATE = "created_date";

    }

	//categories
    public static final class categoriesentitytable {
        public static final String CATEGORIES_TABLE_NAME = "tblcategories";
        //Columns of the table
        public static final String CATEGORY_ID = "category_id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_STATUS = "category_status";
        public static final String CREATED_DATE = "created_date";

    }

    public static final class synchronize_server_api {
        public static final String get_all_crops = "androidapi/get_all_crops.php";
        public static final String get_all_cropsvarieties = "androidapi/get_all_cropsvarieties.php";
        public static final String get_all_diseasespests = "androidapi/get_all_diseasespests.php";
        public static final String get_all_manufacturers = "androidapi/get_all_manufacturers.php";
        public static final String get_all_pestsinsecticides = "androidapi/get_all_pestsinsecticides.php";
        public static final String get_all_settings = "androidapi/get_all_settings.php";
        public static final String get_all_categories = "androidapi/get_all_categories.php";

        public static final int CONNECTION_TIMEOUT = 60000;
        public static final int READ_TIMEOUT = 60000;

    }

    public static final class app_entities_wrapper {
        public static final String crops = "crops";
        public static final String cropsvarieties = "cropsvarieties";
        public static final String diseasespests = "diseasespests";
        public static final String manufacturers = "manufacturers";
        public static final String pestsinsecticides = "pestsinsecticides";
        public static final String settings = "settings";
        public static final String categories = "categories";

    }
	
    public static final String _dto_entity_type = "_dto_entity_type";

	  
	
	
	
	
	

}
















