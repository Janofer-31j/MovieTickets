package info.androidhive.nec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

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
}

