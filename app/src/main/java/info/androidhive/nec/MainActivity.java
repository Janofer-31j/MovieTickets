package info.androidhive.nec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // making toolbar transparent
        transparentToolbar();

        setContentView(R.layout.activity_main);


        File database=getApplicationContext().getDatabasePath(DBHelper1.DATABASE_NAME);
        if(false==database.exists())
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

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
        });
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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



}
