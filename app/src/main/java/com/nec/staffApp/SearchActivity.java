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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nec.staffApp.multichoice.MultiChoiceAdapter;
import com.nec.staffApp.multichoice.SampleCustomViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    DBHelper1 db=null;
    ArrayList<Staff> staffsList = null;
    SearchView searchView;
    SampleCustomViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private Button btnSendSMS,btnSendmail;
    private List<Staff> tempList = new ArrayList<>();
    private List<Staff> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview);
        //ButterKnife.bind(this);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        db= new DBHelper1(this);
        staffsList = db.getAllStaffList();
        tempList.addAll(staffsList);
        setUpMultiChoiceRecyclerView();
        //initViews();
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Staff> selectedStaff = getSelectedItemList();
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
//                    //For Mail with multi Email-ID
//                    String[] recipients = new String[selectedStaff.size()];
//                    for(int i=0;i<selectedStaff.size();i++){
//                        String mailed_name = selectedStaff.get(i).mail.trim().toString();
//                        Log.i("EmailIDs",":"+mailed_name);
//                        recipients[i]=mailed_name;
//                    }
//                    mailSend(recipients);

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
        btnSendmail= (Button) findViewById(R.id.btnSendmail);
        btnSendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Staff> selectedStaff =getSelectedItemList();
                Toast.makeText(SearchActivity.this, selectedStaff.size() + "", Toast.LENGTH_SHORT).show();

                if (selectedStaff != null && selectedStaff.size() > 0) {
                    String numbers[] = new String[selectedStaff.size()];
                    for (int i = 0; i < selectedStaff.size(); i++) {
                        numbers[i] = selectedStaff.get(i).phno;
                    }
                    List<String> recipients = new ArrayList<>();
                    for (int i = 0; i < selectedStaff.size(); i++) {
                        String mailed_name = selectedStaff.get(i).mail.trim();
                        Log.i("EmailIDs", ":" + mailed_name);
                        if(!mailed_name.equals("-")){
                            recipients.add(mailed_name);
                        }
                    }
                    String[] toList = new String[recipients.size()];
                    mailSend(recipients.toArray(toList));


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
               synchronized (this){
                  if(!TextUtils.isEmpty(s)){
                      searchList.clear();
                      for (Staff searchedStaff : tempList) {

                          if ((!TextUtils.isEmpty(searchedStaff.name)) && searchedStaff.name.toLowerCase().contains(s) ||
                                  (!TextUtils.isEmpty(searchedStaff.dept)) && searchedStaff.dept.toLowerCase().contains(s) ||
                                  (!TextUtils.isEmpty(searchedStaff.dsn)) && searchedStaff.dsn.toLowerCase().contains(s)) {
                              searchList.add(searchedStaff);
                          }
                      }
                      adapter.updateStaffList(searchList);
                  }
                  else {
                      adapter.updateStaffList(tempList);
                  }

               }
                // adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private List<Staff> getSelectedItemList(){
        List<Staff> list = new ArrayList<>();
        for(Staff staff : tempList){
            if(staff.selected){
                list.add(staff);
            }
        }
        return list;
    }
    public void mailSend(String[] recipients){
        try{
                Intent email = new Intent(Intent.ACTION_SEND);//, Uri.parse("mailto:"));

    // prompts email clients only
        email.setType("message/rfc822");
    //String,String[]
        email.putExtra(Intent.EXTRA_EMAIL,recipients);
//                             email.putExtra(Intent.EXTRA_EMAIL, "mail@mail.com");
        email.putExtra(Intent.EXTRA_SUBJECT, "Send mail");
        email.putExtra(Intent.EXTRA_TEXT, "Hi....");


        // the user can choose the email client
        //startActivity(email);
        startActivity(Intent.createChooser(email, "Choose an email client from..."));
    } catch (android.content.ActivityNotFoundException ex) {
        Toast.makeText(SearchActivity.this, "No email client installed.",
                Toast.LENGTH_LONG).show();
    }
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
                this ,mRecyclerView
        );
        mRecyclerView.setAdapter(adapter);
    }
}
