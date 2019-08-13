package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import com.meetmyage.com.meetmyageapp.R;

import views.pages.GroupEventCardContent;
import views.pages.GroupEventsFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link views.pages.GroupEventCardContent.GroupEventCardItem} and makes a call to the
 * specified {@link GroupEventsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupEventsRecyclerViewAdapter extends RecyclerView.Adapter<GroupEventsRecyclerViewAdapter.ViewHolder> {

    private final List<GroupEventCardContent.GroupEventCardItem> mValues;
    private final GroupEventsFragment.OnListFragmentInteractionListener mListener;

    public GroupEventsRecyclerViewAdapter(List<GroupEventCardContent.GroupEventCardItem> items, GroupEventsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groupevents, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mNameView.setText(mValues.get(position).eventName);
        holder.mDetailView.setText(mValues.get(position).eventDetails);
        holder.mDateView.setText(mValues.get(position).eventDate);
        holder.mTimeView.setText(mValues.get(position).eventTime);
        holder.mPriceView.setText(mValues.get(position).eventPrice);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mDetailView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final TextView mPriceView;

        public GroupEventCardContent.GroupEventCardItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.name);
            mDetailView = (TextView) view.findViewById(R.id.details);
            mDateView = (TextView) view.findViewById(R.id.date);
            mTimeView = (TextView) view.findViewById(R.id.time);
            mPriceView = (TextView) view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDetailView.getText() + "'";
        }
    }
}
