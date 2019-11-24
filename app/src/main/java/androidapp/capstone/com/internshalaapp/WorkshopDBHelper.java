package androidapp.capstone.com.internshalaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WorkshopDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workshoporg.db";
    //Database version
    private static final int DATABASE_VERSION = 1;

    public WorkshopDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + WorkshopContract.WorkshopEntry.TABLE_NAME + " (" +
                WorkshopContract.WorkshopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkshopContract.WorkshopEntry.WORKSHOP_NAME + " NOT NULL," +
                WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS + " NOT NULL );";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
