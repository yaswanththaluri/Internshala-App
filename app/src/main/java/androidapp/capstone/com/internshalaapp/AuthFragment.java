package androidapp.capstone.com.internshalaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AuthFragment extends Fragment {

    private LinearLayout login, signup;
    private TextView change2login, change2signup;
    private Button submitSignIn, submitSignUp;
    private TextView emailIn, pswdIn, emailUp, pswdUp, name1Up, name2up;
    private UserDBHelper mUserDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private View currView;
    private Context actContext;
    private ImageView closeFrag;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUserDbHelper = new UserDBHelper(getContext());

    }

    public AuthFragment(Context context)
    {
        this.actContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment, container, false);

        currView = view;

        change2login = view.findViewById(R.id.changeToSignIn);
        change2signup = view.findViewById(R.id.changeToSignUp);

        login = view.findViewById(R.id.signinLay);
        signup = view.findViewById(R.id.signupLay);

        submitSignIn = view.findViewById(R.id.signinSubmit);
        submitSignUp = view.findViewById(R.id.signupSubmit);

        emailIn = view.findViewById(R.id.signinUseremail);
        pswdIn = view.findViewById(R.id.signinPswd);

        closeFrag = view.findViewById(R.id.closeAuthFragment);


        emailUp = view.findViewById(R.id.signupUseremail);
        pswdUp = view.findViewById(R.id.signupPswd);
        name1Up = view.findViewById(R.id.signupFirstName);
        name2up = view.findViewById(R.id.signupLastName);


        change2signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setVisibility(View.INVISIBLE);
                signup.setVisibility(View.VISIBLE);
            }
        });


        change2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
            }
        });


        submitSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailIn.getText().toString();
                String pswd = pswdIn.getText().toString();
                if (validateUser(mail, pswd))
                {
                    mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
                    mSqLiteDatabase.execSQL("UPDATE "+UserContract.UserEntry.CURR_USER_TABLE+" SET curr_user="+"'"+mail+"' WHERE curr_id = '01'");
                    Intent i = new Intent(getContext(), MainActivity.class);
                    i.putExtra("showNavs", "true");
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                }
            }
        });

        closeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });


        submitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailUp.getText().toString();
                String pswd = pswdUp.getText().toString();
                String fName = name1Up.getText().toString();
                String sName = name2up.getText().toString();
                createUser(email, pswd, fName, sName);
            }
        });


        return view;
    }


    private boolean validateUser(String email, String pswd)
    {
        if (!email.equals("") && !pswd.equals(""))
        {
            mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
            Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME,
                    new String[]{UserContract.UserEntry.USER_EMAIL, UserContract.UserEntry.USER_PASSWORD},
                    UserContract.UserEntry.USER_EMAIL + "=?",
                    new String[]{email}, null, null, null);
            if(cursor.getCount()<=0){
                Toast.makeText(currView.getContext(), "Please enter registered email id", Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int passwordColIndex = cursor.getColumnIndex(UserContract.UserEntry.USER_PASSWORD);
                    String password = cursor.getString(passwordColIndex);
                    if (password.equals(pswd)) {
                        Toast.makeText(currView.getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        Toast.makeText(currView.getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

            } else {
                Toast.makeText(currView.getContext(), "Invalid email Id", Toast.LENGTH_SHORT).show();
                return false;

            }
        }
        else {
            Toast.makeText(currView.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void createUser(String email, String pswd, String fName, String sName)
    {
        if (!email.equals("") && !pswd.equals("") && !fName.equals("") && !sName.equals(""))
        {
            mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
            try
            {
                mSqLiteDatabase.execSQL("INSERT INTO "+UserContract.UserEntry.TABLE_NAME
                        +" (email, password, firstName, lastName) values('"+email+"', '"+pswd+"', '"+fName+"', '"+sName+"');");
                mSqLiteDatabase.execSQL("INSERT INTO "+UserContract.UserEntry.CURR_USER_TABLE+" (curr_user, curr_id) values('"+email+"', '01');");
                Toast.makeText(currView.getContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(currView.getContext(), "Problem in Creating Account", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(currView.getContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
        }
    }
}
