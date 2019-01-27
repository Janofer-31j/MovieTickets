package info.androidhive.nec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Main2Activity extends AppCompatActivity {

    GridView androidGridView;
    String[] gridViewdeptString = {
            "ADMINISTRATIVE OFFICE","COMPUTER SCIENCE AND ENGINEERING", "MECHANICAL ENGINEERING ", "ELECTRICAL AND ELECTRONICS ENGINEERING", "ELECTRONICS AND INSTRUMENTATION ENGINEERING", "ELECTRONICS AND COMMUNICATION ENGINEERING ", "INFORMATION TECHNOLOGY ",
            "CIVIL ENGINEERING ", "SCIENCE AND HUMANTIES","HUMANITIES","ACADAMIC DEAN","CONTROLLER OF EXAMINATION","TCP","ALUMUNI","PHYSICAL EDUCATION","LIBRARY","TRANSPORT","ESTATE OFFICE","SECURITY","SWEEPER AND GARDNERS"

    } ;

    String[] gridViewString = {
            "Administration","CSE", "MECH", "EEE", "EIE", "ECE", "IT",
            "CIVIL", "S&H","Humanities","Academic","COE","TCP","Alumuni","Physical Education","Library","Transport","Estate Office","Security","Sweeper & Gardener"

    } ;
    int[] gridViewImageId = {
            R.drawable.admin,R.drawable.cse, R.drawable.mech, R.drawable.eee, R.drawable.eie, R.drawable.ece, R.drawable.it,
            R.drawable.civil, R.drawable.sh,R.drawable.ht,R.drawable.ad,R.drawable.coe,R.drawable.tcp,R.drawable.alm,R.drawable.ped,R.drawable.lib,R.drawable.trans,R.drawable.eo,R.drawable.sec,R.drawable.sg
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        File database=getApplicationContext().getDatabasePath(DBHelper1.DATABASE_NAME);
        if(false==database.exists())
        {
            DBHelper1  mDBHelper=new DBHelper1(this);
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)){
                Toast.makeText(this,"Copy Dtabase Success",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Copy Dtabase failure",Toast.LENGTH_SHORT).show();
            }
        }

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(Main2Activity.this, gridViewString, gridViewImageId);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {


                Intent intent = new Intent(Main2Activity.this,Listall.class);
                intent.putExtra("dept",gridViewdeptString[+i]);
                startActivity(intent);
            }
        });

    }


    private boolean copyDatabase(Context context){
        try{
            InputStream inputStream=context.getAssets().open(DBHelper1.DATABASE_NAME);
            String outFileName=DBHelper1.DBLOCATION + DBHelper1.DATABASE_NAME;
            OutputStream outputStream=new FileOutputStream(outFileName);
            byte[] buff=new byte[1024];
            int length=0;
            while((length=inputStream.read(buff))> 0){
                outputStream.write(buff,0,length);

            }
            outputStream.flush();
            outputStream.close();
            Log.v("Mainactivity ","DB Copied");
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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