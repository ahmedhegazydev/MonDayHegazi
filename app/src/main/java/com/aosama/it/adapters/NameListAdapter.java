package com.aosama.it.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aosama.it.R;

import java.util.ArrayList;

public class NameListAdapter extends ArrayAdapter<String>
        implements View.OnClickListener {

    private static final String TAG = "StatusListAdapter";
    Context mContext;
    private ArrayList<String> dataSet;

    public NameListAdapter(ArrayList<String> data, Context context) {
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
            viewHolder.tvName = convertView
                    .findViewById(R.id.tvName);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvName.setText(name);

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
    }
}
