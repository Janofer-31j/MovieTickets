package com.nec.staffApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nec.staffApp.multichoice.MultiChoiceAdapter;
import com.nec.staffApp.multichoice.SampleCustomViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    DBHelper1 db=null;
    ArrayList<Staff> staffsList = null;
    SearchView searchView;
    SampleCustomViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private Button btnSendSMS;

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
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> selectedList = adapter.getSelectedItemList();
                List<Staff> selectedStaff = getSelectedStaff(selectedList);
                Toast.makeText(SearchActivity.this, selectedStaff.size()+"", Toast.LENGTH_SHORT).show();

                if(selectedStaff != null && selectedStaff.size() > 0){
                    String numbers[] = new String[selectedStaff.size()];
                    for (int i = 0; i < selectedStaff.size(); i++) {
                        numbers[i] = selectedStaff.get(i).phno;
                    }
                    String num = "";
                    for (int i = 0; i < numbers.length; i++) {
                        num = num +";"+numbers[i];
                    }

                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // Android 4.4 and up
                    {
                        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());

                        intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(num)));
                        intent.putExtra("sms_body", "Hello");

                        if (defaultSmsPackageName != null) // Can be null in case that there is no default, then the user would be able to choose any app that supports this intent.
                        {
                            intent.setPackage(defaultSmsPackageName);
                        }
                    }
                    else
                    {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", num.toString());
                        intent.putExtra("sms_body", "hello");
                    }
                    startActivity(intent);
                }
            }
        });

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

    private List<Staff> getSelectedStaff(List<Integer> selectedList) {
        List<Staff> selectedStaff = new ArrayList<>();
        for (int i = 0; i < selectedList.size(); i++) {
            Staff staff = staffsList.get(selectedList.get(i));
            selectedStaff.add(staff);
        }
        return selectedStaff;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((MultiChoiceAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ((MultiChoiceAdapter) mRecyclerView.getAdapter()).onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //SearchListAdapter adapter = new SearchListAdapter(staffsList);
        //adapter = new SearchListAdapter(staffsList);
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
        mRecyclerView.setAdapter(adapter);
    }
}
