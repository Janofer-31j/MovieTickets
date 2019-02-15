package com.nec.staffApp;

/*

created by Janofer iBRahima on 14/02/2019
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Button btn= findViewById(R.id.btn_about);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                intent.putExtra("code", "JAV001");
                startActivity(intent);
            }
        });

        Button btn1= findViewById(R.id.btn_about1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                intent.putExtra("code", "JAN002");
                startActivity(intent);
            }
        });
        Button btn2= findViewById(R.id.btn_about2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                intent.putExtra("code", "PRA003");
                startActivity(intent);
            }
        });
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
}
