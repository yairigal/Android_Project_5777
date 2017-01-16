package project.android.com.second_app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.android.com.second_app.R;
import project.android.com.second_app.controller.dummy.DummyContent;
import project.android.com.second_app.controller.dummy.DummyContent.DummyItem;
import project.android.com.second_app.model.backend.Backend;
import project.android.com.second_app.model.backend.BackendFactory;
import project.android.com.second_app.model.entities.Attraction;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AttractionsListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ArrayList<Attraction> attractions = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    MyAttractionsListRecyclerViewAdapter adap;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttractionsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AttractionsListFragment newInstance(int columnCount) {
        AttractionsListFragment fragment = new AttractionsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAttractionListAsyncTask();
/*        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }*/
    }

    private void getAttractionListAsyncTask() {
        class myTask extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... params) {
                Backend db = BackendFactory.getFactoryDatabase();
                attractions = db.getAttractionList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(adap != null)
                    adap.notifyDataSetChanged();
            }
        }
        myTask task = new myTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attractionslist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adap = new MyAttractionsListRecyclerViewAdapter(attractions, mListener);
            recyclerView.setAdapter(adap);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
