package com.nec.staffApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.nec.staffApp.DBHelper1.DATABASE_NAME;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp=getSharedPreferences("login",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sp.edit();
        String check=sp.getString("check",null);

        if(check !=null)
        {
            Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
            startActivity(intent);
        }

     else
        {
        File database=getApplicationContext().getDatabasePath(DATABASE_NAME).getAbsoluteFile();
        if(!database.exists())
        {
            DBHelper1  mDBHelper=new DBHelper1(this);
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)){
                Toast.makeText(this,"Copy Database Success",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Copy Database failure",Toast.LENGTH_SHORT).show();
            }
        }


        Button btn=(Button)findViewById(R.id.btn_login);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e1=(EditText)findViewById(R.id.username);
                EditText e2=(EditText)findViewById(R.id.pass);
                String username=e1.getText().toString();
                String password=e2.getText().toString();
                DBHelper1 dbhelper=new DBHelper1(getApplicationContext());
                Staff result=dbhelper.login(username);
                if(result!=null) {
                    if (password.equals("123456")) {
                        if (result.getId().equals(username)) {
                            editor.putString("check",result.getPhno()).apply();
                            Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                            startActivity(intent);
                        } else {
                            e1.setText("");
                            e2.setText("");
                            Toast.makeText(getApplicationContext(), "Enter Valid Register Number", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        e1.setText("");
                        e2.setText("");
                        Toast.makeText(getApplicationContext(), "Enter Valid Register Number & Password", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    e1.setText("");
                    e2.setText("");
                    Toast.makeText(getApplicationContext(), "Enter Valid Register Number & Password", Toast.LENGTH_LONG).show();

                }
            }
        });

    }}
    private boolean copyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath(DATABASE_NAME).getAbsolutePath();
            OutputStream outputStream=new FileOutputStream(outFileName);
            byte[] buff=new byte[1024];
            int length=0;
            while((length=inputStream.read(buff))> 0){
                outputStream.write(buff,0,length);

            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.e("Mainactivity ","DB Copied");
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

