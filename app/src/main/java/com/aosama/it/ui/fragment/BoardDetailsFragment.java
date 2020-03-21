package com.aosama.it.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.adapters.NameListAdapter;
import com.aosama.it.adapters.StatusListAdapter;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.Status;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.nested.BoardData;
import com.aosama.it.tableview.TableViewAdapter;
import com.aosama.it.tableview.TableViewListener;
import com.aosama.it.tableview.TableViewModel;
import com.aosama.it.ui.adapter.board.HAdapterUsers;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.BoardDetailViewModel;
import com.evrencoskun.tableview.TableView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardDetailsFragment extends Fragment implements
        HAdapterUsers.OnUserClicked {

    private static final String TAG = "BoardDetailsFragment";
    @BindView(R.id.rvAllUseres)
    RecyclerView rvAllUsers;
    @BindView(R.id.tableView)
    TableView tableView;
    @BindView(R.id.tvTeamName)
    TextView tvTeamName;
    @BindView(R.id.llHeader)
    LinearLayout llHeader;
    @BindView(R.id.lvNames)
    ListView lvNames;
    @BindView(R.id.lvStatus)
    ListView lvStatus;
    @BindView(R.id.lvAddDate)
    ListView lvAddDate;
    @BindView(R.id.lvStartDate)
    ListView lvStartDate;
    @BindView(R.id.lvMeetingTime)
    ListView lvMeetingTime;
    @BindView(R.id.lvMeetingUrl)
    ListView lvMeetingUrl;
    @BindView(R.id.lvDueDate)
    ListView lvDueDate;
    @BindView(R.id.lvTeam)
    ListView lvTeam;
    @BindView(R.id.hScrollView)
    HorizontalScrollView horizontalScrollView;


    private BoardDataList boardDataList = new BoardDataList();
    private Gson gson = new Gson();
    private HAdapterUsers adapterUsers;
    private List<UserBoard> userBoards = new ArrayList<>();
    private BoardDetailViewModel boardDetailViewModel = null;
    private android.app.AlertDialog dialog = null;
    private String[] titlesColumns;

    public static BoardDetailsFragment newInstance() {

        Bundle args = new Bundle();

        BoardDetailsFragment fragment = new BoardDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String[] getStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String[] str = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_board_item_details, null);
        //ButterKnife.bind(this, viewRoot);
        ButterKnife.bind(BoardDetailsFragment.this, viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        gettingThePassedBoardModel();
//        initializeTableView();

        showDialog();
        fetchingData();

        //setting an empty list to the adapter
        settingAdapter();

//        settingUpTableView();


    }

    private void settingUpTableView() {

        for (int i = 0; i < titlesColumns.length; i++) {
            View v =
                    LayoutInflater.from(getActivity())
                            .inflate(R.layout.view_col_header, null);
            TextView textView = v.findViewById(R.id.tvHeaderColTitle);
            if (textView.getParent() != null) {
                ((ViewGroup) textView.getParent()).removeView(textView); // <- fix
            }
            textView.setText(titlesColumns[i]);
            llHeader.addView(textView);
        }


    }

    private void initializeTableView() {

        // Create TableView View model class  to group view models of TableView
        TableViewModel tableViewModel = new TableViewModel(titlesColumns);

        // Create TableView Adapter
        TableViewAdapter tableViewAdapter = new
                TableViewAdapter(tableViewModel);

        tableView.setAdapter(tableViewAdapter);
        tableView.setTableViewListener(new TableViewListener(tableView));

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(mTableView);

        // Load the dummy data to the TableView
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
                .getRowHeaderList(), tableViewModel.getCellList());

        //mTableView.setHasFixedWidth(true);

        /*for (int i = 0; i < mTableViewModel.getCellList().size(); i++) {
            mTableView.setColumnWidth(i, 200);
        }*)
        //mTableView.setColumnWidth(0, -2);
        //mTableView.setColumnWidth(1, -2);
        /*mTableView.setColumnWidth(2, 200);
        mTableView.setColumnWidth(3, 300);
        mTableView.setColumnWidth(4, 400);
        mTableView.setColumnWidth(5, 500);*/

    }

    private void settingAdapter() {
        if (!boardDataList.getNestedBoard().isEmpty()) {
//            adapterUsers = new HAdapterUsers(getActivity(),
//                    boardDataList.getNestedBoard()
//                            .get(0).getUsers(), this);
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        adapterUsers = new HAdapterUsers(getActivity(),
                userBoards,
                this);
        rvAllUsers.setAdapter(adapterUsers);
        adapterUsers.notifyDataSetChanged();
    }

    private void fetchingData() {
        String id = boardDataList.getNestedBoard().get(0).getId();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e(TAG, "fetchingData: " + id);


        id = "BOR8493277862";
        String url = MyConfig.NESTED + "?id=" + id;

        boardDetailViewModel.getBoardDetails(url, params)
                .observe(this,
                        basicResponseStateData -> {
                            dialog.dismiss();
                            switch (basicResponseStateData.getStatus()) {
                                case SUCCESS:
                                    if (basicResponseStateData.getData() != null) {
                                        fillingViewsWithData(basicResponseStateData.getData());
                                    }
                                    Log.e(TAG, "fetchingData: success");
                                    break;
                                case FAIL:
                                    Log.e(TAG, "fetchingData: failed");
                                    Toast.makeText(getActivity(),
                                            basicResponseStateData.getErrorsMessages()
                                                    != null ?
                                                    basicResponseStateData.getErrorsMessages()
                                                            .getErrorMessages().get(0) : null,
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR:
                                    Log.e(TAG, "fetchingData: error");
                                    if (basicResponseStateData.getError() != null) {
                                        Toast.makeText(getActivity(),
                                                getString(R.string.
                                                        no_connection_msg), Toast.LENGTH_LONG).show();
                                        Log.v("Statues", "Error" + basicResponseStateData
                                                .getError().getMessage());
                                    }
                                    break;
                                case CATCH:
                                    Toast.makeText(getActivity(),
                                            getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        });

    }

    private NameListAdapter nameListAdapter;

    private void fillingViewsWithData(BasicResponse<BoardData> data) {
        userBoards = data.getData()
                .getBoardData()
                .getNestedBoards()
                .get(0)
                .getUsers();
        refreshAdapter();

        //----------------
        tvTeamName.setText(data.getData().getBoardData()
                .getNestedBoards().get(0).getTeam().getTeamName());


        //----------------------
        List<TaskE> taskES
                = data.getData().getBoardData().getNestedBoards()
                .get(0)
                .getTasksGroup()
                .get(0)
                .getTasks();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Status> status = new ArrayList<>();
        ArrayList<String> meetingUrl = new ArrayList<>();
        ArrayList<String> meetingTime = new ArrayList<>();
        ArrayList<String> dateStart = new ArrayList<>();
        ArrayList<String> dateDue = new ArrayList<>();
        ArrayList<String> dateAdd = new ArrayList<>();
        for (int i = 0; i < taskES.size(); i++) {
            names.add(taskES.get(i).getName());
            dateAdd.add(taskES.get(i).getAddDate());
            dateDue.add(taskES.get(i).getDueDate());
            dateStart.add(taskES.get(i).getStartDate());
            status.add(taskES.get(i).getStatus());
        }

        nameListAdapter =
                new NameListAdapter(dateDue, getActivity());
        lvDueDate.setAdapter(nameListAdapter);
        setListViewHeightBasedOnChildren(lvDueDate);
        //-------------------------
        nameListAdapter =
                new NameListAdapter(dateStart, getActivity());
        lvStartDate.setAdapter(nameListAdapter);
        setListViewHeightBasedOnChildren(lvStartDate);
        //-------------------------
        nameListAdapter =
                new NameListAdapter(dateAdd, getActivity());
        lvAddDate.setAdapter(nameListAdapter);
        setListViewHeightBasedOnChildren(lvAddDate);
        //---------------------
        StatusListAdapter statusListAdapter = new
                StatusListAdapter(status, getActivity());
        lvStatus.setAdapter(statusListAdapter);
        statusListAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lvStatus);
        //-------------------------
        nameListAdapter =
                new NameListAdapter(names, getActivity(), 1);
        lvNames.setAdapter(nameListAdapter);
        setListViewHeightBasedOnChildren(lvNames);
    }

    private void gettingThePassedBoardModel() {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.SELECTED_BORAD)) {
                boardDataList = gson.fromJson(
                        getArguments().getString(Constants.SELECTED_BORAD), BoardDataList.class);
            }
        }
    }

    private void init() {
        boardDetailViewModel = ViewModelProviders.of(getActivity())
                .get(BoardDetailViewModel.class);


        rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvAllUsers.setHasFixedSize(false);


        //columns titles
        titlesColumns
                = getResources().getStringArray(R.array.array_columns);

    }

    private void showDialog() {
        dialog = MyUtilis.myDialog(getActivity());
        dialog.show();
    }

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {

        new AlertDialog.Builder(getActivity())
                .setTitle(userBoard.getShortName())
                .setMessage(userBoard.getFullName())
                .setCancelable(true)
                .show();

    }
}
