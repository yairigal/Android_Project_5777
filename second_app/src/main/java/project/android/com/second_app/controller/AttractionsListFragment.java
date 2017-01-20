package project.android.com.second_app.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import project.android.com.second_app.R;
import project.android.com.second_app.controller.dummy.DummyContent.DummyItem;
import project.android.com.second_app.model.backend.AttractionFilter;
import project.android.com.second_app.model.backend.Backend;
import project.android.com.second_app.model.backend.BackendFactory;
import project.android.com.second_app.model.backend.PublicObjects;
import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

import java.util.ArrayList;

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
    ArrayList<Attraction> beforeFilterList = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    BaseExpandableListAdapter adap;
    ExpandableListView listView;
    ProgressBar pBar;

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

    //region override functions
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attractionslist_list, container, false);
            pBar = (ProgressBar) view.findViewById(R.id.pBarAttractionFragment);
            listView = (ExpandableListView) view.findViewById(R.id.Attlist);
            adap = new BaseExpandableListAdapter() {
                @Override
                public int getGroupCount() {
                    return attractions.size();
                }

                @Override
                public int getChildrenCount(int groupPosition) {
                    return 8;
                }

                @Override
                public Object getGroup(int groupPosition) {
                    return attractions;
                }

                @Override
                public Object getChild(int groupPosition, int childPosition) {
                    Attraction count =  attractions.get(groupPosition);
                    switch (childPosition)
                    {
                        case 0:
                            return count.getAttractionName();
                        case 1:
                            return count.getType().toString();
                        case 2:
                            return count.getBusinessID();
                        case 3:
                            return count.getPrice()+" $";
                        case 4:
                            return count.getStartDate();
                        case 5:
                            return count.getEndDate();
                        case 6:
                            return count.getCountry();
                        case 7:
                            return count.getDescription();
                        default:
                            return count.getPrice()+" $";
                    }
                }

                @Override
                public long getGroupId(int groupPosition) {
                    return 0;
                }

                @Override
                public long getChildId(int groupPosition, int childPosition) {
                    return 0;
                }

                @Override
                public boolean hasStableIds() {
                    return false;
                }

                private String getTitle(int groupPosition, int childPosition) {
                    switch (childPosition)
                    {
                        case 0:
                            return "Name: ";//att name
                        case 1:
                            return "Deal: ";//type
                        case 2:
                            return "Owner: "; //Business name
                        case 3:
                            return "Price ";
                        case 4:
                            return "Starting Date ";
                        case 5:
                            return "Ending Date ";
                        case 6:
                            return "Country ";
                        case 7:
                            return "Description: ";
                        default:
                            return "Name: ";
                    }
                }

                @Override
                public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                    if(convertView == null){
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.parent_layout_att, parent,false);
                    }
                    Attraction current = attractions.get(groupPosition);

                    TextView country = (TextView) convertView.findViewById(R.id.Tvcountry);
                    TextView enddate = (TextView) convertView.findViewById(R.id.TvEndDate);

                    country.setText(current.getCountry());
                    enddate.setText(current.getEndDate().toString());

                    TextView parent_textview = (TextView) convertView.findViewById(R.id.parentTv);
                    parent_textview.setTypeface(null, Typeface.BOLD);
                    parent_textview.setText(current.getAttractionName());
                    ImageView img = (ImageView) convertView.findViewById(R.id.imageViewAtt);
                    TextView desc = (TextView)convertView.findViewById(R.id.TVdesc);
                    desc.setText(current.getDescription());
                    switch (current.getType()){
                        case Airline:
                            img.setImageResource(R.mipmap.airline_icon);
                            break;
                        case EntertainmentShow:
                            img.setImageResource(R.mipmap.enterintemnt_icon);
                            break;
                        case TravelAgency:
                            img.setImageResource(R.mipmap.travel_icon);
                            break;
                        case HotelDeal:
                            img.setImageResource(R.mipmap.hotel_icon);
                            break;
                        default:
                            break;
                    }
                    return convertView;
                }

                @Override
                public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                    if(convertView == null)
                    {
                        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflator.inflate(R.layout.fragment_attractionslist, parent,false);
                    }
                    if(childPosition == 6) // country
                    {
                        ImageView mapBtn = (ImageView)convertView.findViewById(R.id.imageButtonMap);
                        mapBtn.setVisibility(View.VISIBLE);
                        mapBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO goto map intent
                            }
                        });
                    }
                    TextView child_textview = (TextView) convertView.findViewById(R.id.content);
                    TextView title = (TextView) convertView.findViewById(R.id.id);
                    child_textview.setText((String) getChild(groupPosition,childPosition));
                    title.setText(getTitle(groupPosition,childPosition));
                    return convertView;
                }

                @Override
                public boolean isChildSelectable(int groupPosition, int childPosition) {
                    return false;
                }
            };
            //listView.setIndicatorBoundsRelative(listView.getWidth()-50, listView.getWidth());
            listView.setAdapter(adap);
            getAttractionListAsyncTask();
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
    //endregion

    //region other functions
    private void getAttractionListAsyncTask() {
        class myTask extends AsyncTask<Void,Void,Void> {
            ArrayList<Attraction> newList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(listView != null)
                    listView.setVisibility(View.GONE);
                if(pBar != null)
                    pBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Void... params) {
                Backend db = BackendFactory.getFactoryDatabase();
                newList = new ArrayList<>();
                newList = db.getAttractionList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(adap != null)
                    BusinessesListFragment.refreshAdapter(adap,attractions,newList);
                if(pBar != null)
                    pBar.setVisibility(View.GONE);
                if(listView != null)
                    listView.setVisibility(View.VISIBLE);
            }
        }
        myTask task = new myTask();
        task.execute();
    }

    public void updateView() {
        getAttractionListAsyncTask();
    }

    public void Filter(String s) {
        ArrayList list = new ArrayList();
        //saving current list
        beforeFilterList.clear();
        beforeFilterList.addAll(attractions);

        list.addAll(attractions);
        AttractionFilter filter = new AttractionFilter(s,list);
        ArrayList<Attraction> newList;
        try {
            newList = filter.Filter();
            BusinessesListFragment.refreshAdapter(adap,attractions,newList);
        } catch (Exception e) {
            Toast.makeText(getContext(),"Error Parsing Query", Toast.LENGTH_SHORT);
        }
    }

    public void clearFilter(){
        if(beforeFilterList.size() == 0)
            if(attractions.size() != 0)
                return;
        BusinessesListFragment.refreshAdapter(adap,attractions,beforeFilterList);
    }
    //endregion

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
