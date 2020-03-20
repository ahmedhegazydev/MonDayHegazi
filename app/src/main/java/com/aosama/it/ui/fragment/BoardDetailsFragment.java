package com.aosama.it.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.BoardDataList;
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
    private BoardDataList boardDataList = new BoardDataList();
    private Gson gson = new Gson();
    private HAdapterUsers adapterUsers;
    private List<UserBoard> userBoards = new ArrayList<>();
    private BoardDetailViewModel boardDetailViewModel = null;
    private android.app.AlertDialog dialog = null;

    public static BoardDetailsFragment newInstance() {

        Bundle args = new Bundle();

        BoardDetailsFragment fragment = new BoardDetailsFragment();
        fragment.setArguments(args);
        return fragment;
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
        initializeTableView();

        showDialog();
        fetchingData();

        //setting an empty list to the adapter
        settingAdapter();


    }

    private void initializeTableView() {
        // Create TableView View model class  to group view models of TableView
        TableViewModel tableViewModel = new TableViewModel();

        // Create TableView Adapter
        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);

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
