package com.nec.staffApp;

/**
 * Created by janofer ibrahima.j on 11/02/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Main3Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearch;
    private Button selectStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        selectStaff = (Button) findViewById(R.id.btn_one);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);
        selectStaff.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		   /* mPaint.setXfermode(null);
		    mPaint.setAlpha(0xFF);*/

        switch (item.getItemId()) {
            case R.id.action_settings:
                //Do Some Task
                Intent intent = new Intent(getApplicationContext(),Main4Activity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view == btnSearch){
            Intent intent = new Intent(this , SearchActivity.class);
            startActivity(intent);
        }else if(view == selectStaff){
            Intent i = new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(i);
        }
    }
}
