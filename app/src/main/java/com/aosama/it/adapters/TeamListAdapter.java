package com.aosama.it.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aosama.it.R;
import com.aosama.it.models.OverlapImageModel;
import com.mindinventory.overlaprecylcerview.listeners.OverlapRecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TeamListAdapter extends ArrayAdapter<String>
        implements View.OnClickListener, OverlapRecyclerViewClickListener {

    private static final String TAG = "StatusListAdapter";
    Context mContext;
    private ArrayList<String> dataSet;

    public TeamListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.row_item_name, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_name,
                    parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.rvTeamMembers = convertView.findViewById(R.id.rvTeamMembers);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvName.setText(name);


        TeamListAdapter adapter = new
                TeamListAdapter(dataSet, mContext);
//        RecyclerViewAdapter recyclerViewAdapter =
//                new RecyclerViewAdapter(10, (int) -12f);
//        viewHolder.rvTeamMembers.setAdapter(recyclerViewAdapter);
//        adapter.addAll(getDummyArrayList());

        // Return the completed view to render on screen
        return convertView;
    }

    private List<OverlapImageModel> getDummyArrayList() {
        List<OverlapImageModel> items = new ArrayList<>();


        items.add(new OverlapImageModel("https://miro.medium.com/max/5400/1*VcHVCyRSAOF3V6Ldi0iXOQ.jpeg"));
        items.add(new OverlapImageModel("https://i.ytimg.com/vi/n77quSYsyFA/maxresdefault.jpg"));
        items.add(new OverlapImageModel("https://i.ytimg.com/vi/n77quSYsyFA/maxresdefault.jpg"));
        items.add(new OverlapImageModel("https://i.ytimg.com/vi/n77quSYsyFA/maxresdefault.jpg"));
        items.add(new OverlapImageModel("https://i.ytimg.com/vi/n77quSYsyFA/maxresdefault.jpg"));
        items.add(new OverlapImageModel("https://i.ytimg.com/vi/n77quSYsyFA/maxresdefault.jpg"));
        return items;
    }


    @Override
    public void onNormalItemClicked(int i) {

    }

    @Override
    public void onNumberedItemClick(int i) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        RecyclerView rvTeamMembers;
    }
}
