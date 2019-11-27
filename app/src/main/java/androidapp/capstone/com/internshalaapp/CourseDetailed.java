package androidapp.capstone.com.internshalaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class CourseDetailed extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String position = "", selectedWorkshop = "";
    private Button apply;
    private TextView course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detailed);

        try {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getString("Position");
            selectedWorkshop = bundle.getString("WrkshopSelected");
        } catch (Exception e) {
            Log.i("Error on Bundle", e.getMessage());
        }

        apply = findViewById(R.id.applyForCourse);

        course = findViewById(R.id.courseName);

        course.setText(selectedWorkshop);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasAlreadyApplied()) {
                    Toast.makeText(CourseDetailed.this, "This course is already registered by you !", Toast.LENGTH_SHORT).show();
                } else {
                    applyForTheWorkshop();
                }
            }
        });

    }

    private boolean hasAlreadyApplied() {
        UserDBHelper mUserDbHelper = new UserDBHelper(this);
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        String workshopname = getWorkShopName();
        String emailOfTheUser = getEmail();
        String projection[] = {UserContract.UserEntry.USER_EMAIL, workshopname};
        Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME, projection,
                UserContract.UserEntry.USER_EMAIL + "=?",
                new String[]{emailOfTheUser},
                null,
                null, null
        );
        int value = 0;
        if (cursor.moveToFirst()) {
            int selectoedWorkshopColIndex = cursor.getColumnIndex(workshopname);
            value = cursor.getInt(selectoedWorkshopColIndex);

        }
        boolean b = false;
        if (value == 1)
            b = true;
        return b;
    }

    private void applyForTheWorkshop() {
        UserDBHelper mUserDbHelper = new UserDBHelper(CourseDetailed.this);
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
        String workshopname = getWorkShopName();
        String emailOfTheUser = getEmail();
        ContentValues contentValues = new ContentValues();
        contentValues.put(workshopname, 1);
        int rowsUpdated = mSqLiteDatabase.update(UserContract.UserEntry.TABLE_NAME, contentValues, UserContract.UserEntry.USER_EMAIL + "=?", new String[]{emailOfTheUser});
        if (rowsUpdated == 0) {
            Toast.makeText(CourseDetailed.this, "Failed while Updation", Toast.LENGTH_LONG).show();
        } else {
            final AlertDialog.Builder alert = new AlertDialog.Builder(
                    CourseDetailed.this);
            alert.setTitle("Confirmation");
            alert.setMessage("Course Registered Successfully...!");
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }
    }


    private String getWorkShopName() {
        String str = null;
        switch (selectedWorkshop) {
            case "Android":
                str = UserContract.UserEntry.WORKSHOP_ANDROID;
                break;
            case "Artificial Intelligence":
                str = UserContract.UserEntry.WORKSHOP_Artificial_Intelligence;
                break;

            case "BigData Bootcamp":
                str = UserContract.UserEntry.WORKSHOP_BigData_Bootcamp;
                break;
            case "BlockChain Technology":
                str = UserContract.UserEntry.WORKSHOP_BlockChain_Technology;
                break;
            case "Cyber Security":
                str = UserContract.UserEntry.WORKSHOP_Cyber_Security;
                break;
            case "CloudComputing":
                str = UserContract.UserEntry.WORKSHOP_CloudComputing;
                break;
            case "Datascience Bootcamp":
                str = UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp;
                break;
            case "Iot":
                str = UserContract.UserEntry.WORKSHOP_IoT;
                break;
            case "Machine Learning":
                str = UserContract.UserEntry.WORKSHOP_Machine_Learning;
                break;
            case "SAP Bootcamp":
                str = UserContract.UserEntry.WORKSHOP_SAP_Bootcamp;
                break;
            case "Web Development":
                str = UserContract.UserEntry.WORKSHOP_Web_Development;
                break;
        }
        return str;

    }

    public String getEmail() {
        String email = "";
        UserDBHelper mUserDbHelper = new UserDBHelper(CourseDetailed.this);
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.CURR_USER_TABLE,
                new String[]{UserContract.UserEntry.CURRENT_USER_ID, UserContract.UserEntry.CURRENT_USER_EMAIL},
                UserContract.UserEntry.CURRENT_USER_ID + "=?",
                new String[]{"01"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int emailInd = cursor.getColumnIndex(UserContract.UserEntry.CURRENT_USER_EMAIL);
                email = cursor.getString(emailInd);
            }
        }
        return email;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS};
        Uri currentUri = ContentUris.withAppendedId(WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI, Long.parseLong(position));
        return new CursorLoader(CourseDetailed.this,
                currentUri,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int detailsColIndex = data.getColumnIndex(WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS);
            String details = data.getString(detailsColIndex);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
