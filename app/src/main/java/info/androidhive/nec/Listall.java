package info.androidhive.nec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listall extends AppCompatActivity {

    MyCustomAdapter myCustomAdapter=null;
    ListView listView=null;
    DBHelper1 db=null;
    ArrayList<Staff> staff=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listall);
        String dept = getIntent().getExtras().getString("dept");


        db= new DBHelper1(this);
        staff=db.getUsers(dept);
        myCustomAdapter= new MyCustomAdapter(this,R.layout.staff_details,staff);

        listView = (ListView) findViewById(R.id.simpleListView);
        listView.setAdapter(myCustomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Staff stf= staff.get(i);
                if(stf.getDept().equals("SCIENCE AND HUMANTIES"))
                {
                if(stf.getName().equals("NON-Teaching Staff") || stf.getName().equals("CHEMISTRY") || stf.getName().equals("PHYSICS") || stf.getName().equals("MATHEMATICS") || stf.getName().equals("ENGLISH"))
                {

                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Results.class);
                    intent.putExtra("code", stf.getId());
                    startActivity(intent);
                }}
                else
                {
                    if(stf.getName().equals("NON-Teaching Staff"))
                    {

                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), Results.class);
                        intent.putExtra("code", stf.getId());
                        startActivity(intent);
                    }
                }

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
