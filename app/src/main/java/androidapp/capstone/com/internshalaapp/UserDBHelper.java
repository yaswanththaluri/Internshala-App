package androidapp.capstone.com.internshalaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "activeuser.db";
    //Database version
    public static final int DATBASE_VERSION = 1;

    public UserDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE_SQL_QUERY = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME +
                "( " + UserContract.UserEntry.id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserContract.UserEntry.USER_EMAIL + " TEXT NOT NULL, " +
                UserContract.UserEntry.USER_PASSWORD + " TEXT NOT NULL, " +
                UserContract.UserEntry.USER_FIRST_NAME + " TEXT NOT NULL, " +
                UserContract.UserEntry.USER_LAST_NAME + " TEXT NOT NULL, " +
                UserContract.UserEntry.WORKSHOP_ANDROID + " INTEGER DEFAULT 0 ," +
                UserContract.UserEntry.WORKSHOP_Artificial_Intelligence + " INTEGER DEFAULT 0 ," +
                UserContract.UserEntry.WORKSHOP_BigData_Bootcamp + " INTEGER DEFAULT 0 ," +
                UserContract.UserEntry.WORKSHOP_BlockChain_Technology + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_CloudComputing + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_Cyber_Security + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_IoT + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_Machine_Learning + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_SAP_Bootcamp + " INTEGER DEFAULT 0," +
                UserContract.UserEntry.WORKSHOP_Web_Development + " INTEGER DEFAULT 0 );";
        //Execute the query
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
