package views.pages;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class GroupMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Long groupId;

    public GroupMessagesAdapter(Long pGroupId) {
        groupId = pGroupId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int pPosition) {
        return -1;
    }
}
