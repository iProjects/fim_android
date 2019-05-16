package com.tech.nyax.myapplication10;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//diseases/pests
import static com.tech.nyax.myapplication10.DBContract.cropsdiseasesentitytable.CROPS_DISEASES_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsdiseasesentitytable.CROP_DISEASE_CATEGORY;
import static com.tech.nyax.myapplication10.DBContract.cropsdiseasesentitytable.CROP_DISEASE_ID;
import static com.tech.nyax.myapplication10.DBContract.cropsdiseasesentitytable.CROP_DISEASE_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsdiseasesentitytable.CROP_DISEASE_STATUS;
//crops
import static com.tech.nyax.myapplication10.DBContract.cropsentitytable.CROPS_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsentitytable.CROP_ID;
import static com.tech.nyax.myapplication10.DBContract.cropsentitytable.CROP_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsentitytable.CROP_STATUS;
//manufacturers
import static com.tech.nyax.myapplication10.DBContract.manufacturersentitytable.MANUFACTURERS_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.manufacturersentitytable.MANUFACTURER_ID;
import static com.tech.nyax.myapplication10.DBContract.manufacturersentitytable.MANUFACTURER_NAME;
import static com.tech.nyax.myapplication10.DBContract.manufacturersentitytable.MANUFACTURER_STATUS;
//pesticides/insecticides
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_CATEGORY;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_ID;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_NAME;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_STATUS;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_CROP_DISEASE_ID;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_MANUFACTURER_ID;
import static com.tech.nyax.myapplication10.DBContract.pestsinsecticidesentitytable.PESTSINSECTICIDES_TABLE_NAME;
//settings
import static com.tech.nyax.myapplication10.DBContract.settingsentitytable.SETTINGS_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.settingsentitytable.SETTING_ID;
import static com.tech.nyax.myapplication10.DBContract.settingsentitytable.SETTING_NAME;
import static com.tech.nyax.myapplication10.DBContract.settingsentitytable.SETTING_STATUS;
import static com.tech.nyax.myapplication10.DBContract.settingsentitytable.SETTING_VALUE;
//crops varieties
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROPS_VARIETIES_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROP_VARIETY_ID;
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROP_VARIETY_NAME;
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROP_VARIETY_STATUS;
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROP_VARIETY_CROP_ID;
import static com.tech.nyax.myapplication10.DBContract.cropsvarietiesentitytable.CROP_VARIETY_MANUFACTURER_ID;
//categories
import static com.tech.nyax.myapplication10.DBContract.categoriesentitytable.CATEGORIES_TABLE_NAME;
import static com.tech.nyax.myapplication10.DBContract.categoriesentitytable.CATEGORY_ID;
import static com.tech.nyax.myapplication10.DBContract.categoriesentitytable.CATEGORY_NAME;
import static com.tech.nyax.myapplication10.DBContract.categoriesentitytable.CATEGORY_STATUS;

public class DatabasehelperUtilz extends SQLiteOpenHelper {

    public static final String TAG = DatabasehelperUtilz.class.getSimpleName();
    public static int flag;
    public static final String DB_NAME = "ntharenedb.sqlite3";
    //Version of the database. Changing the version will call onUpgrade
    //Database name
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ntharenedb.sqlite3";
    private String DB_PATH;
    private SQLiteDatabase db;
    private final Context context;
    String outFileName = "";
    public static final String dbfoldername = "/databases/";
    public static final String packagename = "com.tech.nyax.myapplication10";
    String _file_path_separator = "/";

    public DatabasehelperUtilz(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;

        try {

            File file_DB_PATH = getdefaultdbpathwithfilename();

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        } finally {
            openDataBase();
            createtables();
        }

    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.e(TAG, "is DataBase: Opened: " + db.isOpen());
    }

    public void createtables() {
        //Create the crops table
        final String SQL_CREATE_CROPS_TABLE = "CREATE TABLE IF NOT EXISTS " + CROPS_TABLE_NAME + " (" +
                CROP_ID + " INTEGER PRIMARY KEY, " +
                CROP_NAME + " TEXT, " +
                CROP_STATUS + " TEXT, " +
                DBContract.cropsentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_CROPS_TABLE);

