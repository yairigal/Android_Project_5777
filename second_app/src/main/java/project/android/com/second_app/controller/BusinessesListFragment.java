package project.android.com.second_app.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import project.android.com.second_app.R;
import project.android.com.second_app.controller.dummy.DummyContent.DummyItem;
import project.android.com.second_app.model.backend.AttractionFilter;
import project.android.com.second_app.model.backend.Backend;
import project.android.com.second_app.model.backend.BackendFactory;
import project.android.com.second_app.model.backend.BusinessFilter;
import project.android.com.second_app.model.backend.PublicObjects;
import project.android.com.second_app.model.backend.StaticDeclarations;
import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BusinessesListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Business> businessList = new ArrayList<>();
    private ArrayList<Business> beforeFilterList = new ArrayList<>();
    Backend db = BackendFactory.getFactoryDatabase();
    private boolean noDataRecieved = true;
    private boolean showingLoadingScreen = false;
    MyBusinessesListFragmentRecyclerViewAdapter adap = null;
    BaseExpandableListAdapter adp = null;
    ExpandableListView listView;
    ProgressBar pBar;
    View RootView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BusinessesListFragment() {
    }

    @SuppressWarnings("unused")
    public static BusinessesListFragment newInstance(int columnCount) {
        BusinessesListFragment fragment = new BusinessesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_businesseslistfragment_list, container, false);
        pBar = (ProgressBar) view.findViewById(R.id.pBarBusinessFragment);
        listView = (ExpandableListView) view.findViewById(R.id.Busslist);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Business current = businessList.get(groupPosition);
                switch (childPosition){
                    case 1: // maps
                        MapsIntent(getActivity(),current.getBusinessAddress().getCity(),current.getBusinessAddress().getStreet());
                        break;
                    case 2: // email
                        emailIntent(getActivity(),current.getEmail());
                        break;
                    case 3: // website
                        WebsiteIntet(getActivity(),current.getWebsite());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        adp = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return businessList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 4;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return businessList;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                Business count = businessList.get(groupPosition);
                switch (childPosition) {
                    case 0:
                        return count.getBusinessName();
                    case 1:
                        return count.getBusinessAddress().toString();
                    case 2:
                        return count.getEmail();
                    case 3:
                        return count.getWebsite();
                    default:
                        return count.getBusinessName();
                }
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            private String getTitle(int groupPosition, int childPosition) {
                Business count = businessList.get(groupPosition);
                switch (childPosition) {
                    case 0:
                        return "Name: ";
                    case 1:
                        return "Country: ";
                    case 2:
                        return "Email: ";
                    case 3:
                        return "Website: ";
                    default:
                        return "Name: ";
                }
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.parent_layout, parent, false);
                }
                TextView email = (TextView) convertView.findViewById(R.id.TVemail);
                email.setText(businessList.get(groupPosition).getEmail());
                TextView country = (TextView) convertView.findViewById(R.id.TVaddress);
                country.setText(businessList.get(groupPosition).getBusinessAddress().toString());
                TextView parent_textview = (TextView) convertView.findViewById(R.id.parentTv);
                parent_textview.setTypeface(null, Typeface.BOLD);
                parent_textview.setText(businessList.get(groupPosition).getBusinessName());
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflator.inflate(R.layout.fragment_businesseslistfragment, parent, false);
                }
                //ImageView btn = (ImageView) convertView.findViewById(R.id.imageButtonMap);
                TextView child_textview = (TextView) convertView.findViewById(R.id.content);

                TextView title = (TextView) convertView.findViewById(R.id.id);
                child_textview.setText((String) getChild(groupPosition, childPosition));
                title.setText(getTitle(groupPosition, childPosition));


                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public int getChildTypeCount() {
                return 4;
            }
        };
        listView.setAdapter(adp);
        getListAsyncTask();
        return view;
    }

    private void dismissLoadingScreen() {
        if (showingLoadingScreen)
            StaticDeclarations.hideLoadingScreen();
    }

    private boolean loadingScreenIsShown() {
        return showingLoadingScreen;
    }

    private void keepShowingLoadingScreen() {
        if (!showingLoadingScreen)
            StaticDeclarations.showLoadingScreen(getContext(), "Loading");
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

    public void updateView() {
        getListAsyncTask();
    }

    public void Filter(String s) {
        ArrayList list = new ArrayList();
        //saving current list
        beforeFilterList.clear();
        beforeFilterList.addAll(businessList);

        list.addAll(businessList);
        BusinessFilter filter = new BusinessFilter(s, list);
        ArrayList<Business> newList;
        try {
            newList = filter.Filter();
            BusinessesListFragment.refreshAdapter(adp, businessList, newList);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Parsing Query", Toast.LENGTH_SHORT);
        }
    }

    public void clearFilter() {
        if (beforeFilterList.size() == 0)
            if (businessList.size() != 0)
                return;
        refreshAdapter(adp, businessList, beforeFilterList);
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

    private void getListAsyncTask() {
        class myTask extends AsyncTask<Void, Void, Void> {
            ArrayList<Business> newList = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(listView!= null)
                    listView.setVisibility(View.GONE);
                if(pBar != null)
                    pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                newList = db.getBusinessList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (pBar != null)
                    pBar.setVisibility(View.GONE);
                if (listView != null)
                    listView.setVisibility(View.VISIBLE);
                if (adp != null)
                    refreshAdapter(adp, businessList, newList);
            }
        }
        myTask task = new myTask();
        task.execute();
    }

    public static void refreshAdapter(BaseExpandableListAdapter ad, ArrayList originList, ArrayList newList) {
        originList.clear();
        originList.addAll(newList);
        ad.notifyDataSetChanged();
    }

    public static void WebsiteIntet(Activity current,String website){
        current.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +website)));
    }
    public static void MapsIntent(Activity curr,String city,String street){
        Intent Chooser;
        String url = "http://maps.google.com/maps?daddr="+city +" "+street;
        Intent iintent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
        Chooser = Intent.createChooser(iintent,"Launch Maps");
        curr.startActivity(Chooser);
    }
    public static void emailIntent(Activity curr,String email){
        if(!email.matches(".+@.+[.]com"))
            if(!email.contains("@"))
                 email += "@gmail.com";
            else
                email += "gmail.com";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        curr.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
