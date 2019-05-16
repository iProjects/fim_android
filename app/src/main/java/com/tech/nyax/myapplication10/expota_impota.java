package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class expota_impota {

    private static expota_impota singleInstance;
    private final Context context;
    private static final String TAG = expota_impota.class.getSimpleName();
    private static final String database_export_folder_path = "/ntharene_db_backups/";
    private static final String database_import_path = "";
    private String packagename = "com.tech.nyax.myapplication10";
    String DB_NAME = "ntharenedb_imported.sqlite3";
    String DB_PATH;
    String outFileName = "";
    String dbfoldername = "/databases/";
    String backup_db_name = "ntharenedb_exported.sqlite3";
    String import_db_name = "ntharenedb.sqlite3";
    responsedto _responsedto = new responsedto();
    // reusable string object
    static StringBuilder _string_builder = new StringBuilder();
    String _file_path_separator = "/";

    public expota_impota(Context context) {
        this.context = context;
    }

    public static expota_impota getInstance(Context context) {
        if (singleInstance == null)
            singleInstance = new expota_impota(context);
        return singleInstance;
    }

    public void importDatabase() {
        try {

            File dbStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dbfoldername);
            if (!dbStorageDir.exists()) {
                if (!dbStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create Storage directory [ " + dbStorageDir.getPath() + " ].");
                }
            }

            File currentDB = getcurrentdatabasepathwithfilename();
            File backupDB = new File(dbStorageDir, import_db_name);

            importDatabasegivenbackuppath(currentDB, backupDB);

        } catch (Exception ex) {
            utilz.getInstance(context).globalloghandler("error importing database ...\n" + ex.toString(), expota_impota.class.getSimpleName(), 4, 0, "expota_impota.importDatabase", "expota_impota.importDatabase");
        }
    }

    public String import_Database_given_uri(Uri _uri) {
        StringBuilder _string_builder = new StringBuilder();
        try {

            File dbStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dbfoldername);
            if (!dbStorageDir.exists()) {
                if (!dbStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create Storage directory [ " + dbStorageDir.getPath() + " ].");
                }
            }

            File currentDB = getcurrentdatabasepathwithfilename();
            File backupDB = new File(dbStorageDir, import_db_name);

            importDatabasegivenbackuppath(currentDB, backupDB);

            return _string_builder.toString();

        } catch (Exception ex) {
            utilz.getInstance(context).globalloghandler("error importing database ...\n" + ex.toString(), expota_impota.class.getSimpleName(), 4, 0, "expota_impota.importDatabase", "expota_impota.importDatabase");
            return _string_builder.toString();
        }
    }

    public void importDatabasegivenbackuppath(File currentDB, File backupDB) {
        try {
            if (!backupDB.exists()) {

                utilz.getInstance(context).globalloghandler("back up database file not found. Make sure  a file named ntharenedb.sqlite exists in path [ " + backupDB.getPath() + " ] ", expota_impota.class.getSimpleName(), 1, 0, "expota_impota.importDatabasegivenbackuppath", "expota_impota.importDatabasegivenbackuppath");
            }
            if (currentDB.exists() && backupDB.exists()) {
                FileChannel source = new FileInputStream(currentDB).getChannel();
                FileChannel destination = new FileOutputStream(backupDB).getChannel();
                source.transferFrom(destination, 0, destination.size());
                destination.close();
                source.close();

                utilz.getInstance(context).globalloghandler("imported Database successfully..." + " currentDB [ " + currentDB.toString() + " ] backupDB [ " + backupDB.toString() + " ] ", expota_impota.class.getSimpleName(), 1, 1, "expota_impota.importDatabasegivenbackuppath", "expota_impota.importDatabasegivenbackuppath");
            }
        } catch (Exception ex) {
            utilz.getInstance(context).globalloghandler(ex.toString(), expota_impota.class.getSimpleName(), 1, 0, "expota_impota.importDatabasegivenbackuppath", "expota_impota.importDatabasegivenbackuppath");
            Log.e(TAG, ex.toString());
        }
    }

    File getcurrentdatabasepathwithfilename() {
        try {

            ContextWrapper cw = new ContextWrapper(context);

            DB_PATH = this.context.getFilesDir().getParent() + dbfoldername;

            Log.e(TAG, "DB_PATH [ " + DB_PATH + " ] ");

            outFileName = DB_PATH + DB_NAME;

            File file_DB_PATH = new File(DB_PATH);

            Log.e(TAG, "db path exists? " + file_DB_PATH.exists());

            if (!file_DB_PATH.exists()) {
                file_DB_PATH.mkdirs();
                Log.e(TAG, "created path [  " + file_DB_PATH + " ]");
            }

            final File _createNewFile = new File(outFileName);
            try {
                if (!_createNewFile.exists()) {
                    _createNewFile.createNewFile();
                    Log.e(TAG, "created file [  " + _createNewFile + " ]");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            makenewfilesvisible(_createNewFile);

            return _createNewFile;

        } catch (Exception ex) {
            utilz.getInstance(context).globalloghandler(ex.toString(), expota_impota.class.getSimpleName(), 1, 0, "expota_impota.exportDatabase", "expota_impota.exportDatabase");
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public void makenewfilesvisible(File _createNewFile) {
        try {
            /* newly created files are not immediately visible in the file explorer running on the connected desktop PC. To to make new files visible, you need to call MediaScannerConnection */

            MediaScannerConnection.scanFile(context, new String[]{_createNewFile.getPath()}, null, null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(_createNewFile)));

        } catch (Exception ex) {
            utilz.getInstance(context).globalloghandler(ex.toString(), expota_impota.class.getSimpleName(), 1, 0, "expota_impota.exportDatabase", "expota_impota.exportDatabase");
            Log.e(TAG, ex.toString());
        }
    }

    public responsedto exportDatabase() {
        try {

            File currentDB = getcurrentdatabasepathwithfilename();

            File ntharenedbbackupsdir = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), database_export_folder_path);
            // Make the directory if it does not yet exist
            try {
                Log.e(TAG, ntharenedbbackupsdir.toString());
                if (!ntharenedbbackupsdir.exists()) {
                    ntharenedbbackupsdir.mkdirs();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            DateFormat _dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date _current_date = new Date();
            String _timestamp = _dateFormat.format(_current_date);

            File backupDB = new File(ntharenedbbackupsdir, _timestamp + "_" + backup_db_name);
            if (!backupDB.exists()) {
                backupDB.createNewFile();
            }

            if (currentDB.exists() && backupDB.exists()) {
                FileChannel source = new FileInputStream(currentDB).getChannel();
                FileChannel destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();

                _string_builder.append("successfully exported Database..." + " \ncurrentDB [ " + currentDB.toString() + " ] \nbackupDB [ " + backupDB.toString() + " ] ");

            }

            _string_builder.append(exportDatabasetomanypaths());

            _responsedto.setresponsesuccessmessage(_string_builder.toString());

            return _responsedto;

        } catch (Exception ex) {

            _string_builder.append(ex.toString());

            _responsedto.setresponseerrormessage(_string_builder.toString());

            Log.e(TAG, _string_builder.toString());

            return _responsedto;
        }
    }

    String exportDatabasetomanypaths() {
        StringBuilder _string_builder = new StringBuilder();
        try {

            File currentDB = getcurrentdatabasepathwithfilename();
            File backupDB = null;
            DateFormat _dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date _current_date = new Date();
            String _timestamp = _dateFormat.format(_current_date);
            String _db_name = backup_db_name.concat("_").concat(_timestamp);

            File _external_files_dirs_path = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), database_export_folder_path);

            try {
                boolean _create_dirs = _external_files_dirs_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_files_dirs_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_files_dirs_path_with_name = new File(_external_files_dirs_path + _file_path_separator + _db_name);

                boolean _create_file = _external_files_dirs_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_files_dirs_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_files_dirs_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                // Public documents directory
                File _public_external_files_dirs_path = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), database_export_folder_path);

                try {
                    boolean _create_dirs = _public_external_files_dirs_path.mkdirs();
                    if (_create_dirs) {
                        _string_builder.append("created path [ " + _public_external_files_dirs_path.getAbsolutePath());
                        _string_builder.append("\n");
                    }
                    File _public_external_files_dirs_path_with_name = new File(_public_external_files_dirs_path + _file_path_separator + _db_name);

                    boolean _create_file = _public_external_files_dirs_path_with_name.createNewFile();
                    if (_create_file) {
                        _string_builder.append("created file [ " + _public_external_files_dirs_path_with_name.toString());
                        _string_builder.append("\n");
                    }
                    backupDB = _public_external_files_dirs_path_with_name;

                    _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                    _string_builder.append("\n");
                    _string_builder.append(ex.getMessage());
                    _string_builder.append("\n");
                }
            }

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                // Public documents directory
                File _public_external_dirs_path = new File(Environment.getExternalStorageDirectory(), database_export_folder_path);

                try {
                    boolean _create_dirs = _public_external_dirs_path.mkdirs();
                    if (_create_dirs) {
                        _string_builder.append("created path [ " + _public_external_dirs_path.getAbsolutePath());
                        _string_builder.append("\n");
                    }
                    File _public_external_files_dirs_path_with_name = new File(_public_external_dirs_path + _file_path_separator + _db_name);

                    boolean _create_file = _public_external_files_dirs_path_with_name.createNewFile();
                    if (_create_file) {
                        _string_builder.append("created file [ " + _public_external_files_dirs_path_with_name.toString());
                        _string_builder.append("\n");
                    }
                    backupDB = _public_external_files_dirs_path_with_name;

                    _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                    _string_builder.append("\n");
                    _string_builder.append(ex.getMessage());
                    _string_builder.append("\n");
                }
            }

            File internal_m1_path = this.context.getDir(database_export_folder_path, 0);

            try {
                boolean _create_dirs = internal_m1_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + internal_m1_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File internal_m1_path_with_name = new File(internal_m1_path + _file_path_separator + _db_name);

                boolean _create_file = internal_m1_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + internal_m1_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = internal_m1_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File internal_m2_path = new File(this.context.getFilesDir(), database_export_folder_path);

            try {
                boolean _create_dirs = internal_m2_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + internal_m2_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File internal_m2_path_with_name = new File(internal_m2_path + _file_path_separator + _db_name);

                boolean _create_file = internal_m2_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + internal_m2_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = internal_m2_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File _external_m1_path = new File(Environment.getExternalStorageDirectory(), database_export_folder_path);

            try {
                boolean _create_dirs = _external_m1_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_m1_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_m1_path_with_name = new File(_external_m1_path + _file_path_separator + _db_name);

                boolean _create_file = _external_m1_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_m1_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_m1_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File _external_m2_path = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), database_export_folder_path);

            try {
                boolean _create_dirs = _external_m2_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_m2_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_m2_path_with_name = new File(_external_m2_path + _file_path_separator + _db_name);

                boolean _create_file = _external_m2_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_m2_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_m2_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File _external_m2_Args_path = this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            try {
                boolean _create_dirs = _external_m2_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_m2_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_m2_path_with_name = new File(_external_m2_path + _file_path_separator + _db_name);

                boolean _create_file = _external_m2_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_m2_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_m2_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File _external_m3_Args_path =
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), database_export_folder_path);

            try {
                boolean _create_dirs = _external_m3_Args_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_m3_Args_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_m3_Args_path_with_name = new File(_external_m3_Args_path + _file_path_separator + _db_name);

                boolean _create_file = _external_m3_Args_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_m3_Args_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_m3_Args_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            File _external_AND_removable_storage_m1_path = new File(this.context.getExternalFilesDir(database_export_folder_path), Environment.DIRECTORY_DOCUMENTS);

            try {
                boolean _create_dirs = _external_AND_removable_storage_m1_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_AND_removable_storage_m1_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_AND_removable_storage_m1_path_with_name = new File(_external_AND_removable_storage_m1_path + _file_path_separator + _db_name);

                boolean _create_file = _external_AND_removable_storage_m1_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_AND_removable_storage_m1_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_AND_removable_storage_m1_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }
            File _external_AND_removable_storage_m1_Args_path =
                    this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            try {
                boolean _create_dirs = _external_AND_removable_storage_m1_Args_path.mkdirs();
                if (_create_dirs) {
                    _string_builder.append("created path [ " + _external_AND_removable_storage_m1_Args_path.getAbsolutePath());
                    _string_builder.append("\n");
                }
                File _external_AND_removable_storage_m1_Args_path_with_name = new File(_external_AND_removable_storage_m1_Args_path + _file_path_separator + _db_name);

                boolean _create_file = _external_AND_removable_storage_m1_Args_path_with_name.createNewFile();
                if (_create_file) {
                    _string_builder.append("created file [ " + _external_AND_removable_storage_m1_Args_path_with_name.toString());
                    _string_builder.append("\n");
                }
                backupDB = _external_AND_removable_storage_m1_Args_path_with_name;

                _string_builder.append(exportDatabasetomanypaths(currentDB, backupDB));

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                _string_builder.append("\n");
                _string_builder.append(ex.getMessage());
                _string_builder.append("\n");
            }

            return _string_builder.toString();

        } catch (Exception ex) {

            _string_builder.append(ex.toString());

            Log.e(TAG, _string_builder.toString());

            return _string_builder.toString();

        }
    }

    String exportDatabasetomanypaths(File currentDB, File backupDB) {
        StringBuilder _string_builder = new StringBuilder();
        try {

            if (currentDB.exists() && backupDB.exists()) {
                FileChannel source = new FileInputStream(currentDB).getChannel();
                FileChannel destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();

                makenewfilesvisible(currentDB);

                makenewfilesvisible(backupDB);

                _string_builder.append("successfully exported Database..." + " \ncurrentDB [ " + currentDB.toString() + " ] \nbackupDB [ " + backupDB.toString() + " ].\n ");

            }

            return _string_builder.toString();

        } catch (Exception ex) {

            _string_builder.append(ex.toString());

            Log.e(TAG, _string_builder.toString());

            return _string_builder.toString();
        }
    }


}
