package com.nec.staffApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.nec.staffApp.multichoice.SampleCustomViewAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    DBHelper1 db=null;
    ArrayList<Staff> staffsList = null;
    SearchView searchView;
    SampleCustomViewAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview);
        ButterKnife.bind(this);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        db= new DBHelper1(this);
        staffsList = db.getAllStaffList();
        setUpMultiChoiceRecyclerView();
        //initViews();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //adapter = new SampleCustomViewAdapter(staffsList);
        mRecyclerView.setAdapter(adapter);
    }

    private void setUpMultiChoiceRecyclerView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new SampleCustomViewAdapter(
                staffsList,
                this
        );
        //adapter.setMultiChoiceToolbar(multiChoiceToolbar);
        mRecyclerView.setAdapter(adapter);

    }
}
