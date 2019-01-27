package info.androidhive.nec;

/**
 * Created by Janofer Ibrahima on 11/14/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Dialog {
    private int REQUEST_CODE = 1;

    public void showdialog(String name, final String phoneno, final String mail,
                           final Activity activity) {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder.setTitle(name);
        alertDialogBuilder.setMessage(phoneno);


        alertDialogBuilder.setNeutralButton("Call",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Toast.makeText(NextActivity1.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(
                                Intent.ACTION_CALL, Uri
                                .parse("tel:" + phoneno));

                        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

                            activity.startActivity(i);

                        }else{

//persmission is not granted yet
//Asking for -permission
                            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
                            Toast.makeText(activity,"Click Call Now Again To Call",Toast.LENGTH_LONG).show();

                        }


                    }
                });

        alertDialogBuilder.setNegativeButton("Sms",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                        sendSMS(activity, phoneno);

                    }
                });

        alertDialogBuilder.setPositiveButton("Mail",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                        sendEmail(activity, mail);


                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void sendSMS(Context activity, String phoneno) {
        try {
            Uri uri = Uri.parse("smsto:"+phoneno);
            // No permisison needed
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            // Set the message to be sent
            smsIntent.putExtra("sms_body", "SMS application launched from stackandroid.com example");
            activity.startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    protected void sendEmail(Context activity, String mail){
        try {
            Uri uri = Uri.parse("mailto:"+mail);
            Intent emailIntent = new Intent(Intent.ACTION_SEND, uri);
            emailIntent.putExtra("mail_body", "Mail application launched from stackandroid.com example");
            emailIntent.setData(Uri.parse("mailto:"));
            activity.startActivity(emailIntent);
            //  finish();
        }catch (Exception e) {
            Toast.makeText(activity,
                    "Mail failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }



}