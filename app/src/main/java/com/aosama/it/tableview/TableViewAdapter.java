/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.aosama.it.tableview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aosama.it.R;
import com.aosama.it.tableview.holder.CellViewHolder;
import com.aosama.it.tableview.holder.ColumnHeaderViewHolder;
import com.aosama.it.tableview.holder.GenderCellViewHolder;
import com.aosama.it.tableview.holder.MoodCellViewHolder;
import com.aosama.it.tableview.holder.RowHeaderViewHolder;
import com.aosama.it.tableview.model.Cell;
import com.aosama.it.tableview.model.ColumnHeader;
import com.aosama.it.tableview.model.RowHeader;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.sort.SortState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader,
        RowHeader, Cell> {

    // Cell View Types by Column Position
    private static final int MOOD_CELL_TYPE = 1;
    private static final int GENDER_CELL_TYPE = 2;
    // add new one if it necessary..

    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    @NonNull
    private TableViewModel mTableViewModel;

    public TableViewAdapter(@NonNull TableViewModel tableViewModel) {
        super();
        this.mTableViewModel = tableViewModel;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO check
        Log.e(LOG_TAG, " onCreateCellViewHolder has been called");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout;

        switch (viewType) {
            case MOOD_CELL_TYPE:
                // Get image cell layout which has ImageView on the base instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new MoodCellViewHolder(layout);
            case GENDER_CELL_TYPE:
                // Get image cell layout which has ImageView instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new GenderCellViewHolder(layout);
            default:
                // For cells that display a text
                layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false);

                // Create a Cell ViewHolder
                return new CellViewHolder(layout);
        }
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell cellItemModel, int
            columnPosition, int rowPosition) {

        switch (holder.getItemViewType()) {
            case MOOD_CELL_TYPE:
                MoodCellViewHolder moodViewHolder = (MoodCellViewHolder) holder;

                moodViewHolder.cell_image.setImageResource(mTableViewModel.getDrawable((int) cellItemModel
                        .getData(), false));
                break;
            case GENDER_CELL_TYPE:
                GenderCellViewHolder genderViewHolder = (GenderCellViewHolder) holder;

                genderViewHolder.cell_image.setImageResource(mTableViewModel.getDrawable((int)
                        cellItemModel.getData(), true));
                break;
            default:
                // Get the holder to update cell item text
                CellViewHolder viewHolder = (CellViewHolder) holder;
                viewHolder.setCell(cellItemModel);
                break;
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: check
        //Log.e(LOG_TAG, " onCreateColumnHeaderViewHolder has been called");
        // Get Column Header xml Layout
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_column_header_layout, parent, false);

        // Create a ColumnHeader ViewHolder
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable ColumnHeader
            columnHeaderItemModel, int columnPosition) {

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel);
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get Row Header xml Layout
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel,
                                          int rowPosition) {

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(rowHeaderItemModel.getData()));
    }
    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        // Get Corner xml layout
        View corner = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_corner_layout, parent, false);
        corner.setOnClickListener(view -> {
            SortState sortState = TableViewAdapter.this.getTableView()
                    .getRowHeaderSortingStatus();
            if (sortState != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.ASCENDING);
            } else {
                Log.d("TableViewAdapter", "Order Descending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.DESCENDING);
            }
        });
        return corner;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int column) {

        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        switch (column) {
            case TableViewModel.MOOD_COLUMN_INDEX:
                return MOOD_CELL_TYPE;
            case TableViewModel.GENDER_COLUMN_INDEX:
                return GENDER_CELL_TYPE;
            default:
                // Default view type
                return 0;
        }
    }
}
