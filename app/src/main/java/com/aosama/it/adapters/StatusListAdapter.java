package com.aosama.it.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.Status;
import com.aosama.it.utiles.MyUtilis;

import java.util.ArrayList;

public class StatusListAdapter extends ArrayAdapter<Status>
        implements View.OnClickListener {

    private static final String TAG = "StatusListAdapter";
    Context mContext;
    private ArrayList<Status> dataSet;

    public StatusListAdapter(ArrayList<Status> data, Context context) {
        super(context, R.layout.row_item_status, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Status status = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_status,
                    parent, false);
            viewHolder.tvStatus = convertView
                    .findViewById(R.id.tvStaus);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvStatus.setText(status.getName());
//        viewHolder.tvStatus.setBackgroundColor(Color.parseColor(status.getColor()));
        MyUtilis.changeColor(mContext, viewHolder.tvStatus,
                Color.parseColor(status.getColor()
                ));
        Log.e(TAG, "getView: " + status.getColor());
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvStatus;
    }
}
