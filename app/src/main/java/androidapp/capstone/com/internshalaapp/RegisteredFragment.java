package androidapp.capstone.com.internshalaapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RegisteredFragment extends Fragment {



    private ListView listView;
    private ArrayList<String> appliedworksops;
    private ImageView nores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.registered_fragment, container, false);

        listView = view.findViewById(R.id.registered_list);
        appliedworksops = new ArrayList<>();
        nores = view.findViewById(R.id.noresults);

        setUpData();

        if (appliedworksops.size() > 0) {
            nores.setVisibility(View.INVISIBLE);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.registered_item, R.id.courseRegistered, appliedworksops);
            listView.setAdapter(arrayAdapter);
        } else {
            nores.setVisibility(View.VISIBLE);
        }


        return view;
    }

    private void setUpData() {
        UserDBHelper mUserDbHelper = new UserDBHelper(getContext());
        SQLiteDatabase mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        String projection[] = {
                UserContract.UserEntry.USER_FIRST_NAME,
                UserContract.UserEntry.USER_LAST_NAME,
                UserContract.UserEntry.WORKSHOP_ANDROID,
                UserContract.UserEntry.WORKSHOP_Artificial_Intelligence,
                UserContract.UserEntry.WORKSHOP_BigData_Bootcamp,
                UserContract.UserEntry.WORKSHOP_BlockChain_Technology,
                UserContract.UserEntry.WORKSHOP_Cyber_Security,
                UserContract.UserEntry.WORKSHOP_CloudComputing,
                UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp,
                UserContract.UserEntry.WORKSHOP_IoT,
                UserContract.UserEntry.WORKSHOP_Machine_Learning,
                UserContract.UserEntry.WORKSHOP_SAP_Bootcamp,
                UserContract.UserEntry.WORKSHOP_Web_Development
        };
        Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME, projection,
                UserContract.UserEntry.USER_EMAIL + "=?",
                new String[]{getEmail()}, null, null, null);

        if (cursor.moveToFirst()) {
            int w1colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_ANDROID);
            int w2colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Artificial_Intelligence);
            int w3colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_BigData_Bootcamp);
            int w4colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_BlockChain_Technology);
            int w5colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Cyber_Security);
            int w6colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_CloudComputing);
            int w7colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Datascience_Bootcamp);
            int w8colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_IoT);
            int w9colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Machine_Learning);
            int w10colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_SAP_Bootcamp);
            int w11colIndex = cursor.getColumnIndex(UserContract.UserEntry.WORKSHOP_Web_Development);
            int colarray[] = {w1colIndex, w2colIndex, w3colIndex, w4colIndex, w5colIndex, w6colIndex, w7colIndex, w8colIndex, w9colIndex, w10colIndex, w11colIndex};

            int sum = 0;
            for (int i : colarray) {
                int val = cursor.getInt(i);
                if (val == 1) {
                    String str = cursor.getColumnName(i);
                    appliedworksops.add(str);
                }
                sum = sum + val;
            }
//            countView.setText(String.valueOf(sum));
            int fnameIndex = cursor.getColumnIndex(UserContract.UserEntry.USER_FIRST_NAME);
            int lnameIndex = cursor.getColumnIndex(UserContract.UserEntry.USER_LAST_NAME);
            String name = cursor.getString(fnameIndex) + " " + cursor.getString(lnameIndex);
//            userNameView.setText(name);
        }
    }

        public String getEmail()
        {
            String email = "";
            UserDBHelper mUserDbHelper = new UserDBHelper(getContext());
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
}
