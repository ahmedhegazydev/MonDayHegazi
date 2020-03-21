package com.aosama.it.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.utiles.MyUtilis;

import java.util.ArrayList;

public class NameListAdapter extends ArrayAdapter<String>
        implements View.OnClickListener {

    private static final String TAG = "StatusListAdapter";
    private Context mContext;
    private ArrayList<String> dataSet;
    private int indexCol = 0;

    public NameListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.row_item_name, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public NameListAdapter(ArrayList<String> data, Context context, int indexCol) {
        super(context, R.layout.row_item_name, data);
        this.dataSet = data;
        this.mContext = context;
        this.indexCol = indexCol;
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
            viewHolder.ivComment = convertView.findViewById(R.id.ivComment);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvName.setText(name);
        viewHolder.ivComment.setVisibility(View.GONE);

        switch (indexCol) {
            case 1:
                viewHolder.ivComment.setVisibility(View.VISIBLE);
                break;
            case 4:
                viewHolder.tvName.setText(MyUtilis.formateDate(name));
                break;
            case 5:
                viewHolder.tvName.setText(MyUtilis.formateDate(name));
                break;
            case 6:
                viewHolder.tvName.setText(MyUtilis.formateDate(name));
                break;
            default:
                viewHolder.ivComment.setVisibility(View.GONE);
                break;
        }
        viewHolder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Icon clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        ImageView ivComment;
    }
}
