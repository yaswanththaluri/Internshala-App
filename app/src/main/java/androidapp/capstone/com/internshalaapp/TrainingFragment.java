package androidapp.capstone.com.internshalaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class TrainingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int WORKSHOP_LOADER = 101;
    ListView mWorkshopListView;
    //Adapter
    WorkshopAdapter mAdapter;
    // WorkshopDbHelper reference variable
    WorkshopDBHelper mWorkshopDbHelper;

    //Context reference variable
    Context context;

    //Workshop items
    String workshopNames[] = {
            "Android", "Artificial Intelligence", "BigData Bootcamp", "BlockChain Technology", "Cyber Security", "CloudComputing", "Datascience Bootcamp", "Iot", "Machine Learning", "SAP Bootcamp", "Web Development"
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mWorkshopDbHelper = new WorkshopDBHelper(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.training_fragment, container, false);


        mWorkshopListView = (ListView) view.findViewById(R.id.training_list);
        mAdapter = new WorkshopAdapter(context, null);
        mWorkshopListView.setAdapter(mAdapter);

        //Kicking off the loader
        getLoaderManager().initLoader(WORKSHOP_LOADER, null, this);

        return view;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define the string projection first
        String projection[] = {
                WorkshopContract.WorkshopEntry._ID,
                WorkshopContract.WorkshopEntry.WORKSHOP_NAME,
                WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS
        };
        // This loader will execute the ContentProvider's query method on a background thread
        Log.i("the old",""+ WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI);
        return new CursorLoader(context,//Context
                WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI,
                projection,
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //It will load the data of the adapter since we passed empty args into workshopadapter
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //resetting the recent cursor
        mAdapter.swapCursor(null);
    }
}
