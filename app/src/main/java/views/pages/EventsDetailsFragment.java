package views.pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;


public class EventsDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    LayoutInflater mInflater;
    public EventsDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;
        View myView  = inflater.inflate(R.layout.fragment_events_details, container, false);
        addEventsTiles(myView);
        return myView;
    }

    private void addEventsTiles(View pView) {
        GridLayout myGrid = pView.findViewById(R.id.eventsFragmentGrid);
        for (int i=0;i<10;i++) {
            View myView = mInflater.inflate(R.layout.fragment_event_tile, null);
            TextView myPeopleGoing = myView.findViewById(R.id.event_details_peopleGoing);
            TextView myEventName = myView.findViewById(R.id.event_details_holder_image_event_name);
            TextView myEventDesc = myView.findViewById(R.id.event_details_holder_image_event_desc);
            TextView myEventLocation = myView.findViewById(R.id.event_details_holder_viewMore);
            myPeopleGoing.setText(" "+i+" people going");
            myEventLocation.setText("Flat 402, Vishnu Sapphire, Kondapur, Hyderabad, 500084");
            myEventDesc.setText(" hello hello hello hello please join hellp join");
            myEventName.setText(" Test Event "+i);
            myGrid.addView(myView);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