        //Create the crops varieties table
        final String SQL_CREATE_CROPS_VARIETIES_TABLE = "CREATE TABLE IF NOT EXISTS " + CROPS_VARIETIES_TABLE_NAME + " (" +
                CROP_VARIETY_ID + " INTEGER PRIMARY KEY, " +
                CROP_VARIETY_NAME + " TEXT, " +
                CROP_VARIETY_STATUS + " TEXT, " +
                CROP_VARIETY_CROP_ID + " TEXT, " +
                CROP_VARIETY_MANUFACTURER_ID + " TEXT, " +
                DBContract.cropsvarietiesentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_CROPS_VARIETIES_TABLE);

        //Create the crops diseases/pests table
        final String SQL_CREATE_CROPS_DISEASES_TABLE = "CREATE TABLE IF NOT EXISTS " + CROPS_DISEASES_TABLE_NAME + " (" +
                CROP_DISEASE_ID + " INTEGER PRIMARY KEY, " +
                CROP_DISEASE_NAME + " TEXT, " +
                CROP_DISEASE_CATEGORY + " TEXT, " +
                CROP_DISEASE_STATUS + " TEXT, " +
                DBContract.cropsdiseasesentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_CROPS_DISEASES_TABLE);

        //Create the manufacturers table
        final String SQL_CREATE_MANUFACTURERS_TABLE = "CREATE TABLE IF NOT EXISTS " + MANUFACTURERS_TABLE_NAME + " (" +
                MANUFACTURER_ID + " INTEGER PRIMARY KEY, " +
                MANUFACTURER_NAME + " TEXT, " +
                MANUFACTURER_STATUS + " TEXT, " +
                DBContract.cropsdiseasesentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_MANUFACTURERS_TABLE);

        //Create the pesticides/insecticides table
        final String SQL_CREATE_PESTSINSECTICIDES_TABLE = "CREATE TABLE IF NOT EXISTS " + PESTSINSECTICIDES_TABLE_NAME + " (" +
                PESTINSECTICIDE_ID + " INTEGER PRIMARY KEY, " +
                PESTINSECTICIDE_NAME + " TEXT, " +
                PESTINSECTICIDE_CATEGORY + " TEXT, " +
                PESTINSECTICIDE_STATUS + " TEXT, " +
                PESTINSECTICIDE_CROP_DISEASE_ID + " TEXT, " +
                PESTINSECTICIDE_MANUFACTURER_ID + " TEXT, " +
                DBContract.pestsinsecticidesentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_PESTSINSECTICIDES_TABLE);

        //Create the settings table
        final String SQL_CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + SETTINGS_TABLE_NAME + " (" +
                SETTING_ID + " INTEGER PRIMARY KEY, " +
                SETTING_NAME + " TEXT, " +
                SETTING_VALUE + " TEXT, " +
                SETTING_STATUS + " TEXT, " +
                DBContract.settingsentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_SETTINGS_TABLE);

        //Create the diseasepestcategories table
        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + CATEGORIES_TABLE_NAME + " (" +
                CATEGORY_ID + " INTEGER PRIMARY KEY, " +
                CATEGORY_NAME + " TEXT, " +
                CATEGORY_STATUS + " TEXT, " +
                DBContract.categoriesentitytable.CREATED_DATE + " TEXT " +
                " ); ";
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);

    }

    boolean tableExists(String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    /**
     * Check if the underlying SQLiteDatabase is open
     *
     * @return whether the DB is open or not
     */
    public boolean isOpen() {
        return (db != null && db.isOpen());
    }

    public String getdefaultdbpath() {
        ContextWrapper cw = new ContextWrapper(context);
        String _defaultdbpath = context.getFilesDir().getParent() + dbfoldername;
        return _defaultdbpath;
    }

    public File getdefaultdbpathwithfilename() {
        ContextWrapper cw = new ContextWrapper(context);

        DB_PATH = context.getFilesDir().getParent() + dbfoldername;

        // Access your app's directory in the device's Public documents directory
        File publicdocsdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dbfoldername);

        String publicdocsdirpath = publicdocsdir.getPath();

        Log.e(TAG, "publicdocsdirpath [ " + publicdocsdirpath + " ]");

        // Access your app's Private documents directory
        File privatedocsdir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                dbfoldername);

        String privatedocsdirpath = privatedocsdir.getPath();

        Log.e(TAG, "privatedocsdirpath [ " + privatedocsdirpath + " ]");

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // Available to read and write
        }

        if (state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            // Available to at least read
        }

        Log.e(TAG, "DB_PATH [ " + DB_PATH + " ]");

        outFileName = DB_PATH + DB_NAME;

        Log.e(TAG, "outFileName [ " + outFileName + " ]");

        File file_DB_PATH = new File(DB_PATH);

        Log.e(TAG, "file exists? [ " + file_DB_PATH.exists() + " ]");

        if (!file_DB_PATH.exists()) {
            Log.e(TAG, "created path [ " + file_DB_PATH + " ]");
            file_DB_PATH.mkdirs();
        }

        final File _createNewFile = new File(outFileName);
        try {
            if (!_createNewFile.exists()) {
                Log.e(TAG, "created file [ " + outFileName + " ]");
                _createNewFile.createNewFile();

                MediaScannerConnection.scanFile(context, new String[]{_createNewFile.getPath()}, null, null);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(_createNewFile)));

            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        } finally {
            openDataBase();
            createtables();
        }
        return _createNewFile;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        try {

            getdefaultdbpathwithfilename();

            File sdcard = Environment.getExternalStorageDirectory();
            File dirdata = Environment.getDataDirectory();
            String sdpath = sdcard.getAbsolutePath();
            String datapath = dirdata.getAbsolutePath();

            String getFilesDir = context.getFilesDir().getAbsolutePath() + dbfoldername;
            String getParent = this.context.getFilesDir().getParent() + dbfoldername;

            File dbpath = context.getDatabasePath(DB_NAME);
            Log.e(TAG, "dbpath " + dbpath.toString());
            // if there was no folder at this path , it create it .

            try {
                if (!dbpath.exists()) {
                    dbpath.mkdirs();
                    dbpath.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            final File dbFile = new File(context.getFilesDir().getParent() + dbfoldername + DB_NAME);
            Log.e(TAG, "Databasehelper: dbFile " + dbFile.toString());
            // if there was no folder at this path , it create it .

            try {
                if (!dbFile.exists()) {
                    dbFile.mkdirs();
                    dbFile.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            String strdbPath = context.getApplicationContext().getDatabasePath(DB_NAME).getAbsolutePath();
            Log.e(TAG, "Databasehelper: strdbPath " + strdbPath);
/* 
			try {
            if (!strdbPath.exists()) {
            strdbPath.mkdirs();
			strdbPath.createNewFile();
        }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        } */

            File data = Environment.getDataDirectory();
            String newdbapppath = "/data/" + packagename + "/databases/sqlite/appdb.sqlite3"; // getting app db path
            File apppathdir = new File(newdbapppath);
            // if there was no folder at this path , it create it .

            try {
                if (!apppathdir.exists()) {
                    apppathdir.mkdirs();
                    apppathdir.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }


            if (Environment.isExternalStorageEmulated()) {
                File sd = Environment.getExternalStorageDirectory(); // getting phone SD card path
                String newdbPath = sd.getAbsolutePath() + dbfoldername; 
				/* if you want to set backup in specific folder name
be careful , foldername must initial like this : "/myFolder" . don't forget "/" at begin
of folder name
you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on ...
*/
                File dir = new File(newdbPath);
                // if there was no folder at this path , it create it .

                try {
                    if (!dir.exists()) {
                        dir.mkdirs();
                        dir.createNewFile();
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }

            }

            // Access your app's directory in the device's Public documents directory
            File Publicdoczdir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "ntharedocz/externalntharenedoc.db");
// Make the directory if it does not yet exist

            try {
                if (!Publicdoczdir.exists()) {
                    Publicdoczdir.mkdirs();
                    Publicdoczdir.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

// Access your app's Private documents directory
            File Privatedoczdir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "ntharedocz/externalntharenedoc.db");
// Make the directory if it does not yet exist

            try {
                if (!Privatedoczdir.exists()) {
                    Privatedoczdir.mkdirs();
                    Privatedoczdir.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

    }

    public String create_DataBase_given_name(String _db_name) throws IOException {

        StringBuilder _string_builder = new StringBuilder();
        try {

            _db_name = _db_name.concat(".sqlite3");
            // getdefaultdbpathwithfilename();

            // File sdcard = Environment.getExternalStorageDirectory();
            // File dirdata = Environment.getDataDirectory();
            // String sdpath = sdcard.getAbsolutePath();
            // String datapath = dirdata.getAbsolutePath();

            // String getFilesDir = context.getFilesDir().getAbsolutePath() + dbfoldername;
            // String getParent = this.context.getFilesDir().getParent() + dbfoldername;

            //default db path
            File _default_db_path = new File(getdefaultdbpath());

            Log.e(TAG, "default db path [ " + _default_db_path.toString() + " ] ");
            // if there was no folder at this path , it create it .

            try {
                boolean _create_dirs = _default_db_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _default_db_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _default_db_path_with_name = new File(getdefaultdbpath() + _db_name);

                boolean _create_file = _default_db_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _default_db_path_with_name.toString());
                    _string_builder.append("\n");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            //files/databases directory
            File _files_databases_path = new File(context.getFilesDir().getParent() + dbfoldername);

            Log.e(TAG, "_files_databases_path [ " + _files_databases_path.toString() + " ].");

            // if there was no folder at this path , it create it .

            try {
                boolean _create_dirs = _files_databases_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _files_databases_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _files_databases_path_with_name = new File(_files_databases_path + _db_name);

                boolean _create_file = _files_databases_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _files_databases_path_with_name.toString());
                    _string_builder.append("\n");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            //databases/sqlite directory
            File _databases_sqlite_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dbfoldername);

            // if there was no folder at this path , it create it .

            try {
                boolean _create_dirs = _databases_sqlite_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _databases_sqlite_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _databases_sqlite_path_with_name = new File(_databases_sqlite_path + _db_name);

                boolean _create_file = _databases_sqlite_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _databases_sqlite_path_with_name.toString());
                    _string_builder.append("\n");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            //getExternalStorageDirectory/dbfoldername
            if (Environment.isExternalStorageEmulated()) {
                File sd = Environment.getExternalStorageDirectory();
                // getting phone SD card path
                String _sd_db_Path = sd.getAbsolutePath() + dbfoldername;
				/* if you want to set backup in specific folder name
					be careful , foldername must initial like this : "/myFolder" . don't forget "/" at begin of folder name you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on ...*/
                File _external_storage_db_folder_path = new File(_sd_db_Path);
                // if there was no folder at this path , it create it .

                try {
                    boolean _create_dirs = _external_storage_db_folder_path.mkdirs();
                    if (_create_dirs) {
                        _string_builder.append("created path [ " + _external_storage_db_folder_path.getAbsolutePath());
                        _string_builder.append("\n");
                    }
                    File _external_storage_db_folder_path_with_name = new File(_external_storage_db_folder_path + _file_path_separator + _db_name);

                    boolean _create_file = _external_storage_db_folder_path_with_name.createNewFile();
                    if (_create_file) {
                        _string_builder.append("created file [ " + _external_storage_db_folder_path_with_name.toString());
                        _string_builder.append("\n");
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                    _string_builder.append("\n");
                    _string_builder.append(ex.getMessage());
                    _string_builder.append("\n");
                }

            }

            // Access your app's directory in the device's Public documents directory
            File _public_documents_path = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), dbfoldername);
            // Make the directory if it does not yet exist

            try {
                boolean _create_dirs = _public_documents_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _public_documents_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _public_documents_path_with_name = new File(_public_documents_path + _file_path_separator + _db_name);

                boolean _create_file = _public_documents_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _public_documents_path_with_name.toString());
                    _string_builder.append("\n");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            // Access your app's Private documents directory
            File _private_documents_path = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    dbfoldername);
            // Make the directory if it does not yet exist

            try {
                boolean _create_dirs = _private_documents_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _private_documents_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _private_documents_path_with_name = new File(_private_documents_path + _file_path_separator + _db_name);

                boolean _create_file = _private_documents_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _private_documents_path_with_name.toString());
                    _string_builder.append("\n");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            _string_builder.append("\n");
            _string_builder.append(ex.getMessage());
            _string_builder.append("\n");
        }

        return _string_builder.toString();
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the
     * application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String outFileName = DB_PATH + DB_NAME;
            Log.e(TAG, "outFileName: " + outFileName);

            checkDB = SQLiteDatabase.openDatabase(outFileName, null,
                    SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {
            try {
                copyDataBase();
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in
     * the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        try {
            Log.i("Database", "New database is being copied to device!");
            byte[] buffer = new byte[1024];
            OutputStream myOutput = null;
            int length;
            // Open your local db as the input stream
            InputStream myInput = null;

            myInput = context.getAssets().open(DB_NAME);
            // transfer bytes from the inputfile to the outputfile
            myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.e("Database", "New database has been copied to device!");
        } catch (IOException ex) {
            Log.e(TAG, ex.toString());
            throw ex;
        }
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createtables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CROPS_TABLE_NAME);
    }

    public boolean createcrop(cropdto _cropdto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CROP_NAME, _cropdto.getcrop_name());
        values.put(CROP_STATUS, _cropdto.getcrop_status());
        values.put(DBContract.cropsentitytable.CREATED_DATE, _cropdto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
        final long id = db.insert(
                CROPS_TABLE_NAME, // The table name in which the data will be inserted
                null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                values // The ContentValues instance which contains the data
        );
        return true;
    }

    public void createmanufacturer(manufacturerdto _manufacturerdto) {
        try {
            openDataBase();
            // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
            final ContentValues values = new ContentValues();
            values.put(MANUFACTURER_NAME, _manufacturerdto.getmanufacturer_name());
            values.put(MANUFACTURER_STATUS, _manufacturerdto.getmanufacturer_status());
            values.put(DBContract.manufacturersentitytable.CREATED_DATE, _manufacturerdto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
            final long id = db.insert(
                    MANUFACTURERS_TABLE_NAME, // The table name in which the data will be inserted
                    null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                    values // The ContentValues instance which contains the data
            );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void createsetting(settingdto _settingdto) {
        try {
            openDataBase();
            // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
            final ContentValues values = new ContentValues();
            values.put(SETTING_NAME, _settingdto.getsetting_name());
            values.put(SETTING_VALUE, _settingdto.getsetting_value());
            values.put(SETTING_STATUS, _settingdto.getsetting_status());
            values.put(DBContract.settingsentitytable.CREATED_DATE, _settingdto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
            final long id = db.insert(
                    SETTINGS_TABLE_NAME, // The table name in which the data will be inserted
                    null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                    values // The ContentValues instance which contains the data
            );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void createcropvariety(cropvarietydto _cropvarietydto) {
        try {
            openDataBase();
            // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
            final ContentValues values = new ContentValues();
            values.put(CROP_VARIETY_NAME, _cropvarietydto.getcropvariety_name());
            values.put(CROP_VARIETY_STATUS, _cropvarietydto.getcropvariety_status());
            values.put(CROP_VARIETY_CROP_ID, _cropvarietydto.getcropvariety_crop_id());
            values.put(CROP_VARIETY_MANUFACTURER_ID, _cropvarietydto.getcropvariety_manufacturer_id());
            values.put(DBContract.cropsvarietiesentitytable.CREATED_DATE, _cropvarietydto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
            final long id = db.insert(
                    CROPS_VARIETIES_TABLE_NAME, // The table name in which the data will be inserted
                    null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                    values // The ContentValues instance which contains the data
            );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void createpestinsecticide(pestinsecticidedto _pestinsecticidedto) {
        try {
            openDataBase();
            // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
            final ContentValues values = new ContentValues();
            values.put(PESTINSECTICIDE_NAME, _pestinsecticidedto.getpestinsecticide_name());
            values.put(PESTINSECTICIDE_CATEGORY, _pestinsecticidedto.getpestinsecticide_category());
            values.put(PESTINSECTICIDE_CROP_DISEASE_ID, _pestinsecticidedto.getpestinsecticide_crop_disease_id());
            values.put(PESTINSECTICIDE_MANUFACTURER_ID, _pestinsecticidedto.getpestinsecticide_manufacturer_id());
            values.put(PESTINSECTICIDE_STATUS, _pestinsecticidedto.getpestinsecticide_status());
            values.put(DBContract.pestsinsecticidesentitytable.CREATED_DATE, _pestinsecticidedto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
            final long id = db.insert(
                    PESTSINSECTICIDES_TABLE_NAME, // The table name in which the data will be inserted
                    null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                    values // The ContentValues instance which contains the data
            );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void createcropdisease(cropdiseasedto _cropdiseasedto) {
        try {
            openDataBase();
            // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
            final ContentValues values = new ContentValues();
            values.put(CROP_DISEASE_NAME, _cropdiseasedto.getcropdisease_name());
            values.put(CROP_DISEASE_CATEGORY, _cropdiseasedto.getcropdisease_category());
            values.put(CROP_DISEASE_STATUS, _cropdiseasedto.getcropdisease_status());
            values.put(DBContract.cropsdiseasesentitytable.CREATED_DATE, _cropdiseasedto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
            final long id = db.insert(
                    CROPS_DISEASES_TABLE_NAME, // The table name in which the data will be inserted
                    null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                    values // The ContentValues instance which contains the data
            );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public boolean createcategory(categorydto _categorydto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, _categorydto.getcategory_name());
        values.put(CATEGORY_STATUS, _categorydto.getcategory_status());
        values.put(DBContract.categoriesentitytable.CREATED_DATE, _categorydto.getcreated_date());
// This call performs the insert
// The return value is the rowId or primary key value for the new row!
// If this method returns -1 then the insert has failed.
        final long id = db.insert(
                CATEGORIES_TABLE_NAME, // The table name in which the data will be inserted
                null, // String: optional; may be null. If your provided values is empty,
// no column names are known and an empty row can't be inserted.
// If not set to null, this parameter provides the name
// of nullable column name to explicitly insert a NULL
                values // The ContentValues instance which contains the data
        );
        return true;
    }

    public boolean updatecrop(cropdto _cropdto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CROP_ID, _cropdto.getcrop_Id());
        values.put(CROP_NAME, _cropdto.getcrop_name());
        values.put(CROP_STATUS, _cropdto.getcrop_status());
        values.put(DBContract.cropsentitytable.CREATED_DATE, _cropdto.getcreated_date());

        db.update(
                CROPS_TABLE_NAME,
                values,
                "crop_id = ? ",
                new String[]{Long.toString(_cropdto.getcrop_Id())});

        return true;

    }

    public boolean updatecropdisease(cropdiseasedto _cropdiseasedto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CROP_DISEASE_ID, _cropdiseasedto.getcropdisease_Id());
        values.put(CROP_DISEASE_NAME, _cropdiseasedto.getcropdisease_name());
        values.put(CROP_DISEASE_CATEGORY, _cropdiseasedto.getcropdisease_category());
        values.put(CROP_DISEASE_STATUS, _cropdiseasedto.getcropdisease_status());
        values.put(DBContract.cropsdiseasesentitytable.CREATED_DATE, _cropdiseasedto.getcreated_date());

        db.update(
                CROPS_DISEASES_TABLE_NAME,
                values,
                "crop_disease_id = ? ",
                new String[]{Long.toString(_cropdiseasedto.getcropdisease_Id())});

        return true;
    }

    public boolean updatecropvariety(cropvarietydto _cropvarietydto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CROP_VARIETY_ID, _cropvarietydto.getcropvariety_Id());
        values.put(CROP_VARIETY_NAME, _cropvarietydto.getcropvariety_name());
        values.put(CROP_VARIETY_STATUS, _cropvarietydto.getcropvariety_status());
        values.put(CROP_VARIETY_CROP_ID, _cropvarietydto.getcropvariety_crop_id());
        values.put(CROP_VARIETY_MANUFACTURER_ID, _cropvarietydto.getcropvariety_manufacturer_id());
        values.put(DBContract.cropsvarietiesentitytable.CREATED_DATE, _cropvarietydto.getcreated_date());

        db.update(
                CROPS_VARIETIES_TABLE_NAME,
                values,
                "crop_variety_id = ? ",
                new String[]{Long.toString(_cropvarietydto.getcropvariety_Id())});

        return true;
    }

    public boolean updatemanufacturer(manufacturerdto _manufacturerdto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(MANUFACTURER_ID, _manufacturerdto.getmanufacturer_Id());
        values.put(MANUFACTURER_NAME, _manufacturerdto.getmanufacturer_name());
        values.put(MANUFACTURER_STATUS, _manufacturerdto.getmanufacturer_status());
        values.put(DBContract.manufacturersentitytable.CREATED_DATE, _manufacturerdto.getcreated_date());

        db.update(
                MANUFACTURERS_TABLE_NAME,
                values,
                "manufacturer_id = ? ",
                new String[]{Long.toString(_manufacturerdto.getmanufacturer_Id())});

        return true;
    }

    public boolean updatepestinsecticide(pestinsecticidedto _pestinsecticidedto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(PESTINSECTICIDE_ID, _pestinsecticidedto.getpestinsecticide_Id());
        values.put(PESTINSECTICIDE_NAME, _pestinsecticidedto.getpestinsecticide_name());
        values.put(PESTINSECTICIDE_CATEGORY, _pestinsecticidedto.getpestinsecticide_category());
        values.put(PESTINSECTICIDE_CROP_DISEASE_ID, _pestinsecticidedto.getpestinsecticide_crop_disease_id());
        values.put(PESTINSECTICIDE_MANUFACTURER_ID, _pestinsecticidedto.getpestinsecticide_manufacturer_id());
        values.put(PESTINSECTICIDE_STATUS, _pestinsecticidedto.getpestinsecticide_status());
        values.put(DBContract.pestsinsecticidesentitytable.CREATED_DATE, _pestinsecticidedto.getcreated_date());

        db.update(
                PESTSINSECTICIDES_TABLE_NAME,
                values,
                "pestinsecticide_id = ? ",
                new String[]{Long.toString(_pestinsecticidedto.getpestinsecticide_Id())});

        return true;
    }

    public boolean updatesetting(settingdto _settingdto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(SETTING_ID, _settingdto.getsetting_Id());
        values.put(SETTING_NAME, _settingdto.getsetting_name());
        values.put(SETTING_VALUE, _settingdto.getsetting_value());
        values.put(SETTING_STATUS, _settingdto.getsetting_status());
        values.put(DBContract.settingsentitytable.CREATED_DATE, _settingdto.getcreated_date());

        db.update(
                SETTINGS_TABLE_NAME,
                values,
                "setting_id = ? ",
                new String[]{Long.toString(_settingdto.getsetting_Id())});

        return true;
    }

    public boolean updatecategory(categorydto _categorydto) {
        openDataBase();
        // Create a ContentValues instance which contains the data for each column
// You do not need to specify a value for the PRIMARY KEY column.
// Unique values for these are automatically generated.
        final ContentValues values = new ContentValues();
        values.put(CATEGORY_ID, _categorydto.getcategory_Id());
        values.put(CATEGORY_NAME, _categorydto.getcategory_name());
        values.put(CATEGORY_STATUS, _categorydto.getcategory_status());
        values.put(DBContract.categoriesentitytable.CREATED_DATE, _categorydto.getcreated_date());

        db.update(
                CATEGORIES_TABLE_NAME,
                values,
                "category_id = ? ",
                new String[]{Long.toString(_categorydto.getcategory_Id())});

        return true;

    }

    public cropdto getcropgivenid(long crop_id) {
        openDataBase();

        List<cropdto> lstcropdtos = filtercropgivenid(String.valueOf(crop_id));

        return lstcropdtos.get(0);

        // select statement
//        final String SELECT_QUERY = "SELECT " +
//                CROP_ID + ", " +
//                CROP_NAME + ", " +
//                CROP_STATUS + ", " +
//                DBContract.cropsentitytable.CREATED_DATE +
//                " FROM " +
//                CROPS_TABLE_NAME +
//                " WHERE " +
//                CROP_ID +
//                " = " +
//                crop_id;
//
//        Cursor rs = db.rawQuery(SELECT_QUERY, null);
//
//        rs.moveToFirst();
//
//        cropdto _cropdto = new cropdto();
//        _cropdto.setcrop_Id(rs.getLong(rs.getColumnIndex(CROP_ID)));
//        _cropdto.setcrop_name(rs.getString(rs.getColumnIndex(CROP_NAME)));
//        _cropdto.setcrop_status(rs.getString(rs.getColumnIndex(CROP_STATUS)));
//        _cropdto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE)));
//
//        if (!rs.isClosed()) {
//            rs.close();
//        }
//
//        Log.e(TAG, SELECT_QUERY);
//
//        return _cropdto;
    }

    public cropdto getcropgivenname(String _crop_name) {
        openDataBase();

        List<cropdto> lstcropdtos = filtercropsgivenname(_crop_name);

        return lstcropdtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // CROP_ID + ", " +
        // CROP_NAME + ", " +
        // CROP_STATUS + ", " +
        // DBContract.cropsentitytable.CREATED_DATE +
        // " FROM " +
        // CROPS_TABLE_NAME +
        // " WHERE " +
        // CROP_NAME +
        // " = " +
        // _crop_name;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // cropdto _cropdto = new cropdto();
        // _cropdto.setcrop_Id(rs.getLong(rs.getColumnIndex(CROP_ID)));
        // _cropdto.setcrop_name(rs.getString(rs.getColumnIndex(CROP_NAME)));
        // _cropdto.setcrop_status(rs.getString(rs.getColumnIndex(CROP_STATUS)));
        // _cropdto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        //return lstcropdtos;
    }

    public cropdiseasedto getcropdiseasegivenid(long cropdisease_id) {
        openDataBase();

        List<cropdiseasedto> lstcropdiseasedtos = filtercropsdiseasesgivenid(String.valueOf(cropdisease_id));

        return lstcropdiseasedtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // CROP_DISEASE_ID + ", " +
        // CROP_DISEASE_NAME + ", " +
        // CROP_DISEASE_CATEGORY + ", " +
        // CROP_DISEASE_STATUS + ", " +
        // DBContract.cropsdiseasesentitytable.CREATED_DATE +
        // " FROM " +
        // CROPS_DISEASES_TABLE_NAME +
        // " WHERE " +
        // CROP_DISEASE_ID +
        // " = " +
        // cropdisease_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // cropdiseasedto _cropdiseasedto = new cropdiseasedto();
        // _cropdiseasedto.setcropdisease_Id(rs.getLong(rs.getColumnIndex(CROP_DISEASE_ID)));
        // _cropdiseasedto.setcropdisease_name(rs.getString(rs.getColumnIndex(CROP_DISEASE_NAME)));
        // _cropdiseasedto.setcropdisease_category(rs.getString(rs.getColumnIndex(CROP_DISEASE_CATEGORY)));
        // _cropdiseasedto.setcropdisease_status(rs.getString(rs.getColumnIndex(CROP_DISEASE_STATUS)));
        // _cropdiseasedto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.cropsdiseasesentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _cropdiseasedto;
    }

    public cropvarietydto getcropvarietygivenid(long cropvariety_id) {
        openDataBase();

        List<cropvarietydto> lstcropvarietydtos = filtercropsvarietiesgivenid(String.valueOf(cropvariety_id));

        return lstcropvarietydtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // CROP_VARIETY_ID + ", " +
        // CROP_VARIETY_NAME + ", " +
        // CROP_VARIETY_CROP_ID + ", " +
        // CROP_VARIETY_MANUFACTURER_ID + ", " +
        // CROP_VARIETY_STATUS + ", " +
        // DBContract.cropsvarietiesentitytable.CREATED_DATE +
        // " FROM " +
        // CROPS_VARIETIES_TABLE_NAME +
        // " WHERE " +
        // CROP_VARIETY_ID +
        // " = " +
        // cropvariety_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // cropvarietydto _cropvarietydto = new cropvarietydto();
        // _cropvarietydto.setcropvariety_Id(rs.getLong(rs.getColumnIndex(CROP_VARIETY_ID)));
        // _cropvarietydto.setcropvariety_name(rs.getString(rs.getColumnIndex(CROP_VARIETY_NAME)));
        // _cropvarietydto.setcropvariety_crop_id(rs.getString(rs.getColumnIndex(CROP_VARIETY_CROP_ID)));
        // _cropvarietydto.setcropvariety_manufacturer_id(rs.getString(rs.getColumnIndex(CROP_VARIETY_MANUFACTURER_ID)));
        // _cropvarietydto.setcropvariety_status(rs.getString(rs.getColumnIndex(CROP_VARIETY_STATUS)));
        // _cropvarietydto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.cropsvarietiesentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _cropvarietydto;
    }

    public manufacturerdto getmanufacturergivenid(long manufacturer_id) {
        openDataBase();

        List<manufacturerdto> lstmanufacturerdtos = filtermanufacturergivenid(String.valueOf(manufacturer_id));

        return lstmanufacturerdtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // MANUFACTURER_ID + ", " +
        // MANUFACTURER_NAME + ", " +
        // MANUFACTURER_STATUS + ", " +
        // DBContract.manufacturersentitytable.CREATED_DATE +
        // " FROM " +
        // MANUFACTURERS_TABLE_NAME +
        // " WHERE " +
        // MANUFACTURER_ID +
        // " = " +
        // manufacturer_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // manufacturerdto _manufacturerdto = new manufacturerdto();
        // _manufacturerdto.setmanufacturer_Id(rs.getLong(rs.getColumnIndex(MANUFACTURER_ID)));
        // _manufacturerdto.setmanufacturer_name(rs.getString(rs.getColumnIndex(MANUFACTURER_NAME)));
        // _manufacturerdto.setmanufacturer_status(rs.getString(rs.getColumnIndex(MANUFACTURER_STATUS)));
        // _manufacturerdto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.manufacturersentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _manufacturerdto;
    }

    public pestinsecticidedto getpestinsecticidegivenid(long pestinsecticide_id) {
        openDataBase();

        List<pestinsecticidedto> lstpestinsecticidedtos = filterpestsinsecticidesgivenid(String.valueOf(pestinsecticide_id));

        return lstpestinsecticidedtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // PESTINSECTICIDE_ID + ", " +
        // PESTINSECTICIDE_NAME + ", " +
        // PESTINSECTICIDE_CATEGORY + ", " +
        // PESTINSECTICIDE_CROP_DISEASE_ID + ", " +
        // PESTINSECTICIDE_MANUFACTURER_ID + ", " +
        // PESTINSECTICIDE_STATUS + ", " +
        // DBContract.pestsinsecticidesentitytable.CREATED_DATE +
        // " FROM " +
        // PESTSINSECTICIDES_TABLE_NAME +
        // " WHERE " +
        // PESTINSECTICIDE_ID +
        // " = " +
        // pestinsecticide_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();
        // _pestinsecticidedto.setpestinsecticide_Id(rs.getLong(rs.getColumnIndex(PESTINSECTICIDE_ID)));
        // _pestinsecticidedto.setpestinsecticide_name(rs.getString(rs.getColumnIndex(PESTINSECTICIDE_NAME)));
        // _pestinsecticidedto.setpestinsecticide_category(rs.getString(rs.getColumnIndex(PESTINSECTICIDE_CATEGORY)));
        // _pestinsecticidedto.setpestinsecticide_crop_disease_id(rs.getString(rs.getColumnIndex(PESTINSECTICIDE_CROP_DISEASE_ID)));
        // _pestinsecticidedto.setpestinsecticide_manufacturer_id(rs.getString(rs.getColumnIndex(PESTINSECTICIDE_MANUFACTURER_ID)));
        // _pestinsecticidedto.setpestinsecticide_status(rs.getString(rs.getColumnIndex(PESTINSECTICIDE_STATUS)));
        // _pestinsecticidedto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.pestsinsecticidesentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _pestinsecticidedto;
    }

    public settingdto getsettinggivenid(long setting_id) {
        openDataBase();

        List<settingdto> lstsettingdtos = filtersettingsgivenid(String.valueOf(setting_id));

        return lstsettingdtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // SETTING_ID + ", " +
        // SETTING_NAME + ", " +
        // SETTING_VALUE + ", " +
        // SETTING_STATUS + ", " +
        // DBContract.settingsentitytable.CREATED_DATE +
        // " FROM " +
        // SETTINGS_TABLE_NAME +
        // " WHERE " +
        // SETTING_ID +
        // " = " +
        // setting_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // settingdto _settingdto = new settingdto();
        // _settingdto.setsetting_Id(rs.getLong(rs.getColumnIndex(SETTING_ID)));
        // _settingdto.setsetting_name(rs.getString(rs.getColumnIndex(SETTING_NAME)));
        // _settingdto.setsetting_value(rs.getString(rs.getColumnIndex(SETTING_VALUE)));
        // _settingdto.setsetting_status(rs.getString(rs.getColumnIndex(SETTING_STATUS)));
        // _settingdto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.settingsentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _settingdto;
    }

    public categorydto getcategorygivenid(long category_id) {
        openDataBase();

        List<categorydto> lstcategorydtos = filtercategoriesgivenid(String.valueOf(category_id));

        return lstcategorydtos.get(0);

        // select statement
        // final String SELECT_QUERY = "SELECT " +
        // CATEGORY_ID + ", " +
        // CATEGORY_NAME + ", " +
        // CATEGORY_STATUS + ", " +
        // DBContract.categoriesentitytable.CREATED_DATE +
        // " FROM " +
        // CATEGORIES_TABLE_NAME +
        // " WHERE " +
        // CATEGORY_ID +
        // " = " +
        // category_id;

        // Cursor rs = db.rawQuery(SELECT_QUERY, null);

        // rs.moveToFirst();

        // categorydto _categorydto = new categorydto();
        // _categorydto.setcategory_Id(rs.getLong(rs.getColumnIndex(CATEGORY_ID)));
        // _categorydto.setcategory_name(rs.getString(rs.getColumnIndex(CATEGORY_NAME)));
        // _categorydto.setcategory_status(rs.getString(rs.getColumnIndex(CATEGORY_STATUS)));
        // _categorydto.setcreated_date(rs.getString(rs.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE)));

        // if (!rs.isClosed()) {
        // rs.close();
        // }

        // Log.e(TAG, SELECT_QUERY);

        // return _categorydto;
    }

    public List<categorydto> getcategorygivenname(String _category_name) {
        openDataBase();
        // When reading data one should always just get a readable database.
        //	final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CATEGORIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_STATUS, DBContract.categoriesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CATEGORY_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + _category_name + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int category_idIndex = cursor.getColumnIndex(CATEGORY_ID);
        final int category_nameIndex = cursor.getColumnIndex(CATEGORY_NAME);
        final int category_statusIndex = cursor.getColumnIndex(CATEGORY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.categoriesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<categorydto> categorydtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                categorydto _categorydto = new categorydto();
                _categorydto.setcategory_Id(cursor.getLong(category_idIndex));
                _categorydto.setcategory_name(cursor.getString(category_nameIndex));
                _categorydto.setcategory_status(cursor.getString(category_statusIndex));
                _categorydto.setcreated_date(cursor.getString(created_dateIndex));
                categorydtos.add(_categorydto);

            } while (cursor.moveToNext());

            return categorydtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }

    }

    public Integer deletecrop(String crop_id) {
        openDataBase();
        return db.delete(
                CROPS_TABLE_NAME,
                "crop_id = ? ",
                new String[]{crop_id});
    }

    public Integer deletecropdisease(String crop_disease_id) {
        openDataBase();
        return db.delete(
                CROPS_DISEASES_TABLE_NAME,
                "crop_disease_id = ? ",
                new String[]{crop_disease_id});
    }

    public Integer deletecropvariety(String crop_variety_id) {
        openDataBase();
        return db.delete(
                CROPS_VARIETIES_TABLE_NAME,
                "crop_disease_id = ? ",
                new String[]{crop_variety_id});
    }

    public Integer deletemanufacturer(String manufacturer_id) {
        openDataBase();
        return db.delete(
                MANUFACTURERS_TABLE_NAME,
                "manufacturer_id = ? ",
                new String[]{manufacturer_id});
    }

    public Integer deletepestinsecticide(String pestinsecticide_id) {
        openDataBase();
        return db.delete(
                PESTSINSECTICIDES_TABLE_NAME,
                "pestinsecticide_id = ? ",
                new String[]{pestinsecticide_id});
    }

    public Integer deletesetting(String setting_id) {
        openDataBase();
        return db.delete(
                SETTINGS_TABLE_NAME,
                "setting_id = ? ",
                new String[]{setting_id});
    }

    public Integer deletecategory(String category_id) {
        return db.delete(
                CATEGORIES_TABLE_NAME,
                "category_id = ? ",
                new String[]{category_id});
    }

    public List<cropdto> getallcrops() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_ID, CROP_NAME, CROP_STATUS, DBContract.cropsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int crop_idIndex = cursor.getColumnIndex(CROP_ID);
        final int crop_nameIndex = cursor.getColumnIndex(CROP_NAME);
        final int crop_statusIndex = cursor.getColumnIndex(CROP_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdto> cropdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdto _cropdto = new cropdto();
                _cropdto.setcrop_Id(cursor.getLong(crop_idIndex));
                _cropdto.setcrop_name(cursor.getString(crop_nameIndex));
                _cropdto.setcrop_status(cursor.getString(crop_statusIndex));
                _cropdto.setcreated_date(cursor.getString(created_dateIndex));
                cropdtos.add(_cropdto);

            } while (cursor.moveToNext());

            return cropdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<cropdiseasedto> getallcropsdiseases() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_DISEASES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_DISEASE_ID, CROP_DISEASE_NAME, CROP_DISEASE_CATEGORY, CROP_DISEASE_STATUS, DBContract.cropsdiseasesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int cropdisease_idIndex = cursor.getColumnIndex(CROP_DISEASE_ID);
        final int cropdisease_nameIndex = cursor.getColumnIndex(CROP_DISEASE_NAME);
        final int cropdisease_categoryIndex = cursor.getColumnIndex(CROP_DISEASE_CATEGORY);
        final int cropdisease_statusIndex = cursor.getColumnIndex(CROP_DISEASE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsdiseasesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdiseasedto> cropdiseasedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdiseasedto _cropdiseasedto = new cropdiseasedto();
                _cropdiseasedto.setcropdisease_Id(cursor.getLong(cropdisease_idIndex));
                _cropdiseasedto.setcropdisease_name(cursor.getString(cropdisease_nameIndex));
                _cropdiseasedto.setcropdisease_category(cursor.getString(cropdisease_categoryIndex));
                _cropdiseasedto.setcropdisease_status(cursor.getString(cropdisease_statusIndex));
                _cropdiseasedto.setcreated_date(cursor.getString(created_dateIndex));
                cropdiseasedtos.add(_cropdiseasedto);

            } while (cursor.moveToNext());

            return cropdiseasedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<cropvarietydto> getallcropsvarieties() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_VARIETIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_VARIETY_ID, CROP_VARIETY_NAME, CROP_VARIETY_CROP_ID, CROP_VARIETY_MANUFACTURER_ID, CROP_VARIETY_STATUS, DBContract.cropsvarietiesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int cropvariety_idIndex = cursor.getColumnIndex(CROP_VARIETY_ID);
        final int cropvariety_nameIndex = cursor.getColumnIndex(CROP_VARIETY_NAME);
        final int cropvariety_crop_idIndex = cursor.getColumnIndex(CROP_VARIETY_CROP_ID);
        final int cropvariety_manufacturer_idIndex = cursor.getColumnIndex(CROP_VARIETY_MANUFACTURER_ID);
        final int cropvariety_statusIndex = cursor.getColumnIndex(CROP_VARIETY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsvarietiesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropvarietydto> cropsvarietiesdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropvarietydto _cropvarietydto = new cropvarietydto();
                _cropvarietydto.setcropvariety_Id(cursor.getLong(cropvariety_idIndex));
                _cropvarietydto.setcropvariety_name(cursor.getString(cropvariety_nameIndex));
                _cropvarietydto.setcropvariety_crop_id(cursor.getString(cropvariety_crop_idIndex));
                _cropvarietydto.setcropvariety_manufacturer_id(cursor.getString(cropvariety_manufacturer_idIndex));
                _cropvarietydto.setcropvariety_status(cursor.getString(cropvariety_statusIndex));
                _cropvarietydto.setcreated_date(cursor.getString(created_dateIndex));
                cropsvarietiesdtos.add(_cropvarietydto);

            } while (cursor.moveToNext());

            return cropsvarietiesdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<manufacturerdto> getallmanufacturers() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                MANUFACTURERS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{MANUFACTURER_ID, MANUFACTURER_NAME, MANUFACTURER_STATUS, DBContract.manufacturersentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int manufacturer_idIndex = cursor.getColumnIndex(MANUFACTURER_ID);
        final int manufacturer_nameIndex = cursor.getColumnIndex(MANUFACTURER_NAME);
        final int manufacturer_statusIndex = cursor.getColumnIndex(MANUFACTURER_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.manufacturersentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<manufacturerdto> manufacturerdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                manufacturerdto _manufacturerdto = new manufacturerdto();
                _manufacturerdto.setmanufacturer_Id(cursor.getLong(manufacturer_idIndex));
                _manufacturerdto.setmanufacturer_name(cursor.getString(manufacturer_nameIndex));
                _manufacturerdto.setmanufacturer_status(cursor.getString(manufacturer_statusIndex));
                _manufacturerdto.setcreated_date(cursor.getString(created_dateIndex));
                manufacturerdtos.add(_manufacturerdto);

            } while (cursor.moveToNext());

            return manufacturerdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<pestinsecticidedto> getallpestsinsecticides() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                PESTSINSECTICIDES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{PESTINSECTICIDE_ID, PESTINSECTICIDE_NAME, PESTINSECTICIDE_CROP_DISEASE_ID, PESTINSECTICIDE_MANUFACTURER_ID, PESTINSECTICIDE_CATEGORY, PESTINSECTICIDE_STATUS, DBContract.pestsinsecticidesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int pestinsecticide_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_ID);
        final int pestinsecticide_nameIndex = cursor.getColumnIndex(PESTINSECTICIDE_NAME);
        final int pestinsecticide_crop_disease_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_CROP_DISEASE_ID);
        final int pestinsecticide_manufacturer_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_MANUFACTURER_ID);
        final int pestinsecticide_categoryIndex = cursor.getColumnIndex(PESTINSECTICIDE_CATEGORY);
        final int pestinsecticide_statusIndex = cursor.getColumnIndex(PESTINSECTICIDE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.pestsinsecticidesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<pestinsecticidedto> pestinsecticidedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();
                _pestinsecticidedto.setpestinsecticide_Id(cursor.getLong(pestinsecticide_idIndex));
                _pestinsecticidedto.setpestinsecticide_name(cursor.getString(pestinsecticide_nameIndex));
                _pestinsecticidedto.setpestinsecticide_crop_disease_id(cursor.getString(pestinsecticide_crop_disease_idIndex));
                _pestinsecticidedto.setpestinsecticide_manufacturer_id(cursor.getString(pestinsecticide_manufacturer_idIndex));
                _pestinsecticidedto.setpestinsecticide_category(cursor.getString(pestinsecticide_categoryIndex));
                _pestinsecticidedto.setpestinsecticide_status(cursor.getString(pestinsecticide_statusIndex));
                _pestinsecticidedto.setcreated_date(cursor.getString(created_dateIndex));
                pestinsecticidedtos.add(_pestinsecticidedto);

            } while (cursor.moveToNext());

            return pestinsecticidedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<settingdto> getallsettings() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                SETTINGS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{SETTING_ID, SETTING_NAME, SETTING_VALUE, SETTING_STATUS, DBContract.settingsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int setting_idIndex = cursor.getColumnIndex(SETTING_ID);
        final int setting_nameIndex = cursor.getColumnIndex(SETTING_NAME);
        final int setting_statusIndex = cursor.getColumnIndex(SETTING_STATUS);
        final int setting_valueIndex = cursor.getColumnIndex(SETTING_VALUE);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.settingsentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<settingdto> settingdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                settingdto _settingdto = new settingdto();
                _settingdto.setsetting_Id(cursor.getLong(setting_idIndex));
                _settingdto.setsetting_name(cursor.getString(setting_nameIndex));
                _settingdto.setsetting_status(cursor.getString(setting_statusIndex));
                _settingdto.setsetting_value(cursor.getString(setting_valueIndex));
                _settingdto.setcreated_date(cursor.getString(created_dateIndex));
                settingdtos.add(_settingdto);

            } while (cursor.moveToNext());

            return settingdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<categorydto> getallcategories() {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CATEGORIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_STATUS, DBContract.categoriesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                null,
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                null,
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int category_idIndex = cursor.getColumnIndex(CATEGORY_ID);
        final int category_nameIndex = cursor.getColumnIndex(CATEGORY_NAME);
        final int category_statusIndex = cursor.getColumnIndex(CATEGORY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.categoriesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<categorydto> categorydtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                categorydto _categorydto = new categorydto();
                _categorydto.setcategory_Id(cursor.getLong(category_idIndex));
                _categorydto.setcategory_name(cursor.getString(category_nameIndex));
                _categorydto.setcategory_status(cursor.getString(category_statusIndex));
                _categorydto.setcreated_date(cursor.getString(created_dateIndex));
                categorydtos.add(_categorydto);

            } while (cursor.moveToNext());

            return categorydtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            db.close();
        }
    }

    public List<cropdto> filtercropsgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_ID, CROP_NAME, CROP_STATUS, DBContract.cropsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int crop_idIndex = cursor.getColumnIndex(CROP_ID);
        final int crop_nameIndex = cursor.getColumnIndex(CROP_NAME);
        final int crop_statusIndex = cursor.getColumnIndex(CROP_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE);
        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdto> cropdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdto _cropdto = new cropdto();
                _cropdto.setcrop_Id(cursor.getLong(crop_idIndex));
                _cropdto.setcrop_name(cursor.getString(crop_nameIndex));
                _cropdto.setcrop_status(cursor.getString(crop_statusIndex));
                _cropdto.setcreated_date(cursor.getString(created_dateIndex));
                cropdtos.add(_cropdto);

            } while (cursor.moveToNext());

            return cropdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropdto> filtercropgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_ID, CROP_NAME, CROP_STATUS, DBContract.cropsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int crop_idIndex = cursor.getColumnIndex(CROP_ID);
        final int crop_nameIndex = cursor.getColumnIndex(CROP_NAME);
        final int crop_statusIndex = cursor.getColumnIndex(CROP_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE);
        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdto> cropdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdto _cropdto = new cropdto();
                _cropdto.setcrop_Id(cursor.getLong(crop_idIndex));
                _cropdto.setcrop_name(cursor.getString(crop_nameIndex));
                _cropdto.setcrop_status(cursor.getString(crop_statusIndex));
                _cropdto.setcreated_date(cursor.getString(created_dateIndex));
                cropdtos.add(_cropdto);

            } while (cursor.moveToNext());

            return cropdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropvarietydto> filtercropsvarietiesgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_VARIETIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_VARIETY_ID, CROP_VARIETY_NAME, CROP_VARIETY_CROP_ID, CROP_VARIETY_MANUFACTURER_ID, CROP_VARIETY_STATUS, DBContract.cropsvarietiesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_VARIETY_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int cropvariety_idIndex = cursor.getColumnIndex(CROP_VARIETY_ID);
        final int cropvariety_nameIndex = cursor.getColumnIndex(CROP_VARIETY_NAME);
        final int cropvariety_crop_idIndex = cursor.getColumnIndex(CROP_VARIETY_CROP_ID);
        final int cropvariety_manufacturer_idIndex = cursor.getColumnIndex(CROP_VARIETY_MANUFACTURER_ID);
        final int cropvariety_statusIndex = cursor.getColumnIndex(CROP_VARIETY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsvarietiesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropvarietydto> cropsvarietiesdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropvarietydto _cropvarietydto = new cropvarietydto();
                _cropvarietydto.setcropvariety_Id(cursor.getLong(cropvariety_idIndex));
                _cropvarietydto.setcropvariety_name(cursor.getString(cropvariety_nameIndex));
                _cropvarietydto.setcropvariety_crop_id(cursor.getString(cropvariety_crop_idIndex));
                _cropvarietydto.setcropvariety_manufacturer_id(cursor.getString(cropvariety_manufacturer_idIndex));
                _cropvarietydto.setcropvariety_status(cursor.getString(cropvariety_statusIndex));
                _cropvarietydto.setcreated_date(cursor.getString(created_dateIndex));
                cropsvarietiesdtos.add(_cropvarietydto);

            } while (cursor.moveToNext());

            return cropsvarietiesdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropvarietydto> filtercropsvarietiesgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_VARIETIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_VARIETY_ID, CROP_VARIETY_NAME, CROP_VARIETY_CROP_ID, CROP_VARIETY_MANUFACTURER_ID, CROP_VARIETY_STATUS, DBContract.cropsvarietiesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_VARIETY_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int cropvariety_idIndex = cursor.getColumnIndex(CROP_VARIETY_ID);
        final int cropvariety_nameIndex = cursor.getColumnIndex(CROP_VARIETY_NAME);
        final int cropvariety_crop_idIndex = cursor.getColumnIndex(CROP_VARIETY_CROP_ID);
        final int cropvariety_manufacturer_idIndex = cursor.getColumnIndex(CROP_VARIETY_MANUFACTURER_ID);
        final int cropvariety_statusIndex = cursor.getColumnIndex(CROP_VARIETY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsvarietiesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropvarietydto> cropsvarietiesdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropvarietydto _cropvarietydto = new cropvarietydto();
                _cropvarietydto.setcropvariety_Id(cursor.getLong(cropvariety_idIndex));
                _cropvarietydto.setcropvariety_name(cursor.getString(cropvariety_nameIndex));
                _cropvarietydto.setcropvariety_crop_id(cursor.getString(cropvariety_crop_idIndex));
                _cropvarietydto.setcropvariety_manufacturer_id(cursor.getString(cropvariety_manufacturer_idIndex));
                _cropvarietydto.setcropvariety_status(cursor.getString(cropvariety_statusIndex));
                _cropvarietydto.setcreated_date(cursor.getString(created_dateIndex));
                cropsvarietiesdtos.add(_cropvarietydto);

            } while (cursor.moveToNext());

            return cropsvarietiesdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropdiseasedto> filtercropsdiseasesgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_DISEASES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_DISEASE_ID, CROP_DISEASE_NAME, CROP_DISEASE_CATEGORY, CROP_DISEASE_STATUS, DBContract.cropsdiseasesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_DISEASE_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int crop_disease_idIndex = cursor.getColumnIndex(CROP_DISEASE_ID);
        final int crop_disease_nameIndex = cursor.getColumnIndex(CROP_DISEASE_NAME);
        final int crop_disease_categoryIndex = cursor.getColumnIndex(CROP_DISEASE_CATEGORY);
        final int crop_disease_statusIndex = cursor.getColumnIndex(CROP_DISEASE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE);
        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdiseasedto> cropdiseasedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdiseasedto _cropdiseasedto = new cropdiseasedto();
                _cropdiseasedto.setcropdisease_Id(cursor.getLong(crop_disease_idIndex));
                _cropdiseasedto.setcropdisease_name(cursor.getString(crop_disease_nameIndex));
                _cropdiseasedto.setcropdisease_category(cursor.getString(crop_disease_categoryIndex));
                _cropdiseasedto.setcropdisease_status(cursor.getString(crop_disease_statusIndex));
                _cropdiseasedto.setcreated_date(cursor.getString(created_dateIndex));
                cropdiseasedtos.add(_cropdiseasedto);

            } while (cursor.moveToNext());

            return cropdiseasedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropdiseasedto> filtercropsdiseasesgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_DISEASES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_DISEASE_ID, CROP_DISEASE_NAME, CROP_DISEASE_CATEGORY, CROP_DISEASE_STATUS, DBContract.cropsdiseasesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_DISEASE_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int crop_disease_idIndex = cursor.getColumnIndex(CROP_DISEASE_ID);
        final int crop_disease_nameIndex = cursor.getColumnIndex(CROP_DISEASE_NAME);
        final int crop_disease_categoryIndex = cursor.getColumnIndex(CROP_DISEASE_CATEGORY);
        final int crop_disease_statusIndex = cursor.getColumnIndex(CROP_DISEASE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsentitytable.CREATED_DATE);
        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropdiseasedto> cropdiseasedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropdiseasedto _cropdiseasedto = new cropdiseasedto();
                _cropdiseasedto.setcropdisease_Id(cursor.getLong(crop_disease_idIndex));
                _cropdiseasedto.setcropdisease_name(cursor.getString(crop_disease_nameIndex));
                _cropdiseasedto.setcropdisease_category(cursor.getString(crop_disease_categoryIndex));
                _cropdiseasedto.setcropdisease_status(cursor.getString(crop_disease_statusIndex));
                _cropdiseasedto.setcreated_date(cursor.getString(created_dateIndex));
                cropdiseasedtos.add(_cropdiseasedto);

            } while (cursor.moveToNext());

            return cropdiseasedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<manufacturerdto> filtermanufacturersgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                MANUFACTURERS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{MANUFACTURER_ID, MANUFACTURER_NAME, MANUFACTURER_STATUS, DBContract.manufacturersentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                MANUFACTURER_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int manufacturer_idIndex = cursor.getColumnIndex(MANUFACTURER_ID);
        final int manufacturer_nameIndex = cursor.getColumnIndex(MANUFACTURER_NAME);
        final int manufacturer_statusIndex = cursor.getColumnIndex(MANUFACTURER_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.manufacturersentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<manufacturerdto> manufacturerdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                manufacturerdto _manufacturerdto = new manufacturerdto();
                _manufacturerdto.setmanufacturer_Id(cursor.getLong(manufacturer_idIndex));
                _manufacturerdto.setmanufacturer_name(cursor.getString(manufacturer_nameIndex));
                _manufacturerdto.setmanufacturer_status(cursor.getString(manufacturer_statusIndex));
                _manufacturerdto.setcreated_date(cursor.getString(created_dateIndex));
                manufacturerdtos.add(_manufacturerdto);

            } while (cursor.moveToNext());

            return manufacturerdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<manufacturerdto> filtermanufacturergivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                MANUFACTURERS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{MANUFACTURER_ID, MANUFACTURER_NAME, MANUFACTURER_STATUS, DBContract.manufacturersentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                MANUFACTURER_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int manufacturer_idIndex = cursor.getColumnIndex(MANUFACTURER_ID);
        final int manufacturer_nameIndex = cursor.getColumnIndex(MANUFACTURER_NAME);
        final int manufacturer_statusIndex = cursor.getColumnIndex(MANUFACTURER_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.manufacturersentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<manufacturerdto> manufacturerdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                manufacturerdto _manufacturerdto = new manufacturerdto();
                _manufacturerdto.setmanufacturer_Id(cursor.getLong(manufacturer_idIndex));
                _manufacturerdto.setmanufacturer_name(cursor.getString(manufacturer_nameIndex));
                _manufacturerdto.setmanufacturer_status(cursor.getString(manufacturer_statusIndex));
                _manufacturerdto.setcreated_date(cursor.getString(created_dateIndex));
                manufacturerdtos.add(_manufacturerdto);

            } while (cursor.moveToNext());

            return manufacturerdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<pestinsecticidedto> filterpestsinsecticidesgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                PESTSINSECTICIDES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{PESTINSECTICIDE_ID, PESTINSECTICIDE_NAME, PESTINSECTICIDE_CROP_DISEASE_ID, PESTINSECTICIDE_MANUFACTURER_ID, PESTINSECTICIDE_CATEGORY, PESTINSECTICIDE_STATUS, DBContract.pestsinsecticidesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                PESTINSECTICIDE_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int pestinsecticide_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_ID);
        final int pestinsecticide_nameIndex = cursor.getColumnIndex(PESTINSECTICIDE_NAME);
        final int pestinsecticide_crop_disease_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_CROP_DISEASE_ID);
        final int pestinsecticide_manufacturer_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_MANUFACTURER_ID);
        final int pestinsecticide_categoryIndex = cursor.getColumnIndex(PESTINSECTICIDE_CATEGORY);
        final int pestinsecticide_statusIndex = cursor.getColumnIndex(PESTINSECTICIDE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.pestsinsecticidesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<pestinsecticidedto> pestinsecticidedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();
                _pestinsecticidedto.setpestinsecticide_Id(cursor.getLong(pestinsecticide_idIndex));
                _pestinsecticidedto.setpestinsecticide_name(cursor.getString(pestinsecticide_nameIndex));
                _pestinsecticidedto.setpestinsecticide_crop_disease_id(cursor.getString(pestinsecticide_crop_disease_idIndex));
                _pestinsecticidedto.setpestinsecticide_manufacturer_id(cursor.getString(pestinsecticide_manufacturer_idIndex));
                _pestinsecticidedto.setpestinsecticide_category(cursor.getString(pestinsecticide_categoryIndex));
                _pestinsecticidedto.setpestinsecticide_status(cursor.getString(pestinsecticide_statusIndex));
                _pestinsecticidedto.setcreated_date(cursor.getString(created_dateIndex));
                pestinsecticidedtos.add(_pestinsecticidedto);

            } while (cursor.moveToNext());

            return pestinsecticidedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<pestinsecticidedto> filterpestsinsecticidesgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                PESTSINSECTICIDES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{PESTINSECTICIDE_ID, PESTINSECTICIDE_NAME, PESTINSECTICIDE_CROP_DISEASE_ID, PESTINSECTICIDE_MANUFACTURER_ID, PESTINSECTICIDE_CATEGORY, PESTINSECTICIDE_STATUS, DBContract.pestsinsecticidesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                PESTINSECTICIDE_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int pestinsecticide_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_ID);
        final int pestinsecticide_nameIndex = cursor.getColumnIndex(PESTINSECTICIDE_NAME);
        final int pestinsecticide_crop_disease_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_CROP_DISEASE_ID);
        final int pestinsecticide_manufacturer_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_MANUFACTURER_ID);
        final int pestinsecticide_categoryIndex = cursor.getColumnIndex(PESTINSECTICIDE_CATEGORY);
        final int pestinsecticide_statusIndex = cursor.getColumnIndex(PESTINSECTICIDE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.pestsinsecticidesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<pestinsecticidedto> pestinsecticidedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();
                _pestinsecticidedto.setpestinsecticide_Id(cursor.getLong(pestinsecticide_idIndex));
                _pestinsecticidedto.setpestinsecticide_name(cursor.getString(pestinsecticide_nameIndex));
                _pestinsecticidedto.setpestinsecticide_crop_disease_id(cursor.getString(pestinsecticide_crop_disease_idIndex));
                _pestinsecticidedto.setpestinsecticide_manufacturer_id(cursor.getString(pestinsecticide_manufacturer_idIndex));
                _pestinsecticidedto.setpestinsecticide_category(cursor.getString(pestinsecticide_categoryIndex));
                _pestinsecticidedto.setpestinsecticide_status(cursor.getString(pestinsecticide_statusIndex));
                _pestinsecticidedto.setcreated_date(cursor.getString(created_dateIndex));
                pestinsecticidedtos.add(_pestinsecticidedto);

            } while (cursor.moveToNext());

            return pestinsecticidedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<settingdto> filtersettingsgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                SETTINGS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{SETTING_ID, SETTING_NAME, SETTING_VALUE, SETTING_STATUS, DBContract.settingsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                SETTING_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int setting_idIndex = cursor.getColumnIndex(SETTING_ID);
        final int setting_nameIndex = cursor.getColumnIndex(SETTING_NAME);
        final int setting_statusIndex = cursor.getColumnIndex(SETTING_STATUS);
        final int setting_valueIndex = cursor.getColumnIndex(SETTING_VALUE);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.settingsentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<settingdto> settingdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                settingdto _settingdto = new settingdto();
                _settingdto.setsetting_Id(cursor.getLong(setting_idIndex));
                _settingdto.setsetting_name(cursor.getString(setting_nameIndex));
                _settingdto.setsetting_status(cursor.getString(setting_statusIndex));
                _settingdto.setsetting_value(cursor.getString(setting_valueIndex));
                _settingdto.setcreated_date(cursor.getString(created_dateIndex));
                settingdtos.add(_settingdto);

            } while (cursor.moveToNext());

            return settingdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<settingdto> filtersettingsgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                SETTINGS_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{SETTING_ID, SETTING_NAME, SETTING_VALUE, SETTING_STATUS, DBContract.settingsentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                SETTING_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int setting_idIndex = cursor.getColumnIndex(SETTING_ID);
        final int setting_nameIndex = cursor.getColumnIndex(SETTING_NAME);
        final int setting_statusIndex = cursor.getColumnIndex(SETTING_STATUS);
        final int setting_valueIndex = cursor.getColumnIndex(SETTING_VALUE);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.settingsentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<settingdto> settingdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                settingdto _settingdto = new settingdto();
                _settingdto.setsetting_Id(cursor.getLong(setting_idIndex));
                _settingdto.setsetting_name(cursor.getString(setting_nameIndex));
                _settingdto.setsetting_status(cursor.getString(setting_statusIndex));
                _settingdto.setsetting_value(cursor.getString(setting_valueIndex));
                _settingdto.setcreated_date(cursor.getString(created_dateIndex));
                settingdtos.add(_settingdto);

            } while (cursor.moveToNext());

            return settingdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<categorydto> filtercategoriesgivenname(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CATEGORIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_STATUS, DBContract.categoriesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CATEGORY_NAME + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int category_idIndex = cursor.getColumnIndex(CATEGORY_ID);
        final int category_nameIndex = cursor.getColumnIndex(CATEGORY_NAME);
        final int category_statusIndex = cursor.getColumnIndex(CATEGORY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.categoriesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<categorydto> categorydtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                categorydto _categorydto = new categorydto();
                _categorydto.setcategory_Id(cursor.getLong(category_idIndex));
                _categorydto.setcategory_name(cursor.getString(category_nameIndex));
                _categorydto.setcategory_status(cursor.getString(category_statusIndex));
                _categorydto.setcreated_date(cursor.getString(created_dateIndex));
                categorydtos.add(_categorydto);

            } while (cursor.moveToNext());

            return categorydtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<categorydto> filtercategoriesgivenid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CATEGORIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_STATUS, DBContract.categoriesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CATEGORY_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int category_idIndex = cursor.getColumnIndex(CATEGORY_ID);
        final int category_nameIndex = cursor.getColumnIndex(CATEGORY_NAME);
        final int category_statusIndex = cursor.getColumnIndex(CATEGORY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.categoriesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<categorydto> categorydtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                categorydto _categorydto = new categorydto();
                _categorydto.setcategory_Id(cursor.getLong(category_idIndex));
                _categorydto.setcategory_name(cursor.getString(category_nameIndex));
                _categorydto.setcategory_status(cursor.getString(category_statusIndex));
                _categorydto.setcreated_date(cursor.getString(created_dateIndex));
                categorydtos.add(_categorydto);

            } while (cursor.moveToNext());

            return categorydtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<cropvarietydto> getcropsvarietiesgivencropid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                CROPS_VARIETIES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{CROP_VARIETY_ID, CROP_VARIETY_NAME, CROP_VARIETY_CROP_ID, CROP_VARIETY_MANUFACTURER_ID, CROP_VARIETY_STATUS, DBContract.cropsvarietiesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                CROP_VARIETY_CROP_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int cropvariety_idIndex = cursor.getColumnIndex(CROP_VARIETY_ID);
        final int cropvariety_nameIndex = cursor.getColumnIndex(CROP_VARIETY_NAME);
        final int cropvariety_crop_idIndex = cursor.getColumnIndex(CROP_VARIETY_CROP_ID);
        final int cropvariety_manufacturer_idIndex = cursor.getColumnIndex(CROP_VARIETY_MANUFACTURER_ID);
        final int cropvariety_statusIndex = cursor.getColumnIndex(CROP_VARIETY_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.cropsvarietiesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<cropvarietydto> cropsvarietiesdtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                cropvarietydto _cropvarietydto = new cropvarietydto();

                _cropvarietydto.setcropvariety_Id(cursor.getLong(cropvariety_idIndex));
                _cropvarietydto.setcropvariety_name(cursor.getString(cropvariety_nameIndex));
                _cropvarietydto.setcropvariety_crop_id(cursor.getString(cropvariety_crop_idIndex));
                _cropvarietydto.setcropvariety_manufacturer_id(cursor.getString(cropvariety_manufacturer_idIndex));
                _cropvarietydto.setcropvariety_status(cursor.getString(cropvariety_statusIndex));
                _cropvarietydto.setcreated_date(cursor.getString(created_dateIndex));

                cropsvarietiesdtos.add(_cropvarietydto);

            } while (cursor.moveToNext());

            return cropsvarietiesdtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }

    public List<search_crop_dto> getcropsvarietiesgivencropname(String searchTerm) {
        try {

            List<search_crop_dto> _lstsearchdtos = new ArrayList<>();
            List<cropdto> _filtercrops = new ArrayList<>();

            if (searchTerm == null || searchTerm.isEmpty()) {
                _filtercrops = getallcrops();
            } else {
                _filtercrops = filtercropsgivenname(searchTerm);
            }

            Log.e(TAG, "filter crops count [ " + _filtercrops.size() + " ]");

            for (cropdto _cropdto : _filtercrops) {

                Log.e(TAG, "crop Id [ " + _cropdto.getcrop_Id() + " ]");

                List<cropvarietydto> _filtercropsvarieties = getcropsvarietiesgivencropid(String.format("%d", _cropdto.getcrop_Id()));

                int _varieties_count = _filtercropsvarieties.size();

                Log.e(TAG, "varieties count [ " + _varieties_count + " ]");

                if (_varieties_count == 0) {
                    continue;
                }

                int counta = 0;

                for (cropvarietydto _dto : _filtercropsvarieties) {

                    Log.e(TAG, "crop name [ " + getcropgivenid(Long.valueOf(_dto.getcropvariety_crop_id())).getcrop_name().toLowerCase() + " ]");

                    Log.e(TAG, "searchTerm [ " + searchTerm.toString().toLowerCase() + " ]");

                    if (!getcropgivenid(Long.valueOf(_dto.getcropvariety_crop_id())).getcrop_name().toLowerCase().contains(searchTerm.toString().toLowerCase())) {
                        continue;
                    }

                    counta++;

                    search_crop_dto _search_dto = new search_crop_dto();

                    _search_dto.setdto_Id(Long.valueOf(counta));

                    _search_dto.setcrop_variety_name(_dto.getcropvariety_name());

                    _search_dto.setcrop_name(getcropgivenid(Long.valueOf(_dto.getcropvariety_crop_id())).getcrop_name());

                    _search_dto.setmanufacturer_name(getmanufacturergivenid(Long.valueOf(_dto.getcropvariety_manufacturer_id())).getmanufacturer_name());

                    _lstsearchdtos.add(_search_dto);

                }

            }

            int _count = _lstsearchdtos.size();
            Log.e(TAG, "search count [ " + _count + " ]");

            //db.close();
            return _lstsearchdtos;

        } catch (Exception ex) {
            //db.close();
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public List<search_cropdiseasepest_dto> getpesticidesinsecticidesgivendiseasepestname(String searchTerm) {
        try {

            List<search_cropdiseasepest_dto> _lstsearchdtos = new ArrayList<>();

            List<cropdiseasedto> _filterrecords = filtercropsdiseasesgivenname(searchTerm);

            Log.e(TAG, "filter records count [ " + _filterrecords.size() + " ]");

            for (cropdiseasedto _diseasepestdto : _filterrecords) {

                Log.e(TAG, "diseasepest Id [ " + _diseasepestdto.getcropdisease_Id() + " ]");

                List<pestinsecticidedto> _filtereddtos = getpesticidesinsecticidesgivendiseasepestid(String.format("%d", _diseasepestdto.getcropdisease_Id()));

                int _dtos_count = _filtereddtos.size();

                Log.e(TAG, "pesticide/insecticide count [ " + _dtos_count + " ]");

                if (_dtos_count == 0) {
                    continue;
                }

                int counta = 0;

                for (pestinsecticidedto _dto : _filtereddtos) {

                    Log.e(TAG, "diseasepest name [ " + getcropdiseasegivenid(Long.valueOf(_dto.getpestinsecticide_crop_disease_id())).getcropdisease_name().toLowerCase() + " ]");

                    Log.e(TAG, "searchTerm [ " + searchTerm.toString().toLowerCase() + " ]");

                    if (!getcropdiseasegivenid(Long.valueOf(_dto.getpestinsecticide_crop_disease_id())).getcropdisease_name().toLowerCase().contains(searchTerm.toString().toLowerCase())) {
                        continue;
                    }

                    counta++;

                    search_cropdiseasepest_dto _search_dto = new search_cropdiseasepest_dto();

                    _search_dto.setdto_Id(Long.valueOf(counta));

                    _search_dto.setcrop_diseasepest_name(_diseasepestdto.getcropdisease_name());

                    _search_dto.setpestinsecticide_name(getpestinsecticidegivenid(Long.valueOf(_dto.getpestinsecticide_Id())).getpestinsecticide_name());

                    _search_dto.setpestinsecticide_manufacturer(getmanufacturergivenid(Long.valueOf(_dto.getpestinsecticide_manufacturer_id())).getmanufacturer_name());

                    _lstsearchdtos.add(_search_dto);

                }

            }

            int _count = _lstsearchdtos.size();
            Log.e(TAG, "search count [ " + _count + " ]");

            //db.close();
            return _lstsearchdtos;

        } catch (Exception ex) {
            //db.close();
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public List<pestinsecticidedto> getpesticidesinsecticidesgivendiseasepestid(String searchTerm) {
// When reading data one should always just get a readable database.
//        final SQLiteDatabase database = this.getReadableDatabase();
        openDataBase();
        final Cursor cursor = db.query(
// Name of the table to read from
                PESTSINSECTICIDES_TABLE_NAME,
// String array of the columns which are supposed to be read
                new String[]{PESTINSECTICIDE_ID, PESTINSECTICIDE_NAME, PESTINSECTICIDE_CROP_DISEASE_ID, PESTINSECTICIDE_MANUFACTURER_ID, PESTINSECTICIDE_CATEGORY, PESTINSECTICIDE_STATUS, DBContract.pestsinsecticidesentitytable.CREATED_DATE},
// The selection argument which specifies which row is read.
// ? symbols are parameters.
                PESTINSECTICIDE_CROP_DISEASE_ID + " LIKE ?",
// The actual parameters values for the selection as a String array.
// ? above take the value from here
                new String[]{"%" + searchTerm + "%"},
// GroupBy clause. Specify a column name to group similar values
// in that column together.
                null,
// Having clause. When using the GroupBy clause this allows you to
// specify which groups to include.
                null,
// OrderBy clause. Specify a column name here to order the results
// according to that column. Optionally append ASC or DESC to specify
// an ascending or descending order.
                null
        );
// To increase performance first get the index of each column in the cursor
        final int pestinsecticide_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_ID);
        final int pestinsecticide_nameIndex = cursor.getColumnIndex(PESTINSECTICIDE_NAME);
        final int pestinsecticide_crop_disease_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_CROP_DISEASE_ID);
        final int pestinsecticide_manufacturer_idIndex = cursor.getColumnIndex(PESTINSECTICIDE_MANUFACTURER_ID);
        final int pestinsecticide_categoryIndex = cursor.getColumnIndex(PESTINSECTICIDE_CATEGORY);
        final int pestinsecticide_statusIndex = cursor.getColumnIndex(PESTINSECTICIDE_STATUS);
        final int created_dateIndex = cursor.getColumnIndex(DBContract.pestsinsecticidesentitytable.CREATED_DATE);

        try {
// If moveToFirst() returns false then cursor is empty
            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<pestinsecticidedto> pestinsecticidedtos = new ArrayList<>();
            do {
// Read the values of a row in the table using the indexes acquired above
                pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();
                _pestinsecticidedto.setpestinsecticide_Id(cursor.getLong(pestinsecticide_idIndex));
                _pestinsecticidedto.setpestinsecticide_name(cursor.getString(pestinsecticide_nameIndex));
                _pestinsecticidedto.setpestinsecticide_crop_disease_id(cursor.getString(pestinsecticide_crop_disease_idIndex));
                _pestinsecticidedto.setpestinsecticide_manufacturer_id(cursor.getString(pestinsecticide_manufacturer_idIndex));
                _pestinsecticidedto.setpestinsecticide_category(cursor.getString(pestinsecticide_categoryIndex));
                _pestinsecticidedto.setpestinsecticide_status(cursor.getString(pestinsecticide_statusIndex));
                _pestinsecticidedto.setcreated_date(cursor.getString(created_dateIndex));
                pestinsecticidedtos.add(_pestinsecticidedto);

            } while (cursor.moveToNext());

            return pestinsecticidedtos;
        } finally {
// Don't forget to close the Cursor once you are done to avoid memory leaks.
// Using a try/finally like in this example is usually the best way to handle this
            cursor.close();
// close the database
            //db.close();
        }
    }


}









