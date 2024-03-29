package androidapp.capstone.com.internshalaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView fragTitle;

    boolean firstTimeEntering = true;
    //Workshop items
    String workshopNames[] = {"Android", "Artificial Intelligence", "BigData Bootcamp", "BlockChain Technology", "Cyber Security", "CloudComputing", "Datascience Bootcamp", "Iot", "Machine Learning", "SAP Bootcamp", "Web Development"};
    //Workshop details
    String workshopDetails = null;
    String showNav = "";
    private ImageView userAuth, signOut;
    private RelativeLayout toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getThestatus();
        //Get the string res
        workshopDetails = getResources().getString(R.string.lorem);
        //feed the database
        LoadtheWorkshops();

        userAuth = findViewById(R.id.authUser);

        try
        {
            Bundle bundle = getIntent().getExtras();
            showNav = bundle.getString("showNavs");
        }
        catch (Exception e)
        {
            Log.i("error",e.getMessage());
        }




        fragTitle = findViewById(R.id.title);
        toolbar = findViewById(R.id.titleBar);

        signOut = findViewById(R.id.signout);

        fragTitle.setPaintFlags(fragTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/fontptsansbold.ttf");

        fragTitle.setTypeface(custom_font);

        final TextView hint = findViewById(R.id.hintText);


        Fragment fragment = new TrainingFragment();
        loadFragent(fragment);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId())
                {
                    case R.id.training_list:
                    {
                        fragment = new TrainingFragment();
                        loadFragent(fragment);
                        fragTitle.setText("Trainings Availible");
                        return true;
                    }
                    case R.id.done_list:
                    {
                        fragment = new RegisteredFragment();
                        loadFragent(fragment);
                        fragTitle.setText("Registered Trainings");
                        return true;
                    }
                }
                return false;
            }
        });

        if (showNav.equals("true"))
        {
            bottomNavigationView.setVisibility(View.VISIBLE);
            userAuth.setVisibility(View.INVISIBLE);
            signOut.setVisibility(View.VISIBLE);
            hint.setVisibility(View.INVISIBLE);
        }
        else
        {
            hint.setVisibility(View.VISIBLE);
        }



        userAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setVisibility(View.GONE);
                hint.setVisibility(View.INVISIBLE);
                Fragment fragment = new AuthFragment(MainActivity.this);
                loadFragent(fragment);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Are you sure you want to exit the app?");
                alert.setPositiveButton("Exit",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                clearCurrUser();
                                finishAffinity();
                            }
                        });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alert.show();

            }
        });


    }

    public void loadFragent(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


    private void getThestatus() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstTimeEntering = sharedPreferences.getBoolean("FIRSTENTRY", true);
    }

    //Loads the workshop
    private void LoadtheWorkshops() {
        if (firstTimeEntering) {
            //If the user is opeing the app for the first time feed the database with the workshops
            WorkshopDBHelper mWorkshopDbHelper = new WorkshopDBHelper(this);
            SQLiteDatabase mSqLiteDatabase = mWorkshopDbHelper.getWritableDatabase();
            ContentValues cv;
            for (String item : workshopNames) {
                cv = new ContentValues();
                cv.put(WorkshopContract.WorkshopEntry.WORKSHOP_NAME, item);
                cv.put(WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS, workshopDetails);
                mSqLiteDatabase.insert(WorkshopContract.WorkshopEntry.TABLE_NAME, null, cv);
            }
            //setting it to false again
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRSTENTRY", false);
            editor.commit();
        }
    }


    private void clearCurrUser()
    {
        UserDBHelper mUserDbHelper = new UserDBHelper(MainActivity.this);
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        try{
            mSqLiteDatabase.execSQL("UPDATE "+UserContract.UserEntry.CURR_USER_TABLE+" SET curr_user="+"'"+"none"+"' WHERE curr_id = '01'");
            Toast.makeText(MainActivity.this, "Session Cleared", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Problem in Clearing session", Toast.LENGTH_SHORT).show();
        }
    }
}


